package main

import (
	"bufio"
	"errors"
	"fmt"
	"log"
	"os"
	"sort"
	"sync"
	"time"
)

const (
	workerCount = 4
	taskCount   = 16
	outputPath  = "../output/go_results.txt"
)

type Task struct {
	ID    int
	Value int
}

type Result struct {
	TaskID     int
	Input      int
	Output     int64
	WorkerName string
}

type TaskQueue struct {
	tasks chan Task
}

func newTaskQueue(capacity int) *TaskQueue {
	return &TaskQueue{tasks: make(chan Task, capacity)}
}

func (q *TaskQueue) addTask(task Task) error {
	if task.ID <= 0 {
		return errors.New("task ID must be positive")
	}
	if task.Value < 0 {
		return errors.New("task value cannot be negative")
	}

	q.tasks <- task
	return nil
}

func (q *TaskQueue) getTask() (Task, bool) {
	task, open := <-q.tasks
	return task, open
}

func (q *TaskQueue) close() {
	close(q.tasks)
}

func processTask(workerName string, task Task) (Result, error) {
	if task.Value < 0 {
		return Result{}, fmt.Errorf("task %d has invalid value %d", task.ID, task.Value)
	}

	time.Sleep(75 * time.Millisecond)
	output := int64(task.Value) * int64(task.Value)
	return Result{
		TaskID:     task.ID,
		Input:      task.Value,
		Output:     output,
		WorkerName: workerName,
	}, nil
}

func worker(name string, queue *TaskQueue, results chan<- Result, workers *sync.WaitGroup) {
	defer workers.Done()
	processedCount := 0
	log.Printf("%s started", name)
	defer func() {
		log.Printf("%s completed after processing %d task(s)", name, processedCount)
	}()

	for {
		task, open := queue.getTask()
		if !open {
			return
		}

		result, err := processTask(name, task)
		if err != nil {
			log.Printf("%s could not process task %d: %v", name, task.ID, err)
			continue
		}
		results <- result
		processedCount++
	}
}

func writeResults(path string, results []Result) (err error) {
	file, err := os.Create(path)
	if err != nil {
		return fmt.Errorf("create %s: %w", path, err)
	}
	defer func() {
		if closeErr := file.Close(); closeErr != nil && err == nil {
			err = fmt.Errorf("close %s: %w", path, closeErr)
		}
	}()

	writer := bufio.NewWriter(file)
	if _, err = fmt.Fprintln(writer, "GO DATA PROCESSING RESULTS"); err != nil {
		return fmt.Errorf("write heading: %w", err)
	}
	if _, err = fmt.Fprintln(writer, "=========================="); err != nil {
		return fmt.Errorf("write heading: %w", err)
	}
	for _, result := range results {
		if _, err = fmt.Fprintf(
			writer,
			"task=%02d input=%d output=%d worker=%s\n",
			result.TaskID,
			result.Input,
			result.Output,
			result.WorkerName,
		); err != nil {
			return fmt.Errorf("write task %d: %w", result.TaskID, err)
		}
	}
	if err = writer.Flush(); err != nil {
		return fmt.Errorf("flush %s: %w", path, err)
	}
	return nil
}

func run() error {
	queue := newTaskQueue(taskCount)
	results := make(chan Result, taskCount)

	var workers sync.WaitGroup
	for workerNumber := 1; workerNumber <= workerCount; workerNumber++ {
		workers.Add(1)
		go worker(fmt.Sprintf("go-worker-%d", workerNumber), queue, results, &workers)
	}

	queuedCount := 0
	var queueErr error
	for taskID := 1; taskID <= taskCount; taskID++ {
		if err := queue.addTask(Task{ID: taskID, Value: taskID + 2}); err != nil {
			queueErr = fmt.Errorf("queue task %d: %w", taskID, err)
			break
		}
		queuedCount++
	}
	queue.close()
	log.Printf("queued %d tasks", queuedCount)

	go func() {
		workers.Wait()
		close(results)
	}()

	collected := make([]Result, 0, taskCount)
	for result := range results {
		collected = append(collected, result)
	}
	if queueErr != nil {
		return queueErr
	}
	if len(collected) != queuedCount {
		return fmt.Errorf("expected %d results but received %d", queuedCount, len(collected))
	}

	sort.Slice(collected, func(i, j int) bool {
		return collected[i].TaskID < collected[j].TaskID
	})
	if err := os.MkdirAll("../output", 0o755); err != nil {
		return fmt.Errorf("create output directory: %w", err)
	}
	if err := writeResults(outputPath, collected); err != nil {
		return err
	}

	log.Printf("processing complete: %d results written to %s", len(collected), outputPath)
	return nil
}

func main() {
	log.SetFlags(log.Ltime | log.Lmicroseconds)
	log.Print("Go data processing system starting")
	if err := run(); err != nil {
		log.Printf("data processing system failed: %v", err)
		os.Exit(1)
	}
}

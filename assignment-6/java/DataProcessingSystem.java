import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DataProcessingSystem {
    private static final Logger LOGGER = Logger.getLogger(DataProcessingSystem.class.getName());
    private static final int WORKER_COUNT = 4;
    private static final int TASK_COUNT = 16;
    private static final Path OUTPUT_PATH = Path.of("output", "java_results.txt");

    private DataProcessingSystem() {
    }

    private record Task(int id, int value) {
        Task {
            if (id <= 0) {
                throw new IllegalArgumentException("Task ID must be positive");
            }
            if (value < 0) {
                throw new IllegalArgumentException("Task value cannot be negative");
            }
        }
    }

    private record Result(int taskId, int input, long output, String workerName) {
        @Override
        public String toString() {
            return String.format(
                    "task=%02d input=%d output=%d worker=%s",
                    taskId, input, output, workerName);
        }
    }

    private static final class TaskQueue {
        private final ArrayDeque<Task> tasks = new ArrayDeque<>();
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition taskAvailable = lock.newCondition();
        private boolean closed;

        void addTask(Task task) {
            lock.lock();
            try {
                if (closed) {
                    throw new IllegalStateException("Cannot add a task after the queue is closed");
                }
                tasks.addLast(task);
                taskAvailable.signal();
            } finally {
                lock.unlock();
            }
        }

        Optional<Task> getTask() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                while (tasks.isEmpty() && !closed) {
                    taskAvailable.await();
                }
                return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.removeFirst());
            } finally {
                lock.unlock();
            }
        }

        void close() {
            lock.lock();
            try {
                closed = true;
                taskAvailable.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    private static final class ResultStore {
        private final List<Result> results = new ArrayList<>();

        synchronized void save(Result result) {
            results.add(result);
        }

        synchronized List<Result> snapshot() {
            return new ArrayList<>(results);
        }
    }

    private static final class Worker implements Runnable {
        private final String name;
        private final TaskQueue taskQueue;
        private final ResultStore resultStore;

        Worker(String name, TaskQueue taskQueue, ResultStore resultStore) {
            this.name = name;
            this.taskQueue = taskQueue;
            this.resultStore = resultStore;
        }

        @Override
        public void run() {
            int processedCount = 0;
            LOGGER.info(() -> name + " started");

            try {
                Optional<Task> nextTask;
                while ((nextTask = taskQueue.getTask()).isPresent()) {
                    Task task = nextTask.get();
                    try {
                        resultStore.save(processTask(task));
                        processedCount++;
                    } catch (IllegalArgumentException exception) {
                        LOGGER.log(Level.WARNING,
                                name + " rejected task " + task.id(), exception);
                    }
                }
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                LOGGER.log(Level.WARNING, name + " was interrupted", exception);
            } catch (RuntimeException exception) {
                LOGGER.log(Level.SEVERE, name + " encountered an unexpected error", exception);
            } finally {
                int completedCount = processedCount;
                LOGGER.info(() -> name + " completed after processing "
                        + completedCount + " task(s)");
            }
        }

        private Result processTask(Task task) throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(75);
            long output = (long) task.value() * task.value();
            return new Result(task.id(), task.value(), output, name);
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Java data processing system starting");

        TaskQueue taskQueue = new TaskQueue();
        ResultStore resultStore = new ResultStore();
        ExecutorService workers = Executors.newFixedThreadPool(WORKER_COUNT);

        for (int workerNumber = 1; workerNumber <= WORKER_COUNT; workerNumber++) {
            workers.execute(new Worker("java-worker-" + workerNumber, taskQueue, resultStore));
        }

        try {
            for (int taskId = 1; taskId <= TASK_COUNT; taskId++) {
                taskQueue.addTask(new Task(taskId, taskId + 2));
            }
            LOGGER.info(() -> "Queued " + TASK_COUNT + " tasks");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            LOGGER.log(Level.SEVERE, "Could not queue a task", exception);
        } finally {
            taskQueue.close();
            workers.shutdown();
        }

        if (!awaitWorkers(workers)) {
            LOGGER.severe("Workers did not terminate cleanly; results were not written");
            return;
        }

        List<Result> results = resultStore.snapshot();
        results.sort(Comparator.comparingInt(Result::taskId));

        try {
            writeResults(results, OUTPUT_PATH);
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Could not write results to " + OUTPUT_PATH, exception);
            return;
        }

        if (results.size() != TASK_COUNT) {
            LOGGER.warning(() -> "Expected " + TASK_COUNT + " results but received "
                    + results.size());
        }
        LOGGER.info(() -> "Processing complete: " + results.size()
                + " results written to " + OUTPUT_PATH);
    }

    private static boolean awaitWorkers(ExecutorService workers) {
        try {
            if (workers.awaitTermination(30, TimeUnit.SECONDS)) {
                return true;
            }
            LOGGER.warning("Worker timeout reached; requesting immediate shutdown");
            workers.shutdownNow();
            return workers.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            workers.shutdownNow();
            LOGGER.log(Level.SEVERE, "Main thread interrupted while waiting for workers", exception);
            return false;
        }
    }

    private static void writeResults(List<Result> results, Path outputPath) throws IOException {
        Path parent = outputPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write("JAVA DATA PROCESSING RESULTS");
            writer.newLine();
            writer.write("============================");
            writer.newLine();
            for (Result result : results) {
                writer.write(result.toString());
                writer.newLine();
            }
        }
    }
}

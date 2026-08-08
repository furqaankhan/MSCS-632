# Assignment 6: Concurrent Data Processing System

This project implements the same worker-pool data processing system in Java and Go. Four concurrent workers retrieve 16 tasks from a shared queue, simulate processing work, square each task value, and save sorted results to a shared output folder.

## Project Files

- `java/DataProcessingSystem.java`: Java implementation using `ReentrantLock`, `Condition`, synchronized result storage, and `ExecutorService`
- `go/main.go`: Go implementation using goroutines, channels, and `sync.WaitGroup`
- `output/`: Captured console logs and generated result files
- `screenshots/`: Code and sample-output images appended to the report
- `report/Assignment6_Report.md`: Editable APA-style report source
- `Assignment6_Report.pdf`: APA-formatted submission document containing the report and screenshots
- `Assignment6_Report.docx`: Editable version of the same submission document

## Requirements

- Java 17 or newer
- Go 1.24 or newer

## Run the Programs

Run these commands from `assignment-6`.

### Java

```bash
javac -Xlint:all java/DataProcessingSystem.java
java -cp java DataProcessingSystem
```

### Go

```bash
go -C go vet ./...
go -C go run .
```

To check the Go implementation for data races:

```bash
go -C go run -race .
```

Both implementations write one result for every task. The assignment of tasks to workers can change between runs because thread and goroutine scheduling is nondeterministic, but the files are sorted by task ID before they are written.

## GitHub Repository

<https://github.com/furqaankhan/MSCS-632>

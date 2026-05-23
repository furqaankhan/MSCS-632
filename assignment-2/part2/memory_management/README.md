# Part 2: Memory Management Programs

This folder contains small memory-management programs for Rust, Java, and C++.

## Rust

Compile and run:

```bash
rustc rust_memory.rs -o rust_memory
./rust_memory
```

The Rust program uses `Box<BankAccount>` for heap allocation, borrows the account with `&BankAccount`, and releases memory when ownership is dropped.

## Java

Compile and run:

```bash
javac JavaMemoryDemo.java
java JavaMemoryDemo
/usr/bin/time -v java JavaMemoryDemo
```

The Java program creates objects with `new`, clears object references, and requests garbage collection using `System.gc()`.

## C++

Compile and run:

```bash
g++ -std=c++17 cpp_memory.cpp -o cpp_memory
./cpp_memory
/usr/bin/time -v ./cpp_memory
```

Optional leak check if Valgrind is available:

```bash
valgrind --leak-check=full ./cpp_memory
```

The C++ program dynamically allocates memory with `new` and releases it with `delete` and `delete[]`.

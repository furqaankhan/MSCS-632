#!/bin/bash

set -e

echo "========================================"
echo "C++ Memory Profiling"
echo "========================================"

echo "Compiling C++ with debug symbols and sanitizers..."
clang++ -std=c++17 -g -O0 -fsanitize=address,undefined cpp_memory.cpp -o cpp_memory_profile

echo "Running C++ program with macOS time memory output..."
/usr/bin/time -l ./cpp_memory_profile > cpp_profile_stdout.txt 2> cpp_profile_stderr.txt

echo "C++ profiling complete."
echo


echo "========================================"
echo "Java Memory Profiling"
echo "========================================"

echo "Compiling Java program..."
javac JavaMemoryDemo.java

echo "Running Java program with GC logging and macOS time memory output..."
/usr/bin/time -l java -Xlog:gc JavaMemoryDemo > java_profile_stdout.txt 2> java_profile_stderr.txt

echo "Java profiling complete."
echo


echo "========================================"
echo "Rust Memory Profiling"
echo "========================================"

echo "Compiling Rust program..."
rustc -g -C opt-level=0 rust_memory.rs -o rust_memory_profile

echo "Running Rust program with macOS time memory output..."
/usr/bin/time -l ./rust_memory_profile > rust_profile_stdout.txt 2> rust_profile_stderr.txt

echo "Rust profiling complete."
echo


echo "========================================"
echo "Done"
echo "Generated profiling files:"
echo "  cpp_profile_stdout.txt"
echo "  cpp_profile_stderr.txt"
echo "  java_profile_stdout.txt"
echo "  java_profile_stderr.txt"
echo "  rust_profile_stdout.txt"
echo "  rust_profile_stderr.txt"
echo "========================================"

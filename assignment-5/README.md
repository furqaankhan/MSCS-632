# Assignment 5: Class-Based Ride Sharing System

This project implements the same ride-sharing scenario in C++ and GNU Smalltalk. It demonstrates encapsulation, inheritance, and runtime polymorphism through `Ride`, its three subclasses, `Driver`, and `Rider`.

## Project Files

- `cpp/ride_sharing.cpp`: C++17 implementation and demonstration
- `smalltalk/ride_sharing.st`: GNU Smalltalk implementation and demonstration
- `output/`: Captured console output from both implementations

## Fare Rules

| Ride type | Fare calculation |
| --- | --- |
| Standard | $3.25 base fare + $1.45 per mile |
| Premium | $7.50 base fare + $2.80 per mile |
| Shared | $2.00 base fare + $1.00 per mile, with a $5.00 minimum |

## Run the Programs

### C++

```bash
g++ -std=c++17 -Wall -Wextra -pedantic cpp/ride_sharing.cpp -o ride_sharing_cpp
./ride_sharing_cpp
```

### GNU Smalltalk

```bash
gst smalltalk/ride_sharing.st
```

Both demonstrations create different ride subclasses, store them in one base-type collection, and invoke `fare` and `rideDetails` polymorphically. They also assign rides to a driver and record requests in a rider's private history.

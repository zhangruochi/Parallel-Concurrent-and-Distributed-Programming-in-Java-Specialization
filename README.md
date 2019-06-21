# Parallel, Concurrent, and Distributed Programming in Java Specialization

Parallel, concurrent, and distributed programming underlies software in multiple domains, ranging from biomedical research to financial services. This specialization is intended for anyone with a basic knowledge of sequential programming in Java, who is motivated to learn how to write parallel, concurrent and distributed programs. Through a collection of three courses (which may be taken in any order or separately), you will learn foundational topics in Parallelism, Concurrency, and Distribution. These courses will prepare you for multithreaded and distributed programming for a wide range of computer platforms, from mobile devices to cloud computing servers.



# Parallel Programming in Java

**About this Course** This course teaches learners (industry professionals and students) the fundamental concepts of parallel programming in the context of Java 8. Parallel programming enables developers to use multicore computers to make their applications run faster by using multiple processors at the same time. By the end of this course, you will learn how to use popular parallel Java frameworks (such as ForkJoin, Stream, and Phaser) to write parallel programs for a wide range of multicore platforms including servers, desktops, or mobile devices, while also learning about their theoretical foundations including computation graphs, ideal parallelism, parallel speedup, Amdahl's Law, data races, and determinism.

**Why take this course?**

- All computers are multicore computers, so it is important for you to learn how to extend your knowledge of sequential Java programming to multicore parallelism.
- Java 7 and Java 8 have introduced new frameworks for parallelism (ForkJoin, Stream) that have significantly changed the paradigms for parallel programming since the early days of Java.
- Each of the four modules in the course includes an assigned mini-project that will provide you with the necessary hands-on experience to use the concepts learned in the course on your own, after the course ends.
- During the course, you will have online access to the instructor and the mentors to get individualized answers to your questions posted on forums.

**The desired learning outcomes of this course are as follows:**

- Theory of parallelism: computation graphs, work, span, ideal parallelism, parallel speedup, Amdahl's Law, data races, and determinism
- Task parallelism using Java’s ForkJoin framework
- Functional parallelism using Java’s Future and Stream frameworks
- Loop-level parallelism with extensions for barriers and iteration grouping (chunking)
- Dataflow parallelism using the Phaser framework and data-driven tasks

Mastery of these concepts will enable you to immediately apply them in the context of multicore Java programs, and will also provide the foundation for mastering other parallel programming systems that you may encounter in the future  (e.g., C++11, OpenMP, .Net Task Parallel Library).


## Resources
- [Course Resouces](./resources)

## Course One Certificate

![Parallel Programming in Java](./Parallel_Programming_in_Java.png)


## My Course One Notes

### Week1
- Overview
  - Task-level Parallelism
    - Task Creation and Termination (Async, Finish)
    - Creating Tasks in Java's Fork/Join Framework
    - Computation Graphs, Work, Span, Ideal Parallelism
      - Computation Graphs (CGs)
      - ideal parallelism
    - Multiprocessor Scheduling, Parallel Speedup
    - Amdahl’s Law
- [Note](./week1/README.md)
- [Slides](./week1/slides)
- [Projects](./week1/projects)
  - [Project0](./week1/projects/miniproject_0)
  - [README.md](./week1/projects/README.md)
  - [Tutorial-of-concurrent-packages.md](./week1/projects/Tutorial-of-Java-s-concurrent-packages.md)
  - [Project1](./week1/projects/miniproject_1)
- [Attachments](./week1/attachments)



### Week2
- Overview
  - Functional Parallelism
    - Creating Future Tasks in Java’s Fork/Join Framework
    - Memoization
    - Java Streams
    - Determinism and Data Races
      - Determinism
      - Data Races
    - Fork/Join 框架与 Java Stream API
    - Stream的并发实现细节
    - Example
- [Note](./week2/README.md)
- [Slides](./week2/slides)
- [Projects](./week2/projects)
  - [README.md](./week2/projects/README.md)
  - [Project2](./week2/projects/miniproject_2)
- [Attachments](./week2/attachments)


### Week3
- Overview
  - Loop Parallelism
    - Parallel Matrix Multiplication
    - Barriers in Parallel Loops
      - One-Dimensional Iterative Averaging
    - Iteration Grouping: Chunking of Parallel Loops
- [Note](./week3/README.md)
- [Slides](./week3/slides)
- [Projects](./week3/projects)
  - [README.md](./week3/projects/README.md)
  - [Project3](./week3/projects/miniproject_3)
- [Attachments](./week3/attachments)


### Week4
- Overview
  - Split-phase Barriers with Java Phasers
   - Phaser Understanding
  - Point-to-Point Synchronization with Phasers
  - One-Dimensional Iterative Averaging with Phasers
  - Pipeline Parallelism
  - Data Flow Parallelism
- [Note](./week4/README.md)
- [Slides](./week4/slides)
- [Projects](./week4/projects)
  - [README.md](./week4/projects/README.md)
  - [Project4](./week4/projects/miniproject_4)
- [Attachments](./week4/attachments)



# Concurrent Programming in Java

**About this Course** This course teaches learners (industry professionals and students) the fundamental concepts of concurrent programming in the context of Java 8.   Concurrent programming enables developers to efficiently and correctly mediate the use of shared resources in parallel programs.  By the end of this course, you will learn how to use basic concurrency constructs in Java such as threads, locks, critical sections, atomic variables, isolation, actors, optimistic concurrency and concurrent collections, as well as their theoretical foundations (e.g., progress guarantees, deadlock, livelock, starvation, linearizability).

**Why take this course?**

- It is important for you to be aware of the theoretical foundations of concurrency to avoid common but subtle programming errors. 
- Java 8 has modernized many of the concurrency constructs since the early days of threads and locks.
- During the course, you will have online access to the instructor and mentors to get individualized answers to your questions posted on the forums.
- Each of the four modules in the course includes an assigned mini-project that will provide you with the necessary hands-on experience to use the concepts learned in the course on your own, after the course ends.

The desired learning outcomes of this course are as follows:

- Concurrency theory: progress guarantees, deadlock, livelock, starvation, linearizability
- Use of threads and structured/unstructured locks in Java
- Atomic variables and isolation
- Optimistic concurrency and concurrent collections in Java (e.g., concurrent queues, concurrent  hashmaps)
- Actor model in Java

Mastery of these concepts will enable you to immediately apply them in the context of concurrent Java programs, and will also help you master other concurrent programming system that you may encounter in the future  (e.g., POSIX threads, .NET threads).


## Resources
- [Course Resouces](./resources)

## Course One Certificate



## My Course One Notes

### Week5
- Overview
  - Threads
  - Structured Locks
  - Unstructured Locks
  - Liveness and Progress Guarantees
    - synchronized 用法
  - Deadlock‘s example
  - Dining Philosophers Problem
- [Note](./week5/README.md)
- [Slides](./week5/slides)
- [Projects](./week5/projects)
  - [README.md](./week5/projects/README.md)
  - [Project4](./week5/projects/miniproject_5)
- [Attachments](./week5/attachments)



### Week6
- Overview
  - Critical Sections
  - Object-Based Isolation
  - Spanning Tree Example
  - Atomic Variables
  - Read-Write Isolation
- [Note](./week6/README.md)
- [Slides](./week6/slides)
- [Projects](./week6/projects)
- [Attachments](./week6/attachments)


### Week7
- Overview
- [Note](./week7/README.md)
- [Slides](./week7/slides)
- [Projects](./week7/projects)
- [Attachments](./week7/attachments)


### Week8
- Overview
- [Note](./week8/README.md)
- [Slides](./week8/slides)
- [Projects](./week8/projects)
- [Attachments](./week8/attachments)


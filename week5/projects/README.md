## Project Goals and Outcomes

Threads are the assembly language of parallel computing. They enable programmers to explicitly express parallel computation at a low level, and are supported across a wide range of processors and operating systems. However, one of the dangers of using threads (and concurrency in general) is the risk of data races.

A data race is an unsafe access to the same piece of data from two independently executing threads, without some mechanism in place to ensure that those accesses do not conflict with each other. A data race may only occur when there is no synchronization in place to prevent concurrent access by multiple threads, and when at least one of those accesses is a write to or modification of the data.

This week, you learned about two mechanisms for performing synchronization in concurrent Java programs: Java locks and the Java synchronized statement. In this mini-project, you will gain hands-on experience with writing code using the synchronized statement, Java locks, and Java read-write locks. You will do so by implementing a concurrent, *sorted* list data structure. Using an artificial workload of randomly generated reads and writes, you will also measure the performance benefit of read-write locks over the other synchronization techniques while inserting, removing, and checking for items in the sorted list.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_5.zip at the top of this description, you should see the project source code file at

miniproject_5/src/main/java/edu/coursera/concurrent/CoarseLists.java

and the project tests in

miniproject_5/src/test/java/edu/coursera/concurrent/ListSetTest.java

It is recommended that you review the demo video for this week before starting this assignment, in which Professor Sarkar works through a similar example.

## Project Instructions

Your modifications should be made entirely inside of CoarseLists.java. You should not change the signatures of any public or protected methods inside of CoarseLists.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of ListSetTest.java in the final grading process, so do not change that file or any other file except CoarseLists .java.

Your main goals for this assignment are as follows:

Inside CoarseLists.java, implement the CoarseList class using a ReentrantLock object to protect the add, remove, and contains methods from concurrent accesses. You should refer to SyncList.java as a guide for placing synchronization and understanding the list management logic.
Inside CoarseLists.java, implement the RWCoarseList class using a ReentrantReadWriteLock object to protect the add, remove, and contains methods from concurrent accesses. You should refer to SyncList.java as a guide for placing synchronization and understanding the list management logic.
There are helpful TODOs in CoarseLists.java to help guide your implementation.

## Project Evaluation

Your assignment submission should consist of only the CoarseLists.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Your submission will be evaluated on Coursera’s auto-grading system using 2 and 4 CPU cores. Note that the performance observed for tests on your local machine may differ from that on Coursera's auto-grading system, but that you will only be evaluated on the measured performance on Coursera. Also note that for all assignments in this course you are free to resubmit as many times as you like. See the Common Pitfalls page under Resources for more details. Please give it a few minutes to complete the grading. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 20% - performance of CoarseList on two cores
- 20% – performance of RWCoarseList on two cores
- 30% – performance of CoarseList on four cores
- 30% – performance of RWCoarseList on four cores

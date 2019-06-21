## Project Goals and Outcomes

One of the major challenges of writing concurrent applications is the correct protection of data shared by multiple, concurrently executing streams of execution. "Correct" protection of shared data generally has several properties:

A deadlock cannot occur (i.e. progress is guaranteed).
A data race cannot occur.
That protection does not cause excessive contention, and hence does not significantly hurt performance.
One of the concurrent programming concepts you learned about this week that helps to prevent all three of the above issues is object-based isolation. In object-based isolation, the objects being accessed are explicitly passed to the synchronization construct being used. The synchronization construct can then use this information on the specific objects being accessed by the critical section to optimize synchronization and improve safety. This is in contrast to global isolation, where a single global lock or construct ensures safety but might experience heavy contention. Two examples of object-based isolation are the Java synchronized statement and the PCDP object-based isolated API.

In this mini-project, you will gain experience writing code that makes use of object-based isolation using PCDP. To motivate this topic, we will implement a concurrent banking system that can accept transactions from many threads of execution at once. We will use object-based isolation to protect bank accounts from concurrent accesses to ensure that all bank balances are correct, and enable more transactions per second than would be possible with global isolation.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_2.zip at the top of this description, you should see the project source code file at

miniproject_2/src/main/java/edu/coursera/concurrent/BankTransactionsUsingObjectIsolation.java

and the project tests in

miniproject_2/src/test/java/edu/coursera/concurrent/BankTransactionsTest.java

It is recommended that you review the demo video for this week before starting this assignment, in which Professor Sarkar works through a similar example.

## Project Instructions

Your modifications should be made entirely inside of BankTransactionsUsingObjectIsolation.java. You should not change the signatures of any public or protected methods inside of BankTransactionsUsingObjectIsolation.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of BankTransactionsTest.java in the final grading process, so do not change that file or any other file except BankTransactionsUsingObjectIsolation.java.

Your main goals for this assignment are as follows:

Inside BankTransactionsUsingObjectIsolation.java, implement the issueTransfer method using object-based isolation to protect against concurrent accesses to the source and destination bank accounts.
There are helpful TODOs in BankTransactionsUsingObjectIsolation.java to help guide your implementation.

## Project Evaluation

Your assignment submission should consist of only the BankTransactionsUsingObjectIsolation.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Your submission will be evaluated on Coursera’s auto-grading system using 2 and 4 CPU cores. Note that the performance observed for tests on your local machine may differ from that on Coursera's auto-grading system, but that you will only be evaluated on the measured performance on Coursera. Also note that for all assignments in this course you are free to resubmit as many times as you like. See the Common Pitfalls page under Resources for more details. Please give it a few minutes to complete the grading. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 50% - performance of object-based isolation on two cores
- 50% - performance of object-based isolation on four cores
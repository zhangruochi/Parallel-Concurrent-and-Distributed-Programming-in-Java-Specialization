## Project Goals and Outcomes

Actors offer a higher level of abstraction for concurrent programming, in which the default form of execution is isolated. By localizing data to specific actors and preventing concurrent access to them using the message passing paradigm, concurrent code without data races can be written much more easily.

In this mini-project, you will gain hands-on experience using actors to find prime numbers using the Sieve of Eratosthenes.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_3.zip at the top of this description, you should see the project source code file at

miniproject_3/src/main/java/edu/coursera/concurrent/SieveActor.java

and the project tests in

miniproject_3/src/test/java/edu/coursera/concurrent/SieveTest.java

It is recommended that you review the demo video for this week as well as lectures 1, 2, and 3 on the actor model and the Sieve of Eratosthenes before starting this assignment. An example sequential implementation is also provided in SieveSequential.java.

## Project Instructions

Your modifications should be made entirely inside of SieveActor.java. You should not change the signatures of any public or protected methods inside of SieveActor.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of SieveTest.java in the final grading process, so do not change that file or any other file except SieveActor.java.

Your main goals for this assignment are as follows:

Inside SieveActor .java, implement the countPrimes method by using actor parallelism to implement a parallel version of the Sieve of Eratosthenes based on the descriptions in the lecture and demo videos from this week. A sample actor declaration is provided in SieveActor.java for you to fill in. Note that the Actor API shown in the demo video and lectures is slightly different to the one used in this assignment. In particular, it is a simplification that does not require the start and exit methods. For example, a start operation is implicitly performed when an actor is created with a new operation. A full description of the Actor APIs can be found in the PCDP Javadocs at https://habanero-rice.github.io/PCDP/.
There are helpful TODOs in SieveActor.java to help guide your implementation.

## Project Evaluation

Your assignment submission should consist of only the SieveActor.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Your submission will be evaluated on Coursera’s auto-grading system using 2 and 4 CPU cores. Note that the performance observed for tests on your local machine may differ from that on Coursera's auto-grading system, but that you will only be evaluated on the measured performance on Coursera. Also note that for all assignments in this course you are free to resubmit as many times as you like. See the Common Pitfalls page under Resources for more details. Please give it a few minutes to complete the grading. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 20% - performance of finding all primes <= 100,000 on two cores
- 20% - performance of finding all primes <= 200,000 on two cores
- 30% - performance of finding all primes <= 100,000 on four cores
- 30% - performance of finding all primes <= 200,000 on four cores
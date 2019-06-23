## Project Goals and Outcomes

This week we learned about two different implementations of the MapReduce programming abstraction: Hadoop and Spark.

Hadoop was one of the first popular distributed MapReduce programming systems, and really paved the way for Big Data analysis at massive scales on commodity hardware. As described in lectures this week, its primary strengths are three-fold: 1) scalability, 2) programmability, and 3) fault tolerance. Hadoop allows programmers to write simple Java applications that run across thousands of networked machines and which, thanks to its MapReduce abstractions, can recover from complete hardware failure of many of those machines.

Spark, on the other hand, is a successor to Hadoop that emphasizes in-memory caching of data and a more flexible API. While Hadoop regularly persisted replicated data to disk to ensure fault tolerance, Spark instead caches information in memory and ensures that it remembers the transformations that were applied to generate a particular piece of data. Spark also offers more than simple map and reduce transformations.

You also learned about the PageRank algorithm, and how it can be used to rank inter-connected websites based on existing ranks and link counts.

In this mini-project, you will gain experience with Spark by implementing the PageRank algorithm using Spark transformations. In particular, this assignment will ask you to implement the transformations needed to complete a single iteration of the iterative PageRank algorithm given an input Spark Resilient Distributed Dataset (RDD) of websites.

Recall from lectures this week that an important concept in MapReduce frameworks is that of "key-value pairs". The key of a key-value pair is some unique identifier for some logical object, e.g. a website. The value is some metadata associated with the logical object the key identifies, e.g. information on the outbound links for a website. In Spark, a key-value pair is represented using the Tuple2 class. For example, to create a new key-value pair from some key k and some value v you can use the following code:

```Java
Tuple2 kvPair = new Tuple2(k, v);
```

Spark uses special-purpose RDD objects to store datasets containing Tuple2 objects (i.e. key-value pairs). Rather than using the standard JavaRDD class, RDDs of Tuple2 objects use the JavaPairRDD class.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_1.zip at the top of this description, you should see the project source code file at

miniproject_9/src/main/java/edu/coursera/distributed/PageRank.java

and the project tests in

miniproject_9/src/test/java/edu/coursera/distributed/SparkTest.java

It is recommended that you review the demo video for this week before starting this assignment, in which Professor Sarkar works through a similar example.

## Project Instructions

Your modifications should be made entirely inside of PageRank.java. You should not change the signatures of any public or protected methods inside of PageRank.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of SparkTest.java in the final grading process, so do not change that file or any other file except PageRank .java.

Your main goals for this assignment are as follows:

Inside PageRank.java, implement the sparkPageRank method to perform a single iteration of the PageRank algorithm using the Spark Java APIs.
There are helpful TODOs in PageRank.java to help guide your implementation.

## Project Evaluation

Your assignment submission should consist of only the PageRank.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Your submission will be evaluated on Coursera’s auto-grading system using 2 and 4 CPU cores. Note that the performance observed for tests on your local machine may differ from that on Coursera's auto-grading system, but that you will only be evaluated on the measured performance on Coursera. Also note that for all assignments in this course you are free to resubmit as many times as you like. See the Common Pitfalls page under Resources for more details. Please give it a few minutes to complete the grading. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 10% - performance of testUniformThirtyThousand on 2 cores
- 10% - performance of testUniformOneHundredThousand on 2 cores
- 5% - performance of testIncreasingThirtyThousand on 2 cores
- 5% - performance of testIncreasingOneHundredThousand on 2 cores
- 10% - performance of testRandomThirtyThousand on 2 cores
- 10% - performance of testRandomOneHundredThousand on 2 cores
- 10% - performance of testUniformThirtyThousand on 4 cores
- 10% - performance of testUniformOneHundredThousand on 4 cores
- 5% - performance of testIncreasingThirtyThousand on 4 cores
- 5% - performance of testIncreasingOneHundredThousand on 4 cores
- 10% - performance of testRandomThirtyThousand on 4 cores
- 10% - performance of testRandomOneHundredThousand on 4 cores
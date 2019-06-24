## Project Goals and Outcomes

In general, most production JVM web servers are multi-threaded. Using multiple threads allows a single JVM server instance to handle many concurrent requests at once.

In Mini Project 2 of this course, you developed a simple file server that responded to HTTP GET requests with the contents of files. In this mini-project, we will make a simple extension to that file server by using multiple Java Threads to handle file requests.

If you need a refresher on HTTP, file servers, or the problem statement you are free to refer back to the Mini Project 2 description in Coursera and to your own Mini Project 2 solution.

Note that for this mini-project, the expected performance is highly dependent on the execution platform you use. The provided performance tests are calibrated to work well on the Coursera autograder, and may fail in your local testing environment even when they would pass on the autograder. Once you see speedup using multiple threads on your local machine, we encourage you to also test your submission on the autograder even if local testing is failing.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_4.zip at the top of this description, you should see the project source code file at

miniproject_4/src/main/java/edu/coursera/distributed/FileServer.java

and the project tests in

miniproject_4/src/test/java/edu/coursera/distributed/FileServerTest.java

It is recommended that you review the demo video for this week before starting this assignment, in which Professor Sarkar works through a similar example.

## Project Instructions

Your modifications should be made entirely inside of FileServer.java. You should not change the signatures of any public or protected methods inside of FileServer.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of FileServerTest.java in the final grading process, so do not change that file or any other file except FileServer.java.

Your main goals for this assignment are as follows:

Inside FileServer.java, implement the run method to use the provided ServerSocket object and root directory to implement a multi-threaded HTTP file server by handling each request received on the ServerSocket in a new thread. There are several helpful TODOs in FileServer.java to help guide your implementation.
Like in Mini Project 2, you are provided with a proxy filesystem class called PCDPFilesystem which you will use to read files requested by file server clients. The only PCDPFilesystem API you need to be aware of is called readFile, which accepts a PCDPPath object representing the path to a file you would like the contents of. readFile returns the contents of the specified file if it exists. Otherwise, it returns null.

The PCDPPath object has a single constructor which accepts the absolute path to a file as a String. Hence, you can construct a PCDPPath object after parsing the requested file from an HTTP GET request.

## Project Evaluation

Your assignment submission should consist of only the FileServer.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Your submission will be evaluated on Coursera’s auto-grading system using 2 and 4 CPU cores. Note that the performance observed for tests on your local machine may differ from that on Coursera's auto-grading system, but that you will only be evaluated on the measured performance on Coursera. Also note that for all assignments in this course you are free to resubmit as many times as you like. See the Common Pitfalls page under Resources for more details. Please give it a few minutes to complete the grading. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 5% - correctness of testTermination
- 5% - correctness of testFileGet
- 5% - correctness of testFileGets
- 10% - correctness of testNestedFileGet
- 10% - correctness of testDoublyNestedFileGet
- 10% - correctness of testLargeFileGet
- 10% - correctness of testMissingFileGet
- 10% - correctness of testMissingNestedFileGet
- 15% - performance of testPerformance on 2 cores
- 20% - performance of testPerformance on 4 cores
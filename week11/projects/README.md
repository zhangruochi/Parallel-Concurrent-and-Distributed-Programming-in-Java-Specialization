## Project Goals and Outcomes

This week we learned how to express Single Program Multiple Data (SPMD) parallelism using MPI. MPI offers high-level APIs for sending messages, receiving messages, and performing collectives across groups of MPI ranks called communicators.

One kernel that can be parallelized using SPMD parallelism is dense matrix-matrix multiplication, in which we multiply two input matrices A and B to produce an output matrix C. The value for cell (i, j) of matrix C is computed by taking the dot product of row i in matrix A and column j in matrix B. A review of dense matrix-matrix multiplication can be found at:

https://www.khanacademy.org/math/precalculus/precalc-matrices/multiplying-matrices-by-matrices/v/matrix-multiplication-intro

In this mini-project, your task is to implement parallel matrix-matrix multiplication using MPI.

## Project Setup

Please refer to Mini-Project 0 for a description of the build and testing process used in this course.

MPI Installation

If you would like to test your solution on your local machine, you will need to install an MPI implementation.

If you are using Ubuntu, you can install OpenMPI with the following commands:

```bash
$ sudo apt-get update
$ sudo apt-get install -y openmpi-bin libopenmpi-dev
```

If you are using Linux or Mac OS, we recommend downloading the OpenMPI implementation from:

https://www.open-mpi.org/software/ompi/v2.0/

and following the build instructions in the "User Builds" section of the included INSTALL file. Following installation, you must also add the created OpenMPI bin/ folder to your PATH and the created OpenMPI lib/ folder to your LD_LIBRARY_PATH (on Linux) or your DYLD_LIBRARY_PATH (on Mac OS).

If you are on Windows or find the installation instructions for OpenMPI difficult to follow, we recommend using print statements and the Cousera autograder to test your code. The Coursera autograder automatically links in an OpenMPI implementation for you.

Project Files

Once you have downloaded and unzipped the project files using the gray button labeled miniproject_3.zip at the top of this description, you should see the project source code file at

miniproject_3/src/main/java/edu/coursera/distributed/MatrixMult.java

and the project tests in

miniproject_3/src/test/java/edu/coursera/distributed/MpiJavaTest.java

It is recommended that you review the demo video for this week before starting this assignment, in which Professor Sarkar works through a similar example.

## Project Instructions

Your modifications should be made entirely inside of MatrixMult.java. You should not change the signatures of any public or protected methods inside of MatrixMult.java, but you can edit the method bodies and add any new methods that you choose. We will use our copy of MpiJavaTest.java in the final grading process, so do not change that file or any other file except MatrixMult.java.

Your main goals for this assignment are as follows:

Inside MatrixMult.java, implement the parallelMatrixMultiply method to perform a matrix-matrix multiply in parallel using SPMD parallelism and MPI. There are helpful TODOs in MatrixMult.java to help guide your implementation.
Note that because this mini-project requires multiple MPI processes to be spawned, performing local testing using Maven as you normally would will only result in a single MPI rank executing. To execute a local MPI program with 4 MPI ranks we have provided a Maven profile which will handle this automatically for you.

To run the provided Maven profile from the command line, simply use the following command after compiling your solution from the same directory as this mini-project's pom.xml file:

```bash
$ mvn -P MPITests-4 test-compile
```

As usual, you can also use the Coursera autograder to automatically run all tests for you, which will handle the creation of multiple MPI ranks.

## Project Evaluation

Your assignment submission should consist of only the MatrixMult.java file that you modified to implement this mini-project. As before, you can upload this file through the assignment page for this mini-project. After that, the Coursera autograder will take over and assess your submission, which includes building your code and running it on one or more tests. Your submission will be evaluated on Coursera’s auto-grading system using 2 and 4 CPU cores. Note that the performance observed for tests on your local machine may differ from that on Coursera's auto-grading system, but that you will only be evaluated on the measured performance on Coursera. See the Common Pitfalls page under Resources for more details. Also note that for all assignments in this course you are free to resubmit as many times as you like. Please give it a few minutes to complete the grading. Once it has completed, you should see a score appear in the “Score” column of the “My submission” tab based on the following rubric:

- 10% - performance of testMatrixMultiplySmall on 2 cores
- 10% - performance of testMatrixMultiplySmall on 4 cores
- 10% - performance of testMatrixMultiplyRectangular1Small on 2 cores
- 10% - performance of testMatrixMultiplyRectangular1Small on 4 cores
- 10% - performance of testMatrixMultiplyRectangular2Small on 2 cores
- 10% - performance of testMatrixMultiplyRectangular2Small on 4 cores
- 10% - performance of testMatrixMultiplySquareLarge on 2 cores
- 10% - performance of testMatrixMultiplySquareLarge on 4 cores
- 10% - performance of testMatrixMultiplyRectangularLarge on 2 cores
- 10% - performance of testMatrixMultiplyRectangularLarge on 4 cores
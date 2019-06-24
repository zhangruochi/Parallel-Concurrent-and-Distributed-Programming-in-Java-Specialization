---
title: Message Passing
date: 2019-06-24 19:11:12
tags: Java
keywords:
mathjax: true
categories: Distributed Computation
description:
---

Table of Contents
=================

  * [MPI(Message Passing Interface)](#mpimessage-passing-interface)
     * [MPI的工作方式](#mpi的工作方式)
     * [点对点通信](#点对点通信)
     * [群体通信](#群体通信)
        * [广播bcast](#广播bcast)
        * [散播scatter](#散播scatter)
        * [收集gather](#收集gather)
        * [规约reduce](#规约reduce)
        * [非阻塞标准通信](#非阻塞标准通信)
  * [Single Program Multiple Data (SPMD) Model](#single-program-multiple-data-spmd-model)
  * [Point-to-Point Communication](#point-to-point-communication)
  * [Message Ordering and Deadlock](#message-ordering-and-deadlock)
  * [Non-Blocking Communications](#non-blocking-communications)
  * [Collective Communication](#collective-communication)


## MPI(Message Passing Interface)

> src https://zhuanlan.zhihu.com/p/25332041
> src https://blog.csdn.net/zouxy09/article/details/49031845

- MPI 是基于分布式内存系统，而thread基于共享内存系统；也就是说 MPI 之间的数据共享需要通过消息传递，因为MPI同步的程序属于不同的进程，甚至不同的主机上的不同进程。
- MPI不同主机之间的进程协调工作需要安装mpi软件（例如mpich）来完成。
- MPI 是一种标准而不是特定的实现，具体的可以有很多不同的实现，例如MPICH、OpenMPI等。
- 是一种消息传递编程模型，顾名思义，它就是专门服务于进程间通信的。

PI的工作方式很好理解，我们可以同时启动一组进程，在同一个通信域中不同的进程都有不同的编号，程序员可以利用MPI提供的接口来给不同编号的进程分配不同的任务和帮助进程相互交流最终完成同一个任务。就好比包工头给工人们编上了工号然后指定一个方案来给不同编号的工人分配任务并让工人相互沟通完成任务。

与多线程的共享式内存不同，由于各个进程都是相互独立的，因此进程间通信再多进程中扮演这非常重要的角色，Python中我们可以使用multiprocessing模块中的pipe、queue、Array、Value等等工具来实现进程间通讯和数据共享，但是在编写起来仍然具有很大的不灵活性。而这一方面正是MPI所擅长的领域。

mpi4py是一个很强大的库，它实现了很多MPI标准中的接口，包括:

1. 点对点通信
2. 组内集合通信
3. 非阻塞通信
4. 重复非阻塞通信
5. 组间通信等


### MPI的工作方式
```Python
from mpi4py import MPI
print("hello world")
print("my rank is: %d" % MPI.rank)
```

```bash
mpirun –np 5 python test.py
```

-np5 指定启动5个mpi进程来执行后面的程序。相当于对脚本拷贝了5份，每个进程运行一份，互不干扰。在运行的时候代码里面唯一的不同，就是各自的rank也就是ID不一样。所以这个代码就会打印5个hello world和5个不同的rank值，从0到4.

### 点对点通信
点对点通信（Point-to-PointCommunication）的能力是信息传递系统最基本的要求。意思就是让两个进程直接可以传输数据，也就是一个发送数据，另一个接收数据。接口就两个，send和recv，来个例子:

```Python
import mpi4py.MPI as MPI
 
comm = MPI.COMM_WORLD
comm_rank = comm.Get_rank()
comm_size = comm.Get_size()
 
# point to point communication
data_send = [comm_rank]*5
comm.send(data_send,dest=(comm_rank+1)%comm_size)
data_recv =comm.recv(source=(comm_rank-1)%comm_size)
print("my rank is %d, and Ireceived:" % comm_rank)
print data_recv

# my rank is 0, and I received:
# [4, 4, 4, 4, 4]
# my rank is 1, and I received:
# [0, 0, 0, 0, 0]
# my rank is 2, and I received:
# [1, 1, 1, 1, 1]
# my rank is 3, and I received:
# [2, 2, 2, 2, 2]
# my rank is 4, and I received:
# [3, 3, 3, 3, 3]
```

可以看到，每个进程都创建了一个数组，然后把它传递给下一个进程，最后的那个进程传递给第一个进程。comm_size就是mpi的进程个数，也就是-np指定的那个数。MPI.COMM_WORLD 表示进程所在的通信组。

但这里面有个需要注意的问题，如果我们要发送的数据比较小的话，mpi会缓存我们的数据，也就是说执行到send这个代码的时候，会缓存被send的数据，然后继续执行后面的指令，而不会等待对方进程执行recv指令接收完这个数据。但是，如果要发送的数据很大，那么进程就是挂起等待，直到接收进程执行了recv指令接收了这个数据，进程才继续往下执行。所以上述的代码发送[rank]*5没啥问题，如果发送[rank]*500程序就会半死不活的样子了。因为所有的进程都会卡在发送这条指令，等待下一个进程发起接收的这个指令，但是进程是执行完发送的指令才能执行接收的指令，这就和死锁差不多了。所以一般，我们将其修改成以下的方式:

```python
import mpi4py.MPI as MPI
 
comm = MPI.COMM_WORLD
comm_rank = comm.Get_rank()
comm_size = comm.Get_size()
 
data_send = [comm_rank]*5
if comm_rank == 0:
   comm.send(data_send, dest=(comm_rank+1)%comm_size)
if comm_rank > 0:
   data_recv = comm.recv(source=(comm_rank-1)%comm_size)
   comm.send(data_send, dest=(comm_rank+1)%comm_size)
if comm_rank == 0:
   data_recv = comm.recv(source=(comm_rank-1)%comm_size)
print("my rank is %d, and Ireceived:" % comm_rank)
print data_recv
```

第一个进程一开始就发送数据，其他进程一开始都是在等待接收数据，这时候进程1接收了进程0的数据，然后发送进程1的数据，进程2接收了，再发送进程2的数据……知道最后进程0接收最后一个进程的数据，从而避免了上述问题。

一个比较常用的方法是封一个组长，也就是一个主进程，一般是进程0作为主进程leader。主进程将数据发送给其他的进程，其他的进程处理数据，然后返回结果给进程0。换句话说，就是进程0来控制整个数据处理流程。


### 群体通信
群体通信是发送和接收两类，一个是一次性把数据发给所有人，另一个是一次性从所有人那里回收结果。
#### 广播bcast

将一份数据发送给所有的进程。例如我有200份数据，有10个进程，那么每个进程都会得到这200份数据。

```python
import mpi4py.MPI as MPI
 
comm = MPI.COMM_WORLD
comm_rank = comm.Get_rank()
comm_size = comm.Get_size()
 
if comm_rank == 0:
   data = range(comm_size)
data = comm.bcast(data if comm_rank == 0 else None, root=0)
print 'rank %d, got:' % (comm_rank)
print data


# rank 0, got:
# [0, 1, 2, 3, 4]
# rank 1, got:
# [0, 1, 2, 3, 4]
# rank 2, got:
# [0, 1, 2, 3, 4]
# rank 3, got:
# [0, 1, 2, 3, 4]
# rank 4, got:
# [0, 1, 2, 3, 4]
```

#### 散播scatter

将一份数据平分给所有的进程。例如我有200份数据，有10个进程，那么每个进程会分别得到20份数据。

```python
import mpi4py.MPI as MPI
 
comm = MPI.COMM_WORLD
comm_rank = comm.Get_rank()
comm_size = comm.Get_size()
 
if comm_rank == 0:
   data = range(comm_size)
   print data
else:
   data = None
local_data = comm.scatter(data, root=0)
print 'rank %d, got:' % comm_rank
print local_data


# [0, 1, 2, 3, 4]
# rank 0, got:
# 0
# rank 1, got:
# 1
# rank 2, got:
# 2
# rank 3, got:
# 3
# rank 4, got:
# 4
```

#### 收集gather

那有发送，就有一起回收的函数。Gather是将所有进程的数据收集回来，合并成一个列表。下面联合scatter和gather组成一个完成的分发和收回过程：

```python
import mpi4py.MPI as MPI
 
comm = MPI.COMM_WORLD
comm_rank = comm.Get_rank()
comm_size = comm.Get_size()
 
if comm_rank == 0:
   data = range(comm_size)
   print data
else:
   data = None
local_data = comm.scatter(data, root=0)
local_data = local_data * 2
print 'rank %d, got and do:' % comm_rank
print local_data
combine_data = comm.gather(local_data,root=0)
if comm_rank == 0:
print combine_data

# [0, 1, 2, 3, 4]
# rank 0, got and do:
# 0
# rank 1, got and do:
# 2
# rank 2, got and do:
# 4
# rank 4, got and do:
# 8
# rank 3, got and do:
# 6
# [0, 2, 4, 6, 8]

```
 Root进程将数据通过scatter等分发给所有的进程，等待所有的进程都处理完后（这里只是简单的乘以2），root进程再通过gather回收他们的结果，和分发的原则一样，组成一个list。

#### 规约reduce

规约是指不但将所有的数据收集回来，收集回来的过程中还进行了简单的计算，例如求和，求最大值等等。为什么要有这个呢？我们不是可以直接用gather全部收集回来了，再对列表求个sum或者max就可以了吗？这样不是累死组长吗？为什么不充分使用每个工人呢？规约实际上是使用规约树来实现的。例如求max，完成可以让工人两两pk后，再返回两两pk的最大值，然后再对第二层的最大值两两pk，直到返回一个最终的max给组长。组长就非常聪明的将工作分配下工人高效的完成了。这是O(n)的复杂度，下降到O(log n)（底数为2）的复杂度。


```python
import mpi4py.MPI as MPI
 
comm = MPI.COMM_WORLD
comm_rank = comm.Get_rank()
comm_size = comm.Get_size()
if comm_rank == 0:
   data = range(comm_size)
   print data
else:
   data = None
local_data = comm.scatter(data, root=0)
local_data = local_data * 2
print 'rank %d, got and do:' % comm_rank
print local_data
all_sum = comm.reduce(local_data, root=0,op=MPI.SUM)
if comm_rank == 0:
print 'sumis:%d' % all_sum

# [0, 1, 2, 3, 4]
# rank 0, got and do:
# 0
# rank 1, got and do:
# 2
# rank 2, got and do:
# 4
# rank 3, got and do:
# 6
# rank 4, got and do:
# 8
# sum is:20
```


#### 非阻塞标准通信

所有的阻塞通信mpi都提供了一个非阻塞的版本，类似与我们编写异步程序不阻塞在耗时的IO上是一样的，MPI的非阻塞通信也不会阻塞消息的传递过程中，这样能够充分利用处理器资源提升整个程序的效率。

```Python
from mpi4py import MPI                                         
import numpy as np                                             
                                                               
comm = MPI.COMM_WORLD                                          
rank = comm.Get_rank()                                         
size = comm.Get_size()                                         
                                                               
if rank == 0:                                                  
    data = range(10)                                           
    comm.isend(data, dest=1, tag=11)                           
    print("process {} immediate send {}...".format(rank, data))
else:                                                          
    data = comm.recv(source=0, tag=11)                         
    print("process {} recv {}...".format(rank, data))

# process 0 immediate send [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]...
# process 1 recv [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]...
```




## Single Program Multiple Data (SPMD) Model

**Lecture Summary**: n this lecture, we studied the Single Program Multiple Data (SPMD) model, which can enable the use of a cluster of distributed nodes as a single parallel computer. Each node in such a cluster typically consist of a multicore processor, a local memory, and a network interface card (NIC) that enables it to communicate with other nodes in the cluster.  One of the biggest challenges that arises when trying to use the distributed nodes as a single parallel computer is that of **data distribution**.In general, we would want to allocate large data structures that span multiple nodes in the cluster; this logical view of data structures is often referred to as a **global view**. However, a typical physical implementation of this global view on a cluster is obtained by distributing pieces of the global data structure across different nodes, so that each node has a **local view** of the piece of the data structure allocated in its local memory. In many cases in practice, the programmer has to undertake the conceptual burden of mapping back and forth between the logical global view and the physical local views. Since there is one logical program that is executing on the individual pieces of data, this abstraction of a cluster is referred to as the Single Program Multiple Data (SPMD) model.

In this module, we will focus on a commonly used implementation of the SPMD model, that is referred to as the Message Passing Interface (MPI). When using MPI, you designate a fixed set of processes that will participate for the entire lifetime of the global application. It is common for each node to execute one MPI process, but it is also possible to execute more than one MPI process per multicore node so as to improve the utilization of processor cores within the node. Each process starts executing its own copy of the MPI program, and starts by calling the mpi.MPI_Init()  method, where mpi instance of the MPI class used by the process. After that, each process can call the MPI application and the MPI_Comm_rank(mpi.MPI_COMM_WORLD) method to determine the process' own rank within the range,0...(S-1), where S = MPI_Comm_size()

In this lecture, we studied how a global view, XG, of array X can be implemented by S local arrays (one per process) of size, XL.length = XG.length / S. For simplicity, assume that XG.length is a multiple of S. Then, if we logically want to set $XG[i] := i$ for all logical elements of XG,  we can instead set XL[i]:= L*R + i in each local array, where L = XL.length and R = MPI_Comm_rank(). Thus process 0's copy of XL will contain logical elements XG[0...L-1], process 1's copy of XL will contain logical elements XG[L...2*L-1], and so on. Thus, we see that the SPMD approach is very different from client server programming, where each process can be executing a different program.

## Point-to-Point Communication

**Lecture Summary**: In this lecture, we studied how to perform point-to-point communication in MPI by sending and receiving messages. In particular, we worked out the details for a simple scenario in which process 0 sends a string, "ABCD'', to process 1. Since MPI programs follow the SPMD model, we have to ensure that the same program behaves differently on processes 0 and 1. This was achieved by using an if-then-else statement that checks the value of the rank of the process that it is executing on. If the rank is zero, we include the necessary code for calling MPI_Send(); otherwise, we include the necessary code for calling MPI_Recv()  (assuming that this simple program is only executed with two processes). Both calls include a number of parameters. The MPI_Send() call specifies the substring to be sent as a subarray by providing the string, offset, and data type, as well as the rank of the receiver, and a tag to assist with matching send and receive calls (we used a tag value of 99 in the lecture). The MPI_Recv() call (in the else part of the if-then-else statement) includes a buffer in which to receive the message, along with the offset and data type, as well as the rank of the sender and the tag. Each send/receive operation waits (or is blocked) until its dual operation is performed by the other process. Once a pair of parallel and compatible MPI_Send() and MPI_Recv() calls is matched, the actual communication is performed by the MPI library. This approach to matching pairs of send/receive calls in SPMD programs is referred to as two-sided communication.

As indicated in the lecture, the current implementation of MPI only supports communication of (sub)arrays of primitive data types. However, since we have already learned how to serialize and deserialize objects into/from bytes, the same approach can be used in MPI programs by communicating arrays of bytes.

## Message Ordering and Deadlock
**Lecture Summary**: In this lecture, we studied some important properties of the message-passing model with send/receive operations, namely message ordering and deadlock. For message ordering, we discussed a simple example with four MPI processes, R0,R1,R2,R3 (with ranks 0...3  respectively). In our example, process R1 sends message A to process R0 and process R2 sends message B to process R3. We observed that there was no guarantee that process R1's send request would complete before process R2's request, even if process R1 initiated its send request before process R2 Thus, there is no guarantee of the temporal ordering of these two messages. In MPI, the only guarantee of message ordering is when multiple messages are sent with the same sender, receiver, data type, and tag -- these messages will all be ordered in accordance with when their send operations were initiated.

We learned that send and receive operations can lead to an interesting parallel programming challenge called deadlock. There are many ways to create deadlocks in MPI programs. In the lecture, we studied a simple example in which process R0 attempts to send message X to process R1, and process R1 attempts to send message Y to Process R0. Since both sends are attempted in parallel, processes R0 and R1 remain blocked indefinitely as they wait for matching receive operations, thus resulting in a classical deadlock cycle.

We also learned two ways to fix such a deadlock cycle. The first is by interchanging the two statements in one of the processes (say process R1). As a result, the send operation in process R0 will match the receive operation in process R1, and both processes can move forward with their next communication requests. Another approach is to use MPI's sendrecv() operation which includes all the parameters for the send and for the receive operations. By combining send and receive into a single operation, the MPI runtime ensures that deadlock is avoided because a sendrecv() call in process R0 can be matched with a sendrecv() call in process R1 instead of having to match individual send and receive operations.


## Non-Blocking Communications

In this lecture, we studied non-blocking communications, which are implemented via the **MPI_Isend()** and **MPI_Irecv()** API calls.
The I in **MPI_Isend()** and **MPI_Irecv()** stands for "Immediate'' because these calls return immediately instead of blocking until completion. Each such call returns an object of type MPI_Request which can be used as a handle to track the progress of the corresponding send/receive operation.

The main benefit of this approach is that the amount of idle time spent waiting for communications to complete is reduced when using non-blocking communications, since the Isend and Irecv operations can be overlapped with local computations. Also, while it is common for Isend and Irecv operations to be paired with each other, it is also possible for a nonblocking send/receive operation in one process to be paired with a blocking receive/send operation in another process.


## Collective Communication
For a broadcast operation, all MPI processes execute an MPI_Bcast() API call with a specified root process that is the source of the data to be broadcasted. A key property of collective operations is that each process must wait until all processes reach the same collective operation, before the operation can be performed. This form of waiting is referred to as a **barrier**. After the operation is completed, all processes can move past the implicit barrier in the collective call. In the case of MPI_Bcast(), each process will have obtained a copy of the value broadcasted by the root process.



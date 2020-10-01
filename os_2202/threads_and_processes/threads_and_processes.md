# OBL2-OS


## Processes and threads

#### Question 1  

A process is like the word implies a program running, they contain stuff like: code, data, files, registers, stack and threads.

So a thread is a segment of a process and can be controlled by a scheduler


#### Question 2

- If a problem can be split into many smaller parts and they don't rely on eachother for the answer (functional problem) running in paralell for speed gains be logical.


- In a server or something where each parallel part needs to be individual in some way we could find it usefull to seperate into process (for secuirity reason for example)


####  3. Explain why each thread requires a thread control block (TCB).

It contains important information about the thread that the OS needs. One could look at the TCB as a manifesto for the thread. Some examples of values is: Thread ID, Thread state, Priority, and pointers.

-------

#### 4. What is the difference between cooperative (voluntary) threading and pre-emptive (involuntary) thread-ing?  Briefly describe the steps necessary for a context switch for each case.

Cooperative threads will themselv give controll back, so in a way it's trusted to be cooperative and not use super long time on the CPU.

Pre-emptive threading means the OS can take back controll if the thread is using a lot of time or something like that.

We can see how just letting code run and waiting til it finishes is efficient and will have good performance. Tough if something goes wrong and it takes a long time or just crashes we are going to have a problem.

With pre-emptive the code can be slow or crash and the OS will just pause the thread and continue running. Of course there will be some overhead with the extra data and logic.

-----


## C program with POSIX threads


 **1. Which  part  of  the  code  (e.g.,  the  task)  is  executed  when  a  thread  runs?   Identify  the  function  and describe briefly what it does.**

Just looking at the code you would think first all of the threads would be created and then the exit codes printed.

This seems to be the case at the start, but then we se the return codes are printed so that means we must be later in the execution. Also the threads are in random order.

The only reason this could be happening must be that the threads are waiting/not executing immediately.


-------

**2. Why does the order of the “Hello from thread X” messages change each time you run the program?**

The OS is running threads pre-emptive and changing when they start on it's own accord/when it wants to.

-------

  **3. What is theminimum and maximum number of threads that could exist when thread 8 prints “Hello”?**

From the execution we see the order is not strictly defined, therefore the thread could be started first or last. 0 or 9. There is no limit (well there is, but it's big) to how many threads can be paused, but there is a limit to how many can be executed at once.

-------

  **4. Explain the use of pthread join function call.**

It forces the given thread to execute before continuing with the code. Can't print return value if thread hasn't returned yet!


>The pthread_join() function waits for the thread specified by thread
       to terminate.  If that thread has already terminated, then
       pthread_join() returns immediately.  The thread specified by thread
       must be joinable.

-------

  **5. What would happen if the function go is changed to behave.**

Because the computer can do other things while the thread is "sleeping" the other threads would be completed while the n=5 thread is waiting.

-------

  **6. When pthread join returns for thread X, in what state is thread X?**

According to the man page it is terminated.
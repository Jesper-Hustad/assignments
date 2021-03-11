import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayDeque


fun main(args: Array<String>) {

    val worker_threads = Workers(4);
//    val event_loop = Workers(1);

    worker_threads.start(); // Create 4 internal threads
//    event_loop.start(); // Create 1 internal thread

    worker_threads.post {
        sleep(1000);
        println("Task A")
    };

    worker_threads.post {
        sleep(1000);
        println("Task B")
        // Might run in parallel with task A
    };
//
//    event_loop.post {
//        sleep(1000);
//        println("Task C")
//        // Might run in parallel with task A and B
//    };
//
//    event_loop.post {
//        sleep(900);
//        println("Task D")
//        // Will run after task C
//        // Might run in parallel with task A and B
//    };

    worker_threads.join(); // Calls join() on the worker threads
//    event_loop.join(); // Calls join() on the event thread

    println("done")

}

class Workers(private val threadCount : Int) {

    val que = ArrayDeque<() -> Unit>()
    private var threads = (1..threadCount).map { WorkerThread(this) }

    private val lock = ReentrantLock()
    private val jobsAvailableCondition = lock.newCondition()
    private val threadStopCondition = lock.newCondition()

    var stopFlag = false

<<<<<<< HEAD
    private fun post(f: () -> Unit, t : Long) {
        lock.lock()
        que.add(f)
        jobsAvailableCondition.awaitNanos(t)
        jobsAvailableCondition.signalAll()
=======
    fun start() = threads.forEach { it.start() }

      fun post(f: () -> Unit) {
        while(lock.hasQueuedThreads()){
            sleep(10)
        }
        lock.lock()
        que.add(f)
        jobsAvailableCondition.signal()
        lock.unlock()
    }

    fun post_timeout(f: () -> Unit, t: Long) {
        while(lock.hasQueuedThreads()){
            sleep(10)
        }
        lock.lock()
        que.add(f)
        jobsAvailableCondition.awaitNanos(t)
        jobsAvailableCondition.signal()
>>>>>>> c5a34ea65d0f251d1bf5b8fb3c7992f97728d677
        lock.unlock()
    }

    fun post(f: () -> Unit) = post(f, 0)

    fun post_timeout(f: () -> Unit, t: Long) = post(f, t)

    fun stop() {
        lock.lock()
        stopFlag = true
        (1..threadCount).forEach { _ -> jobsAvailableCondition.signal() }
        lock.unlock()
    }

    fun join(){
        stop()
        threads.forEach { it.join() }
    }

    fun start() = threads.forEach { it.start() }

    internal class WorkerThread(private val worker: Workers) : Thread() {

        override fun run() {
            while (true) {

                println("Thread ${this.id}: waiting for lock")

                worker.lock.lock()


//                worker.threadStopCondition.await()

                println("Thread ${this.id}: Got the lock")

                if ( worker.stopFlag && worker.que.isEmpty() ) {
                    worker.lock.unlock()
                    println("Thread ${this.id}: i am stopping now")
                    break
                }

                if( worker.que.isEmpty() ) {
                    println("Thread ${this.id}: waiting for job, stopflag is ${worker.stopFlag}")
                    worker.jobsAvailableCondition.await()
                    println("Thread ${this.id}: I just woke up")
                }

                if( worker.que.isEmpty())
                    println("Thread ${this.id}: I just did a spurious wakeup")

                if( worker.que.isEmpty()) continue

                val job = worker.que.removeFirst()

                worker.lock.unlock()

                job.invoke()
                println("Thread ${this.id}: job completed!")
            }
        }
    }
}



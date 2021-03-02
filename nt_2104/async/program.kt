import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayDeque


fun main(args: Array<String>) {

    val worker_threads = Workers(4);
    val event_loop = Workers(1);

    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post {
        sleep(1000);
        println("Task A")
    };

    worker_threads.post {
        sleep(1000);
        println("Task B")
        // Might run in parallel with task A
    };

    event_loop.post {
        sleep(1000);
        println("Task C")
        // Might run in parallel with task A and B
    };

    event_loop.post {
        sleep(1000);
        println("Task D")
        // Will run after task C
        // Might run in parallel with task A and B
    };

    worker_threads.join(); // Calls join() on the worker threads
    event_loop.join(); // Calls join() on the event thread

}

class Workers(private val threadCount : Int) {

    val que = ArrayDeque<() -> Unit>()
    private var threads = (1..threadCount).map { WorkerThread(this) }

    private val lock = ReentrantLock()
    private val jobsAvailableCondition = lock.newCondition()

    var stopFlag = false

    fun start() = threads.forEach { it.start() }

    fun post(f: () -> Unit) {
        lock.lock()
        que.add(f)
        jobsAvailableCondition.signal()
        lock.unlock()
    }

    fun stop() {
        stopFlag = true
        lock.lock()
        jobsAvailableCondition.signalAll()
        lock.unlock()
    }

    fun join(){
        stop()
        threads.forEach { it.join() }
    }

    internal class WorkerThread(private val worker: Workers) : Thread() {

        override fun run() {
            while (true) {

                worker.lock.lock()

                worker.jobsAvailableCondition.await()

                if (worker.stopFlag ) break

                val job = worker.que.removeFirst()

                worker.lock.unlock()

                job.invoke()
            }
        }
    }
}



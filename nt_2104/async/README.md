## Nettverksprogrammering Øving 4
# Asynkrone kall



## Oppgaven

- Lag Workers klassen med
funksjonaliteten vist under.

- Bruk condition variable.

- post()-metodene skal være
trådsikre (kunne brukes
problemfritt i flere tråder samtidig).

- Valg av programmeringssrpråk er
valgfritt, men ikke Python eller
JavaScript. Java, C++ eller Rust
anbefales, men andre
programmeringsspråk som støtter
condition variables går også fint

- Legg til en Workers metode stop
som avslutter workers trådene for
eksempel når task-listen er tom.

- Legg til en Workers metode
post_timeout() som kjører task
argumentet etter et gitt antall
millisekund.

    - Frivillig: forbedre
post_timeout()-metoden med
epoll i Linux, se neste slides.


```c++
Workers worker_threads(4);
Workers event_loop(1);

worker_threads.start(); // Create 4 internal threads
event_loop.start(); // Create 1 internal thread

worker_threads.post([] {
    // Task A
});

worker_threads.post([] {
    // Task B
    // Might run in parallel with task A
});

event_loop.post([] {
    // Task C
    // Might run in parallel with task A and B
});

event_loop.post([] {
    // Task D
    // Will run after task C
    // Might run in parallel with task A and B
});

worker_threads.join(); // Calls join() on the worker threads
event_loop.join(); // Calls join() on the event thread

```

## Løsning
Jeg gjorde oppgaven i Kotlin, en variant av Java. 

*Konsollen*
```
Task C
Task A
Task B
Task D
```



Jeg bruker ReentrantLock klassen i java som gir oss muligheten til å implementere conditions. I post metoden kan vi se hvordan dette funker.

Når vi skal legge til en oppgave for workeren trenger vi først å låse låsen. Jobben som i vårt tilfelle er en funksjon som returnerer void blir lagt til i en stack/que. Etter oppgaven er lagt til kan vi kalle `jobsAvailableCondition`. Vi bruker `signal()` istedenfor `signalall()` fordi vi har bare lagt til en jobb så trenger derfor bare en tråd. Selv fungerer denne metoden fortsatt med flere tråder fordi om en tråd er optatt med en jobb venter den ikke på en `jobsAvailableCondition`.

```Kotlin
fun post(f: () -> Unit) {
        while(lock.hasQueuedThreads()){
            sleep(10)
        }

        lock.lock()
        que.add(f)
        jobsAvailableCondition.signal()
        lock.unlock()
    }
```

Om vi ser nærmere på worker tråden som er implementert inni Workers klassen ser man logikken av trådene. En viktig del er at låsen låses opp etter at jobben er motatt og deretter gjøres jobben. Om vi gjorde jobben imens tråden fortsatt hadde låsen så kunne bare en tråd kjørt om gangen som hadde ødelagt hele poenget.

```Kotlin
internal class WorkerThread(private val worker: Workers) : Thread() {

        override fun run() {
            while (true) {

                worker.lock.lock()

                worker.jobsAvailableCondition.await()

                if (worker.stopFlag) break

                val job = worker.que.removeFirst()

                worker.lock.unlock()

                job.invoke()
            }
        }
    }
```


Her er hele workers klassen i sin helhet.

*Workers*
```Kotlin
class Workers(private val threadCount : Int) {

    val que = ArrayDeque<() -> Unit>()
    private var threads = (0..threadCount).map { WorkerThread(this) }

    private val lock = ReentrantLock()
    private val jobsAvailableCondition = lock.newCondition()

    var stopFlag = false
    
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

                if (worker.stopFlag) break

                val job = worker.que.removeFirst()

                worker.lock.unlock()

                job.invoke()
            }
        }
    }
}

```


Main metoden er ganske identisk til slik den er skrevet i oppgaven

*Main*
```Kotlin
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

```

## [Link til raw code](./program.kt)

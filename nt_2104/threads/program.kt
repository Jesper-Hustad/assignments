
fun main(args: Array<String>) {

    val threads = ( 1..args[0].toInt() ).map { PrimeThread(args[1].toInt(), args[2].toInt(), it - 1, args[0].toInt()) }

    threads.forEach { it.start() }
    threads.forEach { it.join() }

    println(threads.map { it.primes }.flatten().sorted())
}

internal class PrimeThread(var lowerBound: Int, var upperBound: Int, var threadId: Int, var threadCount: Int) : Thread() {

    var primes =  emptyList<Int>()
    override fun run() {
        primes = (lowerBound..upperBound).filter { (it - lowerBound) % threadCount == 0 }.map { it + threadId }.filter { isPrime(it) }
    }
}

private fun isPrime(n: Int): Boolean = if (n < 2) false else (2..n / 2).none { n % it == 0 }
## Nettverksprogrammering Øving 3
# Programmeringsspråk og tråder
 
 


## Oppgaven

Finn alle primtall mellom to gitte tall ved hjelp av et gitt antall
tråder.
- Skriv til slutt ut en sortert liste av alle primtall som er funnet
- Pass på at de ulike trådene får omtrent like mye arbeid
- Valgfritt programmeringsspråk, men bruk gjerne et
programmeringsspråk dere ikke har prøvd før (ikke JavaScript eller
Python), eller for de som vil ha litt extra utfordring: Rust eller C++.

## Løsning
Valgte å bruke Kotlin fordi dette er et språk jeg ikke har brukt mye tid i, og jeg var spent på hvor mye jeg kunne bruke funksjonell kode design til å korte det ned.

```Kotlin
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
```
## Hvordan å kjøre programmet
Kotlin er basert på JVM, så jeg har kompilert det til en jar som du kan nedlaste [her]().  
Slik kalkulerer du alle primtall med 3 tråder mellom 0 og 25.

Argumenter:
```cmd
<antall tråder> <fra> <til>
```
I kommando vinduet:
```cmd
~ $ java -jar program.jar 3 0 25
[2, 3, 5, 7, 11, 13, 17, 19, 23]
```


## Hvordan funker programmet
Vi antar at trådene får omtrent like mye arbeid ved at vi hopper over like mange tall som mengden tråder og legger på en forskyvning av `threadId`. Litt triks med `(it - lowerbound)` forsikrer at vi ikke hopper over noen av de første tallene. 
```Kotlin
(lowerBound..upperBound).filter { (it - lowerBound) % threadCount == 0 }.map { it + threadId }
```

Vi bruker .join() for å vente på at alle tråder er ferdige med sine operasjoner.
```Kotlin
threads.forEach { it.join() }
```

Deretter hentes primtallene ut fra trådene, blir sortert og skrevet ut.
```Kotlin
println(threads.map { it.primes }.flatten().sorted())
```


## Testing

Det er lett med tråder og obfuskert kode å ha koden egentlig ikke gjøre det du tror den gjør. Funker fordelingen? Jobber trådene egentlig samtidig? Dette er spørsmål jeg finner svar på i denne seksjonen.

### Hvordan fordeles arbeidet
Ved å bruke debugging funksjonaliteten innebygd i IntelliJ kan vi se på de forskjellige trådene å se hvilke primtall som er kalkulert. Dette er resultatet:

| Thread ID | Primtall funnet      |
|-----------|----------------------|
| 0         | `[3]`                |
| 1         | `[7, 13, 19]`        |
| 2         | `[2, 5, 11, 17, 23]` |

Den første runden finner barre tråden med id 2 et primtall, på andre runde finner tråd 0 primtallet 3 og tråd 2 primtallet 5. Når vi ser videre i tabellen følges mønsteret som forventet.

### Jobber de samtidig
Når en tråd har funnet et primtall kan vi printe det ut istedenfor å sette det i en liste, på denne måten kan vi se i hvilken rekkefølge tallene blir kalkulert på.

```Kotlin
override fun run() {
        (lowerBound..upperBound).filter { (it - lowerBound) % threadCount == 0 }.map { it + threadId }
        
        // print prime immediately when found
        .filter { isPrime(it) }.forEach { println(it) }
    }
```

Dette er resultatet: `2 5 11 7 13 19 3 17 23`

Vi ser at tråd 2 finner primtallene: `2 5 11` først, etter dette finner tråd 1: `7 13 19`, tråd 0 finner: `3` og tilslutt finner tråd 2: `17 23`

Dette forteller oss at trådene jobber samtidig. Rekkefølgen er ikke konstant når du kjører programmet flere ganger som er forventet fordi operativsystemet velger forskjellige rekkefølger basert på hvilken del av prosessoren er ledig.
## Nettverksprogrammering Øving 2
# UDP, TLS og ASIO 
 

## Oppgave 1 - Kalkulator med UDP 





### Utskrift

Klient utskrift:

```json
Skriv tall operator tall | Eksempel: '5 + 2' | Skriv exit for å stoppe
> 3 + 4
Motatt: 7.0

Skriv tall operator tall | Eksempel: '5 + 2' | Skriv exit for å stoppe
> 20 - 9
Motatt: 11.0

Skriv tall operator tall | Eksempel: '5 + 2' | Skriv exit for å stoppe
> exit
Avslutter program
```

Tjener utskrift:
```json
Logg for tjenersiden. Nå venter vi...
Fikk en packet!
input er : 3 + 4
Fikk en packet!
input er : 20 - 9
Fikk en packet!
input er : exit
Slutter serveren
```

### Lenker

* [Klient Kode](./Oppg1/SocketKlient.java)  
* [Tjener Kode](./Oppg1/SocketTjener.java)

## Oppgave 2 - TLS/SSL 



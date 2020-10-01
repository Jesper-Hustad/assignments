## **Øving3**
# Normalisering og SQL del 1

## Oppgave 1

```
leieforhold (kunde_id, kunde_navn, kunde_adresse, kunde_tlf,
eiendom_id, eiendom_adresse, eier_id, eier_navn, eier_adresse,
eier_tlf, fra_uke, til_uke, pris)
```
---------

**Q:** Foreslå kandidatnøkler for denne tabellen. Anta at en person kun kan leie en eiendom av gangen, og at en eiendom kan leies ut til kun en person av gangen

**A:** Ut fra spørsmållet vet vi at en person kan ikke flere leieforhold på samme tid.

> Kandidatnøkkel er individuelle kolonner i en tabell som kvalifiserer for unikhet for hver rad

Uheldighvis er det ingen egen kolonne som er en kombinasjon av både person og tid. For eksempel hadde `kunde_id` og `fra_uke` eller `til_uke` vært unike.

------------

**Q:** Tabellen er ikke problemfri mht til registrering og sletting av data. Forklar hvorfor

**A:** Hvis vi skal følge GDPR å skal "glemme" en person er vi i trøbbel. Fordi personen og feriehusene er koblet sammen i samme tabell må også dataen om bruket av feriehuset også slettes (vi kunne forsatt gått gjennom alle steder der navnet dukker opp, men det er mye mer arbeid enn å bare slette en person rad).

Når vi skal registrere en person til et feriehus så må vi også hente all informasjonen om det huset. Altså all informasjonen må settes inn vær gang man skal registrere noe.


### funksjonelle avhengigheter mellom alle attributtene
```

kunde_id   ➡ kunde_navn, 
              kunde_adresse, 
              kunde_tlf


eiendom_id ➡ eiendom_adresse,
              pris,
              eier_id


eier_id    ➡ eier_navn, 
              eier_adresse,
              eier_tlf
```

### Ny oppsplitting

Oppsplitting av tabellen i mindre tabeller slik at problemene vedr. registrering og sletting av data unngås.

```
leieforhold (kunde_id, eiendom_id, fra_uke, til_uke, pris)
```

```
kunde (kunde_id, kunde_navn, kunde_adresse, kunde_tlf)
```

```
eiendom (eiendom_id, eiendom_adresse,  eier_id)
```

```
eier (eier_id, eier_navn, eier_adresse, eier_tlf)
```

### 1NF 

For at alle kolonnene skal være atomære må det være ingen lister av verdier i tabellen. Allerede er alle kolonnene unike. Da har vi har oppnådd 1NF.



### 2NF


For 2NF må tabellen ikke inneholde partielle avhengigheter (en ikke-nøkkel-attributt er f.d. av en del av primærnøkkelen). Fra det vi så på tidligere er primærnøkellen en kombinasjon av kunde og tid eller eiendom og tid. Derfor er kunde_navn informasjon en ikke-nøkkel-attributt fordi den avhenger av `kunde_id`, imens nøkkelen er en kominasjon av kunde_id og tid.


```
leieforhold (kunde_id, eiendom_id, fra_uke, til_uke, pris)
```

```
kunde (kunde_id, kunde_navn, kunde_adresse, kunde_tlf)
```

```
eiendom (eiendom_id, eiendom_adresse, eier_id, eier_navn, eier_adresse, eier_tlf)
```



### 3NF  

Eieren er koblet til eiendommen, men for 3NF skal alle kolonnene bare være bestemt av nøkellen og ingen andre verdier. I vårt tilfellet er eier informasjonen også koblet til `eier_id`. 


```
leieforhold (kunde_id, eiendom_id, fra_uke, til_uke, pris)
```

```
kunde (kunde_id, kunde_navn, kunde_adresse, kunde_tlf)
```

```
eiendom (eiendom_id, eiendom_adresse,  eier_id)
```

```
eier (eier_id, eier_navn, eier_adresse, eier_tlf)
```


## Oppgave 2 SQL
### Database   
I denne oppgavenskal vi brukeenborettslag-databasen. Brukfølgendesql-script: MySQL. Scriptet inneholder datasom oppgavene nedenfor spør etter. Sett opp SELECT-setninger som besvarer spørsmålene nedenfor. Kun én setning pr oppgave.  

1. Finn alle borettslag etablert i årene 1975-1985.

Kan også bruke `BETWEEN` operator.

```SQL
SELECT * FROM `borettslag` WHERE borettslag.etabl_aar >= 1975 AND borettslag.etabl_aar <= 1985
```

2. Skriv ut en liste over andelseiere. Listen skal ha linjer som ser slik ut (tekster i kursiv
er data fra databasen):
"fornavn etternavn, ansiennitet: ansiennitet år".
Listen skal være sortert på ansiennitet, de med lengst ansiennitet øverst.

```SQL
SELECT fornavn, etternavn, ansiennitet FROM `andelseier` ORDER BY ansiennitet DESC
```


3. I hvilket år ble det eldste borettslaget etablert?

```SQL
SELECT MIN(etabl_aar) FROM `borettslag`
```

4. Finn adressene til alle bygninger som inneholder leiligheter med minst tre rom.


```SQL
SELECT MAX(leilighet.ant_rom), bygning.bygn_id
FROM bygning
JOIN leilighet
ON bygning.bygn_id = leilighet.bygn_id
WHERE leilighet.ant_rom > 3
GROUP BY bygn_id
```


5. Finn antall bygninger i borettslaget "Tertitten".


```SQL
SELECT COUNT(bygning.bygn_id)
FROM borettslag
JOIN bygning
ON bygning.bolag_navn = borettslag.bolag_navn
WHERE  borettslag.bolag_navn = 'Tertitten'
GROUP BY borettslag.bolag_navn
```

6. Lag en liste som viser antall bygninger i hvert enkelt borettslag. Listen skal være
sortert på borettslagsnavn. Husk at det kan finnes borettslag uten bygninger - de skal
også med.


```SQL
SELECT COUNT(bygning.bygn_id), borettslag.bolag_navn
FROM borettslag
LEFT JOIN bygning
ON bygning.bolag_navn = borettslag.bolag_navn
GROUP BY borettslag.bolag_navn
ORDER BY borettslag.bolag_navn ASC
```

7. Finn antall leiligheter i borettslaget "Tertitten".


```SQL
SELECT COUNT(leilighet.leil_nr)
FROM borettslag
JOIN bygning ON bygning.bolag_navn = borettslag.bolag_navn 
JOIN leilighet ON bygning.bygn_id = leilighet.bygn_id
WHERE  borettslag.bolag_navn = 'Tertitten'
```

8. Hvor høyt kan du bo i borettslaget "Tertitten"?


```SQL
SELECT MAX(leilighet.etasje) 
FROM borettslag
JOIN bygning ON bygning.bolag_navn = borettslag.bolag_navn 
JOIN leilighet ON bygning.bygn_id = leilighet.bygn_id
WHERE  borettslag.bolag_navn = 'Tertitten'
```

9. Finn navn og nummer til andelseiere som ikke har leilighet.


```SQL
SELECT andelseier.fornavn, andelseier.etternavn, andelseier.telefon
FROM `andelseier`
LEFT JOIN leilighet
ON leilighet.and_eier_nr = andelseier.and_eier_nr
WHERE leilighet.leil_nr IS NULL
```

10. Finn antall andelseiere pr borettslag, sortert etter antallet. Husk at det kan finnes
borettslag uten andelseiere - de skal også med.


```SQL
SELECT COUNT(andelseier.and_eier_nr), borettslag.bolag_navn
FROM borettslag
LEFT JOIN bygning ON bygning.bolag_navn = borettslag.bolag_navn 
LEFT JOIN leilighet ON bygning.bygn_id = leilighet.bygn_id
LEFT JOIN andelseier ON andelseier.and_eier_nr = leilighet.and_eier_nr
GROUP BY bolag_navn
ORDER BY COUNT(andelseier.and_eier_nr) DESC
```

11. Skriv ut en liste over alle andelseiere. For de som har leilighet, skal
leilighetsnummeret skrives ut.


```SQL
SELECT andelseier.fornavn, andelseier.etternavn, andelseier.telefon, leilighet.leil_nr FROM `andelseier`
LEFT JOIN leilighet
ON leilighet.and_eier_nr = andelseier.and_eier_nr
```

12. Hvilke borettslag har leiligheter med eksakt 4 rom?


```SQL
SELECT *
FROM borettslag
LEFT JOIN bygning ON bygning.bolag_navn = borettslag.bolag_navn 
LEFT JOIN leilighet ON bygning.bygn_id = leilighet.bygn_id
WHERE  leilighet.ant_rom = 4
```

13. Skriv ut en liste over antall andelseiere pr postnr og poststed, begrenset til de som bor i
leiligheter tilknyttet et borettslag. Husk at postnummeret til disse er postnummeret til
bygningen de bor i, og ikke postnummeret til borettslaget. Du trenger ikke ta med
poststeder med 0 andelseiere. (Ekstraoppgave: Hva hvis vi vil ha med poststeder med
0 andelseiere?)


```SQL
SELECT COUNT(andelseier.and_eier_nr), poststed
FROM `poststed`
LEFT JOIN bygning
ON bygning.postnr = poststed.postnr
LEFT JOIN leilighet
ON bygning.bygn_id = leilighet.bygn_id
LEFT JOIN andelseier
ON andelseier.and_eier_nr = leilighet.and_eier_nr
GROUP BY poststed.poststed
```


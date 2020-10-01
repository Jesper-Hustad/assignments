## **Øving4**
# SQL del 2

## Oppgave 1

## a.
 List ut all informasjon (ordrehode og ordredetalj) om ordrer for leverandør nr 44.

```sql
SELECT ordrehode.ordrenr, ordrehode.dato, ordrehode.levnr, ordrehode.status, ordredetalj.delnr, ordredetalj.kvantum
FROM `levinfo` 
JOIN ordrehode 
ON ordrehode.levnr = levinfo.levnr 
JOIN ordredetalj
ON ordrehode.ordrenr=ordredetalj.ordrenr
WHERE levinfo.levnr=44
```



## b.
 Finn navn og by ("LevBy") for leverandører som kan levere del nummer 1.

```sql
SELECT levinfo.navn, levinfo.levby
FROM delinfo
JOIN prisinfo ON prisinfo.delnr = delinfo.delnr
JOIN levinfo ON levinfo.levnr = prisinfo.levnr
WHERE delinfo.delnr=1
```

## c.
 Finn nummer, navn og pris for den leverandør som kan levere del nummer 201 til
billigst pris.

```sql
SELECT levinfo.levnr, levinfo.levby, prisinfo.pris
FROM delinfo
JOIN prisinfo ON prisinfo.delnr = delinfo.delnr
JOIN levinfo ON levinfo.levnr = prisinfo.levnr
WHERE delinfo.delnr=201
ORDER BY prisinfo.pris
LIMIT 1
```

## d.
 Lag fullstendig oversikt over ordre nr 16, med ordrenr, dato, delnr, beskrivelse,
kvantum, (enhets-)pris og beregnet beløp (=pris*kvantum).

```sql
SELECT 
ordredetalj.ordrenr, 
ordrehode.dato, 
ordredetalj.delnr, 
delinfo.beskrivelse, 
ordredetalj.kvantum, 
prisinfo.pris, 
(ordredetalj.kvantum * prisinfo.pris) AS "beregnet_kostnad"
FROM `ordrehode`
JOIN ordredetalj ON ordredetalj.ordrenr=ordrehode.ordrenr
JOIN delinfo ON delinfo.delnr = ordredetalj.delnr
JOIN prisinfo ON prisinfo.delnr=ordredetalj.delnr AND prisinfo.levnr=ordrehode.levnr
WHERE ordrehode.ordrenr=16
```


## e.
 Finn delnummer og leverandørnummer for deler som har en pris som er høyere enn
prisen for del med katalognr X7770.



```sql
SELECT prisinfo.delnr, prisinfo.levnr 
FROM `prisinfo` 
WHERE prisinfo.pris > (SELECT prisinfo.pris 
                        FROM prisinfo 
                        WHERE prisinfo.katalognr="X7770")
```

## f.
### i)
Tenk deg at tabellen levinfo skal deles i to. Sammenhengen mellom by og fylke skal
tas ut av tabellen. Det er unødvendig å lagre fylkestilhørigheten for hver forekomst av
by. Lag én ny tabell som inneholder byer og fylker. Fyll denne med data fra levinfo.
Lag også en tabell som er lik levinfo unntatt kolonnen Fylke. (Denne splittingen av
tabellen levinfo gjelder bare i denne oppgaven. I resten av oppgavesettet antar du at du
har den opprinnelige levinfo-tabellen.)


```sql
CREATE TABLE `jesperfh`.`steder` 
( 
    `postnr` INTEGER NOT NULL ,
    `fylke` VARCHAR(100) NOT NULL ,
    `by` VARCHAR(100) NOT NULL ,
    PRIMARY KEY (`postnr`)
)
```



```sql
ALTER TABLE `levinfo` ADD INDEX(`postnr`);
    ```


```sql
ALTER TABLE `levinfo` DROP `levby`;
```

```sql
ALTER TABLE `levinfo` DROP `fylke`;
```



|postnr|fylke      |by_navn  |
|------|-----------|---------|
|1222  |Oslo       |Oslo     |
|1234  |Oslo       |Oslo     |
|1456  |Østfold    |Ås       |
|3345  |Telemark   |Ål       |
|7023  |S-Trøndelag|Trondheim|



### ii)
 Lag en virtuell tabell (view) slik at brukerne i størst mulig grad kan jobbe på
samme måte mot de to nye tabellene som den gamle. Prøv ulike kommandoer mot
tabellen (select, update, delete, insert). Hvilke begrensninger, hvis noen, har brukerne i
forhold til tidligere?

```sql
CREATE VIEW virtuelllevinfo AS 
SELECT * FROM `levinfo` NATURAL JOIN steder
```

Update fungerer ikke helt med view fordi det kan være komplekse select statements bak dem.


## g.
 Anta at en vurderer å slette opplysningene om de leverandørene som ikke er
representert i Prisinfo-tabellen. Finn ut hvilke byer en i tilfelle ikke får leverandør i.
(Du skal ikke utføre slettingen.) (Tips: Svaret skal bli kun én by, "Ål".)

```sql
SELECT levinfo.levby FROM `levinfo`
LEFT JOIN prisinfo ON prisinfo.levnr = levinfo.levnr
GROUP BY levinfo.levby HAVING COUNT(levinfo.levby) = 1
```

```sql
SELECT levby from levinfo WHERE levby not in (SELECT levby FROM prisinfo NATURAL JOIN levinfo)
```


## h.
 Finn leverandørnummer for den leverandør som kan levere ordre nr 18 til lavest totale
beløp (vanskelig).


Yupp

```sql
SELECT prisinfo.levnr, SUM(ordredetalj.kvantum * prisinfo.pris) 'cost'
FROM `ordrehode`
NATURAL JOIN ordredetalj
JOIN prisinfo ON prisinfo.delnr = ordredetalj.delnr
WHERE ordrenr = 18
GROUP BY prisinfo.levnr 
HAVING COUNT(prisinfo.delnr) = (SELECT COUNT(ordrenr) FROM `ordrehode` NATURAL JOIN ordredetalj WHERE ordrenr = 18)
```

## Oppgave 2


### a)
```sql
SELECT * FROM `forlag`HAVING SUBSTRING(forlag.telefon, 1, 2) = '22'
UNION
SELECT * FROM `forlag`HAVING SUBSTRING(forlag.telefon, 1, 2) != '22' OR forlag.telefon IS NULL
```

### b)
```sql
SELECT AVG(YEAR(CURRENT_DATE) - fode_aar) 'alder_avg' FROM `forfatter`
WHERE fode_aar 
IS NOT NULL 
AND (dod_aar IS NOT NULL OR fode_aar > 1900)
```

### c)
```sql
SELECT COUNT(forfatter_id) FROM `forfatter` WHERE fode_aar IS NOT NULL
```

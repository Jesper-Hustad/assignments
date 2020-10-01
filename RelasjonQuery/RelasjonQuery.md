# Øving 2: Relasjonsmodellen, del 2

## Oppgave 1 

### a)

- Seleksjon er hvilken rader som blir returnert

- Projeksjon er hvilken kolonne i spørringen som blir returnert 

```SQL
SELECT tittel, utgitt_aar FROM `bok` WHERE forlag_id=2
```

**Seleksjon**: `tittel, utgitt_aa`  
**Projeksjon**: `forlag_id=2`

### b)

```SQL
SELECT forlag_navn, tittel FROM `forlag`, `bok`
```

Her får vi alle mulige kombinasjoner av bok og forlag, altså en tabell med alle bøkene koblet til alle forlagene. Her er et eksempel av det vi får når man kjører SQL coden over.  


| forlag_navn          | tittel        |
|----------------------|---------------|
| Cappelen             | Universet     |
| Universitetsforlaget | Universet     |
| Achehaug             | Universet     |
| Oktober              | Universet     |
| Tiden                | Universet     |
| Harper Collins       | Universet     |
| Tapir                | Nølen         |
| Gyldendal            | Nølen         |
| Cappelen             | Nølen         |
| Universitetsforlaget | Nølen         |
| Achehaug             | Nølen         |
| Oktober              | Nølen         |
| Tiden                | Nølen         |
| Harper Collins       | Nølen         |
| Tapir                | Markens gråde |
| Gyldendal            | Markens gråde |


### c)

`EQUIJOIN` og `NATURAL JOIN` er to forskjellige måter å join to tabeller i SQL. Når man bruker natural join så finner databasen selv hvilke to kolonner som har like navn og bruker de veriene til å matche dem til å legge sammen tabellene.  

I equijoin så skriver man selv i spørringen hvilke kolonner man vil sammenligne og disse tabellene trenger ikke å ha samme navn heller.  

#### Natural join
```SQL
SELECT * FROM bok NATURAL JOIN forlag
```

#### Equijoin
```SQL
SELECT tittel, utgitt_aar, forlag_navn 
FROM `bok`,`forlag` 
WHERE bok.forlag_id=forlag.forlag_id
```

#### Resultat fra begge:

| tittel             | utgitt_aar | forlag_navn    |
|--------------------|-----------:|----------------|
| Silkeridderen      |       1994 | Gyldendal      |
| Den hvite hingsten |       1992 | Gyldendal      |
| Hunder             |       1992 | Gyldendal      |
| Rebecca            |       1981 | Cappelen       |
| Klosterkrønike     |       1982 | Cappelen       |
| Universet          |       1988 | Cappelen       |
| Nølen              |       1978 | Cappelen       |
| Se terapeuten      |       1998 | Cappelen       |
| Sa mor             |       1996 | Cappelen       |
| Jubel              |       1995 | Cappelen       |
| Tatt av kvinnen    |       1993 | Cappelen       |
| Supernaiv          |       1996 | Cappelen       |
| Gutter er gutter   |       1995 | Achehaug       |
| Bridget Jones      |       1995 | Achehaug       |
| Tåpenes            |       1995 | Tiden          |
| Microserfs         |       1991 | Harper Collins |
| Generation X       |       1995 | Harper Collins |


Generelt er equijoin foretrukket fordi man explesivt velger kolonnene. Dette er en god ide fordi i fremtiden kan man ville ønske å legge til eller forandre på tabeller, med natrual join kan man da få problemer fordi tabellnavnene stemmer ikke overens.

I vårt tilfelle så får vi samme resultat fra begge spørringene og natrual join. Natrual join er kortere som er en av fordelene av metoden, men hvis for eksempel `forlag_id` het `id` hadde ikke metoden fungert som er den store ulempen med natrual join

### d)


>To tabeller sies å være unionskompatible hvis begge tabellene har samme antall attributter (kolonne) og tilsvarende attributter har samme datatype (int, char, float, date etc.)  

Konsulent og forfatter kan bli unionkompatible fordi de begge har `fornavn` og `etternavn`, det er derfor ganske åpenbart å bruke union på dem.

```SQL
SELECT fornavn, etternavn FROM forfatter
UNION
SELECT fornavn, etternavn FROM konsulent;
```

------

Oppgaven ønsker at vi skal gjøre union mer den en gang, når man ser på tabellene som står igjen er det få logiske ting å bruke union på. Så her har jeg valgt å ta union på forfatter navn og fødselsår med bok navn og utgitt år.  

```SQL
SELECT tittel, utgitt_aar FROM bok
UNION
SELECT fornavn, fode_aar FROM forfatter;
```

## Oppgave 2 

### a)
```SQL
SELECT forlag_navn FROM `forlag`
```

**Seleksjon**: forlag_navn  
**Projeksjon**: ingen

### b)
```SQL
SELECT * FROM forlag
WHERE forlag.forlag_id IN (
    SELECT bok.forlag_id FROM bok
)
```

Brukte `IN` operasjonen fordi da det er logisk.

>IN-operatøren er en kortere versjon av flere ELLER operasjoner.

**Seleksjon**: alle  
**Projeksjon**: forlag som er referet til i bok tabellen

### c)
```SQL
SELECT * FROM `forfatter` WHERE forfatter.fode_aar=1948
```

**Seleksjon**: alle  
**Projeksjon**: forfattere med fødsel år lik 1948

### d)  

```SQL
SELECT * FROM forlag
WHERE forlag.forlag_id IN (
    SELECT forlag_id FROM `bok` WHERE bok.tittel = "Generation x"
)
```

Brukte `IN` operasjonen fordi da det er logisk.  

**Seleksjon**: alle  
**Projeksjon**: bok med navn lik "Generation x"


### e)


En relasjonstabell kobler to tabeller sammen med relesjonene i form av fremmednøkler. Trenger en ganske lang SQL spørring for å koble dem sammen.
For denne oppgaven er det litt overkill men jeg hadde lagd spørringen fra før så jeg bare brukte den. Her bruker vi inner join som er en type equijoin.


```SQL
SELECT tittel, utgitt_aar, fornavn, etternavn, nasjonalitet
FROM bok_forfatter
    INNER JOIN bok
        ON bok_forfatter.bok_id = bok.bok_id
    INNER JOIN forfatter 
        ON bok_forfatter.forfatter_id = forfatter.forfatter_id
WHERE fornavn="Hamsun"
```

### f)

```SQL
(
    SELECT tittel, utgitt_aar, forlag_navn, forlag.adresse, forlag.telefon  
	FROM `bok`,`forlag` 
	WHERE bok.forlag_id=forlag.forlag_id
)
UNION
(
    SELECT null as `tittel`, null as `utgitt_aar`, 
    forlag_navn, forlag.adresse, forlag.telefon FROM forlag
	WHERE forlag.forlag_id NOT IN (
    	SELECT bok.forlag_id FROM bok
    )
)
```

Her kombinerer vi kode fra før med en union og `collum as` for å legge sammen alt.  
Finnes kansje en enklere SQL-spørrings som gjør dette mer effektivt, men denne løsningen fungerer.

Operasjonene brukte er `JOIN` for første delen, `IN` for den siste, og satt alt sammen med en `UNION`
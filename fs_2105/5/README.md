## IDATT2105 - Full-stack applikasjonsutvikling
# Øving 5 - : Kalkulator med backend


## Oppgave

Denne øvingen er en enkel utvidelse av forrige øving, hvor du skal implementere et rest-api i backend for å utføre regneoperasjonene i kalkulatoren. Regnestykkene skal sendes over til backend via async-kall og resultatet man får tilbake skal vises displayet (husk feilhåndtering).

En kan selv velge hvor enkelt eller vanskelig man vil gjøre dette. En måte å gjøre det enkelt på, er å si at man tillater kun én type regneoperasjon i gangen (bare divisjon, f.eks), og ha et rest-endepunkt for hver regneoperasjon (gange, dele, plusse og minuse (det er et ord, ikke slå det opp)). En er da nødt til å ha feilhåndtering med info til brukeren i kalkulatoren, om han/hun prøver å bruke flere operasjoner. Om du vil ha en mer avansert løsning som inneholder mer funksjonalitet, er dette selvsagt også lov :)

## Løsning
Trykk på bildet for å se video demo.
[![](screenshot.jpg)](https://www.youtube.com/watch?v=3dN9Lv7AnHU)

### Backend

Jeg brukte NodeJS som backend.  
Serveren returnerer en JSON med status og resultat.  
La til noen tester som returnerer feilmeldinger.  

```javascript
app.get('/api/v1/equation', (req, res) =>
    res.json(calculate(req.query.equation))
)

const calculate = (equation, errorMsg = hasError(equation)) => (
    {
        "status": (errorMsg) ? "error" : "success",
        "result": (errorMsg) ? errorMsg[0] : eval(equation)
    })

const hasError = (equation) => Object.entries(
    {
        "Calculation is empty": hasNoCalculation,
        "Can't divide by zero": hasDivisionByZero,
        "Only one decimal allowed": hasMultipleDecimals,
        "Must operate on number": hasDuplicateOperators,
        "Must close parentheses": hasUnbalancedParentheses
    })
    .find(([_, failsTest]) => failsTest(equation))

```
### Frontend

I frontend bytta jeg ut kalkulasjonsdelen med en `queryServer` metode jeg la til.  
URLSearchParams objektet håndterer escaping av strengene.  
Programnøkkelordene async og await gjør koden mer lesbar og forståelig.  

```javascript
async queryServer(equation) {
      const params = { equation: equation };
      const url = "/api/v1/equation?" + new URLSearchParams(params).toString();
      const response = await fetch(url);
      const data = await response.json();

      return data;
    }
```

### Notater
For å forstå bedre hva som foregår i bakgrunnen skriver jeg ut `HTTP GET` metoden og `JSON` responsen i frontend.  


Implementerte "status" delen ved å gjøre skriften rød når kalkulasjonen returnerer `error`.  


Prøvde å implementere backend delen med "funksjonell" kode fordi hvorfor ikke :)

```javascript
const hasNoCalculation = (equation) => equation
    == ''

const hasDivisionByZero = (equation) => equation
    .includes('/0')

const hasMultipleDecimals = (equation) => equation
    .split('')
    .filter(i => i == '.').length > 1

const hasDuplicateOperators = (equation) => equation
    .split('')
    .map(i => "+-/*%.".includes(i))
    .some((_, i, arr) => arr[i] && arr[i + 1])

const hasUnbalancedParentheses = (equation) => equation
    .split('')
    .filter(i => "()".includes(i))
    .map(i => i == '(')
    .map(i => i ? 1 : -1)
    .reduce((i, j) => i + j, 0) != 0
```

### Setup
Nedlast prosjektet med git eller fra en zip fil [her](https://downgit.github.io/#/home?url=https://github.com/Jesper-Hustad/assignments/tree/master/fs_2105/5)

```
npm install
node index.js

> Server started at http://localhost:8080
```
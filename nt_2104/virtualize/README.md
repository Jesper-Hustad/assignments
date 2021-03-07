## Nettverksprogrammering Øving 5
# Virtualisering
 


## Oppgaven
- Lag en nettside som lar deg
(kompilere og) kjøre kildekode
som blir skrevet inn

    - Resultatet skal vises på
nettsiden

- Bruk Docker til å (kompilere og)
kjøre kildekoden trygt på
serversiden

- Valgfritt programmeringsspråk
(JavaScript og Python kan også
brukes)

## Løsning

Jeg har lastet opp en video av websiden i bruk på YouTube, først kjører jeg en enkel "hello world", deretter et program som printer ut fibonacci sekvensen. Om en ny IP ikke har blitt satt for serveren min (uheldigvis ganske sansynlig) kan du teste websiden på JesperHustad.com

<!-- DIRECT YOUTUBE THUMBNAIL FALLBACK
[![Video av virtualisering](https://img.youtube.com/vi/fmvWEYnWIlY/0.jpg)](https://youtu.be/fmvWEYnWIlY) -->

[![Video av virtualisering](https://i.imgur.com/v77Xa44.png)](https://youtu.be/fmvWEYnWIlY)


Trykk på bilde for å se YouTube videoen

## Server siden

Dockerfilen ser slik ut

```dockerfile
FROM openjdk:8
RUN javac Main.java
CMD ["java", "Main"]
```

På server siden så skrives brukes nodejs. Koden skrives til en temp filen og docker kompilerer og kjører koden. 

```javascript
// compiles java code in docker container and returns result
async function run_code(code_string){

    await fs.writeFile('Main.java', code_string);
    
    await exec("docker build -t compile-app .")

    return (await exec("docker run --rm compile-app")).stdout
} 

// routes
app.get('/', (req, res) => 
    res.status(200).sendFile(__dirname + '/index.html') 
)

app.post('/compile', async (req, res) => 
    res.send({"code_output" : (await run_code(req.body.code_input))}) 
)

app.listen(80, () => {
    console.log('http://localhost:80')
})
```

## Klient siden
På websiden er dette metoden som kjøreres nå man trykker på "compile and run".

```javascript
async function send_code(){

    const input = document.getElementById("code_input")
    const output = document.getElementById("code_output")

    // clear output and activate loading gif
    output.textContent = ""
    output.classList.toggle("loading")

    // send code to server to compile
    let raw_response = await fetch('/compile', {
        method: 'POST', 
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({"code_input": input.value}),
    })

    // parse response
    let code_output = (await raw_response.json()).code_output

    // display code output and remove loading gif
    document.getElementById("code_output").textContent = code_output
    output.classList.toggle("loading")
}
```
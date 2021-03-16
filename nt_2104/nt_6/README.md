## Nettverksprogrammering Øving 6
# WebSocket



## Oppgaven
Ta utgangspunkt i  rfc6455 og programmer et WebSocket-server bibliotek som gjennom socket-programmering (du skal  ikke bruke et ferdig WebSocket bibliotek på serversiden):

- Utfører handshake med klienter
- Leserer korte meldinger fra klienter
- Kan sende korte meldinger til tilknyttede klienter


Med WebSocket-server biblioteket skal du lage et WebSocket endepunkt som kan motta melding fra en klient, og deretter sender mottatt melding til alle tilknyttede klienter.

Du velger selv programmeringsspråk, men et alternativ er å starte med inklurdert serverside JavaScript-kode:


## Løsning

Denne oppgaven består i utgangspunktet av disse 3 delene: 
- Generere en handshake med riktig Sec-WebSocket-Accept
- Lage nye meldinger i riktig WebSocket format
- Dekode meldinger med mask


I denne oppgaven har jeg delt disse i vær sin funksjon så vi kan se litt nærmere på dem.

## Handshake
Her handler det om å lese `Sec-WebSocket-Key` og følge websocket spec med denne nøkkelen.

I funksjonen `generateHandshakeHeaders()` blir respons headers laget.

Crypto modulen i NodeJS hjelpe oss med SHA og base64 omgjøring.

```javascript
function generateHandshakeHeaders(headers){
    
    const key = headers.split('\n').filter(s => s.includes("Sec-WebSocket-Key"))[0].substring(19, 19+24)

    const GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
    const acceptKey = crypto.createHash("sha1").update(key + GUID, "binary").digest("base64");
        
    return [
        "HTTP/1.1 101 Web Socket Protocol Handshake",
        "Upgrade: websocket",
        "Connection: Upgrade",
        "Sec-WebSocket-Accept: " + acceptKey
        ,"",""].join("\r\n")
}
```

## Generere meldinger

I WebSocket spec så er meldingen som blir sent fra serveren relativt enkle fordi det ikke er noe masking som må gjøres.

Første byte er formatet som i vårt tilfelle er tekst og derfor `0x81`

Ande byte sin første bit er om meldingen er maskert. En WebSocket server skal aldri maskere meldingene den sender ut så vi kan la den være `0`.

Andre byte sine siste 7 bits er lengden på meldingen, dette gjelder bare så lenge meldingene er under 128 (som er for oss alltid vårt tilfelle ifølge oppgaven). 


```javascript
function generateWebSocketMessage(message){
    
    const messageLength = '0x' + (message.length).toString(16)
    const messageBytes = Buffer.from(message)
    
    return Buffer.from([0x81, messageLength, ...messageBytes])
}
```

## Dekode meldinger
Denne er tatt fra side 8 i presentasjonen vår om WebSockets.

Operatoren `^` eller caret på engelsk er en XOR operator.
I denne funksjonen går for loopen gjennom meldingsdelen og tar XOR på meldingen og riktig del av masken for å da hente ut den orginale meldingen.

```javascript
function decodeWebSocketMessage(messageBytes){
    let bytes = Buffer.from(messageBytes);
    
    let length = bytes[1] & 127;
    let maskStart = 2;
    let dataStart = maskStart + 4;

    let result = ""

    for (let i = dataStart; i < dataStart + length; i++) {
        let byte = bytes[i] ^ bytes[maskStart + ((i - dataStart) % 4)];
        result += String.fromCharCode(byte)
    }

    return result
}
```

## Alt koblet sammen
I funksjonen `handleWebSocketDataConnection()` tas alle disse forrige delene og koples sammen for å skape en fungerende server.

```javascript
function handleWebSocketDataConnection(connection, data){

    // check if it's a handshake
    if(data.toString().substring(0,14) == "GET / HTTP/1.1"){ 
        
        // generate response headers with Sec-WebSocket-Accept
        const headers = data.toString()
        const responseHeaders = generateHandshakeHeaders(headers)
        
        // send handskade response to client 
        connection.write(responseHeaders);
        
        // add connection to users list
        users.push(connection)

        return
    }
    
    // parse message
    const recievedMessage = decodeWebSocketMessage(data)

    // forward recieved message to all other users (except themselves)
    const forwardMessage = generateWebSocketMessage(recievedMessage)
    users.filter(user => user != connection).forEach(user => user.write(forwardMessage))
}
```



## Setup

Nedlast koden [her]() og unzip

```
cd nt_6
node program.js
```
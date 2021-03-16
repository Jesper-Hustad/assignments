const net = require('net');
const crypto = require("crypto");
const fs = require('fs').promises;

// store connected users
var users = []

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

// create byte array following WebSocket server message protocol
function generateWebSocketMessage(message){
    
    const messageLength = '0x' + (message.length).toString(16)
    const messageBytes = Buffer.from(message)
    
    return Buffer.from([0x81, messageLength, ...messageBytes])
}

// function from presentation page 8
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


// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
    connection.on('data', async () => {

        const content = await fs.readFile("index.html", "utf8")

        connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
    });
});
  
httpServer.listen(3002, () => console.log('HTTP server listening on port 3002'));


// Incomplete WebSocket server
const wsServer = net.createServer((connection) => {

    console.log('Client connected');

    connection.on('data', data => handleWebSocketDataConnection(connection, data));

    connection.on('end', () => console.log('Client disconnected'));
});

wsServer.on('error', (error) => console.error('Error: ', error));

wsServer.listen(3001, () => console.log('WebSocket server listening on port 3001'));

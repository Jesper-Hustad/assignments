
// Imports
const util = require('util');
const exec = util.promisify(require('child_process').exec);
const fs = require('fs').promises;

const express = require('express');
const app = express();
app.set('view engine', 'pug');

app.use(express.urlencoded({extended: true}))
app.use(express.json())


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

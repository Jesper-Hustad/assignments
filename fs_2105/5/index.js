const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 8080;

app.use(express.static(__dirname + '/'));

app.get('/', function(req, res) {
  res.sendFile(__dirname + '/index.html')
});

app.get('/api/v1/equation', (req, res) => 
    res.json(calculate(req.query.equation))
)

function calculate(equation){

    if(equation.includes('/0')) 
        return {"status" : "error", "result" : "Can't devide by zero"}


    if(hasDuplicateOperators(equation)) 
        return {"status" : "error", "result" : "must operate on number"}

    return {"status" : "success", "result" : eval(equation)}
}

const hasDuplicateOperators = (equation) => {
    const operators = "+-/*"

    let wasOperator = false

    for (const i of equation.split('')) {

        const newChar = operators.includes(i) 

        if(wasOperator && newChar) return true
        
        wasOperator = newChar
    }

    return false
}

app.listen(port);
console.log('Server started at http://localhost:' + port);
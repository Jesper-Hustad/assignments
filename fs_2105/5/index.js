const express = require('express');
const path = require('path');

const app = express();
const port = process.env.PORT || 8080;

app.use(express.static(__dirname + '/'));

app.get('/', function (req, res) {
    res.sendFile(__dirname + '/index.html')
});

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


app.listen(port);
console.log('Server started at http://localhost:' + port);
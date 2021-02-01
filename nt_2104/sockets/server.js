const express = require('express')
const app = express();
const bodyParser = require('body-parser');
app.use(bodyParser.json());
const port = 8000;



// defining available operations
const availableOperations = {
  'addition' : new Operation( (a,b) => a + b),
  'subtraction' : new Operation( (a,b) => a - b),
}

app.get('/api/v1/math/:operation/:numOne/:numTwo', (req, res) => {

  const rawNumOne = req.params.numOne
  const rawNumTwo = req.params.numTwo
  const operation = req.params.operation
  
  // ERROR checking
  if( !Object.keys(availableOperations).includes(operation) ){
    throwError(res, `Could not find operation: '${operation}'`)
  }

  if(isNaN(rawNumOne)){
    throwError(res, `Could not interpret numOne: '${req.numOne}'`)
  }

  if(isNaN(rawNumTwo)){
    throwError(res, `Could not interpret numOne: '${req.numTwo}'`)
  }

  // parsing numbers
  const numOne = Number(rawNumOne)
  const numTwo = Number(rawNumTwo)

  availableOperations[operation].handleRequest(res, numOne, numTwo)
});


app.listen(port, () => {
  console.log(`Example app listening on port ${port}!`)
});



class Operation{
  constructor (operation){
    this.operation = operation
  }

  handleRequest(res, numOne, numTwo){
    res.status(200).json({
      status: 'succes',
      data: this.operation(numOne, numTwo),
    })
  }
}

function throwError(res, message) {
  res.status(400).json({
    status: 'error',
    error: message
  });
}
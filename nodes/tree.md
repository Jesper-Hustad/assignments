# Øving 3



## Deloppgave 1, lenka lister  
<!-- Fra de forrige arbeidskravene har jeg blitt inspirert til å implementere funksjonene på en rekursiv måte.  
Brukte en "getattr()" metode som man kan sette en default value for (f.eks. 0 eller None), dette gjorde at jeg ikke måtte implementere masse ekstra kode for å sjekke om atributtene eksisterte hele tiden.  
Brukte et bilotek som heter pyllist (Python Linked List) for lenka lister, denne hadde en hjelpsom funksjon som omgjorde en array til lenka liste som sparte mye tid/kode. -->

### Lenka Lister Addisjon

```python
def addLinked(a, b, carry=0, result=dllist()):

    if a is None and b is None: 
        if carry > 0: result.appendleft(1)
        return result

    aVal, bVal = getattr(a, 'value', 0), getattr(b, 'value', 0)
    addition =  aVal + bVal + carry

#   appending addition to result
    current = addition % 10
    result.appendleft(current)

    nextA, nextB = getattr(a, 'prev', None), getattr(b, 'prev', None)
    nextCarry = addition // 10

#   run process again on next number
    return addLinked(nextA, nextB, nextCarry, result)
```

### Lenka Lister Subtraksjon

```python
def diffLinked(a, b, loans=False, result=dllist()):

    if a is None and b is None:
        while result.first.value == 0: result.popleft()
        return result

    loanCost = 1 if loans else 0
    aVal, bVal = getattr(a, 'value', 0), getattr(b, 'value', 0)
    subtraction =  aVal - bVal - loanCost

#   add 10 if current period needs to loan
    loans = subtraction < 0
    if loans: subtraction += 10

    result.appendleft(subtraction)

    nextA, nextB = getattr(a, 'prev', None), getattr(b, 'prev', None)

    return diffLinked(nextA, nextB, loans, result)
```

### Implementasjon av funksjonene

```python
def calcBigNums(arg):

#   defining vars
    args = arg.split(" ")
    numOne, operator, numTwo = args[0], args[1], args[2]
    
#   using pyllist to convert string to linked list
    n1 = dllist([int(n) for n in list(numOne)])
    n2 = dllist([int(n) for n in list(numTwo)])
    
#   getting result
    result = addLinked(n1.last, n2.last) if operator == "+" else diffLinked(n1.last, n2.last)
    
#   pretty print results
    resultStr = "".join([str(n.value) for n in result.iternodes()])
    nums = [numOne, numTwo, resultStr]
    longest = len(max(nums, key=len))
    ops = [" ", operator, "="]
    
    for i in range(3): print('{} {:>{fill}}'.format(ops[i], nums[i], fill=longest))
```

## Resultat


```python
calcBigNums("100000000019999999999001 + 100007")
```

      100000000019999999999001
    +                   100007
    = 100000000020000000099008
    


```python
calcBigNums("840000000000000200000 - 100000000007000060004")
```

      840000000000000200000
    - 100000000007000060004
    = 739999999993000139996
    


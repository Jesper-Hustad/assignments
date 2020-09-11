# Øving 3

## Deloppgave 1, lenka lister  
Fra de forrige arbeidskravene har jeg blitt inspirert til å implementere funksjonene på en rekursiv måte.  


```python
from pyllist import dllist, dllistnode
```

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
    
    n1 = dllist([int(n) for n in list(numOne)])
    n2 = dllist([int(n) for n in list(numTwo)])
    
#   getting result
    result = addLinked(n1.last, n2.last) if operator == "+" else diffLinked(n1.last, n2.last)
    
#   pretty print results
    resultStr = "".join([str(n.value) for n in result.iternodes()])
    nums = [numOne, numTwo, resultStr]
    longest = len(max(nums, key=len))
    ops = [" ", operator, "="]
    
    for i in range(3): print('{} {:>{fill}}'.format(ops[i] ,nums[i] , fill=longest))
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
    

---------
## Deloppgave 2, trær  


Her brukte jeg bare et object/dict for å representere en node i treet.  
Gikk for rekusjon igjen fordi det føltes naturlig for mange av oppgavene.

### Noder


```python
def Node(val):
    return {"val": val, "a": None, "b": None}
```

### Legge til noder


```python
def addNode(val, head):
    
#   Compare value with current node value
    branch = "a" if val < head['val'] else "b"

#   There is already a node in branch
    if head[branch] is not None: return addNode(val, head[branch])

#   Set new branch with val
    head[branch] = Node(val)

    
def makeTree(words):
    
    args = words.split(" ")

#   create tree
    head = Node(args[0])
    for a in range(1, len(args)): addNode(args[a], head)
    
#   parse and pretty print
    tree_layers = treeParse(head)
    printTree(tree_layers)
```

### Visualisering (pretty print)


```python
def getNodeAttr(node, attr, default=None):
    if node is None: return default
    return node[attr]

def treeParse(node, lev = 0, result={}):
    
    result[str(lev)] = result.get(str(lev),[]) +  [getVal(node)]

#   max recursion depth reached
    if lev > 4: return
    
    treeParse(getNodeAttr(node,'a',None), lev + 1)
    treeParse(getNodeAttr(node,'b',None), lev + 1)
    return result.values()

def printTree(tree_layers):
    full_width = 64
    for level in tree_layers:
        width = int(full_width / len(level))
        tree_layer = ['{:^{}s}'.format(v, width) for v in level]
        print( "".join(tree_layer) + "\n\n")
```


```python
makeTree("hode bein hals arm tann hånd tå")
```

                                  hode                              
    
    
                  bein                            tann              
    
    
          arm             hals            hånd             tå       
    
    

```python
makeTree("f d h b e g i a c")
```

                                   f                                
    
    
                   d                               h                
    
    
           b               e               g               i        
    
    
       a       c                                                    

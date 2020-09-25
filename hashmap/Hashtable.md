### Importering av navn fil og biblotek


```python
from pyllist import sllist, sllistnode
import io
file = io.open("navn20.txt", mode="r", encoding="utf-8")
navn = file.read().replace(',', ' ').split('\n')
```

## Deloppgave 1: Hashtabell med tekstnøkler



```python
def putIn(hashmap, value):
    global collisions
    
    hashCode = abs(hash(value) % len(hashmap))
    
    node = hashmap[hashCode].first
    
    if hashmap[hashCode].size > 0:
        
        while True:
            print(f"Kollisjon {value} - {node.value}")
            collisions += 1
            
            if node.next is None: break
            node = node.next
        
    hashmap[hashCode].append(value)
```

### Utskrivingkode


```python
size = 97
hashmap = [sllist() for i in range(size)]
collisions = 0

for n in navn: putIn(hashmap, n)
    
lastfaktor = 1 - (hashmap.count(sllist()) / len(hashmap))
print("\ndata:")
print(f"{collisions = }")
print(f"{lastfaktor = }")
```

    Kollisjon Morten Stavik Eggen - Torbjørn Bakke
    Kollisjon Sigmund Ole Granaas - Henrik Tengs Hafsø
    Kollisjon Mathilde Kvam Bugge-Hundere - Hermann Owren Elton
    Kollisjon Ida Heggen Trosdahl - Lars-Håvard Holter Bråten
    Kollisjon Eirik Steira - Hans William Forbrigd
    Kollisjon Thomas Thien Dinh Tran - Ilona Podliashanyk
    Kollisjon Sergio Martinez - Ola Kristoffer Hoff
    Kollisjon Sindre August Strøm - Ilona Podliashanyk
    Kollisjon Sindre August Strøm - Thomas Thien Dinh Tran
    Kollisjon Olaf Rosendahl - Ingebrigt Kristoffer Thomassen Hovind
    Kollisjon Magnus Nordahl - Jørgen Selsøyvold
    Kollisjon Magnus Bredeli - Matilde Volle Fiborg
    Kollisjon Niklas Johan Bjøru - Tommy Duc Luu
    Kollisjon Endré Hadzalic - Ilona Podliashanyk
    Kollisjon Endré Hadzalic - Thomas Thien Dinh Tran
    Kollisjon Endré Hadzalic - Sindre August Strøm
    Kollisjon Hogne Heggdal Winther - Truls Kolstad Stephensen
    Kollisjon Robin Christoffer Vold - Thomas Huru
    Kollisjon Lukas Øystein Normann Stjernen - Ingebrigt Kristoffer Thomassen Hovind
    Kollisjon Lukas Øystein Normann Stjernen - Olaf Rosendahl
    Kollisjon Jens Mjønes Loe - Stian Fjæran Mogen
    Kollisjon Mats Erik Tuhus Olsen - Mathias Myrold
    Kollisjon Sander Pettersen - Mai Helene Grosås
    Kollisjon Nora Evensen Jansrud - Torbjørn Øverås
    Kollisjon Arvid Jr Kirkbakk - Mai Helene Grosås
    Kollisjon Arvid Jr Kirkbakk - Sander Pettersen
    Kollisjon Henrik Latsch Haugberg - Matilde Volle Fiborg
    Kollisjon Henrik Latsch Haugberg - Magnus Bredeli
    Kollisjon Scott Rydberg Sonen - Lars-Håvard Holter Bråten
    Kollisjon Scott Rydberg Sonen - Ida Heggen Trosdahl
    Kollisjon Jostein Johansen Aune - Andrea Marie Ramberg Berge
    
    data:
    collisions = 31
    lastfaktor = 0.6391752577319587
    

## Deloppgave 2: Hashtabeller med heltallsnøkler og ytelse


```python
def putInInt(hashmap, value):
    global collisions
    
#   single hash
    hashCode = abs(value) % len(hashmap)
    
#   double hashing
    if hashmap[hashCode].size > 0:
        hashCode = hashCode - (value % hashCode)
        
    node = hashmap[hashCode].first
    
    if hashmap[hashCode].size > 0:
        
        while True:
            collisions += 1
            
            if node.next is None: break
            node = node.next
        
    hashmap[hashCode].append(value)
```


```python
def putInNative(hashmap, value):
    hashmap[value] = True
```

### Utskrivingkode


```python
sizeList = 10**6
collisions = 0

intHashmap = [sllist() for i in range(sizeList)]

import random
randNumbers = [random.randint(1, 2147483647) for i in range(sizeList)]

import time
start_time = time.time()

for n in randNumbers: putInInt(intHashmap, n)

timeTaken = time.time() - start_time
lastfaktor = 1 - (intHashmap.count(sllist()) / len(intHashmap))
print("Egen hashmap med dobbel hashing:")
print(f"{timeTaken = }")
print(f"{collisions = }")
print(f"{lastfaktor = }")


nativeHashmap = {}
start_time = time.time()

for n in randNumbers: putInNative(nativeHashmap, n)

timeTaken = time.time() - start_time
print("\nNative hashmap i python:")
print(f"{timeTaken = }")
```

    Egen hashmap med dobbel hashing:
    timeTaken = 4.994411468505859
    collisions = 556906
    lastfaktor = 0.725595
    
    Native hashmap i python:
    timeTaken = 0.336503267288208
    

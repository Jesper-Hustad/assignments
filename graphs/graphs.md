### Øving 5 i algoritmer og datastrukturer
# Uvektede grafer

Main programmet tar en liste av uvekta grafer fra lenker, nedlaster dem, og finner sterkt sammenhengende komponenter.  
```python
graphs = {
    'L7g1' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g1',
    'L7g6' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g6',
    'L7g2' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g2',
    'L7g5' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g5'
}

for name, url in graphs.items():
    edges_table = wget(url)
    components = scc(edges_table)

    print(f"Grafen {name} har {len(components)} sterkt\nsammenhengende komponenter.")
    pretty_print(components)
```

## Resultater

### L7g1
```python
Grafen L7g6 har 1 sterkt
sammenhengende komponenter.

Komponent  Noder i komponenten
1          0 2 3 1 4 6 5
```

### L7g6
```python
Grafen L7g6 har 5 sterkt
sammenhengende komponenter.

Komponent  Noder i komponenten
1          6
2          1 7 2
3          3 4
4          5
5          0
```

### L7g2
```python
Grafen L7g6 har 26 sterkt
sammenhengende komponenter.

Komponent  Noder i komponenten
1          42
2          30
3          23
4          16
5          10
6          7
7          25
8          5
9          1
10          0
11          41
12          43 35 6 33 36 15 20 18 45 28 21 19 17 29 2 38 34 37 8 27 14 24 39 12 11       
13          31
14          3
15          22
16          9
17          44
18          48
19          32
20          46
21          26
22          13
23          47
24          49
25          40
26          4
```

### L7g5
```python
Grafen L7g6 har 7 sterkt
sammenhengende komponenter.

Komponent  Noder i komponenten
1          6
2          0
3          3
4          2
5          4
6          5
7          1
```

## Depth First Search

Som man kan se så blir `dfs` ganske enkelt implementert i python, såklart gjør jo denne koden ingenting for oss akkurat nå. Videre lager jeg en funkjson som jeg kaller `full_dfs`, denne går gjennom hele grafen og legger til kode i `dfs` som logger funnet tid og ferdig tid.

```python
def dfs(graph, node):
    if node not in visited:
        visited.append(node)
        for neighbour in graph[node]:
            dfs(graph, neighbour)
```

## Complete graph search
Here we add code to get the order nodes are completed (`completed_order`) and the nodes found in every depth first search (`search_finds`). These are the requirements to use the algorithm given in the book to find scc's.

```python
def full_dfs(graph, order):

    # ferdig tid
    completed_order = []
    
    # nodes already visited
    visited = []

    def dfs(graph, node):

        if node not in visited:

            # funnet tid
            found.append(node)

            visited.append(node)
            for neighbour in graph[node]:
                dfs(graph, neighbour)

            # ferdig tid
            completed_order.append(node)
    

    search_finds = []

    # go trough every node in given order skipping already visited
    for n in order:
        if n in visited: continue

        found = []
        dfs(graph, n)
        search_finds.append(found)

    return {'search_finds': search_finds, 'completed_order': completed_order}

```

## Strongly Connected Components

This is where the algorithm given in the book is implemented. It's a bit long, but the algorithm has a many steps so i don't see many ways to simplify further. Tried to comment the function so if you are following along with the book it's recognizable.

```python
def scc(edges_table):

    # split by newline, then by space, removing empty along the way
    edges = [ [n for n in path.split(' ') if n!=''] 
            for path in edges_table.split('\n') if path != '' ]
        
    headers = edges.pop(0)
    n = int(headers[0])
    k = int(headers[1])

    # graph generated from edges list
    graph = edgesToGraph(edges, n)

    # order doesn't matter for first search just want all nodes ( using range() )
    linear_order = [str(n) for n in list(range(n))]

    first_dfs = full_dfs(graph, linear_order)

    # get reversed order
    completed_order = first_dfs['completed_order']
    reversed_order = list(reversed(completed_order))
    
    # make the inverted graph G^t
    edges_inverted = [ [e[1], e[0]] for e in edges]
    inverted_graph = edgesToGraph(edges_inverted, n)

    dfs_on_reversed = full_dfs(inverted_graph, reversed_order)

    return dfs_on_reversed['search_finds']

```



`edgesToGraph` creates dictonary (hashmap) where key is a node and value is array of edges to other nodes
```python
def edgesToGraph(edges, n):

    graph = {}

    for n in range(n): graph[str(n)] = []

    for e in edges:
        node = e[0]
        edge = e[1]

        graph[node].append(edge)
        
    return graph
```


## Importering og private hjelpemetoder
Dette er kjedelige saker som ikke er så relevant for fullføringen av oppgaven.

<details>
  <summary>Kjedelig kode</summary>
  
  
```python
import requests
def wget(url):
    return requests.get(url, allow_redirects=True).text

def pretty_print(r):
    if len(r) > 100: return

    print(f"\nKomponent  Noder i komponenten")
    for i in range(len(r)):
        group = " ".join(r[i])
        print(f"{i+1}          {group}")

```
  
</details>

## Raw python code
Can't guarantee this code will be identical to the one showed here. Sometimes i make small changes while writing the markdown to make the code more readable, brief or cohesive.  

[Link to code](graphCode.py)

# Øving 8 Vektede grafer
Valgte å implementere Dijkstras algoritme.  
Leser grafen gjennom en txt fra url, men kan lese fra fil (se source kode).  
Funker også på vg3 og vg4, men utskriften blir veldig lang så hadde vært upraktiskt å vise det her.

## Main
```python
graphs = {
    1 : 'http://www.iie.ntnu.no/fag/_alg/v-graf/vg1',
    0 : 'http://www.iie.ntnu.no/fag/_alg/v-graf/vg5',
    7 : 'http://www.iie.ntnu.no/fag/_alg/v-graf/vg2'
}

for start_node, url in graphs.items():
    graph_string = wget(url)
    graph = build_graph(graph_string)
    result = dijkstra(graph, start_node)
    name = url.split('/')[-1]
    pretty_print(result, start_node, name)

```


## Resultater

```python
Dijkstras algoritme på vg1
Node  forgjenger   distanse
   0              nåes ikke
   1       start          0
   2           3          3
   3           1          2
```

```python
Dijkstras algoritme på vg5 
Node  forgjenger   distanse
   0       start          0
   1              nåes ikke
   2           4          2
   3           2          3
   4           0          2
```


```python
Dijkstras algoritme på vg2 
Node  forgjenger   distanse
   0              nåes ikke
   1              nåes ikke
   2           6         17
   3          19         23
   4          49         19
   5          25          9
   6          33         16
   7       start          0
   8              nåes ikke
   9          36         88
  10              nåes ikke
  11              nåes ikke
  12              nåes ikke
  13           7          3
  14          28         13
  15              nåes ikke
  16              nåes ikke
  17           6         18
  18          28        790
  19          24         19
  20              nåes ikke
  21           5          9
  22          36         14
  23              nåes ikke
  24          21         16
  25           7          7
  26          28         21
  27              nåes ikke
  28          21         13
  29           2         40
  30              nåes ikke
  31          25          9
  32              nåes ikke
  33          36         13
  34          14         21
  35              nåes ikke
  36           5         12
  37          28         20
  38          34         29
  39              nåes ikke
  40          49         11
  41              nåes ikke
  42              nåes ikke
  43           6         28
  44          19         20
  45              nåes ikke
  46          31         11
  47          21         11
  48          24         17
  49          21         11
```


## Example of Graph

### Input table
```python
4 6
0 2 12
0 1 4
0 3 10
1 2 6
1 3 2
3 2 1
```


### Returned object
```python
build_graph(input_table)
>>> {
    'n' : 4,
    'k' : 6,
    'nodes' : [
        {2:12, 1:4, 3:10},
        {2:6, 3:2},
        {},
        {2:1}
        ]
    }
```


## Dijkstra implementasjon



```python
def dijkstra(graph, source):

    nodes = graph['nodes']
    n = graph['n']
    previous = [None] * n
    distances = [inf] * n

    distances[source] = 0
    unvisited = list(range(n))
    
    queue = [(0, source)]

    while queue:
        dist, selected  = heappop(queue)
        
        # can't reach any other values (not strongly connected)
        if selected not in unvisited: break

        unvisited.remove(selected)

        # neighbour example: {2:12, 1:4, 3:10}
        neighbours = nodes[selected]
        for pointer, edge_dist in neighbours.items():

            new_distance = distances[selected] + int(edge_dist)

            if new_distance < distances[pointer]:
                distances[pointer] = new_distance 
                previous[pointer] = selected
                heappush(queue, (new_distance, pointer))


    return zip( previous, distances )
```


## Boring stuff
<details>
  <summary>Se på kjedelig kode</summary>
  
  `Build_graph` er kansje litt viktig, men er veldig lik metoden brukt i forrige øving så satt den her.
  
```python
from math import inf 
from heapq import heappush, heappop

def build_graph(graph_string):

    table = [[e for e in i.split(' ') if e != ''] for i in graph_string.split("\n") if i != '']

    headers = table.pop(0)
    n = int(headers[0])
    k = int(headers[1])

    graph = {
    'n' : n,
    'k' : k,
    'nodes' : [{} for _ in range(n)]
    }

    for data in table:
        nodeInex = int(data[0])
        refrence = int(data[1])
        weight   = int(data[2])

        # edge = (refrence, weight)
        graph['nodes'][nodeInex][refrence] = weight

    return graph

import requests
def wget(url):
    return requests.get(url, allow_redirects=True).text

def pretty_print(results, source, name):
    print(f"\nDijkstras algoritme på {name}")
    print("{0:6}{1:11}{2:>10}".format('Node', 'forgjenger', 'distanse'))
    n = 0
    for p, d in results:
        if d >= 10**9: d = "nåes ikke"
        if p == None: p = ""
        if n == source: p = "start"
        print("{0:4}  {1:>10} {2:>10}".format(n,str(p),str(d)) )
        n += 1
  ```
  
</details>


## Source code
[Python kode link](weigthed_graph.py)

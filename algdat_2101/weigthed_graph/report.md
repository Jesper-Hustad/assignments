# Øving 8 Vektede grafer
Valgte å implementere Dijkstras algoritme.  
Leser grafen gjennom en txt fra url, men kan lese fra fil (se source kode).  
Funker også på vg3 og vg4, men utskriften blir veldig lang så hadde vært upraktiskt å vise det her.

## Main
```python
graphs = {
    'vg1' : 'http://www.iie.ntnu.no/fag/_alg/v-graf/vg1',
    'vg5' : 'http://www.iie.ntnu.no/fag/_alg/v-graf/vg5',
    'vg2' : 'http://www.iie.ntnu.no/fag/_alg/v-graf/vg2'
}

start_node = 1

for name, url in graphs.items():
    graph_string = wget(url)
    graph = build_graph(graph_string)
    result = dijkstra(graph, start_node)
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
   0           1          2
   1       start          0
   2           4          4
   3           2          5
   4           0          4
```


```python
Dijkstras algoritme på vg2
Node  forgjenger   distanse
   0              nåes ikke
   1       start          0
   2           6       1143
   3           1          8
   4          49        129
   5              nåes ikke
   6          33       1142
   7              nåes ikke
   8          15        909
   9          27        910
  10              nåes ikke
  11          18        967
  12          20        906
  13          26        132
  14          17         42
  15          20        905
  16              nåes ikke
  17           1          9
  18          28        900
  19          17         21
  20          18        904
  21          19        119
  22           8        932
  23              nåes ikke
  24          14         50
  25              nåes ikke
  26          28        131
  27          15        908
  28          21        123
  29          38         58
  30              nåes ikke
  31          17         11
  32           1          7
  33          36       1139
  34          14         50
  35          43       1231
  36          15       1138
  37          28        130
  38          34         58
  39          12        987
  40          29         59
  41              nåes ikke
  42              nåes ikke
  43           6       1154
  44          19         22
  45          20        913
  46           1          0
  47           3         10
  48          24         51
  49          21        121
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
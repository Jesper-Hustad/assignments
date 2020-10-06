from math import inf 
from heapq import heappush, heappop

import requests
def wget(url):
    return requests.get(url, allow_redirects=True).text


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

        neighbours = nodes[selected]
        for pointer, edge_dist in neighbours.items():

            new_distance = distances[selected] + int(edge_dist)

            if new_distance < distances[pointer]:
                distances[pointer] = new_distance 
                previous[pointer] = selected
                heappush(queue, (new_distance, pointer))


    return zip( previous, distances )


def pretty_print(results, source, name):
    print(f"\nDijkstras algoritme på {name}")
    print("{0:6}{1:11}{2:>10}".format('Node', 'forgjenger', 'distanse'))
    # print(len(list(results)))
    n = 0
    for p, d in results:
        if d >= 10**9: d = "nåes ikke"
        if p == None: p = ""
        if n == source: p = "start"
        print("{0:4}  {1:>10} {2:>10}".format(n,str(p),str(d)) )
        n += 1

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


# if for some reason url won't work you can import text file vg1.txt

# graph_string = open('vg1.txt', 'r').read()
# graph = build_graph(graph_string)
# res = dijkstra(graph, start_node)
# pretty_print(res, start_node, name)

import requests
def wget(url):
    return requests.get(url, allow_redirects=True).text

def pretty_print(r):
    if len(r) > 100: return

    print(f"\nKomponent  Noder i komponenten")
    for i in range(len(r)):
        group = " ".join(r[i])
        print(f"{i+1}          {group}")

def edgesToNodes(edges, n):
    nodes = {}
    for n in range(n):
        nodes[str(n)] = []

    for e in edges:

        node = e[0]
        edge = e[1]

        if node not in nodes: nodes[node] = []
        nodes[node].append(edge)
        
    return nodes

def invertEdges(edges):
    return [ [e[1], e[0]] for e in edges]

def full_dfs(full_graph, order):

    # ferdig tid
    completed_order = []
    
    # nodes already visited
    visited = []

    def dfs(graph, node):

        if node not in visited:

            # found in search
            found.append(node)

            visited.append(node)
            for neighbour in graph[node]:
                dfs(graph, neighbour)

            # completed time
            completed_order.append(node)
    

    result = []

    # go trough every node in given order if the dfs doesn't find everything first try 
    for n in order:
        if n in visited: continue

        found = []
        dfs(full_graph, n)
        result.append(found)

    return {'result': result, 'completed_order': completed_order}

def scc(edges_table):

    # split by newline, then by space, removing empty along the way (!='')
    edges = [ 
            [n for n in path.split(' ') if n!=''] 
            for path in edges_table.split('\n') if path != ''
            ]
        
    headers = edges.pop(0)
    n = int(headers[0])
    k = int(headers[1])

    # graph generated from edges list
    graph = edgesToNodes(edges, n)

    # order doesn't matter for first search just want all nodes
    linear_order = [str(n) for n in list(range(n))]

    first_dfs = full_dfs(graph, linear_order)

    # get completed time and reverse it to make it descending order
    completed_order = first_dfs['completed_order']
    reversed_order = list(reversed(completed_order))
    
    # make the inverted graph G^t
    edges_inverted = invertEdges(edges)
    inverted_graph = edgesToNodes(edges_inverted, n)


    dfs_on_reversed = full_dfs(inverted_graph, reversed_order)

    result = dfs_on_reversed['result']
    return result


graphs = {
    'L7g1' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g1',
    'L7g6' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g6',
    'L7g2' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g2',
    'L7g5' : 'http://www.iie.ntnu.no/fag/_alg/uv-graf/L7g5'
}

for name, url in graphs.items():
    edges_table = wget(url)
    components = scc(edges_table)

    print(f"\n\nGrafen {name} har {len(components)} sterkt sterkt\nsammenhengende komponenter.")
    pretty_print(components)



# if for some reason url won't work you can import text file L7g6.txt

# edges_table = open('L7g6.txt', 'r').read()
# components = scc(edges_table)
# print(f"\n\nGrafen L7g6 har {len(components)} sterkt sterkt\nsammenhengende komponenter.")
# pretty_print(components)
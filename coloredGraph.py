import matplotlib.pyplot as plt
import networkx as nx

file = open("coloredGraph.txt", "r")
content = file.readlines()
file.close()

numberOfVertexes = content[0]
numberOfEdges = content[1]
graphType = content[2]
vertexesFromFile = content[3].split(",")
vertexesColorsFromFile = content[4].split(",")
vertexes = []
vertexesColors = []
vertexesLabels = {}

for i in range(int(numberOfVertexes)):
    vertexes.append(vertexesFromFile[i])

for i in range(int(numberOfVertexes)):
    vertexesColors.append(vertexesColorsFromFile[i])

edges = []
for i in range(int(numberOfEdges)):
    edgesFromFile = content[i+5].replace("\n", "")
    edgesFromFile = edgesFromFile.split(",")
    edges.append((edgesFromFile[1], edgesFromFile[2]))

colorMap = []

if graphType == "Directed\n":
    G = nx.MultiDiGraph(edges)
    G.add_nodes_from(vertexes)
    i = 0
    for v in G.nodes:
        edges.append(vertexesColors[i])
        i += 1
    ax = plt.gca()
    pos = nx.spring_layout(G)

    similarEdges = []
    for e in G.edges:
        if e[2] >= 1:
            ax.annotate("",
                        xy=pos[e[0]], xycoords='data',
                        xytext=pos[e[1]], textcoords='data',
                        arrowprops=dict(arrowstyle="<-", color="black",
                                        shrinkA=5, shrinkB=5,
                                        patchA=None, patchB=None,
                                        connectionstyle="arc3,rad=rrr".replace('rrr',str(0.3*e[2])
                                        ),
                                        ),
                        )

    nx.draw(G, pos, with_labels=True, font_size=9, node_size=110, node_color=vertexesColors)

    plt.axis('off')
    plt.savefig("coloredGraph.png", dpi=100, bbox_inches='tight', pad_inches=0)
elif graphType == "Undirected\n":
    G = nx.MultiGraph(edges)
    G.add_nodes_from(vertexes)
    i = 0
    for v in G.nodes:
        edges.append(vertexesColors[i])
        i += 1
    ax = plt.gca()
    pos = nx.random_layout(G)

    for e in G.edges:
        ax.annotate("",
                    xy=pos[e[0]], xycoords='data',
                    xytext=pos[e[1]], textcoords='data',
                    arrowprops=dict(arrowstyle="-", color="black",
                                    shrinkA=5, shrinkB=5,
                                    patchA=None, patchB=None,
                                    connectionstyle="arc3,rad=rrr".replace('rrr', str(0.3 * e[2])
                                                                           ),
                                    ),
                    )
    nx.draw(G, pos, with_labels=True, font_size=9, node_size=110, node_color=vertexesColors)

    plt.axis('off')
    plt.savefig("coloredGraph.png", dpi=100, bbox_inches='tight', pad_inches=0)


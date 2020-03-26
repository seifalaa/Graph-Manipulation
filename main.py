import networkx as nx
import matplotlib.pyplot as plt


file = open("graph.txt", "r")
content = file.readlines()
file.close()
numberOfVertexes = content[0]
numberOfEdges = content[1]
graphType = content[2]
vertexesFromFile = content[3].split(",")
vertexes = []
vertexesLabels = {}
for i in range(int(numberOfVertexes)):
    vertexes.append(vertexesFromFile[i])

edges = []
for i in range(int(numberOfEdges)):
    edgesFromFile = content[i+4].replace("\n","")
    edgesFromFile = edgesFromFile.split(",")
    edges.append((edgesFromFile[2],edgesFromFile[3]))
if graphType == "Directed\n":
    G = nx.MultiDiGraph(edges)
    G.add_nodes_from(vertexes)
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

    nx.draw(G,pos,with_labels=True,font_size=9,node_size=110,node_color="red")

    plt.axis('off')
    plt.savefig("graph.png",dpi =100,bbox_inches='tight',pad_inches=0)
elif graphType == "Undirected\n":
    G = nx.MultiGraph(edges)
    G.add_nodes_from(vertexes)
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
    nx.draw(G,pos,with_labels=True,font_size=9,node_size=110,node_color="red")

    plt.axis('off')
    plt.savefig("graph.png", dpi=100, bbox_inches='tight', pad_inches=0)

import os
from typing import Mapping, List

from parserGraph.parserGraph import Graph, Vertex, Edge
from cp_parser import Class
from cp_parser.ConfigParserAPI import ConfigParserAPI
# from staticParser.parserDijkstra import Dijkstra
import networkx
import matplotlib.pyplot as plt


THIS_SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
RUNTIME_DIR = os.path.join(THIS_SCRIPT_DIR, "..", "__runtime__")


if __name__ == "__main__":
    config_parser = ConfigParserAPI(RUNTIME_DIR)
    # config_parser.init(f"{THIS_SCRIPT_DIR}/../networks/tests/route-map")
    config_parser.init(f"{THIS_SCRIPT_DIR}/../networks/fattree/bgp/bgp_fattree04")

    G = networkx.DiGraph()

    # parse topology
    topology = config_parser.get_topology()
    cpDeviceModels = config_parser.get_cp_device_models()
    device_Vertex: Mapping[str, Vertex] = {}

    # 1. create vertices
    for updateType in config_parser.configUpdates:
        for configUpdate in config_parser.configUpdates[updateType]:
            # print(updateType, configUpdate)
            if updateType == "insert":
                if Class.forName("org.ants.parser.relation.Node").isInstance(configUpdate):
                    # print("Node", configUpdate.node)
                    v = Vertex(configUpdate.node)
                    device_Vertex[configUpdate.node] = v.getLabel()
                    v.AS = cpDeviceModels[configUpdate.node]["default"].getNode().getAS()
                    # print(v.AS)
                    v.config.bgpAggregations = [
                        aggr.prefix for aggr in cpDeviceModels[configUpdate.node]["default"].getBgpAggregations()
                    ]
                    # G.addVertex(v)

    # 2. create edges
    for link in topology:
        # print(link, topology[link])
        G.add_edge(
            device_Vertex[link.getHostname()],
            device_Vertex[topology[link].getHostname()]
        )
        
    networkx.draw(G, with_labels=True, font_weight='bold')
    plt.show()

    # 3. add routing-related attributes on vertices and edges
    # for updateType in config_parser.configUpdates:
    #     for configUpdate in config_parser.configUpdates[updateType]:
    #         # print(configUpdate)
    #         if updateType == "insert":
    #             # Type 3 is replaced with Graph.outNeighborMap
    #             # if Class.forName("org.ants.parser.relation.neighbor.EBgpNeighbor").isInstance(configUpdate):
    #             #   # Type3
    #             #   # append this update to device 'ebgpNeighbors' attribute
    #             #   node = device_Vertex[configUpdate.node1.node]
    #             #   node['ebgpNeighbors'].append(configUpdate.node2.node)

    #             if Class.forName("org.ants.parser.relation.BgpNetwork").isInstance(configUpdate):
    #                 # Type4
    #                 if configUpdate.prefix not in device_Vertex[configUpdate.node.node].config.bgpAggregations:
    #                     print("Type4", configUpdate.node.node, configUpdate.prefix)
    #                     node = device_Vertex[configUpdate.node.node]
    #                     node.config.pendingPropogateBgpNetworks.append(configUpdate.prefix)

    #             if Class.forName("org.ants.parser.relation.routeMap.RouteMapIn").isInstance(configUpdate):
    #                 # Type5
    #                 device_Vertex[configUpdate.node.node].config.routeMapIns.append(configUpdate)

    #             if Class.forName("org.ants.parser.relation.routeMap.RouteMapOut").isInstance(configUpdate):
    #                 # Type5
    #                 device_Vertex[configUpdate.node.node].config.routeMapOuts.append(configUpdate)

    # # parse topology ends
    # pass

    # # 4. run dijkstra
    # # Dijkstra(G, device_Vertex[list(device_Vertex.keys())[0]])
    # from parserGraph.parserTree import Path

    # for vlabel in G.vertexMap.keys():
    #     v = G.vertexMap[vlabel]
    #     for network in v.config.pendingPropogateBgpNetworks:
    #         print("Testing propogate", network)
    #         p1 = Path(network, G, v)
    #         p1.extend(None, device_Vertex["b"])

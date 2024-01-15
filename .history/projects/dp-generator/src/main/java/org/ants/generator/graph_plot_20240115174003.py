{\rtf1\ansi\ansicpg936\cocoartf2758
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fnil\fcharset0 Menlo-Regular;}
{\colortbl;\red255\green255\blue255;\red145\green57\blue154;\red225\green215\blue189;\red47\green45\blue40;
\red122\green91\blue30;\red170\green60\blue52;\red43\green113\blue43;\red12\green99\blue165;}
{\*\expandedcolortbl;;\cssrgb\c64314\c32157\c66667;\cssrgb\c90588\c87059\c78824;\cssrgb\c24314\c23137\c20784;
\cssrgb\c55294\c42745\c15294;\cssrgb\c72941\c31765\c26275;\cssrgb\c20784\c50588\c21961;\cssrgb\c0\c47059\c70588;}
\paperw11900\paperh16840\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\deftab720
\pard\pardeftab720\partightenfactor0

\f0\fs24 \cf2 \cb3 \expnd0\expndtw0\kerning0
\outl0\strokewidth0 \strokec2 import\cf4 \strokec4  os\cb1 \
\cf2 \cb3 \strokec2 from\cf4 \strokec4  typing \cf2 \strokec2 import\cf4 \strokec4  Mapping, List\cb1 \
\
\cf2 \cb3 \strokec2 from\cf4 \strokec4  parserGraph.parserGraph \cf2 \strokec2 import\cf4 \strokec4  Graph, Vertex, Edge\cb1 \
\cf2 \cb3 \strokec2 from\cf4 \strokec4  cp_parser \cf2 \strokec2 import\cf4 \strokec4  Class\cb1 \
\cf2 \cb3 \strokec2 from\cf4 \strokec4  cp_parser.ConfigParserAPI \cf2 \strokec2 import\cf4 \strokec4  ConfigParserAPI\cb1 \
\cf2 \cb3 \strokec2 from\cf4 \strokec4  staticParser.parserDijkstra \cf2 \strokec2 import\cf4 \strokec4  Dijkstra\cb1 \
\cf2 \cb3 \strokec2 import\cf4 \strokec4  networkx\cb1 \
\cf2 \cb3 \strokec2 import\cf4 \strokec4  matplotlib.pyplot \cf2 \strokec2 as\cf4 \strokec4  plt\cb1 \
\
\
\pard\pardeftab720\partightenfactor0
\cf4 \cb3 THIS_SCRIPT_DIR = os.path.\cf5 \strokec5 dirname(os.path.abspath(__file__))\cf4 \cb1 \strokec4 \
\cb3 RUNTIME_DIR = os.path.\cf5 \strokec5 join(THIS_SCRIPT_DIR, \cf6 \strokec6 ".."\cf5 \strokec5 , \cf6 \strokec6 "__runtime__"\cf5 \strokec5 )\cf4 \cb1 \strokec4 \
\
\
\pard\pardeftab720\partightenfactor0
\cf2 \cb3 \strokec2 if\cf4 \strokec4  __name__ == \cf6 \strokec6 "__main__"\cf4 \strokec4 :\cb1 \
\pard\pardeftab720\partightenfactor0
\cf4 \cb3     config_parser = \cf5 \strokec5 ConfigParserAPI(RUNTIME_DIR)\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 # config_parser.init(f"\{THIS_SCRIPT_DIR\}/../networks/tests/route-map")\cf4 \cb1 \strokec4 \
\cb3     config_parser.\cf5 \strokec5 init(\cf8 \strokec8 f\cf6 \strokec6 "\cf5 \strokec5 \{THIS_SCRIPT_DIR\}\cf6 \strokec6 /../networks/fattree/bgp/bgp_fattree04"\cf5 \strokec5 )\cf4 \cb1 \strokec4 \
\
\cb3     G = networkx.\cf5 \strokec5 DiGraph()\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 # parse topology\cf4 \cb1 \strokec4 \
\cb3     topology = config_parser.\cf5 \strokec5 get_topology()\cf4 \cb1 \strokec4 \
\cb3     cpDeviceModels = config_parser.\cf5 \strokec5 get_cp_device_models()\cf4 \cb1 \strokec4 \
\cb3     device_Vertex: Mapping[str, Vertex] = \{\}\cb1 \
\
\cb3     \cf7 \strokec7 # 1. create vertices\cf4 \cb1 \strokec4 \
\cb3     \cf2 \strokec2 for\cf4 \strokec4  updateType \cf2 \strokec2 in\cf4 \strokec4  config_parser.configUpdates:\cb1 \
\cb3         \cf2 \strokec2 for\cf4 \strokec4  configUpdate \cf2 \strokec2 in\cf4 \strokec4  config_parser.configUpdates[updateType]:\cb1 \
\cb3             \cf7 \strokec7 # print(updateType, configUpdate)\cf4 \cb1 \strokec4 \
\cb3             \cf2 \strokec2 if\cf4 \strokec4  updateType == \cf6 \strokec6 "insert"\cf4 \strokec4 :\cb1 \
\cb3                 \cf2 \strokec2 if\cf4 \strokec4  Class.\cf5 \strokec5 forName(\cf6 \strokec6 "org.ants.parser.relation.Node"\cf5 \strokec5 )\cf4 \strokec4 .\cf5 \strokec5 isInstance(configUpdate)\cf4 \strokec4 :\cb1 \
\cb3                     \cf7 \strokec7 # print("Node", configUpdate.node)\cf4 \cb1 \strokec4 \
\cb3                     v = \cf5 \strokec5 Vertex(configUpdate.node)\cf4 \cb1 \strokec4 \
\cb3                     device_Vertex[configUpdate.node] = v.\cf5 \strokec5 getLabel()\cf4 \cb1 \strokec4 \
\cb3                     v.AS = cpDeviceModels[configUpdate.node][\cf6 \strokec6 "default"\cf4 \strokec4 ].\cf5 \strokec5 getNode()\cf4 \strokec4 .\cf5 \strokec5 getAS()\cf4 \cb1 \strokec4 \
\cb3                     \cf7 \strokec7 # print(v.AS)\cf4 \cb1 \strokec4 \
\cb3                     v.config.bgpAggregations = [\cb1 \
\cb3                         aggr.prefix \cf2 \strokec2 for\cf4 \strokec4  aggr \cf2 \strokec2 in\cf4 \strokec4  cpDeviceModels[configUpdate.node][\cf6 \strokec6 "default"\cf4 \strokec4 ].\cf5 \strokec5 getBgpAggregations()\cf4 \cb1 \strokec4 \
\cb3                     ]\cb1 \
\cb3                     \cf7 \strokec7 # G.addVertex(v)\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 # 2. create edges\cf4 \cb1 \strokec4 \
\cb3     \cf2 \strokec2 for\cf4 \strokec4  link \cf2 \strokec2 in\cf4 \strokec4  topology:\cb1 \
\cb3         \cf7 \strokec7 # print(link, topology[link])\cf4 \cb1 \strokec4 \
\cb3         G.\cf5 \strokec5 add_edge(\cf4 \cb1 \strokec4 \
\pard\pardeftab720\partightenfactor0
\cf5 \cb3 \strokec5             device_Vertex[link.getHostname()],\cf4 \cb1 \strokec4 \
\cf5 \cb3 \strokec5             device_Vertex[topology[link].getHostname()]\cf4 \cb1 \strokec4 \
\cf5 \cb3 \strokec5         )\cf4 \cb1 \strokec4 \
\pard\pardeftab720\partightenfactor0
\cf4 \cb3         \cb1 \
\cb3     networkx.\cf5 \strokec5 draw(G, with_labels=\cf8 \strokec8 True\cf5 \strokec5 , font_weight=\cf6 \strokec6 'bold'\cf5 \strokec5 )\cf4 \cb1 \strokec4 \
\cb3     plt.\cf5 \strokec5 show()\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 # 3. add routing-related attributes on vertices and edges\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 # for updateType in config_parser.configUpdates:\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #     for configUpdate in config_parser.configUpdates[updateType]:\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #         # print(configUpdate)\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #         if updateType == "insert":\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #             # Type 3 is replaced with Graph.outNeighborMap\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #             # if Class.forName("org.ants.parser.relation.neighbor.EBgpNeighbor").isInstance(configUpdate):\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #             #   # Type3\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #             #   # append this update to device 'ebgpNeighbors' attribute\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #             #   node = device_Vertex[configUpdate.node1.node]\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #             #   node['ebgpNeighbors'].append(configUpdate.node2.node)\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 #             if Class.forName("org.ants.parser.relation.BgpNetwork").isInstance(configUpdate):\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                 # Type4\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                 if configUpdate.prefix not in device_Vertex[configUpdate.node.node].config.bgpAggregations:\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                     print("Type4", configUpdate.node.node, configUpdate.prefix)\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                     node = device_Vertex[configUpdate.node.node]\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                     node.config.pendingPropogateBgpNetworks.append(configUpdate.prefix)\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 #             if Class.forName("org.ants.parser.relation.routeMap.RouteMapIn").isInstance(configUpdate):\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                 # Type5\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                 device_Vertex[configUpdate.node.node].config.routeMapIns.append(configUpdate)\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 #             if Class.forName("org.ants.parser.relation.routeMap.RouteMapOut").isInstance(configUpdate):\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                 # Type5\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #                 device_Vertex[configUpdate.node.node].config.routeMapOuts.append(configUpdate)\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 # # parse topology ends\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 # pass\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 # # 4. run dijkstra\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 # # Dijkstra(G, device_Vertex[list(device_Vertex.keys())[0]])\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 # from parserGraph.parserTree import Path\cf4 \cb1 \strokec4 \
\
\cb3     \cf7 \strokec7 # for vlabel in G.vertexMap.keys():\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #     v = G.vertexMap[vlabel]\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #     for network in v.config.pendingPropogateBgpNetworks:\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #         print("Testing propogate", network)\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #         p1 = Path(network, G, v)\cf4 \cb1 \strokec4 \
\cb3     \cf7 \strokec7 #         p1.extend(None, device_Vertex["b"])\cf4 \cb1 \strokec4 \
\
}
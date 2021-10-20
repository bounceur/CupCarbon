# CupCarbon
CupCarbon is a Smart City and Internet of Things Wireless Sensor Network (SCI-WSN) simulator. Its objective is to design, visualize, debug and validate distributed algorithms for monitoring, environmental data collection, etc., and to create environmental scenarios such as fires, gas, mobiles, and generally within educational and scientific projects. Not only it can help to visually explain the basic concepts of sensor networks and how they work; it may also support scientists to test their wireless topologies, protocols, etc.

CupCarbon offers two simulation environments. The first simulation environment enables the design of mobility scenarios and the generation of natural events such as fires and gas as well as the simulation of mobiles such as vehicles and flying objects (e.g. UAVs, insects, etc.). The second simulation environment represents a discrete event simulation of wireless sensor networks which takes into account the scenario designed on the basis of the first environment.

Networks can be designed and prototyped by an ergonomic and easy to use interface using the OpenStreetMap (OSM) framework to deploy sensors directly on the map. It includes a script called SenScript, which allows to program and to configure each sensor node individually. From this script, it is also possible to generate codes for hardware platforms such as Arduino/XBee. This part is not fully implemented in CupCarbon, it allows to generate codes for simple networks and algorithms.

CupCarbon simulation is based on the application layer of the nodes. This makes it a real complement to existing simulators. It does not simulate all protocol layers due to the complex nature of urban networks which need to incorporate other complex and resource consuming information such as buildings, roads, mobility, signals, etc.

CupCarbon offers the possibility to simulate algorithms and scenarios in several steps. For example, there could be a step for determining the nodes of interest, followed by a step related to the nature of the communication between these nodes to perform a given task such as the detection of an event, and finally, a step describing the nature of the routing to the base station in case that an event is detected. The current version of CupCarbon allows to configure the nodes dynamically in order to be able to split nodes into separate networks or to join different networks, a task which is based on the network addresses and the channel. The energy consumption can be calculated and displayed as a function of the simulated time. This allows to clarify the structure, feasibility and realistic implementation of a network before its real deployment. The propagation visibility and the interference models are integrated and includes the ZigBee, LoRa and WiFi protocols.

CupCarbon represents the main kernel of the ANR project PERSEPTEUR that aims to develop algorithms for an accurate simulation of the propagation and interference of signals in a 3D urban environment.

# Installation 

http://cupcarbon.com/src_download.html

# amp-java Simulation Framework

This simulation framework was written as part of my masters thesis (TODO link). See section 5.1.

Its main purpose was to simulate AMP mesh networks during the development phase of the protocol's algorithms.

The project is composed of two modules: Simulation and network model. Both are structured as seperate maven modules. An aggregator POM file on the root level allows to build both projects at once.

## amp-sim
The amp-sim module separates the runtime environment from the network model. The module implements a Spring Shell based Command Line Interface (CLI) which can be used for scripted or interactive test-sessions.

## amp-network
The amp-network module contains the modeled data link layer and the AMP communication stack. To have an appropriate logical representation of a physical network, layers 2 and 3 are kept separate. This layered architecture with known interfaces between the individual layers (see 3.2) corresponds nicely with the clean architecture approach described above. The data exchange between the layers is structured using Java interfaces and abstract classes.

### CLI
The simulation framework is controllable via a custom CLI. Based on the Spring Shell framework, a user can create, operate and diagnose an in-memory AMP network. The CLI can be invoked by building the top level Maven project and invoking the executable jar-file:

```
mvn clean package
java -jar amp-sim/target/amp-sim-0.1.0-SNAPSHOT.jar
```


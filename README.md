Microservice Sample
==============

This is a complete sample of java microservice ecosystem implementation. This template was applied on real projects that use Bamboo for building and Marathon as hoisting service platform. Just copy from user-service submodule to build scalable and maintainable microservice architecture. 

Technologies
------------
- Spring Cloud 
- Spring Boot 2
- MongoDB for storing user data in sample service
- Eureka for Lookup (NOT READY YET)
- Ribbon for Load Balancing. (NOT READY YET)
- Hystrix is used for resilience. (NOT READY YET)
- Hystrix has a dashboard. Turbine can be used to combine the data
from multiple sources. However, this does not work at the moment.
- Zuul is used to route HTTP requests from the outside to the different services. (NOT READY YET)
- Zuul is used to route HTTP requests from the outside to the different services. (NOT READY YET)
- Maven
- Intellij IDEA 2018.1

How To Run
----------

The demo can be run with Docker Machine and Docker. See [How to run](HOW-TO-RUN.md) for more details.


Remarks on the Code
-------------------

The servers for the infrastruture are pretty simple thanks to Spring Cloud:

- api-gateway to redirect all requests to concrete microservices
- microservice-demo-eureka is the Eureka server for service discovery. (NOT READY YET)
- microservice-demo-zuul is the Zuul server. It distributes the requests to the  microservices. (NOT READY YET)
- microservice-demo-turbine can be used to consolidate the Hystrix metrics and has a Hystrix dashboard. (NOT READY YET)


The microservices have a test main application in src/test/java to run them stand alone.

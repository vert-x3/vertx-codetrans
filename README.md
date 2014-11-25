vertx-examples
========

Vert.x 3.0 examples, can be run from the Vert.x command line or deployed programmatically.

## Test Drive

~~~~
mvn test -Pexample

...

##########################
# Vert.x examples runner #
##########################

Choose an example:
1:echo
2:eventbus_pointtopoint
3:eventbus_pubsub
4:eventbusbridge
5:fanout
6:http
7:https
8:proxy
9:route_matcher
10:sendfile
11:simpleform
12:simpleformupload
13:sockjs
14:ssl
15:upload
16:websockets
> 6
Deployed: groovy:http/Server.groovy as d1868f9d-5d87-4125-9ff8-3002b2c54659
Deployed: groovy:http/Client.groovy as 22b0c9d1-fb82-4da1-b69d-5e36564fb879
Press a key after run...
Got response 200
Got data <html><body><h1>Hello from vert.x!</h1></body></html>
~~~~

## Supported languages

### Groovy

~~~~
mvn test -Pexample
~~~~

or

~~~~
mvn test -Pexample -Dlang=groovy
~~~~

### JavaScript (partial)

~~~~
mvn test -Pexample -Dlang=js
~~~~

This is a REST API written using JAX-RS (Jersey). It exposes a PageTrafficResource that accepts server log data representing users' navigation of a website pages. The resource returns the top N most frequently visited path whose depth is equal to the value specified in the context parameter. Example:

Valid Requests

Request # 1 

PUT /TopHitsAnalytics/PageTraffic/3/maxhits HTTP/1.1 

{"log":[
"U1       /", 
"U1       careers", 
"U2       /", 
"U2       careers", 
"U1       mission", 
"U1       contact", 
"U2       mission", 
"U2       contact", 
"U3       /", 
"U3       about", 
"U3       blog" 
]}
 
Response 

HTTP/1.1 200 OK

[
    "careers -> mission -> contact",
    "/ -> careers -> mission"
]

Request # 2: 

PUT /TopHitsAnalytics/PageTraffic/4/maxhits HTTP/1.1 

{"log":[
"U1       /", 
"U1       careers", 
"U2       /", 
"U2       careers", 
"U1       mission", 
"U1       contact", 
"U2       mission", 
"U2       contact", 
"U3       /", 
"U3       about", 
"U3       blog",
"U5       /",
"U4       mission",
"U1       overview",
"U2       overview"
]}

Response: 

HTTP/1.1 200 OK
 
[
    "/ -> careers -> mission -> contact",
    "careers -> mission -> contact -> overview"
]


Request # 3: 

PUT /TopHitsAnalytics/PageTraffic/3/maxhits HTTP/1.1 

{"log":[
"U1       /", 
"U1       careers", 
"U2       /", 
"U2       careers", 
"U3       /", 
"U3       about"  
]}

Response: 

HTTP/1.1 200 OK
 
[]


Invalid Requests 

Request # 1: 

PUT /TopHitsAnalytics/PageTraffic/3/maxhits HTTP/1.1 

{"log":[]}

Response: 

HTTP/1.1 400 Bad Request 

Request # 2: 

PUT /TopHitsAnalytics/PageTraffic/0/maxhits HTTP/1.1 

{"log":[
"U1       /", 
"U1       careers", 
"U2       /", 
"U2       careers", 
"U1       mission", 
"U1       contact", 
"U2       mission", 
"U2       contact", 
"U3       /", 
"U3       about", 
"U3       blog" 
]}

Response: 

HTTP/1.1 400 Bad Request  

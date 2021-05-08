# Textalyze

A REST service for simple text analysis including word frequency and levenshtein distance measure. 

The analysis results are cached internally for one hour. Once the maximum storage time is exceeded old records will be evicted from the cache. 

## Analysis

* **word frequency**: The number of occurrences of a given word in the analyzed text. A frequency of 1 means that the word occurs one time-
* **levenshtein distance**: The difference of two words calculated by the levenshtein algorithm.

## REST services

**TODO** full documentation either here or as swagger

## Testing the rest service 

### Requirements

* jdk 11

### Building 

~~~
mvn package
~~~

### start up

~~~
java -jar target/textalyze-0.0.1-SNAPSHOT.war
~~~

### Try out

(Below [httpie](https://httpie.io/) is being used)

Post a test to run the analysis. The REST service responds with core information on the new created resource, including its **id**

~~~
http POST  :8080/textalyze  
~~~

using the **id** from the above response the resource can be requested again. 
This can be useful to test if the resource still exists in the cache. 

~~~
http GET  :8080/textalyze/pgnLHE7  
~~~


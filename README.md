# Textalyze

A REST service for simple text analysis including word frequency and levenshtein distance measure. 

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

~~~
http POST  :8080/textalyze  
~~~


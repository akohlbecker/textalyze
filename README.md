# Textalyze

A REST service for simple text analysis including word frequency and Levenshtein distance measure. 

The analysis results are cached internally for one hour. Once the maximum storage time is exceeded old records will be evicted from the cache. 

## Analysis

Prior to the text analysis characters are normalized in accordance with the Unicode Standard Annex #15 (convert characters with diacritical marks, change all letters case, decompose ligatures, or convert half-width katakana characters to full-width). 

The tokenization process removed unwanted characters like punctuation and so on. The StandardTokenizer of the lucene framework is being used.

The actual word analyses performed are:

* **word frequency**: The number of occurrences of a given word in the analyzed text. A frequency of 1 means that the word occurs one time.
* **Levenshtein distance**: The difference of two words calculated by the Levenshtein algorithm.

## REST services

For documentation on the REST services please refer to the Swagger-UI, e.g.: [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

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

Post a text to run the analysis. The REST service responds with core information on the new created resource, including its **id**

~~~
echo "Word wörd worrd Other Word" | http POST :8080/textalyze Content-Type:text/plain
~~~

By using the **id** from above response, the resource can be requested again. 
This can be useful to test if the resource still exists in the cache. 

~~~
http GET :8080/textalyze/2cofSjg/ 
~~~

**frequency for given word**

~~~
http GET :8080/textalyze/2cofSjg/Word/frequency  
~~~

**similar words**

~~~
http GET :8080/textalyze/2cofSjg/Word/similar 
~~~

or with specification of a Levenshtine distance threshold

~~~
http GET :8080/textalyze/z-Pj5Sx/Word/similar threshold==1   
~~~


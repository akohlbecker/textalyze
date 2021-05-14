package de.neotop.textalyze;

import java.io.IOException;
import java.util.List;

import org.ehcache.spi.loaderwriter.CacheLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/textalyze")
public class TextalyzeController {

    private static Logger logger = LoggerFactory.getLogger(TextalyzeController.class);

    @Autowired
    private ITextalyzeCache cache;

    @PostMapping(path="", consumes={"text/plain", "application/json"}, produces = "application/json")
    @ApiOperation(value = "Analyze and register the supplied text as a resource for subsequent requests.",
        notes = "The id returned in the response object the identifier to be used with other service endpoints"
    )
    public TextalyzeRecord doAnalyzeText(
            @RequestBody(required = true) String text
            ) throws IOException {

        TextalyzeRecord record = new TextalyzeRecord(generateId());
        TextAnalyzer analyzer = new TextAnalyzer(record, text);
        analyzer.calculateLevenstheinDistance();
        cache.put(analyzer.getRecord());
        return analyzer.getRecord();
    }

    @GetMapping(path="/{id}", produces = "application/json")
    @ApiOperation(value = "Provides basic information on the specified resource.")
    public TextalyzeRecord doGet(
            @PathVariable(name="id") String id
            ) throws IOException {

        return cache.lookup(id);
    }

    @GetMapping(path="/{id}/{word}/frequency", produces = "text/plain")
    @ApiOperation(value = "Returns the frequency of word occurrence of the supplied word.",
        notes = "A frequency of 3, means that the word occurs 3 times in the text.")
    public String doWordFrequency(
            @PathVariable(name="id") String id,
            @PathVariable(name="word") String word
            ) throws IOException, CacheLoadingException, WordNotFoundException {

        return cache.lookup(id).getWordFrequency(word).toString();
    }

    @GetMapping(path="/{id}/{word}/similar", produces = "application/json")
    @ApiOperation(value = "Lists similar words.",
        notes = "Per default word with a Levenshtein distance of <= 2.5, are returned. This behavior can be adjusted by the threshold parameter.")
    public List<String> doSimilarWords(
            @PathVariable(name="id") String id,
            @PathVariable(name="word") String word,
            @RequestParam(name="threshold", defaultValue = "2.5") double threshold
            ) throws IOException, CacheLoadingException, WordNotFoundException {

        return cache.lookup(id).findSimilarWords(word, threshold);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementFoundException(IOException exception) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(exception.getMessage());
    }

    @ExceptionHandler(WordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleWordNotFoundException(WordNotFoundException exception) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(exception.getMessage());
    }

    @ExceptionHandler(CacheLoadingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleCacheLoadingException(CacheLoadingException exception) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(exception.getMessage());
    }


    private String generateId() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 7);

    }
}

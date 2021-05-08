package de.neotop.textalyze;

import java.io.IOException;

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


@RestController
@RequestMapping("/textalyze")
public class TextalyzeController {

    private static Logger logger = LoggerFactory.getLogger(TextalyzeController.class);

    @Autowired
    private ITextalyzeCache cache;

    @PostMapping("")
    public TextalyzeRecord doAnalyzeText(
            @RequestBody(required = true) String text
            ) throws IOException {

        TextalyzeRecord record = new TextalyzeRecord(generateId());
        TextAnalyzer analyzer = new TextAnalyzer(record, text);
        analyzer.calculateLevenstheinDistance();
        cache.put(analyzer.getRecord());
        return analyzer.getRecord();
    }

    @GetMapping("/{id}")
    public TextalyzeRecord doGet(
            @PathVariable(name="id") String id
            ) throws IOException {

        return cache.lookup(id);
    }

    @GetMapping("/{id}/{word}/frequency")
    public Integer doWordFrequency(
            @PathVariable(name="id") String id,
            @PathVariable(name="word") String word
            ) throws IOException, CacheLoadingException, WordNotFoundException {

        return cache.lookup(id).getWordFrequency(word);
    }

    @GetMapping(name="/{id}/{word}/similar")
    public Long doSimilarWords(
            @PathVariable(name="id") String id,
            @PathVariable(name="word") String word,
            @RequestParam(name="threshold", defaultValue = "2.5") double threshold
            ) throws IOException {

        // TODO find similar words

        return 0l;
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
    public ResponseEntity<String> handleNoSuchElementFoundException(WordNotFoundException exception) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(exception.getMessage());
    }


    private String generateId() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 7);

    }
}

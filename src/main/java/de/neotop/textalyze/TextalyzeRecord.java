package de.neotop.textalyze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Holds all information on the analyzed text.
 *
 * <p>
 * In future versions, stop words like "a" or "the" are removed.
 *
 * <h3>Basic information:</h3> Fields exposed via the REST service responses.
 * <ul>
 * <li>{@link #id}</id> The identifier for the record under which it can be
 * referred to at the REST service end-point (see for example
 * {@link ITextalyzeCache#lookup(String)} and
 * {@link TextalyzeController#doGet(String)})</li>
 * <li>{@link #wordCount}:</li> Number of words that have been passed to the
 * analysis after cleaning the text.
 * </ul>
 * <h3>Analysis details:</h3> Fields hidden from the REST service responses.
 * <ul>
 * <li>{@link #wordCount}</li>
 * <li>{@link #wordFrequencies}</li>
 * </ul>
 *
 * TODO separate business logic from data container
 *
 * @author a.kohlbecker
 */
public class TextalyzeRecord implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(TextalyzeRecord.class);

    private static final long serialVersionUID = 4757891973778716697L;

    private String id;
    private long wordCount = 0;

    private double[][] distances;
    // wordFrequencies is backed up by the distinctWords list
    private Map<String, Integer> wordFrequencies = new HashMap<>();
    private List<String> distinctWords = new ArrayList<>();

    public TextalyzeRecord(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /**
     * Total number of words analyzed.
     */
    public long getWordCount() {
        return wordCount;
    }

    public void setWordCount(long wordCount) {
        this.wordCount = wordCount;
    }

    @JsonIgnore // hidden from the REST service response
    public double[][] getDistances() {
        return distances;
    }

    protected void setDistances(double[][] distances) {
        this.distances = distances;
    }

    /**
     * Word Frequencies, that is the number of occurrences of each word in the
     * analyzed text.
     * <p>
     * For getting the frequency for a specific word
     * {@link {@link #getWordFrequency(String)} has to be used as this also applies the
     * normalization.c
     *
     *
     * @return an unmodifiable map of the word frequencies
     */
    protected Map<String, Integer> getWordFrequencies() {
        return Collections.unmodifiableMap(wordFrequencies);
    }

    public Integer getWordFrequency(String word) throws WordNotFoundException {
        String normalizedWord = TextAnalyzer.normalizeText(word);
        if(wordFrequencies.containsKey(normalizedWord)) {
            return wordFrequencies.get(normalizedWord);
        } else {
            throw new WordNotFoundException(word);
        }
    }

    /**
     * Puts a word consistently into the wordFrequencies map an into the
     * distinctWords list. The word frequency count is maintained by this
     * method.
     *
     * @param word
     *            The word to add
     * @return true when the word was not yet registered.
     */
    public boolean putWord(String word) {
        if (!wordFrequencies.containsKey(word)) {
            wordFrequencies.put(word, 1);
            distinctWords.add(word);
            logger.info("added " + word);
            return true;
        } else {
            wordFrequencies.put(word, wordFrequencies.get(word) + 1);
            logger.info("updated " + word);
            return false;
        }
    }

    /**
     * Prior analysis, the text is usually cleaned from
     * punctuation characters and is normalized (see {@link TextAnalyzer}),
     * hence, the word count may be lower than in the original text.
     *
     * @return the distinctWords as unmodifiable list
     */
    public List<String> distinctWords() {
        return Collections.unmodifiableList(distinctWords);
    }

    /**
     * Provides the levenshtein distance for two given words from the internal matrix.
     *
     * @throws WordNotFoundException
     */
    public double getLevenshteinDistance(String a, String b) throws WordNotFoundException {
        int indexA = distinctWords.indexOf(TextAnalyzer.normalizeText(a));
        int indexB = distinctWords.indexOf(TextAnalyzer.normalizeText(b));
        if(indexA < 0) {
            throw new WordNotFoundException(a);
        }
        if(indexB < 0) {
            throw new WordNotFoundException(b);
        }
        return distances[indexA][indexB];
    }



}

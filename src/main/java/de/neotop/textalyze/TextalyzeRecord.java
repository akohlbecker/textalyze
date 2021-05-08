package de.neotop.textalyze;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Holds all information on the analyzed text.

 * <p>In future versions, stop words like "a" or "the" are removed.
 *
 * <h3>Basic information:</h3>
 * Fields exposed via the REST service responses.
 * <ul>
 * <li>{@link #id}</id> The identifier for the record under which it can be
 * referred to at the REST service end-point (see for example
 * {@link ITextalyzeCache#lookup(String)} and
 * {@link TextalyzeController#doGet(String)})</li>
 * <li>{@link #wordCount}:</li> Number of words that have been passed to the analysis after cleaning the text.
 * </ul>
 * <h3>Analysis details:</h3>
 * Fields hidden from the REST service responses.
 * <ul>
 * <li>{@link #wordCount}</li>
 * <li>{@link #wordFrequencies}</li>
 * </ul>
 *
 * @author a.kohlbecker
 */
public class TextalyzeRecord implements Serializable {

    private static final long serialVersionUID = 4757891973778716697L;

    private String id;
    private long wordCount = 0;

    private double[][] distances;
    private Map<String, Integer> wordFrequencies = new HashMap<>();

    public TextalyzeRecord(String id) {
        this.id = id;
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

    public void setDistances(double[][] distances) {
        this.distances = distances;
    }

    /**
     * Word Frequencies, that is the number of occurrences of each word in the analyzed text.
     * Prior analysis, the text is usually cleaned from punctuation characters
     * and is normalized (see {@link TextAnalyzer}), hence, the word count may be lower
     * than in the original text.
     */
    @JsonIgnore // hidden from the REST service response
    public Map<String, Integer> getWordFrequencies() {
        return wordFrequencies;
    }

    public String getId() {
        return id;
    }

}

package de.neotop.textalyze;

import java.io.Serializable;
import java.util.List;

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
    private long wordCount;

    private double[][] distances;
    private List<String> wordFrequencies;

    public TextalyzeRecord(String id) {
        this.id = id;
    }

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
     * Word
     * Prior analysis, the text is cleaned from punctuation characters
     * and is normalized, hence, the word count may be lower than in the original text.
     * @return
     */
    @JsonIgnore // hidden from the REST service response
    public List<String> getWordCout() {
        return wordFrequencies;
    }

    public void setWordFrequencies(List<String> wordFrequencies) {
        this.wordFrequencies = wordFrequencies;
    }

    public String getId() {
        return id;
    }

}

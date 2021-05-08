package de.neotop.textalyze;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Text analysis functions. The analysis is applied to a whole text.
 *
 * <ul>
 * <li>word frequencies</li>
 * <li>Levenshtein distance</li>
 * </ul>
 *
 * @author a.kohlbecker
 */
public class TextAnalyzer {

    private static Logger logger = LoggerFactory.getLogger(TextAnalyzer.class);

    private TextalyzeRecord record;

    public TextAnalyzer(TextalyzeRecord record, StringReader stringReader) throws IOException {
        this.record = record;
        tozenizeAndCount(stringReader);
    }

    private void tozenizeAndCount(StringReader stringReader) throws IOException {

        AttributeFactory factory = AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY;

        StandardTokenizer tokenizer = new StandardTokenizer(factory);
        tokenizer.setReader(stringReader);
        tokenizer.reset();

        CharTermAttribute attr = tokenizer.addAttribute(CharTermAttribute.class);

        long wordCount = 0;
        logger.debug("starting analysis ....");
        while (tokenizer.incrementToken()) {
            String word = attr.toString();
            logger.debug(word);
            if(!record.getWordFrequencies().containsKey(word)) {
                record.getWordFrequencies().put(word, 1);
            } else {
                record.getWordFrequencies().put(word, record.getWordFrequencies().get(word) + 1);
            }
            wordCount++;
        }
        tokenizer.close();
        record.setWordCount(wordCount);

        logger.info("... analysis done");

    }

    public TextalyzeRecord getRecord() {
        return record;
    }

}

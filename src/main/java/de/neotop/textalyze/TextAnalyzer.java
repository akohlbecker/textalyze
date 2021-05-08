package de.neotop.textalyze;

import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;
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

    public TextAnalyzer(TextalyzeRecord record, String stringReader) throws IOException {
        this.record = record;
        tozenizeAndCount(new StringReader(TextAnalyzer.normalizeText(stringReader)));
    }

    public static String normalizeText(String string) {
        // TODO consider using the lucene normalizer ?
       return Normalizer.normalize(string, Normalizer.Form.NFKD);
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
            record.putWord(word);
            wordCount++;
        }
        tokenizer.close();
        record.setWordCount(wordCount);

        logger.info("... analysis done");
    }

    /**
     * calculates a matrix of levenshtein distances for each distinct word combination,
     * and stored the result in {@link TextalyzeRecord#getDistances()}
     */
    public void calculateLevenstheinDistance() {
        LevenshteinDistance ld =  new LevenshteinDistance();
        List<String> words = record.distinctWords();
        double [][] matrix = new double[words.size()][words.size()];
        for(int i = 0 ; i < words.size(); i++) {
            for(int j = 0; j < words.size(); j++) {
                matrix[i][j] = ld.apply(words.get(i), words.get(j));
            }
        }
        record.setDistances(matrix);
    }

    public TextalyzeRecord getRecord() {
        return record;
    }

}

package de.neotop.textalyze;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TextalyzeApplicationTests {


    @Autowired
    private TextalyzeController controller;

    static final String O_COMBINED_MARK = "o\u0308";
    static final String simpleText = "​Word Word Words Wor word​ Wörd  W" + O_COMBINED_MARK + "rd";
    static final String punctationText = "​Word Word, Words. Wor (word​)? !!!! Wörd W" + O_COMBINED_MARK + "rd";
    private static final int totalCount = 7;
    private static final int distinctWords = 5;

    @Test
    void contextLoads() {

    }

    @Test
    public void post_and_get() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(simpleText);
        assertNotNull(record);
        assertThat(record.getId()).isNotBlank();
        TextalyzeRecord record2 = controller.doGet(record.getId());
        assertNotNull(record2);
        assertEquals(record2.getId(), record.getId());
    }

   @Test
   public void wordCount_simple() throws Exception {
       TextalyzeRecord record = controller.doAnalyzeText(simpleText);
       assertThat(record.getWordCount()).isEqualTo(totalCount);
   }

    @Test
    public void wordCount_punctation() throws Exception {
        TextalyzeRecord record2 = controller.doAnalyzeText(punctationText);
        assertThat(record2.getWordCount()).isEqualTo(totalCount);
    }

    @Test
    public void distinctWords_simple() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(simpleText);
        assertThat(record.distinctWords().size()).isEqualTo(distinctWords);
    }

     @Test
     public void distinctWords_punctation() throws Exception {
         TextalyzeRecord record2 = controller.doAnalyzeText(punctationText);
         assertThat(record2.distinctWords().size()).isEqualTo(distinctWords);
     }

    @Test
    public void wordFrequency_simple() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(simpleText);
        assertEquals(2, record.getWordFrequency("Word"));
        assertEquals(2, record.getWordFrequency("Wörd"));
        assertEquals(1, record.getWordFrequency("Words"));
    }

    @Test
    public void wordFrequency_punctation() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(punctationText);
        assertEquals(2, record.getWordFrequency("Word"));
        assertEquals(2, record.getWordFrequency("Wörd"));
        assertEquals(1, record.getWordFrequency("Words"));
    }

    @Test
    public void distances() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(punctationText);
        assertEquals(0,record.getLevenshteinDistance("Word", "Word"));
        assertEquals(1, record.getLevenshteinDistance("Wörd", "Word"));
        assertEquals(1, record.getLevenshteinDistance("Words", "Word"));
    }

    @Test
    public void wordFrequency_unknownWord() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(simpleText);
        assertThatExceptionOfType(WordNotFoundException.class)
            .isThrownBy(() -> {
            assertEquals(2, record.getWordFrequency("unknown-Word"));
        });
    }

    @Test
    public void distances_unknownWor() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(punctationText);
        assertThatExceptionOfType(WordNotFoundException.class)
            .isThrownBy(() -> {
               record.getLevenshteinDistance("unknown-Word", "Word");
            });
    }

    @Test
    public void workflow() throws Exception {
        String id = controller.doAnalyzeText(punctationText).getId();
        assertEquals(2, controller.doWordFrequency(id, "Wörd"));
    }


}

package de.neotop.textalyze;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void wordFrequencies() throws Exception {
        TextalyzeRecord record = controller.doAnalyzeText(simpleText);
        assertThat(record.getId()).isNotBlank();
        TextalyzeRecord record2 = controller.doGet(record.getId());
        assertNotNull(record2);
        assertEquals(record2.getId(), record.getId());
    }

}

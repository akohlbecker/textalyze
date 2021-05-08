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

    static final String simpleText = "​Word Words Wor word​";

    static final String punctationText = "​Word, Words. Wor (word​)? !!!!    ";

	@Test
	void contextLoads() {

	}

	@Test
    public void post_and_get_record() throws Exception {
	    TextalyzeRecord record = controller.doAnalyzeText(simpleText);
	    assertNotNull(record);
	    assertThat(record.getId()).isNotBlank();
	    TextalyzeRecord record2 = controller.doGet(record.getId());
	    assertNotNull(record2);
	    assertEquals(record2.getId(), record.getId());
	}

}

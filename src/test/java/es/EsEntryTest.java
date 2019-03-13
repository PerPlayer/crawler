package es;


import com.crawler.CrawlerApplication;
import com.crawler.es.document.EntryDocument;
import com.crawler.es.service.EsEntryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerApplication.class)
public class EsEntryTest {

    @Autowired
    private EsEntryService entryService;

    @Test
    public void saveTest(){
        EntryDocument entryDocument = new EntryDocument();
        entryDocument.setId("10001");
        entryDocument.setTitle("Test");
        entryDocument.setContent("TestContent");
        entryService.save(entryDocument);
    }
}

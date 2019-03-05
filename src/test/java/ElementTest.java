import com.crawler.CrawlerApplication;
import com.crawler.crawler.model.Entry;
import com.crawler.service.EntryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by SHW on 2019/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = CrawlerApplication.class)
@SpringBootTest(classes = CrawlerApplication.class)
public class ElementTest {

    @Autowired
    private EntryService elementService;

    @Test
    public void saveTest(){
        Entry entry = new Entry();
        entry.setId("1001");
        entry.setTitle("ATest");
        entry.setContent("A test content");
        entry.setWeight(10);
        entry.setTaskId("1002");
        entry.setVersion(1);
        elementService.save(entry);
    }

    @Test
    public void updateTest(){
        elementService.update("1001", "title", "Test");
    }
}

import com.crawler.CrawlerApplication;
import com.crawler.crawler.model.Element;
import com.crawler.service.ElementService;
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
    private ElementService elementService;

    @Test
    public void saveTest(){
        Element element = new Element();
        element.setId("1001");
        element.setTitle("ATest");
        element.setContent("A test content");
        element.setWeight(10);
        element.setTaskId("1002");
        element.setVersion(1);
        elementService.save(element);
    }

    @Test
    public void updateTest(){
        elementService.update("1001", "title", "Test");
    }
}

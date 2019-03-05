import com.crawler.CrawlerApplication;
import com.crawler.crawler.model.Task;
import com.crawler.service.TaskService;
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
public class TaskTest {

    @Autowired
    private TaskService taskService;

    @Test
    public void saveTest(){
        Task task = new Task();
        task.setId("1001");
        task.setHref("www.bing.com");
        task.setSiteId("1001");
        task.setDomain("www.bing.com");
        task.setWeight(20);
        task.setDeep(0);
        task.setStatus(6);
        taskService.save(task);
    }

    @Test
    public void updateTest(){
        taskService.update("1001", "title", "Test");
    }
}

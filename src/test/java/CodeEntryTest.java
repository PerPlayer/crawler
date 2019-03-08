import com.crawler.CrawlerApplication;
import com.crawler.crawler.model.CodeEntry;
import com.crawler.service.CodeEntryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by SHW on 2019/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerApplication.class)
public class CodeEntryTest {

    @Autowired
    private CodeEntryService codeEntryService;

    @Test
    public void findTest(){
        CodeEntry codeEntry = codeEntryService.findByCode("code");
        System.out.println(codeEntry);
    }

}

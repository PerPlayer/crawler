import com.crawler.CrawlerApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

public class SeleniumTest {

    private static ChromeDriver browser;

    @Before
    public void openBrowser() {
        CrawlerApplication.main(new String[]{});
        System.setProperty("webdriver.chrome.driver", "D:\\central\\chromedriver.exe");
        browser = new ChromeDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void mainTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        browser.get("http://localhost/perplayer/statics/html/index.html");
        search("spring");
        search("redis");
    }

    private void search(String text) throws InterruptedException {
        System.out.println("Search Text: " + text);
        WebElement webElement = browser.findElementById("search");
        webElement.clear();
        webElement.sendKeys(text);
        browser.findElementById("exec").click();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("Result: ");
        browser.findElementsByClassName("row").stream().forEach((WebElement element) -> {
            System.out.println("\t" + element.getText());
        });
    }

    @After
    public void closeBrowser(){
        browser.quit();
        CrawlerApplication.exit();
    }

}

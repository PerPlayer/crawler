import com.crawler.service.rabbitmq.Sender;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RabbitMQTest extends BaseTest {

    @Autowired
    private Sender sender;

    @Test
    public void sendTest(){
        sender.send("你好");
    }
}

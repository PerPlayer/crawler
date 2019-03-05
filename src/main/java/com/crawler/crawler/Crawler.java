package com.crawler.crawler;

import com.crawler.crawler.model.Entry;
import com.crawler.crawler.model.Task;
import com.crawler.crawler.rules.CsdnRule;
import com.crawler.service.EntryService;
import com.crawler.service.TaskService;
import com.crawler.util.ApplicationContextUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Crawler {

    private TaskService taskService;

    private EntryService entryService;

    Logger logger = LoggerFactory.getLogger(Crawler.class);

    public static void main(String[] args) throws InterruptedException {
        new Crawler().execute();
    }

    public void execute() throws InterruptedException {
        taskService = (TaskService) ApplicationContextUtil.getBean(TaskService.class);
        entryService = (EntryService) ApplicationContextUtil.getBean(EntryService.class);
        while (true) {
            List<Task> tasks = taskService.findByStatusAndDeepLessThan(0, 3);
            logger.info("开始解析");
            for (Task task : tasks) {
                Connection connection = getConnection(task.getHref());
                try {
                    Document doc = connection.get();

                    System.out.println(doc.title());
                    Entry entry = new Entry();
                    entry.setTitle(doc.title());
                    entry.setContent(doc.getElementsByTag("p").first().text());
                    entry.setTaskId(task.getId());
                    entryService.save(entry);

                    Elements elements = doc.getElementsByAttribute("href");
                    Iterator<Element> iterator = elements.iterator();
                    while (iterator.hasNext()) {
                        Element element = iterator.next();
                        if (CsdnRule.match(element)) {
                            Task childTask = new Task();
                            String href = element.attr("href");
                            childTask.setHref(href);
                            childTask.setDeep(task.getDeep() + 1);
                            childTask.setDomain(href.replaceFirst("https://([a-zA-Z0-9]*)/.*", "$1"));
                            childTask.setDescription(element.attr("title"));
                            childTask.setParentTask(task);
                            taskService.save(childTask);
                        }
                    }
                    Thread.sleep(500L);
                    task.setStatus(1);
                    taskService.update(task);
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private Connection getConnection(String href) {
//        https://baijiahao.baidu.com/s?id=1626678723346182275&wfr=spider&for=pc
//        https://blog.csdn.net/zwhfyy/article/details/885588
        return Jsoup.connect(href)
                 .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                 .timeout(10000);
    }
}

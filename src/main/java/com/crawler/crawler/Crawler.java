package com.crawler.crawler;

import com.crawler.crawler.model.CodeEntry;
import com.crawler.crawler.model.Entry;
import com.crawler.crawler.model.Task;
import com.crawler.crawler.rules.CsdnRule;
import com.crawler.service.CodeEntryService;
import com.crawler.service.EntryService;
import com.crawler.service.TaskService;
import com.crawler.util.ApplicationContextUtil;
import com.google.common.collect.Lists;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Crawler {

    private TaskService taskService;

    private EntryService entryService;

    private CodeEntryService codeEntryService;

    private Random random = new Random();

    private int taskSleep = 5;

    private int taskDeep = 3;

    Logger logger = LoggerFactory.getLogger(Crawler.class);

    public static void main(String[] args) throws Exception {
        new Crawler().execute();
    }

    public void execute() throws Exception {
        taskService = (TaskService) ApplicationContextUtil.getBean(TaskService.class);
        entryService = (EntryService) ApplicationContextUtil.getBean(EntryService.class);
        codeEntryService = (CodeEntryService) ApplicationContextUtil.getBean(CodeEntryService.class);
        taskSleep = getCodeIntValue("task.sleep", taskSleep);
        taskDeep = getCodeIntValue("task.deep", taskDeep);
        logger.info("-------- 开始解析 --------");
        while (true) {
            if ("off".equals(getCodeStringValue("task.switch", "on"))) {
                TimeUnit.SECONDS.sleep(taskSleep);
                continue;
            }
            List<Task> tasks = taskService.findByStatusAndDeepLessThan(0, 3);
            for (Task task : tasks) {
                logger.info("task: {}, title: {}", task.getId(), task.getDescription());
                Connection connection = getConnection(task.getHref());
                Document doc = null;
                try {
                    doc = connection.get();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                parseEntry(task, doc);
                parseChildHref(task, doc);
                updateTask(task);
            }
        }
    }

    private void updateTask(Task task) {
        task.setStatus(1);
        taskService.update(task);
    }

    private void parseChildHref(Task task, Document doc) {
        Elements elements = doc.getElementsByAttribute("href");
        Iterator<Element> iterator = elements.iterator();
        ArrayList<String> tempHrefs = Lists.newArrayList();
        int i = 0;
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String href = element.attr("href");
            if(taskService.countByHref(href)>0) continue;
            if (!tempHrefs.contains(href)&& CsdnRule.match(element)) {
                Task childTask = new Task();
                tempHrefs.add(href);
                childTask.setHref(href);
                childTask.setDeep(task.getDeep() + 1);
                childTask.setDomain(href.replaceFirst("(?:https|http)://([a-zA-Z0-9\\.]*)/.*", "$1"));
                childTask.setDescription(element.attr("title"));
                childTask.setParentTask(task);
                taskService.save(childTask);
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(taskSleep));
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
                i++;
            }
        }
        logger.info("-- 获得 {} 个子链接", i);
    }

    private void parseEntry(Task task, Document doc) {
        Entry entry = new Entry();
        entry.setTitle(doc.title());
        entry.setContent(doc.getElementsByTag("article").first().text());
        entry.setTaskId(task.getId());
        entryService.save(entry);
    }

    private Connection getConnection(String href) {
        return Jsoup.connect(href)
                .header("content-type", "text/html; charset=UTF-8")
                 .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36")
                 .timeout(10000);
    }

    private int getCodeIntValue(String code, int defaultValue) {
        CodeEntry codeEntry = codeEntryService.findByCode(code);
        if (codeEntry == null) {
            return defaultValue;
        }
        return Integer.valueOf(codeEntry.getValue());
    }

    private String getCodeStringValue(String code, String defaultValue) {
        CodeEntry codeEntry = codeEntryService.findByCode(code);
        if (codeEntry == null) {
            return defaultValue;
        }
        return codeEntry.getValue();
    }

}

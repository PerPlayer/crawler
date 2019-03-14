package es;


import com.crawler.CrawlerApplication;
import com.crawler.crawler.model.Entry;
import com.crawler.es.document.EntryDocument;
import com.crawler.es.service.EsEntryService;
import com.crawler.service.EntryService;
import com.crawler.util.BPage;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CrawlerApplication.class)
public class EsEntryTest {

    @Autowired
    private EntryService service;

    @Autowired
    private EsEntryService entryService;

    @Test
    public void saveTest() {
        EntryDocument entryDocument = new EntryDocument();
        entryDocument.setId("10001");
        entryDocument.setTitle("Test");
        entryDocument.setContent("TestContent");
        entryService.save(entryDocument);
    }

    @Test
    public void queryTest() {
        BPage<Entry> page = BPage.<Entry>of(1, 10);
        SearchResponse response = null;
        entryService.queryWithHighLight("Spring", page);
        for (SearchHit searchHit : response.getHits()) {
            if (response.getHits().getHits().length <= 0) {
                return;
            }
            System.out.println(searchHit.getScore());
            System.out.println(searchHit.getId());
            System.out.println((String) searchHit.getSource().get("title"));
            System.out.println(searchHit.getHighlightFields().get("content").fragments()[0].toString());
        }
        System.out.println(response);
    }

    @Test
    public void insertEs(){
        Page<Entry> entryPage = service.findAll(PageRequest.of(0, 10));
        for (Entry entry : entryPage.getContent()) {
            EntryDocument entryDocument = new EntryDocument();
            BeanUtils.copyProperties(entry, entryDocument);
            entryDocument.setHref(entry.getTask().getHref());
            entryService.save(entryDocument);
        }
        System.out.println("done...");
    }
}

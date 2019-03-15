package com.crawler.es.service.impl;

import com.crawler.crawler.model.Entry;
import com.crawler.es.document.EntryDocument;
import com.crawler.es.repository.EsEntryRepo;
import com.crawler.es.service.EsEntryService;
import com.crawler.util.BPage;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryParseContext;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EsEntryImpl implements EsEntryService {


    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private EsEntryRepo repo;

    @Override
    public void save(EntryDocument document) {
        repo.save(document);
    }

    @Override
    public BPage<Entry> queryWithHighLight(String content, BPage<Entry> page){
        HighlightBuilder highlightBuilder = new HighlightBuilder().field("content").requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        SearchResponse response = template.getClient().prepareSearch("crawler_entry").setTypes("crawler_entry")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.matchQuery("content", content)) // 全文检索
//            .addHighlightedField("content")// 高亮，可以设置前缀和后缀
                .setFrom(page.getCurrent()).setSize(page.getSize()).setExplain(true)// 分页
                .addSort(new ScoreSortBuilder().order(SortOrder.DESC))// 排序
                .setTrackScores(true)// 获取得分
                .highlighter(highlightBuilder)
                .execute().actionGet();
        page.setList(convertEntry(response));
        page.setTotal(response.getHits().totalHits/page.getSize());
        return page;
    }

    @Override
    public void deleteById(String id){
        repo.deleteById(id);
    }

    private List<Entry> convertEntry(SearchResponse response) {
        ArrayList<Entry> entries = Lists.newArrayList();
        for (SearchHit searchHit : response.getHits()) {
            Entry entry = new Entry();
            entry.setId(searchHit.getId());
            Map<String, Object> source = searchHit.getSource();
            entry.setTitle(source.get("title").toString());
            entry.setContent(searchHit.getHighlightFields().get("content").fragments()[0].toString());
            entry.setHref(source.get("href").toString());
            entry.setCreateTime(new Date(Long.valueOf(source.get("createTime").toString())));
            entries.add(entry);
        }
        return entries;
    }

}

package com.small.es;

import org.elasticsearch.action.search.SearchRequest;

import java.util.List;

/**
 * @author wesson
 * Create at 2023/8/5 00:33 周六
 */
public class EsUtil {

    public List<Object> searchUserByCity(String city) throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        //
        //SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("city", city);
        //searchSourceBuilder.query(termQueryBuilder);
        //searchRequest.source(searchSourceBuilder);
        //SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        //return getSearchResult(searchResponse);
        return null;
    }

}

package com.dcmall.back.Controller;

import com.dcmall.back.CosineSimilarityCalculate;
import com.dcmall.back.Service.EmbeddingService;
import com.dcmall.back.model.embedDAO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@RestController
public class searchController {
    class node implements Comparable<node>{
        @JsonProperty("title")
        String title;
        @JsonProperty("percentage")
        double percentage;

        public node(String title, double percentage){
            this.title = title;
            this.percentage = percentage;
        }

        public int compareTo(node n) {
            // percentage를 기준으로 내림차순 정렬
            if (this.percentage > n.percentage) {
                return -1;
            } else if (this.percentage < n.percentage) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    @Autowired
    private EmbeddingService embeddingService;
    @Autowired
    private embedDAO eDAO;
    private ArrayList<node> recomList;

    @PostMapping("/receiveSearch")
    public ArrayList<node> receiveText(@RequestBody String searchText) throws IOException {    //올바르게 받아왔음
        ArrayList<Double> searchResult = embeddingService.getEmbedding(searchText);
        var dbEmbedding = eDAO.selectEmbed();    //이걸 매번 가져오는 건 좀 그런데;; title / embedding으로 구성됨

        CosineSimilarityCalculate cosineSimilarityCalculate = new CosineSimilarityCalculate();
        recomList = new ArrayList<>();
        for(var embed : dbEmbedding){
            recomList.add(new node(embed.getTitle(), cosineSimilarityCalculate.cosineSimilarity(searchResult, embed.getEmbedding())));
        }
        Collections.sort(recomList);
        return recomList;
    }
}

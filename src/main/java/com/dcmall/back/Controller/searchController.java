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
/*
@RequestBody는 HTTP 요청의 body 부분에 포함된 데이터를 Java 객체로 역직렬화(Deserialization)하여 메서드 매개변수로 받을 수 있게 해주는 어노테이션입니다.

이를 통해 클라이언트에서 보낸 JSON, XML 등의 데이터를 서버 측에서 쉽게 처리할 수 있습니다. 클라이언트가 보낸 데이터를 자동으로 Java 객체로 변환해주므로,
별도의 파싱 과정 없이 데이터를 사용할 수 있습니다.
@RequestBody를 사용할 때 클라이언트가 보내는 데이터가 여러 개의 값으로 구성되어 있다면 Map<String, Object>로 선언하면 됩니다.

이렇게 하면 클라이언트가 보낸 JSON 데이터의 키-값 쌍들이 Map에 자동으로 매핑됩니다.
그 후에는 Map의 get() 메서드를 사용하여 각 값을 개별적으로 꺼내 사용할 수 있습니다.

이를 통해 복잡한 JSON 데이터도 손쉽게 처리할 수 있습니다.
 */
/*
성능 향상 여지:
recomList를 HashSet<node>로 구성할 경우
HashSet는 중복값을 걸러주는 자료형이다.
그치만 어떤 값을 기준으로 중복값으로 판단하게 할 지는 세팅해줘야 하는 것으로 보이며,
이를 static으로 선언 함으로써, 서버가 꺼질 때까지 증발하지 않게 두고
recomList와 DB Count의 값이 달라질 때만, selectEmbed()를 한다면 불필요한 select 횟수를 줄일 수 있을 것으로 보인다.
 */
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
        var dbEmbedding = eDAO.selectEmbed();

        CosineSimilarityCalculate cosineSimilarityCalculate = new CosineSimilarityCalculate();
        recomList = new ArrayList<>();
        for(var embed : dbEmbedding){
            recomList.add(new node(embed.getTitle(), cosineSimilarityCalculate.cosineSimilarity(searchResult, embed.getEmbedding())));
        }
        Collections.sort(recomList);
        return recomList;
    }
}

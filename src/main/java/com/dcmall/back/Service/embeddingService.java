package com.dcmall.back.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class embeddingService {
    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ArrayList<Double> getEmbedding(String text) throws IOException {
        ObjectNode jsonBody = objectMapper.createObjectNode();
        jsonBody.put("model", "text-embedding-3-small");
        jsonBody.put("input", text);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/embeddings")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JsonNode jsonNode = objectMapper.readTree(response.body().string());
            JsonNode embeddingNode = jsonNode.get("data").get(0).get("embedding");

            ArrayList<Double> result = new ArrayList<>();
            if (embeddingNode.isArray()) {
                for (JsonNode node : embeddingNode) {
                    result.add(node.asDouble());
                }
            }
            return result;
        }
    }
}

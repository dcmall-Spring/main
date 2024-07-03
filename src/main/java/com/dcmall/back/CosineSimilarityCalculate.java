package com.dcmall.back;

import java.util.ArrayList;

public class CosineSimilarityCalculate
{
    public double cosineSimilarity(ArrayList<Double> vecA, ArrayList<Double> vecB) {
        if (vecA.size() != vecB.size()) {
            throw new IllegalArgumentException("벡터의 길이는 같아야 합니다.");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.size(); i++) {
            dotProduct += vecA.get(i) * vecB.get(i);
            normA += Math.pow(vecA.get(i), 2);
            normB += Math.pow(vecB.get(i), 2);
        }

        normA = Math.sqrt(normA);
        normB = Math.sqrt(normB);

        double result = ((dotProduct / (normA * normB)) *100);
        return Double.parseDouble(String.format("%.2f", result));   //3번째 자리에서 반올림한 것을(String) Double로
    }
}

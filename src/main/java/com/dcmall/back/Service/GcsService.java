package com.dcmall.back.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

@Service
public class GcsService {
    @Value("${gcp.storage.bucket}")
    private String bucket;
    @Value("${gcp.storage.project-id}")
    private String projectId;
    @Value("${gcp.storage.credentials.location}")
    private Resource credentials;   //얘가 지금 null 이유는 자세히 모르겠음(PostConstruct로 해결 순서 문제였던 것 같다)

    private Storage storage;

    // GCS 클라이언트 초기화
    @PostConstruct // 이 메서드는 Spring이 빈을 초기화한 후 호출됩니다.
    public void init() throws IOException {
        if (credentials == null) {
            throw new IllegalStateException("GCP credentials are not loaded. Please check the credentials path.");
        }
        InputStream credentialsStream = credentials.getInputStream();
        this.storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build()
                .getService();
    }

    public ArrayList<String> uploadFile(ArrayList<String> listImageUrl, String siteName) {
        ArrayList<String> accessUrlList = new ArrayList<>();
        for (String imageUrl : listImageUrl) {
            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;

            try {
                // URL로부터 연결을 생성하고 데이터를 가져옴
                URL url = new URL(imageUrl);
                URLConnection urlConnection = url.openConnection();
                inputStream = urlConnection.getInputStream();

                // Content-Type과 Content-Length 가져오기
                String contentType = urlConnection.getContentType();

                // BufferedInputStream을 사용하여 데이터 버퍼링
                bufferedInputStream = new BufferedInputStream(inputStream);

                // 파일 이름은 URL에서 마지막 부분을 추출하여 사용
                String fileName = extractFileNameFromUrl(imageUrl);
                fileName = siteName + "/" + fileName;

                if(fileName.contains("Arcalive")){
                    String[] splitStr = fileName.split("\\?");
                    fileName = splitStr[0];
                }

                // GCS에 파일 업로드 (stream 방식)
                String Url =  uploadToGcs(fileName, bufferedInputStream, contentType);

                accessUrlList.add(Url);

            } catch (IOException e) {
                e.printStackTrace();  // 예외 처리
                accessUrlList.add("no data");
            } finally {
                // 자원 해제
                try {
                    if (bufferedInputStream != null) bufferedInputStream.close();
                    if (inputStream != null) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return accessUrlList;
    }

    // 파일 이름 추출 (URL에서 마지막 "/" 이후의 부분을 파일 이름으로 사용)
    private String extractFileNameFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    // GCS에 파일 업로드
    private String uploadToGcs(String fileName, InputStream inputStream, String contentType) {
        // 퍼블릭 접근을 허용하도록 설정
        BlobInfo blobInfo = BlobInfo.newBuilder(bucket, fileName)
                .setContentType(contentType)
                .build();

        // GCS에 stream으로 데이터를 업로드
        storage.create(blobInfo, inputStream);

        String accessURL = "https://storage.googleapis.com/"+bucket+"/"+fileName;

        return accessURL;
    }
}

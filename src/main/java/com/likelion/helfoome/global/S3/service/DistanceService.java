package com.likelion.helfoome.global.S3.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.helfoome.global.auth.dto.CoordinateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DistanceService {

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String apiKey;

  private String addrSearchUrl = "https://dapi.kakao.com/v2/local/search/address.json";
  private final String coordTransformUrl = "https://dapi.kakao.com/v2/local/geo/transcoord.json";

  public Long getDistance(String addr1, String addr2) {
    CoordinateDto coor1 = getCoordinate(addr1);
    CoordinateDto coor2 = getCoordinate(addr2);

    // 오류가 있으면 오류 출력하고 null반환
    if (coor1.getError() != null || coor2.getError() != null) {
      System.out.println(
          "좌표 변환중 문제 생김: "
              + (coor1.getError() != null ? coor1.getError() : "")
              + (coor2.getError() != null ? coor2.getError() : ""));
      return null;
    }

    // x, y 값을 double로 변환
    double x1 = Double.parseDouble(coor1.getX());
    double y1 = Double.parseDouble(coor1.getY());
    double x2 = Double.parseDouble(coor2.getX());
    double y2 = Double.parseDouble(coor2.getY());

    // 두점사이 거리 공식임
    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

    return Math.round(distance);
  }

  // 이 아래가 온갖 API호출하는 부분임

  public CoordinateDto getCoordinate(String addr) {
    CoordinateDto coordinateDto = new CoordinateDto();
    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "KakaoAK " + apiKey);
      HttpEntity<String> entity = new HttpEntity<>(headers);

      // 첫 번째 API 호출(주소 검색을 통해 x, y 위도 경도 얻기)
      String url = addrSearchUrl + "?query=" + addr;
      ResponseEntity<String> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

      // 첫 번째 응답 파싱
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode root = objectMapper.readTree(response.getBody());
      JsonNode documents = root.path("documents");

      if (documents.isArray() && !documents.isEmpty()) {
        JsonNode firstDocument = documents.get(0);
        String x = firstDocument.path("x").asText();
        String y = firstDocument.path("y").asText();

        // 두 번째 API 호출(변환된 좌표 얻기)
        String transUrl =
            coordTransformUrl + "?x=" + x + "&y=" + y + "&input_coord=WGS84&output_coord=WTM";
        ResponseEntity<String> transResponse =
            restTemplate.exchange(transUrl, HttpMethod.GET, entity, String.class);

        // 두 번째 응답 파싱
        JsonNode transRoot = objectMapper.readTree(transResponse.getBody());
        JsonNode transDocuments = transRoot.path("documents");

        if (transDocuments.isArray() && !transDocuments.isEmpty()) {
          JsonNode transformedCoordinates = transDocuments.get(0);
          String transformedX = transformedCoordinates.path("x").asText();
          String transformedY = transformedCoordinates.path("y").asText();

          coordinateDto.setX(transformedX);
          coordinateDto.setY(transformedY);

        } else {
          coordinateDto.setError("변환 좌표 못찾음");
        }
      } else {
        coordinateDto.setError("주어진 주소에 해당하는 결과 없음");
      }
    } catch (Exception e) {
      e.printStackTrace();
      coordinateDto.setError("좌표 변환중 에러발생(API 호출중에 문제 생김)");
    }
    return coordinateDto;
  }
}

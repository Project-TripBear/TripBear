package com.project.trip.allplace.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.project.trip.allplace.model.TourApiResponseVO;
import com.project.trip.allplace.model.TourItemVO;

/**
 * Tour API 호출을 실제로 담당하는 구현체 (주방)
 */
@Service
public class TourApiServiceImpl implements TourApiService {

    // 1. root-context.xml에 등록한 RestTemplate 빈(Bean)을 주입받습니다.
    @Autowired
    private RestTemplate restTemplate;

    // ※주의※: 이 서비스 키는 GitHub 등에 절대 올리면 안 됩니다!
    // (보안을 위해 나중에 .properties 파일로 분리하는 것을 강력히 권장합니다)
    // data.go.kr에서 받은 '디코딩된' 원본 서비스 키를 사용하세요.
    private final String serviceKey = "YOUR_SERVICE_KEY"; // ◀◀◀ [중요] 여기에 본인 키 입력

    /**
     * API 엔드포인트: 공통정보조회 (detailCommon)
     * (좌표, 주소, 제목, 대표 이미지 등을 한 번에 가져옴)
     */
    private final String DETAIL_COMMON_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon";

    @Override
    public TourItemVO getPlaceDetail(String contentId) {
        
        // 2. Spring의 UriComponentsBuilder를 사용해 URL을 안전하게 조립합니다.
        // (자동으로 파라미터 인코딩 처리를 해줍니다)
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DETAIL_COMMON_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("MobileApp", "TripBear")     // [필수] 앱 이름
                .queryParam("MobileOS", "ETC")           // [필수] OS 구분
                .queryParam("contentId", contentId)      // [핵심] 조회할 장소 ID
                .queryParam("defaultYN", "Y")            // 기본 정보 조회
                .queryParam("addrinfoYN", "Y")           // 주소 정보 조회
                .queryParam("mapinfoYN", "Y")            // 좌표 정보 조회
                .queryParam("firstImageYN", "Y")         // 대표 이미지 조회
                .queryParam("_type", "json")             // [필수] 응답 형식 JSON
                .build(true) // [중요] serviceKey가 2중 인코딩되는 것을 방지
                .toUri();

        try {
            // 3. RestTemplate으로 API(URI)를 호출하고,
            //    응답(JSON)을 TourApiResponseVO 객체로 자동 변환(파싱)합니다.
            TourApiResponseVO response = restTemplate.getForObject(uri, TourApiResponseVO.class);

            // 4. 중첩된 DTO에서 실제 데이터(TourItemVO)만 추출하여 반환합니다.
            //    (response -> body -> items -> item)
            if (response != null && response.getResponse().getBody() != null &&
                response.getResponse().getBody().getItems() != null) {
                
                return response.getResponse().getBody().getItems().getItem();
            }
            return null; // API 결과가 비어있을 경우

        } catch (Exception e) {
            // API 호출 실패 시 (네트워크 오류, 404, 500, JSON 파싱 실패 등)
            System.err.println("[TourApiServiceImpl] API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null; // 실패 시 null 반환
        }
    }
}
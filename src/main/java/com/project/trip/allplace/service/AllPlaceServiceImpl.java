package com.project.trip.allplace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.trip.allplace.mapper.PlaceMapper;
import com.project.trip.allplace.model.PlaceDTO;
import com.project.trip.allplace.model.TourItemVO;

@Service
public class AllPlaceServiceImpl implements AllPlaceService {

    // 1. DB에 접근하기 위한 매퍼 주입
    @Autowired
    private PlaceMapper placeMapper;

    // 2. API를 호출하기 위한 서비스 주입 (방금 만든 것!)
    @Autowired
    private TourApiService tourApiService;


    /**
     * [핵심 On-Demand 로직]
     * '루트 담기' 시 Controller에서 이 메소드를 호출합니다.
     */
    @Transactional // (DB 저장 중 오류가 나면 롤백되도록 트랜잭션 처리)
    @Override
    public PlaceDTO addPlaceOnDemand(String contentId) {
        
        // 1. 우리 DB(tblPlace)에 이 contentId가 이미 있는지 확인
        PlaceDTO place = placeMapper.findPlaceByContentId(contentId);

        // 2. IF (DB에 없음) -> API 호출 후 DB에 저장
        if (place == null) {
            System.out.println("[OnDemand] DB에 없음. TourAPI에서 상세정보를 가져옵니다...");
            
            // 3. TourApiService를 사용해 API 호출
            TourItemVO item = tourApiService.getPlaceDetail(contentId);

            if (item != null && item.getContentId() != null) {
                // 4. API 결과(TourItemVO)를 우리 DB DTO(PlaceDTO)로 변환
                PlaceDTO newPlace = new PlaceDTO();
                newPlace.setApiContentId(item.getContentId());
                newPlace.setName(item.getTitle());
                newPlace.setAddress(item.getAddress());
                
                // [중요] API가 주는 값은 String이므로, DB 타입에 맞게 변환합니다.
                try {
                    newPlace.setLatitude(Double.parseDouble(item.getLatitude()));
                    newPlace.setLongitude(Double.parseDouble(item.getLongitude()));
                } catch (NumberFormatException e) {
                    System.err.println("[OnDemand] 좌표 변환 실패 (contentId: " + contentId + ")");
                    // 좌표가 없는 경우 0.0 또는 기본값 처리
                    newPlace.setLatitude(0.0);
                    newPlace.setLongitude(0.0);
                }
                
                // 5. 우리 DB(tblPlace)에 INSERT
                placeMapper.insertPlace(newPlace);
                
                // 6. 방금 INSERT한 DTO를 반환 (PK 등이 포함됨. Mapper.xml 설정 필요)
                return newPlace;
            } else {
                // API 호출 실패 또는 결과가 없음
                System.err.println("[OnDemand] API 호출 실패 (contentId: " + contentId + ")");
                return null; 
            }
        }
        
        // 2. ELSE (DB에 이미 있음) -> 기존 정보 반환
        System.out.println("[OnDemand] DB에 이미 존재함. 기존 정보를 반환합니다.");
        return place;
    }
}
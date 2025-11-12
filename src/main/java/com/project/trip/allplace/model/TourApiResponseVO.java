package com.project.trip.allplace.model;

import java.io.IOException; // [import 추가]
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty; // [import 추가]
import com.fasterxml.jackson.core.JsonParser; // [import 추가]
import com.fasterxml.jackson.core.JsonProcessingException; // [import 추가]
import com.fasterxml.jackson.core.JsonToken; // [import 추가]
import com.fasterxml.jackson.databind.DeserializationContext; // [import 추가]
import com.fasterxml.jackson.databind.JsonDeserializer; // [import 추가]
import com.fasterxml.jackson.databind.JsonNode; // [import 추가]
import com.fasterxml.jackson.databind.ObjectMapper; // [import 추가]
import com.fasterxml.jackson.databind.annotation.JsonDeserialize; // [import 추가]

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiResponseVO {

    private Response response;
    
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Body body;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        
        // --- [수정] ---
        // API가 0건일 때 items: "" (빈 문자열)을 반환하는 문제를 해결하기 위해
        // JsonNode를 사용한 커스텀 Setter를 추가합니다.
        
        private Items items;
        
        @JsonProperty("totalCount")
        private Integer totalCount;   // 총 건수 (없을 수도 있어 Integer 권장)

        @JsonProperty("pageNo")
        private Integer pageNo;       // 현재 페이지

        @JsonProperty("numOfRows")
        private Integer numOfRows;    // 페이지당 건수

        @JsonProperty("items")
        public void setItems(JsonNode node) {
            ObjectMapper mapper = new ObjectMapper();
            
            if (node.isTextual() && node.asText().isEmpty()) {
                // 1. API가 "" (빈 문자열)을 반환하면 -> null로 설정
                this.items = null;
            } else if (node.isObject()) {
                // 2. API가 { ... } (정상 객체)를 반환하면 -> Items.class로 변환
                try {
                    this.items = mapper.treeToValue(node, Items.class);
                } catch (JsonProcessingException e) {
                    this.items = null;
                }
            } else {
                // 3. 그 외 (null 등)
                this.items = null;
            }
        }
        // --- [수정 끝] ---
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        
        // (TourApiResponseVO의 List<TourItemVO> item; 선언을 참고하여 수정)
        // API가 item: [ ... ] (배열)을 반환할 수도 있고, 
        // item: { ... } (단일 객체)를 반환할 수도 있는 경우,
        // List로 받되, 단일 객체도 허용하도록 설정합니다.
        @JsonDeserialize(using = ItemListDeserializer.class)
        private List<TourItemVO> item;
    }
    
    
    // --- [신규 추가] ---
    // TourAPI는 결과가 1건이면 "item": { ... } (객체)
    // 2건 이상이면 "item": [ ... ] (배열)을 반환합니다.
    // 이 둘을 모두 List<TourItemVO>로 변환하는 Deserializer입니다.
    public static class ItemListDeserializer extends JsonDeserializer<List<TourItemVO>> {
        
        private ObjectMapper mapper = new ObjectMapper();

        @Override
        public List<TourItemVO> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            
            // 1. [ ... ] (배열)로 오는 경우
            if (p.currentToken() == JsonToken.START_ARRAY) {
                // Jackson 기본 List<TourItemVO> 변환 기능을 사용
                return p.getCodec().readValue(p, ctxt.getTypeFactory().constructCollectionType(List.class, TourItemVO.class));
            }
            
            // 2. { ... } (단일 객체)로 오는 경우
            if (p.currentToken() == JsonToken.START_OBJECT) {
                // 객체를 1개 읽어서 List에 담아 반환
                TourItemVO item = p.getCodec().readValue(p, TourItemVO.class);
                return List.of(item);
            }
            
            // 3. 그 외 (null, "", 등)
            return null;
        }
    }
    // --- [여기까지] ---
}
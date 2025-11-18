package com.project.trip.allplace.model;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 한국관광공사 Tour API의 응답을 매핑하기 위한 최상위 값 객체(Value Object)입니다.
 * API 응답의 중첩된 JSON 구조(response > body > items > item)를 표현합니다.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourApiResponseVO {

    /**
     * API 응답의 'response' 필드를 나타냅니다.
     */
    private Response response;

    /**
     * 'response' 객체 내부를 표현합니다.
     */
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        /**
         * 'response' 객체 내부의 'body' 필드를 나타냅니다.
         */
        private Body body;
    }

    /**
     * 'body' 객체 내부를 표현하며, 실제 데이터 목록(items)과 페이지네이션 정보를 포함합니다.
     */
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {

        private Items items;

        // ⭐ 추가한 setter (JSON 파싱용 setter와는 별도)
        public void setItems(Items items) {
            this.items = items;
        }

        /**
         * 전체 결과 수
         */
        @JsonProperty("totalCount")
        private Integer totalCount;

        /**
         * 현재 페이지 번호
         */
        @JsonProperty("pageNo")
        private Integer pageNo;

        /**
         * 한 페이지당 결과 수
         */
        @JsonProperty("numOfRows")
        private Integer numOfRows;

        /**
         * Tour API에서 'items' 필드가 비어있는 문자열("")로 오는 비정상적인 경우를
         * Jackson 파싱 시 오류 없이 처리하기 위한 커스텀 setter입니다.
         *
         * @param node 'items' 필드에 해당하는 JSON 노드
         */
        @JsonProperty("items")
        public void setItems(JsonNode node) {
            ObjectMapper mapper = new ObjectMapper();

            if (node == null || (node.isTextual() && node.asText().isEmpty())) {
                this.items = null;
                return;
            }
            if (node.isObject()) {
                try {
                    this.items = mapper.treeToValue(node, Items.class);
                } catch (JsonProcessingException e) {
                    this.items = null;
                }
                return;
            }
            this.items = null;
        }
    }

    /**
     * 'items' 객체 내부를 표현하며, 실제 장소 정보인 'item' 목록을 포함합니다.
     */
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        /**
         * Tour API에서 'item' 필드는 결과가 1개일 때 객체로, 2개 이상일 때 배열로 반환됩니다.
         * 이러한 가변적인 구조를 처리하기 위해 커스텀 Deserializer를 사용합니다.
         */
        @JsonDeserialize(using = ItemListDeserializer.class)
        private List<TourItemVO> item;
    }

    /**
     * Tour API의 'item' 필드가 단일 객체 또는 객체 배열로 오는 경우를 모두 처리하여
     * 항상 {@code List<TourItemVO>} 형태로 변환하는 커스텀 Deserializer입니다.
     */
    public static class ItemListDeserializer extends JsonDeserializer<List<TourItemVO>> {
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public List<TourItemVO> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            // 토큰이 배열 시작이면, 배열 전체를 List<TourItemVO>로 파싱
            if (p.currentToken() == JsonToken.START_ARRAY) {
                return p.getCodec().readValue(p,
                        ctxt.getTypeFactory().constructCollectionType(List.class, TourItemVO.class));
            }
            // 토큰이 객체 시작이면, 단일 객체를 TourItemVO로 파싱하여 리스트에 담아 반환
            if (p.currentToken() == JsonToken.START_OBJECT) {
                TourItemVO item = p.getCodec().readValue(p, TourItemVO.class);
                return List.of(item);
            }
            return null;
        }
    }
}

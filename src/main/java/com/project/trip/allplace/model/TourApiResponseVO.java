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

        private Items items;

        // ⭐ 추가한 setter (JSON 파싱용 setter와는 별도)
        public void setItems(Items items) {
            this.items = items;
        }

        // 페이지네이션 메타
        @JsonProperty("totalCount")
        private Integer totalCount;

        @JsonProperty("pageNo")
        private Integer pageNo;

        @JsonProperty("numOfRows")
        private Integer numOfRows;

        // JSON에서 items가 "" 로 올 때 방어 처리
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

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Items {
        @JsonDeserialize(using = ItemListDeserializer.class)
        private List<TourItemVO> item;
    }

    public static class ItemListDeserializer extends JsonDeserializer<List<TourItemVO>> {
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public List<TourItemVO> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            if (p.currentToken() == JsonToken.START_ARRAY) {
                return p.getCodec().readValue(p,
                        ctxt.getTypeFactory().constructCollectionType(List.class, TourItemVO.class));
            }
            if (p.currentToken() == JsonToken.START_OBJECT) {
                TourItemVO item = p.getCodec().readValue(p, TourItemVO.class);
                return List.of(item);
            }
            return null;
        }
    }
}

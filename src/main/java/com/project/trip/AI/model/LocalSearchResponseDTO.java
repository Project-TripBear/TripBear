package com.project.trip.AI.model;

/**
 * 지역 검색 결과로 반환되는 장소의 정보를 표현하고 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * <p>
 * 장소의 위도(latitude), 경도(longitude), 이름을 포함합니다.
 * </p>
 */
public class LocalSearchResponseDTO {

    /**
     * 장소의 위도 (latitude)
     */
    private double lat;
    /**
     * 장소의 경도 (longitude)
     */
    private double lng;
    /**
     * 장소의 이름
     */
    private String name;

    /**
     * 위도, 경도, 이름을 받아 {@code LocalSearchResponseDTO} 객체를 생성하는 생성자입니다.
     * @param lat 장소의 위도
     * @param lng 장소의 경도
     * @param name 장소의 이름
     */
    public LocalSearchResponseDTO(double lat, double lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    /**
     * 장소의 위도를 반환합니다.
     * @return 위도
     */
    public double getLat() { return lat; }
    /**
     * 장소의 경도를 반환합니다.
     * @return 경도
     */
    public double getLng() { return lng; }
    /**
     * 장소의 이름을 반환합니다.
     * @return 이름
     */
    public String getName() { return name; }

    /**
     * 장소의 위도를 설정합니다.
     * @param lat 설정할 위도
     */
    public void setLat(double lat) { this.lat = lat; }
    /**
     * 장소의 경도를 설정합니다.
     * @param lng 설정할 경도
     */
    public void setLng(double lng) { this.lng = lng; }
    /**
     * 장소의 이름을 설정합니다.
     * @param name 설정할 이름
     */
    public void setName(String name) { this.name = name; }
}

// 파일 경로: com.project.trip.admin.board.model.PagingDTO.java

package com.project.trip.admin.board.model;

import lombok.Getter;
import lombok.ToString;

/**
 * 페이징 처리에 필요한 모든 정보를 계산하고 관리하는 데이터 전송 객체(DTO)입니다.
 * <p>
 * 생성자에서 현재 페이지, 전체 아이템 수, 페이지당 아이템 수, 페이지 블록 크기를 받아
 * 페이징 UI를 구현하는 데 필요한 모든 값(총 페이지, 시작/끝 행, 시작/끝 페이지 등)을 계산합니다.
 * </p>
 */
@Getter
@ToString
public class PagingDTO {

    private int page;           // 현재 페이지 번호
    private int totalCount;     // 전체 아이템 수
    private int pageSize;       // 한 페이지에 보여줄 아이템 수
    private int pageBlock;      // 한 화면에 보여줄 페이지 번호 개수

    private int totalPage;      // 전체 페이지 수
    private int startRow;       // DB 조회용 시작 행 번호
    private int endRow;         // DB 조회용 끝 행 번호
    
    private int startPage;      // 페이지 블록의 시작 페이지 번호
    private int endPage;        // 페이지 블록의 끝 페이지 번호

    private boolean prev;       // '이전' 버튼 표시 여부
    private boolean next;       // '다음' 버튼 표시 여부

    /**
     * 페이징 정보를 계산하는 생성자입니다.
     * @param page 현재 페이지 번호
     * @param totalCount 전체 아이템 수
     * @param pageSize 한 페이지에 보여줄 아이템 수
     * @param pageBlock 한 화면에 보여줄 페이지 번호 개수
     */
    public PagingDTO(int page, int totalCount, int pageSize, int pageBlock) {
        
        // ★★★ 1. [수정] page가 0 또는 음수일 경우 1로 강제 보정 ★★★
        if (page <= 0) {
            this.page = 1;
        } else {
            this.page = page;
        }
        
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.pageBlock = pageBlock;

        // 2. 전체 페이지 수 계산
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
        
        // ★★★ 3. [추가] totalCount가 0일 때 totalPage가 1이 되도록 보정 ★★★
        if (this.totalPage == 0) {
            this.totalPage = 1;
        }

        // 4. DB 조회용 startRow, endRow 계산
        this.startRow = (this.page - 1) * pageSize + 1;
        this.endRow = this.page * pageSize;

        // 5. 페이지 블록 startPage, endPage 계산
        this.startPage = (int) ((Math.ceil((double) this.page / pageBlock)) - 1) * pageBlock + 1;
        this.endPage = this.startPage + pageBlock - 1;

        // 6. endPage 보정 (전체 페이지 수보다 클 수 없음)
        if (this.endPage > this.totalPage) {
            this.endPage = this.totalPage;
        }

        // 7. '이전' 버튼 (startPage가 1이 아닐 때)
        this.prev = this.startPage > 1;

        // 8. '다음' 버튼 (endPage가 totalPage보다 작을 때)
        this.next = this.endPage < this.totalPage;
    }
}
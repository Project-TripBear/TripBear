// 파일 경로: com.project.trip.admin.board.model.PagingDTO.java

package com.project.trip.admin.board.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PagingDTO {

    private int page;
    private int totalCount;
    private int pageSize;
    private int pageBlock;

    private int totalPage;
    private int startRow;
    private int endRow;
    
    private int startPage;
    private int endPage;

    private boolean prev;
    private boolean next;

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
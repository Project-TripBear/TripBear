package com.project.trip.board.hotdeal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.trip.board.hotdeal.mapper.HotDealLikeMapper;
import com.project.trip.board.hotdeal.mapper.HotDealMapper;
import com.project.trip.board.hotdeal.model.HotDealCommentDTO;
import com.project.trip.mypage.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class HotdealCommentController {
	
	 private final HotDealMapper mapper;
	 private final MemberMapper membermapper;
	 private final HotDealLikeMapper likemapper;
	 
	   
    @PostMapping("/hotdeal/addcomment")
    public ResponseEntity<Map<String, Object>> addComment(@RequestBody HotDealCommentDTO dto, Authentication auth) {
        String userId = auth.getName();
        dto.setId(userId);
        int result = mapper.addcomment(dto);
        HotDealCommentDTO comment = mapper.getComment(dto.getSeq());
        
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        response.put("dto", comment);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/hotdeal/editcomment")
    public ResponseEntity<Integer> editComment(@RequestBody HotDealCommentDTO dto, Authentication auth) {
        // 사용자 인증 및 dto.id 일치 체크 생략 가능
        
        int result = mapper.editComment(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/hotdeal/delcomment")
    public ResponseEntity<Integer> deleteComment(@RequestParam String seq, Authentication auth) {
        String userId = auth.getName();
        int result = mapper.delComment(seq, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/hotdeal/listcomment")
    public ResponseEntity<List<HotDealCommentDTO>> listComment(
        @RequestParam String bseq,
        @RequestParam(defaultValue = "1") int begin
    ) {
        List<HotDealCommentDTO> list = mapper.moreComment(bseq, begin);
        return ResponseEntity.ok(list);
    }
}

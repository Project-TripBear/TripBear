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
import com.project.trip.mypage.model.UserDTO;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class HotDealCommentController {
	
	 private final HotDealMapper mapper;
	 private final MemberMapper membermapper;
	 private final HotDealLikeMapper likemapper;
	 
	   
    @PostMapping("/hotdeal/addcomment")
    public ResponseEntity<Map<String, Object>> addComment(@RequestBody HotDealCommentDTO dto, Authentication auth) {
        String userId = auth.getName();
        dto.setId(userId);
        UserDTO userDto = membermapper.get(userId);
        dto.setUseq(userDto.getSeq()); 
        int result = mapper.addComment(dto);
        System.out.println("dto"+dto.getSeq());
        HotDealCommentDTO comment = mapper.getComment(dto.getSeq());
        
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        response.put("dto", comment);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/hotdeal/editcomment")
 // JSON 전체를 Map으로 받습니다.
 public ResponseEntity<Integer> editComment(@RequestBody Map<String, Object> params, Authentication auth) {
     
     // 1. Map에서 댓글 고유 번호(seq)와 내용(content) 추출
     Long seq = null;
     String content = (String) params.get("content");
     System.out.println("### 추출된 seq 값: " + seq); // <--- 이 부분이 null인지 확인

     // ... DTO에 값 설정 로직
     
     
     // JSON의 'seq' 값을 Long 타입으로 안전하게 변환
     Object seqObject = params.get("seq");
     if (seqObject instanceof Number) {
         seq = ((Number) seqObject).longValue();
     } else if (seqObject != null) {
         // 문자열로 전송된 경우를 대비하여 Long으로 파싱 시도
         try {
             seq = Long.parseLong(seqObject.toString());
         } catch (NumberFormatException e) {
             // seq가 숫자가 아닌 경우의 에러 처리 (필요시)
             System.err.println("Error: seq 값이 유효한 숫자가 아닙니다.");
         }
     }
     
     
     
     // 2. DTO 객체 생성 및 값 설정
     HotDealCommentDTO dto = new HotDealCommentDTO();
     // 추출한 seq 값을 DTO에 설정
     dto.setSeq(seq);        
     // 추출한 content 값을 DTO에 설정
     dto.setContent(content); 

     // 3. 기존 로직 유지 (사용자 정보 설정)
     String userId = auth.getName();
     UserDTO userDto = membermapper.get(userId);
     System.out.println("### DTO의 seq 값: " + dto.getSeq());

     
     dto.setUseq(userDto.getSeq());
     System.out.println("수정 DTO: " + dto);
     // 이 시점에서 System.out.println("DTO seq: " + dto.getSeq()); 를 통해 값이 들어왔는지 확인 가능

     // 4. 매퍼 실행
     int result = mapper.editComment(dto);
     
     return ResponseEntity.ok(result);
 }

    @PostMapping("/hotdeal/delcomment")
    public ResponseEntity<Integer> deleteComment(@RequestParam String seq, Authentication auth) {
        String id = auth.getName();
        UserDTO userDto = membermapper.get(id);
        int result = mapper.delComment(seq, userDto.getSeq());
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/hotdeal/morecomment")
    public ResponseEntity<List<HotDealCommentDTO>> listComment(
        @RequestParam String bseq,
        @RequestParam(defaultValue = "1") int begin
    ) {
    	 System.out.println("Received bseq: " + bseq);
    	    System.out.println("Received begin: " + begin);

    	Map<String, Object> params = new HashMap<>();
    	params.put("bseq", bseq);
    	params.put("begin", begin);
       

        List<HotDealCommentDTO> list = mapper.moreComment(params);
        return ResponseEntity.ok(list);
    }
    
    
}

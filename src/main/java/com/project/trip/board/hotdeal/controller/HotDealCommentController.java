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
import org.springframework.web.bind.annotation.ResponseBody;
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
        UserDTO userDto = membermapper.get(userId);
        dto.setId(userId);
        dto.setUseq(userDto.getSeq()); 
        System.out.println("dto(add):"+dto);

        int result = mapper.addComment(dto);
        System.out.println("dto(get):"+dto);
        HotDealCommentDTO comment = mapper.getComment(dto.getSeq());
        
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        response.put("dto", comment);

        return ResponseEntity.ok(response);
    }

@PostMapping("/hotdeal/editcomment")
    @ResponseBody
    public Map<String, String> editComment(@RequestBody HotDealCommentDTO dto, Authentication auth) {
        Map<String, String> response = new HashMap<>();
        
        String userId = auth.getName();
        UserDTO userDto = membermapper.get(userId);

        System.out.println("Received DTO seq: " + dto.getSeq()); // Debug log added here

        if (userDto == null) {
            response.put("result", "0");
            response.put("message", "로그인이 필요하거나 유효하지 않은 사용자입니다.");
            return response;
        }

        dto.setUseq(userDto.getSeq());

        System.out.println("수정 DTO: " + dto);

        int result = mapper.editComment(dto);
        
        response.put("result", String.valueOf(result));
        
        return response;
    }

    @PostMapping("/hotdeal/delcomment")
    @ResponseBody
    public Map<String, String> deleteComment(@RequestBody Map<String, Object> payload, Authentication auth) {
        Map<String, String> response = new HashMap<>();
        
        String id = auth.getName();
        UserDTO userDto = membermapper.get(id);

        if (userDto == null) {
            response.put("result", "0");
            response.put("message", "로그인이 필요하거나 유효하지 않은 사용자입니다.");
            return response;
        }

        String seq = payload.get("seq").toString();
        int result = mapper.delComment(seq, userDto.getSeq());
        
        
        response.put("result", String.valueOf(result));
        
        return response;
    }

//    @GetMapping("/hotdeal/morecomment")
//    public ResponseEntity<List<HotDealCommentDTO>> listComment(
//        @RequestParam String bseq,
//        @RequestParam(defaultValue = "1") int begin
//    ) {
//    	 System.out.println("Received bseq: " + bseq);
//    	    System.out.println("Received begin: " + begin);
//
//    	Map<String, Object> params = new HashMap<>();
//    	params.put("bseq", bseq);
//    	params.put("begin", begin);
//       
//
//        List<HotDealCommentDTO> list = mapper.moreComment(params);
//        return ResponseEntity.ok(list);
//    }
    
    
    @GetMapping("/hotdeal/morecomment")
    @ResponseBody
    public List<HotDealCommentDTO> moreComment(
            @RequestParam("bseq") String bseq,
            @RequestParam("begin") int begin) {

        Map<String, Object> params = new HashMap<>();
        params.put("bseq", bseq);
        params.put("begin", begin);

        return mapper.moreComment(params);
    }
    
}

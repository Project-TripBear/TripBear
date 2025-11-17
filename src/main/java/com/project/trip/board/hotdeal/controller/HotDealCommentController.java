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
	 
	   
    /**
     * 핫딜 게시글에 댓글을 추가하는 REST API입니다.
     * 로그인한 사용자의 정보를 기반으로 댓글을 등록하고, 등록된 댓글 정보와 함께 응답을 반환합니다.
     *
     * @param dto  등록할 댓글 정보를 담은 {@link HotDealCommentDTO} 객체
     * @param auth Spring Security의 Authentication 객체
     * @return 처리 결과(result)와 등록된 댓글 정보(dto)를 담은 {@code ResponseEntity<Map<String, Object>>}
     */
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

//    @PostMapping("/hotdeal/editcomment")
//    @ResponseBody
//    public Map<String, String> editComment(@RequestBody HotDealCommentDTO dto, Authentication auth) {
//        Map<String, String> response = new HashMap<>();
//        
//        String userId = auth.getName();
//        UserDTO userDto = membermapper.get(userId);
//        
//        System.out.println("Received DTO seq: " + dto.getSeq()); // Debug log added here
//
//        if (userDto == null) {
//            response.put("result", "0");
//            response.put("message", "로그인이 필요하거나 유효하지 않은 사용자입니다.");
//            return response;
//        }
//
//        dto.setUseq(userDto.getSeq());
//
//        
//        // 추가 디버그 로그
//        System.out.println("userDto.getSeq(): " + userDto.getSeq());
//        System.out.println("userDto.getSeq() type: " + (userDto.getSeq() != null ? userDto.getSeq().getClass().getName() : "null"));
//        
//        System.out.println("수정 DTO: " + dto);
//        System.out.println("dto.getUseq(): " + dto.getUseq());
//
//        int result = mapper.editComment(dto);
//        
//        response.put("result", String.valueOf(result));
//        
//        return response;
//    }
    
    /**
     * 핫딜 게시글의 댓글을 수정하는 REST API입니다.
     * 로그인한 사용자의 정보를 확인하고, 댓글 수정 권한이 있는 경우 댓글을 업데이트합니다.
     *
     * @param dto  수정할 댓글 정보를 담은 {@link HotDealCommentDTO} 객체
     * @param auth Spring Security의 Authentication 객체
     * @return 처리 결과(result)와 메시지(message)를 담은 {@code Map<String, String>}
     */
    @PostMapping("/hotdeal/editcomment")
    @ResponseBody
    public Map<String, String> editComment(@RequestBody HotDealCommentDTO dto, Authentication auth) {
        Map<String, String> response = new HashMap<>();
        
        try {
            // 1. 사용자 인증 정보 확인
            String userId = auth.getName();
            UserDTO userDto = membermapper.get(userId);
            
            System.out.println("Received DTO seq: " + dto.getSeq()); // 디버그 로그

            if (userDto == null) {
                response.put("result", "0");
                response.put("message", "로그인이 필요하거나 유효하지 않은 사용자입니다.");
                return response;
            }

            // 2. DTO에 사용자 고유 번호(useq) 설정
            dto.setUseq(userDto.getSeq());

            // 3. 디버그 로그 (요청 DTO 확인)
            System.out.println("userDto.getSeq(): " + userDto.getSeq());
            System.out.println("수정 DTO (mapper로 전달): " + dto);
            System.out.println("dto.getUseq(): " + dto.getUseq());

            // 4. 데이터베이스 수정 시도 (이곳에서 예외 발생 가능성 있음)
            int result = mapper.editComment(dto);
            
            // 5. 성공 응답 구성
            response.put("result", String.valueOf(result)); // "1" (성공) 또는 "0" (실패)

        } catch (Exception e) {
            // 6. 예외 발생 시 (catch 블록 실행)
            System.err.println("===== [ERROR] 댓글 수정 중 예외 발생 =====");
            e.printStackTrace(); // 👈 콘솔에 실제 오류 내용(NullPointerException, SQL Error 등)이 찍힙니다.
            System.err.println("=====================================");

            // 7. 실패 응답 구성 (AJAX가 받을 JSON)
            response.put("result", "0"); 
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }
        
        // 8. (성공하든 예외가 터지든) 무조건 Map(JSON)을 반환
        return response;
    }

    /**
     * 핫딜 게시글의 댓글을 삭제하는 REST API입니다.
     * 로그인한 사용자의 정보를 확인하고, 댓글 삭제 권한이 있는 경우 댓글을 삭제합니다.
     *
     * @param payload 삭제할 댓글의 ID("seq")를 포함하는 {@code Map<String, Object>}
     * @param auth    Spring Security의 Authentication 객체
     * @return 처리 결과(result)와 메시지(message)를 담은 {@code Map<String, String>}
     */
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
    
    
    /**
     * 특정 핫딜 게시글의 추가 댓글 목록을 조회하는 REST API입니다.
     * 페이지네이션을 위해 `begin` 파라미터를 사용합니다.
     *
     * @param bseq  댓글을 조회할 핫딜 게시글의 고유 번호
     * @param begin 조회를 시작할 댓글의 인덱스
     * @return 조회된 댓글 {@link HotDealCommentDTO}의 리스트
     */
    @GetMapping("/hotdeal/morecomment")
    @ResponseBody
    public List<HotDealCommentDTO> moreComment(
            @RequestParam("bseq") String bseq,
            @RequestParam("begin") int begin) {

        Map<String, Object> params = new HashMap<>();
        params.put("bseq", bseq);
        params.put("begin", begin);

        System.out.println("list: " + mapper.moreComment(params));

        return mapper.moreComment(params);
    }
    
}

package com.project.trip.AI.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.trip.AI.service.AiRouteSaveService;
import com.project.trip.mypage.model.CustomUser;
import com.project.trip.mypage.model.UserDTO;

@Controller
public class AiRouteSaveController {

    @Autowired
    private AiRouteSaveService aiRouteSaveService;

    /**
     * AI가 생성한 여행 경로를 현재 로그인한 사용자의 '내 여행'으로 저장합니다.
     * <p>
     * Spring Security 컨텍스트에서 현재 사용자 정보를 가져와,
     * AI 경로를 사용자의 경로로 복사하는 서비스를 호출합니다.
     *
     * @param aiRouteId Long 저장할 AI 여행 경로의 ID
     * @return Map&lt;String, Object&gt; 작업의 성공 여부(success),
     *         성공 시 새로 생성된 사용자 경로 ID(newUserRouteId),
     *         실패 시 오류 메시지(message)를 담은 맵
     */
    @PostMapping("/ai/saveUserRoute")
    @ResponseBody
    public Map<String, Object> saveUserRoute(@RequestParam("aiRouteId") Long aiRouteId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // ✅ 현재 로그인한 사용자 정보 가져오기 (Spring Security 세션)
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !(auth.getPrincipal() instanceof CustomUser)) {
                result.put("success", false);
                result.put("message", "로그인 정보가 없습니다.");
                return result;
            }

            CustomUser loginUser = (CustomUser) auth.getPrincipal();
            UserDTO user = loginUser.getUdto();

            //  UserDTO.seq → Long 변환 (DB는 NUMBER)
            Long userId = Long.parseLong(user.getSeq());

            System.out.println("✅ 로그인 사용자 ID: " + userId);

            //  AI 루트 복사 수행
            Long newUserRouteId = aiRouteSaveService.saveUserRouteFromAi(aiRouteId, userId);

            result.put("success", true);
            result.put("newUserRouteId", newUserRouteId);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
        }

        return result;
    }
}

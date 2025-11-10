package com.project.trip.mypage.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.UserDTO;
import com.project.trip.mypage.service.MailSender;

import lombok.RequiredArgsConstructor; 

@Controller
@RequestMapping("/member/mail") // JSP의 AJAX 경로와 일치
@RequiredArgsConstructor
public class MailController {

    // MailSender 서비스를 자동으로 주입받습니다. (의존성 주입)
    @Autowired
    private final MailSender mailSender;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/sendmail")
	@ResponseBody
	public Map<String, Integer> sendVerificationMail(@RequestParam("email") String email, HttpSession session) {

		Random rnd = new Random();
		int validNumber = rnd.nextInt(90000) + 10000; 
		session.setAttribute("validNumber", validNumber);

		Map<String, Integer> response = new HashMap<>();
		
		try {
			Map<String, String> emailParams = new HashMap<>();
			emailParams.put("email", email);
			emailParams.put("validNumber", String.valueOf(validNumber));

			mailSender.sendVerificationMail(emailParams);

			response.put("result", 1); // 성공

		} catch (Exception e) {
			System.out.println("MailController.sendVerificationMail() Error");
			e.printStackTrace();
			response.put("result", 0); // 실패
		}
		
		return response;
	}

	/**
	 * [회원가입] 인증번호 확인
	 */
	@PostMapping("/validmail")
	@ResponseBody
	public Map<String, Integer> validateMail(@RequestParam("validNumber") String validNumber, HttpSession session) {
		
		Map<String, Integer> response = new HashMap<>();
		
		try {
			Integer correctValidNumber = (Integer) session.getAttribute("validNumber");

			if (correctValidNumber != null && validNumber.equals(String.valueOf(correctValidNumber))) {
				response.put("result", 1);
				session.removeAttribute("validNumber"); 
			} else {
				response.put("result", 0);
			}

		} catch (Exception e) {
			System.out.println("MailController.validateMail() Error");
			e.printStackTrace();
			response.put("result", 0);
		}
		
		return response;
	}

	/**
	 * [회원가입] 인증 시간 만료
	 */
	@PostMapping("/delmail")
	@ResponseBody
	public Map<String, Integer> deleteMail(HttpSession session) {
		
		Map<String, Integer> response = new HashMap<>();
		
		try {
			session.removeAttribute("validNumber");
			response.put("result", 1);
		} catch (Exception e) {
			System.out.println("MailController.deleteMail() Error");
			e.printStackTrace();
			response.put("result", 0);
		}
		
		return response;
	}


	/**
	 * [아이디 찾기]
	 */
	@PostMapping("/findid")
	@ResponseBody
	// (★) 2. 파라미터를 @RequestParam 2개 대신 UserDTO 1개로 받음
	public Map<String, Integer> findId(UserDTO dto) {
		
		Map<String, Integer> response = new HashMap<>();
		int result = 0; 
		
		try {
			// (★) 3. DTO 객체를 mapper로 그대로 전달
			String id = memberMapper.idSelect(dto);
			
			if (id != null && !id.isEmpty()) {
				// (★) 4. DTO에서 email 값을 꺼내서 사용
				mailSender.sendIdVerificationMail(dto.getEmail(), id);
				result = 1; 
			}
			
		} catch (Exception e) {
			System.out.println("MailController.findId() Error");
			e.printStackTrace(); 
		}
		
		response.put("result", result);
		return response;
	}
	
	@PostMapping("/findpw")
	@ResponseBody
	public Map<String, Integer> findPw(@RequestParam("id") String id,
									   @RequestParam("email") String email) {
		
		Map<String, Integer> response = new HashMap<>();
		int result = 0; // 0: 실패, 1: 성공
		
		try {
			// 1. DB에서 아이디와 이메일이 일치하는 사용자가 있는지 확인
			// (UserDTO를 재사용하여 파라미터 전달)
			UserDTO checkDto = new UserDTO();
			checkDto.setId(id);
			checkDto.setEmail(email);
			
			// (MemberMapper에 userCheckByIdAndEmail 쿼리가 필요합니다)
			int userCount = memberMapper.userCheckByIdAndEmail(checkDto);
			
			// 2. 일치하는 사용자가 있을 경우(1)에만
			if (userCount > 0) {
				
				// 3. 임시 비밀번호 생성 (6자리 숫자)
				Random rnd = new Random();
				int validNumber = rnd.nextInt(900000) + 100000;
				String strNumber = String.valueOf(validNumber);

				// 4. (★중요★) DB에 저장하기 전에 Spring Security로 암호화
				String encodedNewPw = passwordEncoder.encode(strNumber);

				// 5. DB에 암호화된 새 비밀번호로 업데이트
				UserDTO updateDto = new UserDTO();
				updateDto.setId(id);
				updateDto.setEmail(email);
				updateDto.setPw(encodedNewPw); // 암호화된 비밀번호
				
				// (MemberMapper에 PwUpdate 쿼리가 필요합니다)
				memberMapper.PwUpdate(updateDto);

				// 6. 사용자에게는 '암호화되지 않은' 원본 번호를 메일로 발송
				mailSender.sendPwVerificationMail(email, strNumber);
				result = 1; // 성공
			}
			
		} catch (Exception e) {
			System.out.println("MailController.findPw() Error");
			e.printStackTrace();
		}
		
		response.put("result", result);
		return response;
	}

}
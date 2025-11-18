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

/**
 * 회원가입, 아이디/비밀번호 찾기 등 메일 인증과 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * 인증번호 발송 및 확인, 아이디/비밀번호 찾기 메일 발송 기능을 제공합니다.
 */
@Controller
@RequestMapping("/member/mail") // JSP의 AJAX 경로와 일치
@RequiredArgsConstructor
public class MailController {

    // MailSender 서비스를 자동으로 주입받습니다. (의존성 주입)
    @Autowired
    private final MailSender mailSender;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;


    /**
     * 이메일 인증번호를 발송합니다.
     * 5자리 난수를 생성하여 세션에 저장하고, 해당 번호를 포함한 메일을 발송합니다.
     *
     * @param email 인증번호를 받을 이메일 주소
     * @param session HTTP 세션 객체
     * @return 처리 결과 (1: 성공, 0: 실패)를 담은 {@code Map<String, Integer>}
     */
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
	
	/**
	 * 비밀번호 찾기 요청을 처리합니다.
	 * 아이디와 이메일이 일치하는 사용자를 확인하고, 임시 비밀번호를 생성하여 메일로 발송합니다.
	 * 임시 비밀번호는 암호화되어 데이터베이스에 업데이트됩니다.
	 *
	 * @param id 사용자의 아이디
	 * @param email 사용자의 이메일 주소
	 * @return 처리 결과 (1: 성공, 0: 실패)를 담은 {@code Map<String, Integer>}
	 */
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
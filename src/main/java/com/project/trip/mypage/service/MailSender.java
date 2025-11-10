package com.project.trip.mypage.service;

import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

	@Value("${my.mail.username}")
	private String username; 

	@Value("${my.mail.password}")
	private String password;

	private Properties getMailProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}

	/**
	 * (★) 디버깅용 파라미터 제거
	 * @Value로 주입된 클래스 필드를 직접 사용합니다.
	 */
	private Session getMailSession() {
		Properties props = getMailProperties();
		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// (★) @Value로 주입된 필드를 사용
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	/**
	 * [1] 회원가입 인증번호 발송 (UserController에서 사용)
	 */
	public void sendVerificationMail(Map<String,String> map) throws Exception {
		
		// (★) 디버깅용 System.out.println 코드 제거
				
		// (★) 파라미터 없는 getMailSession() 호출로 변경
		Session session = getMailSession(); 
				
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.username)); 
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(map.get("email")));
			message.setSubject("프로젝트에서 발송한 인증 번호입니다.");
			
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder.append("<h2>인증 번호 발송</h2>");
			contentBuilder.append("<div style=\"border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;\">");
			contentBuilder.append("인증번호: <span style=\"font-weight: bold;\">");
			contentBuilder.append(map.get("validNumber"));
			contentBuilder.append("</span>");
			contentBuilder.append("</div>");
			contentBuilder.append("<div>위의 인증 번호를 확인하세요.</div>");
			
			message.setContent(contentBuilder.toString(), "text/html; charset=UTF-8");
			Transport.send(message);
			System.out.println("인증 이메일 전송 완료!!");
			
		} catch (Exception e) {
			System.out.println("MailSender.sendVerificationMail() Error");
			e.printStackTrace();
			throw e; 
		}
	}

	
	/**
	 * [2] 아이디 찾기 발송 (★ 다시 추가된 메서드 ★)
	 */
	public void sendIdVerificationMail(String email, String id) throws Exception {
		
		Session session = getMailSession(); // 공통 세션 사용
				
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("요청하신 아이디입니다.");
			
			// Java 14 이하 StringBuilder
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder.append("<h2>아이디 발송</h2>");
			contentBuilder.append("<div style=\"border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;\">");
			contentBuilder.append("회원님의 아이디는 <span style=\"font-weight: bold;\">");
			contentBuilder.append(id); // 아이디 삽입
			contentBuilder.append("</span> 입니다.");
			contentBuilder.append("</div>");
			
			message.setContent(contentBuilder.toString(), "text/html; charset=UTF-8");
			Transport.send(message);
			System.out.println("아이디 이메일 전송 완료!!");
			
		} catch (Exception e) {
			System.out.println("MailSender.sendIdVerificationMail() Error");
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * [3] 새 비밀번호(임시 비밀번호) 발송 (원본 메서드 추가)
	 */
	public void sendPwVerificationMail(String email, String validNumber) throws Exception {
		
		Session session = getMailSession(); // (★) 파라미터 없는 getMailSession() 호출
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("새 비밀번호");
			
			// Java 14 이하 StringBuilder
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder.append("<h2>새 비밀번호 발송</h2>");
			contentBuilder.append("<div style=\"border: 1px solid #CCC; width: 300px; height: 120px; border-radius: 5px; background-color: #EEE; display: flex; justify-content: center; align-items: center; margin: 20px 0;\">");
			contentBuilder.append("회원님의 새 비밀번호 <span style=\"font-weight: bold;\">");
			contentBuilder.append(validNumber); // 새 비밀번호 삽입
			contentBuilder.append("</span> 입니다.");
			contentBuilder.append("</div>");
									
			message.setContent(contentBuilder.toString(), "text/html; charset=UTF-8");
			Transport.send(message);
			System.out.println("새 비밀번호 이메일 전송 완료!!");
			
		} catch (Exception e) {
			System.out.println("MailSender.sendPwVerificationMail() Error");
			e.printStackTrace();
			throw e;
		}
	}
			
}
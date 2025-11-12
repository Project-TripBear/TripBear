import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 🚨 여기에 형님이 사용할 실제 비밀번호를 입력하세요 (예: "admin1234")
        String rawPassword = "1111"; 

        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("--- 암호화된 비밀번호 (이걸 복사해서 DB에 넣으세요) ---");
        System.out.println(encodedPassword);
        System.out.println("----------------------------------------------");
    }
}	
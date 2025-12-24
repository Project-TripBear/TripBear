# TripBear

TripBear는 여행 정보를 탐색하고 공유하며 예약까지 이어지는 통합 여행 플랫폼입니다. 여행지/날씨 정보, 커뮤니티 게시판, AI 기반 여행 루트 추천, 예약 및 마이페이지, 운영자(Admin) 관리 기능을 제공하는 Spring MVC 기반 웹 애플리케이션입니다.

## 주요 기능
- **여행지 탐색**: 지도/검색, 트렌드/축제/뉴스, 날씨 정보 제공 (`allplace`, `weather` 모듈)
- **커뮤니티**: 루트 공유, 핫딜, 리뷰, Q&A, 분실물/찾기, 공지 게시판 (`board` 모듈)
- **AI 여행 루트 추천**: 여행 계획/경로 생성 및 결과 뷰 제공 (`AI` 모듈)
- **예약 및 마이페이지**: 예약 관리, 사용자 개인 기능 (`reservation`, `mypage` 모듈)
- **관리자 기능**: 사용자/신고/차량/숙소/게시판 관리 (`admin` 모듈)
- **보안 및 인증**: Spring Security 기반 로그인/권한 관리 (`auth` 모듈)

## 기술 스택
- **Backend**: Java 11, Spring MVC 5, Spring Security
- **DB/ORM**: Oracle DB, MyBatis, HikariCP
- **View**: JSP, Apache Tiles
- **기타**: WebSocket, Swagger, Log4j, Lombok
- **빌드**: Maven (WAR 패키징)

## 프로젝트 구조
```
src/main/java/com/project/trip
├─ AI                # AI 여행 루트 추천
├─ admin             # 관리자 기능
├─ allplace          # 여행지/지도/검색
├─ auth              # 로그인/권한/세션
├─ board             # 게시판(루트/핫딜/리뷰/QnA/찾기/공지)
├─ mypage            # 마이페이지
├─ reservation       # 예약
└─ weather           # 날씨 정보

src/main/webapp/WEB-INF/views
├─ content           # JSP 뷰
└─ tiles*.xml        # Tiles 레이아웃
```

## 실행 방법
> 이 프로젝트는 WAR 패키지로 빌드하여 **서블릿 컨테이너(Tomcat 등)** 에 배포하는 구성을 전제로 합니다.

1. **필수 환경**
   - Java 11
   - Maven
   - Oracle DB 및 Wallet 설정 (TNS_ADMIN)

2. **DB 설정**
   - `src/main/webapp/WEB-INF/spring/root-context.xml`의 JDBC 설정을 환경에 맞게 수정합니다.
   - Oracle Wallet 경로와 계정 정보를 업데이트하세요.

3. **파일 업로드 경로 설정**
   - `src/main/webapp/WEB-INF/web.xml`의 `multipart-config` 경로를 환경에 맞게 조정합니다.

4. **빌드**
   ```bash
   mvn clean package
   ```

5. **배포/실행**
   - 생성된 `target/*.war` 파일을 Tomcat에 배포합니다.

## 테스트
- 테스트 코드는 `src/test/java`에 위치합니다.
- 필요 시 다음 명령으로 실행할 수 있습니다.
  ```bash
  mvn test
  ```

## 참고
- 설정 및 데이터 소스 변경 시 애플리케이션 재시작이 필요합니다.
- Swagger 의존성이 포함되어 있으며, 필요 시 설정 파일에서 활성화하여 API 문서를 확인할 수 있습니다.

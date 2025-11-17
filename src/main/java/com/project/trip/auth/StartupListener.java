package com.project.trip.auth;

import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 웹 애플리케이션 시작 및 종료 시 특정 작업을 수행하는 리스너 클래스입니다.
 * <p>
 * {@link ServletContextListener}를 구현하여 애플리케이션 컨텍스트의 생명주기 이벤트를 처리합니다.
 * 주로 애플리케이션 시작 시 필요한 초기화 작업(예: 파일 시스템 디렉토리 생성)을 수행합니다.
 * </p>
 */
// web.xml에 등록할 것이므로 @WebListener 어노테이션은 필요 없습니다.
public class StartupListener implements ServletContextListener {

    // web.xml에 <location> 태그로 지정한 바로 그 경로
    private final String UPLOAD_DIR = "C:/dev-temp/trip-upload";

    /**
     * 톰캣(서버)이 시작될 때 이 메서드가 자동으로 실행됩니다.
     * <p>
     * 파일 업로드를 위한 임시 디렉토리({@code UPLOAD_DIR})가 존재하는지 확인하고,
     * 존재하지 않으면 해당 디렉토리를 생성합니다.
     * </p>
     * @param sce 서블릿 컨텍스트 이벤트 객체
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        File uploadDir = new File(UPLOAD_DIR);

        // 1. 폴더가 존재하는지 확인합니다.
        if (!uploadDir.exists()) {
            System.out.println("임시 업로드 폴더가 존재하지 않습니다: " + UPLOAD_DIR);
            
            // 2. 폴더가 없으면, 상위 폴더까지(mkdirs) 모두 생성합니다.
            boolean created = uploadDir.mkdirs();
            
            if (created) {
                System.out.println("임시 업로드 폴더 생성 성공.");
            } else {
                System.err.println("임시 업로드 폴더 생성 실패. (권한 등 확인 필요)");
            }
        } else {
            System.out.println("임시 업로드 폴더가 이미 존재합니다.");
        }
    }

    /**
     * 서버가 종료될 때 실행됩니다.
     * <p>
     * 현재는 특별히 수행하는 작업이 없습니다.
     * </p>
     * @param sce 서블릿 컨텍스트 이벤트 객체
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 특별히 할 작업이 없으면 비워둡니다.
    }
}

package com.project.trip.auth;

import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

// web.xml에 등록할 것이므로 @WebListener 어노테이션은 필요 없습니다.
public class StartupListener implements ServletContextListener {

    // web.xml에 <location> 태그로 지정한 바로 그 경로
    private final String UPLOAD_DIR = "C:/dev-temp/trip-upload";

    /**
     * 톰캣(서버)이 시작될 때 이 메서드가 자동으로 실행됩니다.
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
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 특별히 할 작업이 없으면 비워둡니다.
    }
}

// 파일 경로: com.project.trip.admin.report.controller.AdminReportController.java

package com.project.trip.admin.report.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.admin.report.model.reportDTO;
import com.project.trip.admin.report.service.AdminReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin") // '/admin' 최상위 경로 사용
public class AdminReportController {

    private final AdminReportService reportService;
    
    /**
     * 1. 신고 접수 목록 (reportList.java 대체)
     * URL: /admin/report/list (메인 경로)
     */
    @GetMapping({"/report/list"}) // 기존: /report.do
    public String getPendingReports(Model model) {
        
        // TODO: Spring Security 설정 완료 시 권한 확인 로직 추가 예정
        
        List<reportDTO> list = reportService.getPendingReports();
        model.addAttribute("list", list);
        
        // reportlist.jsp를 타일즈 뷰 이름으로 반환
        return "admin/reportlist"; 
    }

    /**
     * 2. 신고 처리 내역 (reportHistory.java 대체)
     * URL: /admin/report/history
     */
    @GetMapping("/report/history") // 기존: /report/history.do
    public String getProcessedReports(Model model) {
        
        List<reportDTO> list = reportService.getProcessedReports();
        model.addAttribute("list", list);
        
        // reporthistory.jsp를 타일즈 뷰 이름으로 반환
        return "admin/reporthistory"; 
    }

    /**
     * 3. 신고 처리 (processReport.java 대체)
     * URL: /admin/report/process
     * JSP에서 전송하는 파라미터: reportId, action, targetType, targetId
     */
    @PostMapping("/report/process")
    public String processReport(
            @RequestParam("reportId") int reportId,
            @RequestParam("action") String processType,
            @RequestParam(value = "targetType", required = false) String targetType,
            @RequestParam(value = "targetId", required = false, defaultValue = "0") int targetId,
            RedirectAttributes rttr) {

        try {

            // 여기서 매핑 통일
            String action = processType.equals("HIDE") ? "approve" : "reject";

            reportService.processReport(reportId, action, targetType, targetId);

            String msg = action.equals("approve")
                    ? "게시글이 숨김 처리되었습니다."
                    : "신고가 반려 처리되었습니다.";

            rttr.addFlashAttribute("msg", msg);

        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "신고 처리 중 오류가 발생했습니다.");
            e.printStackTrace();
        }

        return "redirect:/admin/report/list";
    }
}
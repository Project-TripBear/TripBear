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

/**
 * 관리자 페이지의 신고 관리와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 신고 목록 조회, 신고 내역 조회, 신고 처리 등의 기능을 제공합니다.
 * </p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin") // '/admin' 최상위 경로 사용
public class AdminReportController {

    private final AdminReportService reportService;
    
    /**
     * 처리 대기 중인 신고 목록 페이지를 반환합니다.
     * @param model 뷰에 신고 목록 데이터를 전달하기 위한 Model 객체
     * @return 신고 목록 페이지의 뷰 이름
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
     * 처리 완료된 신고 내역 페이지를 반환합니다.
     * @param model 뷰에 신고 내역 데이터를 전달하기 위한 Model 객체
     * @return 신고 내역 페이지의 뷰 이름
     */
    @GetMapping("/report/history") // 기존: /report/history.do
    public String getProcessedReports(Model model) {
        
        List<reportDTO> list = reportService.getProcessedReports();
        model.addAttribute("list", list);
        
        // reporthistory.jsp를 타일즈 뷰 이름으로 반환
        return "admin/reporthistory"; 
    }

    /**
     * 신고를 처리합니다. (게시글 숨김 또는 신고 반려)
     * @param reportId 처리할 신고의 고유 ID
     * @param processType 처리 유형 ("HIDE" 또는 "REJECT")
     * @param targetType 신고 대상 유형 (예: "findboard", "review")
     * @param targetId 신고 대상의 고유 ID
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 신고 목록 페이지로 리다이렉트
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
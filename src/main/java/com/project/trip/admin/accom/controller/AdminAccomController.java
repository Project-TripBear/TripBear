// 파일 경로: com.project.trip.admin.accom.controller.AdminAccomController.java

package com.project.trip.admin.accom.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.admin.accom.model.accomAllInfoDTO;
import com.project.trip.admin.accom.model.accomDTO;
import com.project.trip.admin.accom.service.AdminAccomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/accom")
public class AdminAccomController {

    private final AdminAccomService accomService;

    /**
     * 1. 숙소 목록 조회 (accomList.java 마이그레이션)
     */
    @GetMapping("/list")
    public String getAccomList(
            Model model,
            @RequestParam(value = "type", required = false) String[] accomTypes,
            @RequestParam(value = "minPrice", required = false) Integer minPriceParam,
            @RequestParam(value = "maxPrice", required = false) Integer maxPriceParam,
            @RequestParam(value = "sort", required = false) String sortOrder
    ) {
        
        int maxPriceFromDB = accomService.getMaxPrice();
        
        int minPrice = (minPriceParam != null) ? minPriceParam : 0;
        int maxPrice = (maxPriceParam != null) ? maxPriceParam : maxPriceFromDB;
        
        List<accomDTO> list = accomService.getAllAccommodations(accomTypes, minPrice, maxPrice, sortOrder);
        
        model.addAttribute("list", list);
        model.addAttribute("selectedTypes", (accomTypes != null) ? Arrays.asList(accomTypes) : null);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("maxPriceFromDB", maxPriceFromDB);

        // accomlist.jsp를 Tiles 뷰 이름으로 반환
        return "admin/accomlist"; 
    }

    /**
     * 2. 신규 숙소 등록 폼 (addAccom.java의 doGet)
     */
    @GetMapping("/add")
    public String addAccomForm() {
        // accomadd.jsp를 Tiles 뷰 이름으로 반환
        return "admin/accomadd";
    }
    
    /**
     * 3. 신규 숙소 등록 처리 (addAccom.java의 doPost)
     */
    @PostMapping("/add")
    public String addAccomProcess(accomAllInfoDTO dto, RedirectAttributes rttr) {
        
        try {
            accomService.addAccommodation(dto);
            rttr.addFlashAttribute("msg", "신규 숙소 등록이 완료되었습니다.");
            return "redirect:/admin/accom/list";
            
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "숙소 등록 중 오류가 발생했습니다.");
            e.printStackTrace();
            return "redirect:/admin/accom/add";
        }
    }
    /**
     * [추가] 숙소 상세 보기 (GET)
     * accomlist.jsp에서 '숙소명' 클릭 시
     */
    @GetMapping("/view")
    public String viewAccom(@RequestParam("roomId") int roomId, Model model) {
        accomAllInfoDTO dto = accomService.getAccommodationDetails(roomId);
        model.addAttribute("dto", dto);
        return "admin/accomview"; // accomview.jsp 뷰 이름
    }

    /**
     * 4. [추가] 숙소 수정 페이지로 이동 (GET)
     * accomlist.jsp에서 '수정' 버튼 클릭 시
     */
    @GetMapping("/edit")
    public String editAccomForm(@RequestParam("roomId") int roomId, Model model) {
        
        // DTO에 3개 테이블(Place, Accom, Room)의 모든 정보를 조인해서 가져옵니다.
        accomAllInfoDTO dto = accomService.getAccommodationDetails(roomId);
        
        model.addAttribute("dto", dto);
        
        return "admin/accomedit"; // accomedit.jsp 뷰 이름
    }

    /**
     * 5. [추가] 숙소 수정 처리 (POST)
     * accomedit.jsp에서 '수정하기' 버튼 클릭 시
     */
    @PostMapping("/edit")
    public String editAccomProcess(accomAllInfoDTO dto, RedirectAttributes rttr) {
        
        try {
            accomService.updateAccommodation(dto);
            rttr.addFlashAttribute("msg", "숙소 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "수정 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        
        // 수정 완료 후 다시 목록 페이지로 이동
        return "redirect:/admin/accom/list";
    }

    /**
     * 6. [추가] 숙소(객실) 삭제 처리 (POST)
     * accomlist.jsp에서 '삭제' 버튼 클릭 시
     */
    @PostMapping("/delete")
    public String deleteAccomProcess(@RequestParam("roomId") int roomId, RedirectAttributes rttr) {
        
        try {
            // 이 객실(Room)과 연결된 숙소(Accom), 위치(Place) 정보를 함께 삭제합니다.
            accomService.deleteAccommodation(roomId);
            rttr.addFlashAttribute("msg", "숙소(객실)가 삭제되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "삭제 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        
        return "redirect:/admin/accom/list";
    }
}
// 파일 경로: com.project.trip.admin.accom.controller.AdminAccomController.java

package com.project.trip.admin.accom.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

    // ------------------------------
    // 1. 숙소 리스트
    // ------------------------------
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

        return "admin/accomlist";
    }

    // ------------------------------
    // 2. 신규 등록 폼
    // ------------------------------
    @GetMapping("/add")
    public String addAccomForm() {
        return "admin/accomadd";
    }

    // ------------------------------
    // 3. 신규 등록 처리 (파일 업로드)
    // ------------------------------
    @PostMapping("/add")
    public String addAccomProcess(
            accomAllInfoDTO dto,
            @RequestParam("placeMainImageFile") MultipartFile placeImg,
            @RequestParam("roomImageFile") MultipartFile roomImg,
            HttpServletRequest request,
            RedirectAttributes rttr) {

        try {
            // === 1) 숙소 대표 이미지 업로드 ===
            if (placeImg != null && !placeImg.isEmpty()) {

                String uploadDir = request.getServletContext().getRealPath("/resources/img/accom");

                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + placeImg.getOriginalFilename();
                File saveFile = new File(dir, fileName);

                placeImg.transferTo(saveFile);

                // DB 저장용
                dto.setPlaceMainImageUrl(fileName);
            }

            // === 2) 객실 이미지 업로드 ===
            if (roomImg != null && !roomImg.isEmpty()) {

                String uploadDir = request.getServletContext().getRealPath("/resources/img/room");

                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + roomImg.getOriginalFilename();
                File saveFile = new File(dir, fileName);

                roomImg.transferTo(saveFile);

                dto.setRoomImageUrl(fileName);
            }

            accomService.addAccommodation(dto);

            rttr.addFlashAttribute("msg", "신규 숙소 등록이 완료되었습니다.");
            return "redirect:/admin/accom/list";

        } catch (Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", "숙소 등록 중 오류가 발생했습니다.");
            return "redirect:/admin/accom/add";
        }
    }

    // ------------------------------
    // 4. 상세보기
    // ------------------------------
    @GetMapping("/view")
    public String viewAccom(@RequestParam("roomId") int roomId, Model model) {
        accomAllInfoDTO dto = accomService.getAccommodationDetails(roomId);
        model.addAttribute("dto", dto);
        return "admin/accomview";
    }

    // ------------------------------
    // 5. 수정 폼으로 이동
    // ------------------------------
    @GetMapping("/edit")
    public String editAccomForm(@RequestParam("roomId") int roomId, Model model) {

        accomAllInfoDTO dto = accomService.getAccommodationDetails(roomId);
        model.addAttribute("dto", dto);

        return "admin/accomedit";
    }

    // ------------------------------
    // 6. 수정 처리 (파일 업로드 포함)
    // ------------------------------
    @PostMapping("/edit")
    public String editAccomProcess(
            accomAllInfoDTO dto,
            @RequestParam(value = "placeMainImageFile", required = false) MultipartFile placeImg,
            @RequestParam(value = "roomImageFile", required = false) MultipartFile roomImg,
            @RequestParam("oldPlaceImage") String oldPlaceImage,
            @RequestParam("oldRoomImage") String oldRoomImage,
            HttpServletRequest request,
            RedirectAttributes rttr) {

        try {
            // === 대표 이미지 수정 ===
            if (placeImg != null && !placeImg.isEmpty()) {

                String uploadDir = request.getServletContext().getRealPath("/resources/img/accom");
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + placeImg.getOriginalFilename();
                File saveFile = new File(dir, fileName);

                placeImg.transferTo(saveFile);
                dto.setPlaceMainImageUrl(fileName);

            } else {
                dto.setPlaceMainImageUrl(oldPlaceImage); // 기존 유지
            }

            // === 객실 이미지 수정 ===
            if (roomImg != null && !roomImg.isEmpty()) {

                String uploadDir = request.getServletContext().getRealPath("/resources/img/room");
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + roomImg.getOriginalFilename();
                File saveFile = new File(dir, fileName);

                roomImg.transferTo(saveFile);
                dto.setRoomImageUrl(fileName);

            } else {
                dto.setRoomImageUrl(oldRoomImage); // 기존 유지
            }

            accomService.updateAccommodation(dto);

            rttr.addFlashAttribute("msg", "숙소 정보가 성공적으로 수정되었습니다.");
            return "redirect:/admin/accom/list";

        } catch (Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", "수정 중 오류가 발생했습니다.");
            return "redirect:/admin/accom/edit?roomId=" + dto.getRoomId();
        }
    }

    // ------------------------------
    // 7. 삭제 처리
    // ------------------------------
    @PostMapping("/delete")
    public String deleteAccomProcess(@RequestParam("roomId") int roomId, RedirectAttributes rttr) {

        try {
            accomService.deleteAccommodation(roomId);
            rttr.addFlashAttribute("msg", "숙소(객실)가 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", "삭제 중 오류가 발생했습니다.");
        }

        return "redirect:/admin/accom/list";
    }
}

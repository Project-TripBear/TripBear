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

/**
 * 관리자 페이지의 숙소 관리와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 숙소 목록 조회, 신규 숙소 등록, 상세 정보 조회, 정보 수정, 삭제 등의 기능을 제공합니다.
 * </p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/accom")
public class AdminAccomController {

    private final AdminAccomService accomService;

    /**
     * 숙소 목록 페이지를 반환합니다.
     * <p>
     * 숙소 유형, 가격 범위, 정렬 순서 등 다양한 필터링 조건을 받아
     * 조건에 맞는 숙소 목록을 조회하고 뷰에 전달합니다.
     * </p>
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param accomTypes 필터링할 숙소 유형 배열
     * @param minPriceParam 최소 가격 파라미터
     * @param maxPriceParam 최대 가격 파라미터
     * @param sortOrder 정렬 순서
     * @return 숙소 목록 페이지의 뷰 이름
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

        return "admin/accomlist";
    }

    /**
     * 신규 숙소 등록 폼 페이지를 반환합니다.
     * @return 신규 숙소 등록 페이지의 뷰 이름
     */
    @GetMapping("/add")
    public String addAccomForm() {
        return "admin/accomadd";
    }

    /**
     * 신규 숙소를 등록 처리합니다.
     * <p>
     * 숙소 정보와 함께 대표 이미지, 객실 이미지를 업로드하고 데이터베이스에 저장합니다.
     * </p>
     * @param dto 숙소 정보를 담은 DTO
     * @param placeImg 숙소 대표 이미지 파일
     * @param roomImg 객실 이미지 파일
     * @param request HTTP 요청 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 성공 시 숙소 목록 페이지로, 실패 시 등록 폼 페이지로 리다이렉트
     */
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

    /**
     * 숙소 상세 정보 페이지를 반환합니다.
     * @param roomId 조회할 객실의 고유 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 숙소 상세 정보 페이지의 뷰 이름
     */
    @GetMapping("/view")
    public String viewAccom(@RequestParam("roomId") int roomId, Model model) {
        accomAllInfoDTO dto = accomService.getAccommodationDetails(roomId);
        model.addAttribute("dto", dto);
        return "admin/accomview";
    }

    /**
     * 숙소 정보 수정 폼 페이지를 반환합니다.
     * @param roomId 수정할 객실의 고유 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 숙소 정보 수정 페이지의 뷰 이름
     */
    @GetMapping("/edit")
    public String editAccomForm(@RequestParam("roomId") int roomId, Model model) {

        accomAllInfoDTO dto = accomService.getAccommodationDetails(roomId);
        model.addAttribute("dto", dto);

        return "admin/accomedit";
    }

    /**
     * 숙소 정보를 수정 처리합니다.
     * <p>
     * 수정된 숙소 정보와 함께 새로운 이미지가 업로드된 경우 이를 반영하여
     * 데이터베이스를 업데이트합니다.
     * </p>
     * @param dto 수정된 숙소 정보를 담은 DTO
     * @param placeImg 새로 업로드된 숙소 대표 이미지 파일
     * @param roomImg 새로 업로드된 객실 이미지 파일
     * @param oldPlaceImage 기존 숙소 대표 이미지 파일명
     * @param oldRoomImage 기존 객실 이미지 파일명
     * @param request HTTP 요청 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 성공 시 숙소 목록 페이지로, 실패 시 수정 폼 페이지로 리다이렉트
     */
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

    /**
     * 숙소 정보를 삭제 처리합니다.
     * @param roomId 삭제할 객실의 고유 ID
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 숙소 목록 페이지로 리다이렉트
     */
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

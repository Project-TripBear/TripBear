// 파일 경로: com.project.trip.admin.car.controller.AdminCarController.java

package com.project.trip.admin.car.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.admin.car.model.carDTO;
import com.project.trip.admin.car.service.AdminCarService;

import lombok.RequiredArgsConstructor;

/**
 * 관리자 페이지의 렌터카 관리와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 렌터카 목록 조회, 신규 등록, 상세 정보 조회, 정보 수정, 삭제 등의 기능을 제공합니다.
 * </p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/car")
public class AdminCarController {

    private final AdminCarService carService;

    /**
     * 렌터카 목록 페이지를 반환합니다.
     * <p>
     * 연료 유형, 가격 범위, 정렬 순서 등 다양한 필터링 조건을 받아
     * 조건에 맞는 렌터카 목록을 조회하고 뷰에 전달합니다.
     * </p>
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @param fuelTypes 필터링할 연료 유형 배열
     * @param minPriceParam 최소 가격 파라미터
     * @param maxPriceParam 최대 가격 파라미터
     * @param sortOrder 정렬 순서
     * @return 렌터카 목록 페이지의 뷰 이름
     */
    @GetMapping("/list")
    public String getCarList(
            Model model,
            @RequestParam(value = "fuel", required = false) String[] fuelTypes,
            @RequestParam(value = "minPrice", required = false) Integer minPriceParam,
            @RequestParam(value = "maxPrice", required = false) Integer maxPriceParam,
            @RequestParam(value = "sort", required = false) String sortOrder
    ) {
        int maxPriceFromDB = carService.getMaxPrice();
        
        int minPrice = (minPriceParam != null) ? minPriceParam : 0;
        int maxPrice = (maxPriceParam != null) ? maxPriceParam : maxPriceFromDB;
        
        List<carDTO> list = carService.getAllCars(fuelTypes, minPrice, maxPrice, sortOrder);
        
        model.addAttribute("list", list);
        model.addAttribute("selectedFuels", (fuelTypes != null) ? Arrays.asList(fuelTypes) : null);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("maxPriceFromDB", maxPriceFromDB);

        return "admin/carlist"; 
    }

    /**
     * 신규 렌터카 등록 폼 페이지를 반환합니다.
     * @param model 뷰에 지역 목록 데이터를 전달하기 위한 Model 객체
     * @return 신규 렌터카 등록 페이지의 뷰 이름
     */
    @GetMapping("/add")
    public String addCarForm(Model model) {
    	
    	List<Map<String, Object>> locations = carService.getAllLocations();
        model.addAttribute("locations", locations); // jsp에서 ${locations}로 사용
        
        
        return "admin/addcar"; 
    }

    /**
     * 신규 렌터카를 등록 처리합니다.
     * <p>
     * 렌터카 정보와 함께 이미지를 업로드하고 데이터베이스에 저장합니다.
     * </p>
     * @param dto 렌터카 정보를 담은 DTO
     * @param imgFile 차량 이미지 파일
     * @param request HTTP 요청 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 성공 시 렌터카 목록 페이지로, 실패 시 등록 폼 페이지로 리다이렉트
     */
    @PostMapping("/add")
    public String addCarProcess(
            carDTO dto,
            @RequestParam("carImageFile") MultipartFile imgFile,
            HttpServletRequest request,
            RedirectAttributes rttr) {

        try {
            // 1. 파일이 올라왔으면 저장
            if (imgFile != null && !imgFile.isEmpty()) {

                // 실제 업로드 경로 (webapp/resources/img/car)
                String uploadDir = request.getServletContext().getRealPath("/resources/img/car");

                // 디렉토리 없으면 생성
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 저장할 파일명 (중복 방지용 타임스탬프)
                String fileName = System.currentTimeMillis() + "_" + imgFile.getOriginalFilename();

                File saveFile = new File(dir, fileName);

                // 실제 파일 저장
                imgFile.transferTo(saveFile);

                // DB에는 파일명만 저장
                dto.setCarImage(fileName);
            }

            // 2. 서비스 호출해서 DB 저장
            carService.addCar(dto);

            // 3. 성공 메시지
            rttr.addFlashAttribute("msg", "신규 차량이 성공적으로 등록되었습니다.");

            return "redirect:/admin/car/list";

        } catch (Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", "차량 등록 중 오류가 발생했습니다.");
            return "redirect:/admin/car/add";
        }
    }

    /**
     * 렌터카 정보를 삭제 처리합니다.
     * @param carId 삭제할 렌터카의 고유 ID
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 렌터카 목록 페이지로 리다이렉트
     */
    @PostMapping("/delete")
    public String deleteCarProcess(@RequestParam("carId") int carId, RedirectAttributes rttr) {
        try {
            carService.deleteCar(carId);
            rttr.addFlashAttribute("msg", "차량이 삭제되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "삭제 중 오류가 발생했습니다. (예약 내역이 존재할 수 있습니다)");
            e.printStackTrace();
        }
        return "redirect:/admin/car/list";
    }

    /**
     * 렌터카 정보 수정 폼 페이지를 반환합니다.
     * @param carId 수정할 렌터카의 고유 ID
     * @param model 뷰에 차량 상세 정보와 지역 목록을 전달하기 위한 Model 객체
     * @return 렌터카 정보 수정 페이지의 뷰 이름
     */
    @GetMapping("/edit")
    public String editCarForm(@RequestParam("carId") int carId, Model model) {
        
        // 1. 차량 상세 정보 조회
        carDTO carDetail = carService.getCarDetail(carId); 
        
        // 2. 등록 지역 목록 조회 (locations 데이터 추가)
        List<Map<String, Object>> locations = carService.getAllLocations();
        model.addAttribute("locations", locations);
        
        if (carDetail == null) {
            model.addAttribute("msg", "수정할 차량 정보를 찾을 수 없습니다.");
            return "redirect:/admin/car/list";
        }
        
        model.addAttribute("carDetail", carDetail);
        
        // editcar.jsp로 이동
        return "admin/editcar"; 
    }

    /**
     * 렌터카 정보를 수정 처리합니다.
     * <p>
     * 수정된 렌터카 정보와 함께 새로운 이미지가 업로드된 경우 이를 반영하여
     * 데이터베이스를 업데이트합니다.
     * </p>
     * @param dto 수정된 렌터카 정보를 담은 DTO
     * @param imgFile 새로 업로드된 차량 이미지 파일
     * @param originImage 기존 차량 이미지 파일명
     * @param request HTTP 요청 객체
     * @param rttr 리다이렉트 시 메시지를 전달하기 위한 RedirectAttributes 객체
     * @return 성공 시 렌터카 목록 페이지로, 실패 시 수정 폼 페이지로 리다이렉트
     */
    @PostMapping("/edit")
    public String editCarProcess(
            carDTO dto,
            @RequestParam("carImageFile") MultipartFile imgFile,
            @RequestParam("originImage") String originImage,
            HttpServletRequest request,
            RedirectAttributes rttr) {

        try {

            if (imgFile != null && !imgFile.isEmpty()) {

                String uploadDir = request.getServletContext().getRealPath("/resources/img/car");

                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                String fileName = System.currentTimeMillis() + "_" + imgFile.getOriginalFilename();

                File saveFile = new File(dir, fileName);
                imgFile.transferTo(saveFile);

                dto.setCarImage(fileName);

            } else {
                // ⭐ 새 파일 없으면 기존 이미지 유지
                dto.setCarImage(originImage);
            }

            int result = carService.editCar(dto);

            if (result > 0) {
                rttr.addFlashAttribute("msg", "차량 정보가 성공적으로 수정되었습니다.");
            } else {
                rttr.addFlashAttribute("msg", "수정된 내용이 없거나 차량 ID가 유효하지 않습니다.");
            }

            return "redirect:/admin/car/list";

        } catch (Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", "차량 수정 중 오류가 발생했습니다.");
            return "redirect:/admin/car/edit?carId=" + dto.getCarId();
        }
    }
    
    /**
     * 렌터카 상세 정보 페이지를 반환합니다.
     * @param carId 조회할 렌터카의 고유 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 렌터카 상세 정보 페이지의 뷰 이름
     */
    @GetMapping("/view")
    public String viewCarDetail(@RequestParam("carId") int carId, Model model) {
        
        // carService에 이미 구현된 getCarDetail(int carId)를 사용합니다.
        carDTO carDetail = carService.getCarDetail(carId); 
        
        if (carDetail == null) {
            model.addAttribute("msg", "상세 정보를 찾을 수 없는 차량 ID입니다.");
            return "redirect:/admin/car/list";
        }
        
        // carview.jsp에서 'carDetail'이라는 이름으로 DTO를 사용합니다.
        model.addAttribute("carDetail", carDetail);
        
        // content/admin/carview 타일즈 뷰 이름 반환
        return "admin/carview"; 
    }
    
}
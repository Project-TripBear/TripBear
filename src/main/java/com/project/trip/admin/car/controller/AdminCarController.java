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

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/car")
public class AdminCarController {

    private final AdminCarService carService;

    // 렌터카 목록 조회 및 필터링
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

    // 렌터카 등록 폼 페이지로 이동
    @GetMapping("/add")
    public String addCarForm(Model model) {
    	
    	List<Map<String, Object>> locations = carService.getAllLocations();
        model.addAttribute("locations", locations); // jsp에서 ${locations}로 사용
        
        
        return "admin/addcar"; 
    }
    // ✅ 렌터카 등록 처리 (파일 업로드 포함)
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
    // 렌터카 삭제 처리
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

    // 렌터카 수정 폼 페이지로 이동
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
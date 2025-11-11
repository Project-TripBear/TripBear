// 파일 경로: com.project.trip.admin.car.controller.AdminCarController.java

package com.project.trip.admin.car.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        return "content/admin/carlist"; 
    }

    // 렌터카 등록 폼 페이지로 이동
    @GetMapping("/add")
    public String addCarForm(Model model) {
    	
    	List<Map<String, Object>> locations = carService.getAllLocations();
        model.addAttribute("locations", locations); // jsp에서 ${locations}로 사용
        
        
        return "content/admin/addcar"; 
    }

    // 렌터카 등록 처리
    @PostMapping("/add")
    public String addCarProcess(carDTO dto, RedirectAttributes rttr) {
        try {
            carService.addCar(dto);
            rttr.addFlashAttribute("msg", "신규 차량이 성공적으로 등록되었습니다.");
            return "redirect:/admin/car/list";
            
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "차량 등록 중 오류가 발생했습니다.");
            e.printStackTrace();
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
        carDTO carDetail = carService.getCarDetail(carId); 
        
        if (carDetail == null) {
            model.addAttribute("msg", "수정할 차량 정보를 찾을 수 없습니다.");
            return "redirect:/admin/car/list";
        }
        
        model.addAttribute("carDetail", carDetail);
        
        // ★★★ [수정] accomedit -> editcar로 뷰 이름 변경 ★★★
        return "content/admin/editcar"; 
    }

    /**
     * 4. 렌터카 수정 처리 (editCar.java의 doPost 역할)
     */
    @PostMapping("/edit")
    public String editCarProcess(carDTO dto, RedirectAttributes rttr) {
        try {
            int result = carService.editCar(dto);
            
            if (result > 0) {
                rttr.addFlashAttribute("msg", "차량 정보가 성공적으로 수정되었습니다.");
            } else {
                rttr.addFlashAttribute("msg", "수정된 내용이 없거나 차량 ID가 유효하지 않습니다.");
            }
            
            // ★★★ [참고] 수정 후 상세 보기 페이지가 있다면 거기로 보내는 것이 좋습니다. ★★★
            // (지금은 목록으로 다시 보냅니다)
            // return "redirect:/admin/car/view?carId=" + dto.getCarId();
            return "redirect:/admin/car/list"; // 목록으로 리다이렉트
            
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "차량 수정 중 오류가 발생했습니다.");
            e.printStackTrace();
            return "redirect:/admin/car/edit?carId=" + dto.getCarId(); // 실패 시 수정 폼으로
        }
    }
}
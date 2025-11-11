// 파일 경로: com.project.trip.admin.car.controller.AdminCarController.java

package com.project.trip.admin.car.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.trip.admin.car.model.carDTO;

// DTO는 accom 패키지가 아닌 admin.model 패키지에 있습니다.
	 
import com.project.trip.admin.car.service.AdminCarService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/car") // 레거시 URL(/admin/car/...) 기준
public class AdminCarController {

    private final AdminCarService carService;

    
    @GetMapping("/list")
    public String getCarList(
            Model model,
            @RequestParam(value = "fuel", required = false) String[] fuelTypes,
            @RequestParam(value = "minPrice", required = false) Integer minPriceParam,
            @RequestParam(value = "maxPrice", required = false) Integer maxPriceParam,
            @RequestParam(value = "sort", required = false) String sortOrder
    ) {
        // carDAO.java의 로직을 서비스로 이동
        int maxPriceFromDB = carService.getMaxPrice();
        
        // carList.java의 로직과 동일하게
        int minPrice = (minPriceParam != null) ? minPriceParam : 0;
        int maxPrice = (maxPriceParam != null) ? maxPriceParam : maxPriceFromDB;
        
        List<carDTO> list = carService.getAllCars(fuelTypes, minPrice, maxPrice, sortOrder);
        
        // carlist.jsp에 필요한 모든 어트리뷰트 전달
        model.addAttribute("list", list);
        model.addAttribute("selectedFuels", (fuelTypes != null) ? Arrays.asList(fuelTypes) : null);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("maxPriceFromDB", maxPriceFromDB);

        // carlist.jsp 뷰 이름 반환 (Tiles에서 처리)
        return "content/admin/carlist"; 
    }
    /**
     * 1. 렌터카 등록 폼 페이지로 이동 (addCar.java의 doGet)
     */
    @GetMapping("/add")
    public String addCarForm() {
        // addcar.jsp 뷰 이름을 반환 (Tiles에서 처리)
        return "content/admin/addcar";
    }

    /**
     * 2. 렌터카 등록 처리 (addCar.java의 doPost)
     */
    @PostMapping("/add")
    public String addCarProcess(carDTO dto, RedirectAttributes rttr) {
        // 1. Spring이 자동으로 폼 데이터를 carDTO에 바인딩합니다.
        // 2. JSP의 JS(removeCommasBeforeSubmit)가 콤마를 제거하므로
        //    자바에서 별도 처리가 필요 없습니다.
        
        try {
            carService.addCar(dto);
            rttr.addFlashAttribute("msg", "신규 차량이 성공적으로 등록되었습니다.");
            
            // 3. 레거시 DAO의 흐름대로 목록 페이지로 리다이렉트
            return "redirect:/admin/car/list";
            
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "차량 등록 중 오류가 발생했습니다.");
            e.printStackTrace();
            return "redirect:/admin/car/add"; // 실패 시 등록 폼으로
        }
    }
    @PostMapping("/delete")
    public String deleteCarProcess(@RequestParam("carId") int carId, RedirectAttributes rttr) {
        
        try {
            // TODO: tblCarReservation에 FK 제약조건이 있으므로, 
            // 실제로는 예약 내역을 먼저 삭제하거나, FK를 ON DELETE CASCADE로 변경해야 합니다.
            // 지금은 carDAO.java의 로직을 그대로 따릅니다.
            carService.deleteCar(carId);
            rttr.addFlashAttribute("msg", "차량이 삭제되었습니다.");
        } catch (Exception e) {
            rttr.addFlashAttribute("msg", "삭제 중 오류가 발생했습니다. (예약 내역이 존재할 수 있습니다)");
            e.printStackTrace();
        }
        
        return "redirect:/admin/car/list";
    }
}
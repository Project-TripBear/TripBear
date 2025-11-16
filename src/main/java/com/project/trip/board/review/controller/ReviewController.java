package com.project.trip.board.review.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.project.trip.board.review.model.ReviewDTO;
import com.project.trip.board.review.model.ReviewImageDTO;
import com.project.trip.board.review.service.ReviewService;
import com.project.trip.mypage.model.CustomUser; // CustomUser 경로는 동일하다고 가정

@Controller
@RequestMapping("/review") // ✅ URL 경로는 /review 로 수정
public class ReviewController {

    @Autowired
    private ReviewService reviewService; // ✅ postService -> reviewService

    // 게시글 목록
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue="1") int page, Model model) {

        int pageSize = 10;
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);

        List<ReviewDTO> list = reviewService.list(map);
        int totalCount = reviewService.totalCount(map);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        model.addAttribute("list", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "board.review.list"; // View 경로
    }


    // 게시글 상세보기
    @GetMapping("/view/{reviewPostId}") // ✅ 변수명은 ERD를 따르는 reviewPostId
    public String view(@PathVariable int reviewPostId, // ✅ 변수명은 ERD를 따르는 reviewPostId
                       Model model,
                       Authentication authentication) {

        // 기본 데이터
        reviewService.increaseViewCount(reviewPostId);
        ReviewDTO review = reviewService.get(reviewPostId); // ✅ post -> review 로 변경
        List<ReviewImageDTO> images = reviewService.getImages(reviewPostId);

        model.addAttribute("review", review); // ✅ "post" -> "review" 로 변경
        model.addAttribute("images", images);

        // 로그인 사용자 확인
        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {

            CustomUser user = (CustomUser) authentication.getPrincipal();
            model.addAttribute("userId", user.getUdto().getSeq());
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userId", null);
            model.addAttribute("userName", null);
        }

        return "board.review.view"; // View 경로
    }

    // 게시글 작성 폼
    @GetMapping("/add")
    public String addForm() {
        return "board.review.add"; // View 경로
    }

    // 게시글 작성 처리
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String add(@ModelAttribute ReviewDTO dto,
                      @RequestParam(value = "images", required = false) MultipartFile[] images,
                      HttpServletRequest req,
                      Authentication authentication) throws Exception {

        CustomUser user = (CustomUser) authentication.getPrincipal();
        long uid = Long.parseLong(user.getUdto().getSeq());
        dto.setUserId(uid);

        reviewService.add(dto); 

        String uploadPath = "C:/tripbear/review/"; // ✅ 경로 수정

        File folder = new File(uploadPath);
        if (!folder.exists()) folder.mkdirs();

        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {

                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    File dest = new File(uploadPath, fileName);
                    file.transferTo(dest);

                    ReviewImageDTO imgDto = new ReviewImageDTO();
                    imgDto.setReviewPostId(dto.getReviewPostId()); // ✅ ERD 기준
                    imgDto.setReviewImageUrl(fileName);

                    reviewService.addImage(imgDto);
                }
            }
        }

        return "redirect:/review/list";
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{reviewPostId}") // ✅ ERD 기준
    public String editForm(@PathVariable int reviewPostId, Model model) {
        ReviewDTO dto = reviewService.get(reviewPostId);
        model.addAttribute("dto", dto);
        return "board.review.edit"; // View 경로
    }

    // 게시글 수정 처리
    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String edit(@ModelAttribute ReviewDTO dto,
                       @RequestParam(value = "images", required = false) MultipartFile[] images,
                       HttpServletRequest req) throws Exception {

        reviewService.edit(dto);

        reviewService.delImages(dto.getReviewPostId()); // ✅ ERD 기준

        String uploadPath = "C:/tripbear/review/"; // ✅ 경로 수정

        File folder = new File(uploadPath);
        if (!folder.exists()) folder.mkdirs();

        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    File dest = new File(uploadPath, fileName);
                    file.transferTo(dest);

                    ReviewImageDTO imgDto = new ReviewImageDTO();
                    imgDto.setReviewPostId(dto.getReviewPostId()); // ✅ ERD 기준
                    imgDto.setReviewImageUrl(fileName);
                    reviewService.addImage(imgDto);
                }
            }
        }

        return "redirect:/review/view/" + dto.getReviewPostId(); // ✅ ERD 기준
    }


    // 게시글 삭제
    @GetMapping("/del/{reviewPostId}") // ✅ ERD 기준
    public String del(@PathVariable int reviewPostId) {
        reviewService.del(reviewPostId);
        return "redirect:/review/list";
    }
}
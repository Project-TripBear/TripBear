package com.project.trip.board.routepost.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.http.MediaType;
import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;
import com.project.trip.board.routepost.service.RoutePostService;
import com.project.trip.mypage.model.CustomUser;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/routepost")
public class RoutePostController {

    @Autowired
    private RoutePostService postService;

    // 게시글 목록
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue="1") int page, Model model) {

        int pageSize = 10; // 한 페이지당 게시글 수
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);

        List<RoutePostDTO> list = postService.list(map);
        model.addAttribute("list", list);
        model.addAttribute("currentPage", page);

        return "board.routepost.list";
    }


 // 게시글 상세보기
    @GetMapping("/view/{routepostId}")
    public String view(@PathVariable int routepostId,
                       Model model,
                       Authentication authentication) {

        // 기본 데이터
        postService.increaseViewCount(routepostId);
        RoutePostDTO post = postService.get(routepostId);
        List<RoutePostImageDTO> images = postService.getImages(routepostId);

        model.addAttribute("post", post);
        model.addAttribute("images", images);

        // 로그인 사용자 확인
        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {

            CustomUser user = (CustomUser) authentication.getPrincipal();

            System.out.println("✅ 로그인 사용자: " + user.getUdto().getSeq() + " / " + user.getUsername());

            model.addAttribute("userId", user.getUdto().getSeq());   // NUMBER (댓글 INSERT용)
            model.addAttribute("userName", user.getUsername());      // 문자열 (표시용)
        } else {
            model.addAttribute("userId", null);
            model.addAttribute("userName", null);
        }

        return "board.routepost.view";
    }



    // 게시글 작성 폼
    @GetMapping("/add")
    public String addForm() {
        return "board.routepost.add";
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String add(@ModelAttribute RoutePostDTO dto,
                      @RequestParam(value = "images", required = false) MultipartFile[] images,
                      HttpServletRequest req,
                      Authentication authentication) throws Exception {

        // 1) 로그인 유저 정보
        CustomUser user = (CustomUser) authentication.getPrincipal();
        long uid = Long.parseLong(user.getUdto().getSeq());
        dto.setUserId(uid);

        // 2) 게시글 DB insert
        postService.add(dto);

        // 3) 로컬 이미지 저장 경로
        String uploadPath = "C:/tripbear/routepost/";

        File folder = new File(uploadPath);
        if (!folder.exists()) folder.mkdirs();

        // 4) 이미지 저장 + DB 저장
        if (images != null && images.length > 0) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {

                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    File dest = new File(uploadPath, fileName);
                    file.transferTo(dest);

                    RoutePostImageDTO imgDto = new RoutePostImageDTO();
                    imgDto.setRoutepostId(dto.getRoutepostId());
                    imgDto.setRoutepostImageUrl(fileName);

                    postService.addImage(imgDto);
                }
            }
        }

        return "redirect:/routepost/list";
    }



    @GetMapping("/edit/{routepostId}")
    public String editForm(@PathVariable int routepostId,
                           Model model,
                           Authentication authentication) {

        // 게시글 정보
        RoutePostDTO dto = postService.get(routepostId);
        List<RoutePostImageDTO> images = postService.getImages(routepostId);

        model.addAttribute("dto", dto);
        model.addAttribute("imageList", images);

        // 로그인 정보 넘기기 (수정/삭제 버튼 제어용)
        if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getPrincipal())) {

            CustomUser user = (CustomUser) authentication.getPrincipal();

            model.addAttribute("userId", user.getUdto().getSeq());
            model.addAttribute("userName", user.getUsername());
        } else {
            model.addAttribute("userId", null);
            model.addAttribute("userName", null);
        }

        return "board.routepost.edit";
    }



    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String edit(@ModelAttribute RoutePostDTO dto,
                       @RequestParam(value = "images", required = false) MultipartFile[] images,
                       @RequestParam(value = "deleteImageIds", required = false) String deleteImageIds,
                       HttpServletRequest req) throws Exception {

        // 1) 게시글 기본 정보 업데이트
        postService.edit(dto);

        // 2) 삭제할 이미지 처리
        if (deleteImageIds != null && !deleteImageIds.trim().isEmpty()) {
            String[] ids = deleteImageIds.split(",");
            for (String id : ids) {
                postService.deleteImageById(Integer.parseInt(id));
            }
        }

        // 3) 새 이미지 업로드 처리
        String uploadPath = "C:/tripbear/routepost/";
        File folder = new File(uploadPath);
        if (!folder.exists()) folder.mkdirs();

        if (images != null) {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    File dest = new File(uploadPath, fileName);
                    file.transferTo(dest);

                    RoutePostImageDTO imgDto = new RoutePostImageDTO();
                    imgDto.setRoutepostId(dto.getRoutepostId());
                    imgDto.setRoutepostImageUrl(fileName);

                    postService.addImage(imgDto);
                }
            }
        }

        return "redirect:/routepost/view/" + dto.getRoutepostId();
    }




    // 게시글 삭제
    @GetMapping("/del/{routepostId}")
    public String del(@PathVariable int routepostId) {
        postService.del(routepostId);
        return "redirect:/routepost/list";
    }

}

package com.project.trip.board.routepost.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;
import com.project.trip.board.routepost.service.RoutePostService;

@Controller
@RequestMapping("/routepost")
public class RoutePostController {

    @Autowired
    @Qualifier("routePostService")
    private RoutePostService postService;

    // 게시글 목록
    @GetMapping("/list")
    public String list(Model model) {
        List<RoutePostDTO> list = postService.list();
        model.addAttribute("list", list);
        return "board/routepost/list"; // Tiles 기준: /WEB-INF/views/board/routepost/list.jsp
    }

    // 게시글 상세보기
    @GetMapping("/view/{routepostId}")
    public String view(@PathVariable String routepostId, Model model) {
        // 조회수 증가
        postService.increaseViewCount(routepostId);

        // 게시글 정보
        RoutePostDTO post = postService.get(routepostId);
        model.addAttribute("post", post);

        // 이미지 목록
        List<RoutePostImageDTO> images = postService.getImages(routepostId);
        model.addAttribute("images", images);

        return "board/routepost/view";
    }

    // 게시글 작성 폼
    @GetMapping("/add")
    public String addForm() {
        return "board/routepost/add";
    }

    // 게시글 등록 처리
    @PostMapping("/add")
    public String add(RoutePostDTO dto) {
        int result = postService.add(dto);
        return "redirect:/routepost/list";
    }

    // 게시글 수정 폼
    @GetMapping("/edit/{routepostId}")
    public String editForm(@PathVariable String routepostId, Model model) {
        RoutePostDTO dto = postService.get(routepostId);
        model.addAttribute("dto", dto);
        return "board/routepost/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/edit")
    public String edit(RoutePostDTO dto) {
        postService.edit(dto);
        return "redirect:/routepost/view/" + dto.getRoutepostId();
    }

    // 게시글 삭제
    @GetMapping("/del/{routepostId}")
    public String del(@PathVariable String routepostId) {
        postService.del(routepostId);
        return "redirect:/routepost/list";
    }

}

package com.project.trip.board.routepost.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

import com.project.trip.board.routepost.model.RoutePostDTO;
import com.project.trip.board.routepost.model.RoutePostImageDTO;
import com.project.trip.board.routepost.service.RoutePostService;

// 🔹 HotDeal 방식과 동일하게, 로그인 아이디로 회원 정보를 조회하기 위해 MemberMapper 사용
import com.project.trip.mypage.mapper.MemberMapper;
import com.project.trip.mypage.model.CustomUser;
import com.project.trip.mypage.model.UserDTO;

/**
 * 여행 경로 게시판(RoutePost)과 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * <p>
 * 게시글 목록 조회, 상세 보기, 작성, 수정, 삭제 기능을 제공하며,
 * 이미지 파일 업로드 및 Spring Security를 활용한 사용자 인증을 포함합니다.
 * </p>
 */
@Controller
@RequestMapping("/routepost")
public class RoutePostController {

    @Autowired
    private RoutePostService postService;

    // 🔹 로그인 아이디(username) -> 회원 DTO(seq 등) 조회용
    @Autowired
    private MemberMapper membermapper;

    /**
     * 여행 경로 게시글 목록을 조회하고 뷰에 전달합니다.
     *
     * @param page  현재 페이지 번호 (기본값: 1)
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return "board.routepost.list" 게시글 목록 뷰 이름
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {

        int pageSize = 10; // 한 페이지당 게시글 수
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("end", end);

        List<RoutePostDTO> list = postService.list(map);
        int totalCount = postService.totalCount(map);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        model.addAttribute("list", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "board.routepost.list";
    }

    /**
     * 여행 경로 게시글의 상세 내용을 조회합니다.
     * <p>
     * HotDeal 방식 적용: auth.getName()으로 로그인 아이디 조회 후
     * membermapper.get(username)을 통해 DB에서 사용자 정보를 안전하게 가져옵니다.
     * </p>
     * @param routepostId 조회할 게시글의 고유 번호
     * @param model       뷰에 데이터를 전달하기 위한 Model 객체
     * @param auth        Spring Security의 Authentication 객체
     * @return "board.routepost.view" 게시글 상세 뷰 이름
     */
    @GetMapping("/view/{routepostId}")
    public String view(@PathVariable int routepostId,
                       Model model,
                       Authentication auth) {

        // 1) 기본 데이터 조회 (기존 로직 유지)
        postService.increaseViewCount(routepostId);
        RoutePostDTO post = postService.get(routepostId);
        List<RoutePostImageDTO> images = postService.getImages(routepostId);

        model.addAttribute("post", post);
        model.addAttribute("images", images);

        // 2) 로그인 사용자 정보 (HotDeal과 동일한 방식)
        String loginUsername = null; // 로그인 아이디(문자열)
        Long loginUserSeq = null;    // DB의 사용자 식별자 (숫자)

        // 로그인 상태이고 익명 사용자가 아닐 때만 처리
        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {

            // (1) SecurityContext에서 username만 가져오기
            loginUsername = auth.getName(); // 관리자/일반 모두 공통

            // (2) username으로 DB에서 사용자 조회
            UserDTO userDto = membermapper.get(loginUsername);

            // (3) 조회 성공 시 seq를 Long으로 변환해서 JSP에서 사용 가능하게 제공
            if (userDto != null && userDto.getSeq() != null) {
                try {
                    loginUserSeq = Long.parseLong(userDto.getSeq());
                } catch (NumberFormatException ignore) {
                    // seq가 숫자가 아니라면 null로 두고 넘어갑니다.
                    loginUserSeq = null;
                }
            }
        }

        // 3) JSP에서 댓글/버튼 노출 등에 사용할 값 전달
        //    - userId: 숫자(seq) — INSERT/권한 체크 등에 사용
        //    - userName: 문자열(username) — 화면 표시용
        model.addAttribute("userId", loginUserSeq);
        model.addAttribute("userName", loginUsername);

        return "board.routepost.view";
    }

    /**
     * 게시글 작성 폼 페이지를 표시합니다.
     *
     * @return "board.routepost.add" 게시글 작성 폼 뷰 이름
     */
    @GetMapping("/add")
    public String addForm() {
        return "board.routepost.add";
    }

    /**
     * 게시글 등록 요청을 처리합니다 (파일 업로드 포함).
     * <p>
     * HotDeal 방식 적용: auth.getName() &rarr; membermapper.get(username) 으로 seq 조회.
     * </p>
     * @param dto    등록할 게시글 정보를 담은 {@link RoutePostDTO}
     * @param images 업로드할 이미지 파일 배열
     * @param req    HttpServletRequest 객체
     * @param auth   Spring Security의 Authentication 객체
     * @return "redirect:/routepost/list" 게시글 목록 페이지로 리다이렉트
     * @throws Exception 파일 업로드 또는 데이터베이스 처리 중 오류 발생 시
     */
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String add(@ModelAttribute RoutePostDTO dto,
                      @RequestParam(value = "images", required = false) MultipartFile[] images,
                      HttpServletRequest req,
                      Authentication auth) throws Exception {

        // 1) 로그인 체크 + username 확보
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            // 비로그인 상태면 글 등록이 불가하다면, 로그인 페이지/리스트 등으로 보냅니다.
            return "redirect:/login";
        }

        String loginUsername = auth.getName();
        UserDTO userDto = membermapper.get(loginUsername);
        if (userDto == null || userDto.getSeq() == null) {
            // 사용자 정보를 못 찾으면 방어적으로 처리
            return "redirect:/login";
        }

        // 2) RoutePostDTO 에 userId 세팅 (DB 스키마에 맞춰 Long/Number 사용)
        try {
            long uid = Long.parseLong(userDto.getSeq());
            dto.setUserId(uid);
        } catch (NumberFormatException e) {
            return "redirect:/login";
        }

        // 3) 게시글 DB insert
        postService.add(dto);

        // 4) 이미지 업로드 + DB 저장 (기존 로직 유지)
        String uploadPath = "C:/tripbear/routepost/";
        File folder = new File(uploadPath);
        if (!folder.exists()) folder.mkdirs();

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


    /**
     * 게시글 수정 폼 페이지를 표시합니다.
     *
     * @param routepostId 수정할 게시글의 고유 번호
     * @param model       뷰에 데이터를 전달하기 위한 Model 객체
     * @param authentication Spring Security의 Authentication 객체
     * @return "board.routepost.edit" 게시글 수정 폼 뷰 이름
     */
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
    /**
     * 게시글 수정 요청을 처리합니다.
     *
     * @param dto            수정할 게시글 정보를 담은 {@link RoutePostDTO}
     * @param images         새로 업로드할 이미지 파일 배열
     * @param deleteImageIds 삭제할 이미지 ID들을 콤마로 구분한 문자열
     * @param req            HttpServletRequest 객체
     * @return "redirect:/routepost/view/{routepostId}" 게시글 상세 페이지로 리다이렉트
     * @throws Exception 파일 업로드 및 처리 중 발생할 수 있는 예외
     */
    @PostMapping(value = "/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String edit(@ModelAttribute RoutePostDTO dto,
                       @RequestParam(value = "images", required = false) MultipartFile[] images,
                       @RequestParam(value = "deleteImageIds", required = false) String deleteImageIds,
                       HttpServletRequest req) throws Exception {

        // 1) 게시글 기본 정보 업데이트
        postService.edit(dto);

        // 기존 이미지 삭제 후 재등록 (기존 로직 유지)
        postService.delImages(dto.getRoutepostId());

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
    /**
     * 게시글 삭제 요청을 처리합니다.
     *
     * @param routepostId 삭제할 게시글의 고유 번호
     * @return "redirect:/routepost/list" 게시글 목록 페이지로 리다이렉트
     */
    @GetMapping("/del/{routepostId}")
    public String del(@PathVariable int routepostId) {
        postService.del(routepostId);
        return "redirect:/routepost/list";
    }
}
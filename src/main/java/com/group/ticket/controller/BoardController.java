package com.group.ticket.controller;

import com.group.ticket.DAO.*;
import com.group.ticket.domain.entity.Board;
import com.group.ticket.domain.entity.MemberEntity;
import com.group.ticket.domain.entity.research;
import com.group.ticket.service.BoardService;
import com.group.ticket.service.MemberService;
import com.group.ticket.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BoardController {
    private BoardService boardService;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private MemberService memberService;


    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }
    @GetMapping("/")
    public String main(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
        }

        return "/main.html";
    }

    @GetMapping("/list")
    public String list(Model model){
        List<BoardDAO> boradDAOList = boardService.getBoardList();
        model.addAttribute("postList", boradDAOList);
        return "/list.html";
    }
    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword,
                         @RequestParam(value = "type", required = false) String type,
                         Model model, @SessionAttribute(name = "userId", required = false) Long userId) {

        if(type != null){
            // 쿼리할 때 TYPE을 WHERE
        }
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
        }

        List<ResearchDAO> researchResults = boardService.searchResearchByKeyword(keyword);
        researchResults.addAll(boardService.searchResearchByImagePath(keyword));

        model.addAttribute("postList", researchResults);
        return "/search-results.html"; // 검색 결과를 나타낼 HTML 페이지의 이름
}
    @GetMapping("/cseat/select")
    public String showSeatSelectionPage(Model model, @SessionAttribute(name = "userId", required = false) Long userId, HttpServletRequest request,
                                        @RequestParam(name = "sid", required = false) Long sid) {
        HttpSession session = request.getSession();

        if (userId == null) {
            return "redirect:/members/login";
        }
        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if (loginUser != null) {
            model.addAttribute("loginType", "session-login");
            model.addAttribute("pageName", "세션 로그인");
            model.addAttribute("email", loginUser.getMemberEmail());

            List<String> reservedSeats = reservationService.getReservedSeatsBySid(sid);
            model.addAttribute("reservedSeats", reservedSeats);
        }
        return "/booking/CSeatSelect.html";
    }
    @GetMapping("/post")
    public String post() {
        return "/post.html";
    }
    @PostMapping("/post")
    public String write(BoardDAO boardDAO) {
        boardService.savePost(boardDAO);
        return "redirect:/list";
    }
//    @GetMapping("/post/{id}")
//    public String detail(@PathVariable("id") Long id, Model model) {
//        BoardDAO boardDAO = boardService.getPost(id);
//        model.addAttribute("post", boardDAO);
//        return "/detail.html";
//    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model,@SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
        }

        ResearchDAO researchDAO = boardService.getResearch(id);
        model.addAttribute("post", researchDAO);
        return "/booking/CInformation.html";
    }

//    // 멤버 컨트롤러
//
//    @GetMapping("/members/new")
//    public String createFrom(Model model) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//        model.addAttribute("joinRequest", new JoinRequest());
//        return "/members/createAccount";
//    }
//    @PostMapping("/members/new")
//    public String create(@ModelAttribute JoinRequest joinRequest, Model model, BindingResult bindingResult){
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//        // Email 중복 체크
//        if(memberService.checkMemberEmailDuplicate(joinRequest.getMemberEmail())) {
//            bindingResult.addError(new FieldError("joinRequest", "MemberEmail", "이메일이 중복됩니다."));
//        }
//
//        // password와 passwordCheck가 같은지 체크
//        if(!joinRequest.getMemberPassword().equals(joinRequest.getMemberPasswordCheck())) {
//            bindingResult.addError(new FieldError("joinRequest", "MemberPasswordCheck", "비밀번호가 일치하지 않습니다."));
//        }
//
//        if(bindingResult.hasErrors()) {
//            return "/members/createAccount";
//        }
//
//        memberService.join(joinRequest);
//        return "redirect:/members/login";
//    }
//
//
///*    @GetMapping("/members")
//    public String list(Model model) {
//        List<Member> members = memberService.findMembers();
//        model.addAttribute("members", members);
//        return  "members/memberList";
//    }*/
//
//    @GetMapping("/members/login")
//    public String loginForm(Model model) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//        model.addAttribute("LoginRequest", new LoginRequest());
//
//        return "/members/login";
//    }
//
//    @PostMapping("/members/login")
//    public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model, BindingResult bindingResult,
//                        HttpServletRequest httpServletRequest) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//        model.addAttribute("LoginRequest", new LoginRequest());
//
//        MemberEntity memberEntity = memberService.login(loginRequest);
//        if (memberEntity == null){
//            // 로그인 실패
//            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
//        }if (bindingResult.hasErrors()) {
//            System.out.println(bindingResult.getAllErrors());
//            // 로그인 실패
//            return "/members/login";
//        }
//        // 로그인 성공 => 세션 성공
//        httpServletRequest.getSession(true);  // Session이 없으면 생성
//        // 세션에 userId를 넣어줌
//        assert memberEntity != null;
//        session.setAttribute("userId", memberEntity.getMno());
//        session.setMaxInactiveInterval(1800); // Session이 30분동안 유지
//
//
//
//        return "redirect:/";
//    }
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, Model model) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//        HttpSession session = request.getSession(false);  // Session이 없으면 null return
//        if(session != null) {
//            session.invalidate();
//        }
//        return "redirect:/members/login";
//    }
//
//    @GetMapping("/mypage")
//    public String mypage(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
//        if (userId == null) {
//            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
//            return "redirect:/login";
//        }
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//
//        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
//
//        if(loginUser != null) {
//            model.addAttribute("name", loginUser.getMemberName());
//            model.addAttribute("email", loginUser.getMemberEmail());
//            model.addAttribute("role", loginUser.getRole());
//        }
//        return "/mypage";
//    }
//    @GetMapping("/changePassword")
//    public String changePassword(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
//        if (userId == null) {
//            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
//            return "redirect:/login";
//        }
//
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
//
//        return "changePassword";
//    }
//    @PostMapping("/changePassword")
//    public String changePasswordSubmit(@ModelAttribute ChangePasswordRequest changePasswordRequest, Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
//        if (userId == null) {
//            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
//            return "redirect:/login";
//        }
//
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//        boolean isChanged = memberService.changePassword(changePasswordRequest);
//
//        if (isChanged) {
//            model.addAttribute("message", "비밀번호가 변경되었습니다.");
//            return "passwordChangeSuccess";
//        } else {
//            model.addAttribute("message", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
//            return "changePassword";
//        }
//    }
//    @GetMapping("/recentBooking")
//    public String recentBooking(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
//        model.addAttribute("loginType", "session-login");
//        model.addAttribute("pageName", "세션 로그인");
//
//
//        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
//
//        if(loginUser != null) {
//            model.addAttribute("name", loginUser.getMemberName());
//            model.addAttribute("email", loginUser.getMemberEmail());
//            model.addAttribute("role", loginUser.getRole());
//        }
//        return "/recentBooking";
//    }

}

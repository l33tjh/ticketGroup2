package com.group.ticket.controller;
import com.group.ticket.DAO.ChangePasswordRequest;
import com.group.ticket.DAO.JoinRequest;
import com.group.ticket.DAO.LoginRequest;
import com.group.ticket.domain.entity.MemberEntity;
import com.group.ticket.domain.entity.Reservation;
import com.group.ticket.service.MemberService;
import com.group.ticket.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ReservationService ticketService;

    @GetMapping("/members/new")
    public String createFrom(Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("joinRequest", new JoinRequest());

        return "/members/createAccount";
    }
    @PostMapping("/members/new")
    public String create(@ModelAttribute JoinRequest joinRequest, Model model, BindingResult bindingResult){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        // Email 중복 체크
        if(memberService.checkMemberEmailDuplicate(joinRequest.getMemberEmail())) {
            bindingResult.addError(new FieldError("joinRequest", "MemberEmail", "이메일이 중복됩니다."));
        }

        // password와 passwordCheck가 같은지 체크
        if(!joinRequest.getMemberPassword().equals(joinRequest.getMemberPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "MemberPasswordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()) {
            return "/members/createAccount";
        }

        memberService.join(joinRequest);
        return "redirect:/members/login";
    }


    @GetMapping("/members/login")
    public String loginForm(Model model) {

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("LoginRequest", new LoginRequest());

        return "/members/login";
    }

    @PostMapping("/members/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model, BindingResult bindingResult,
                        HttpServletRequest httpServletRequest) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("LoginRequest", new LoginRequest());

        MemberEntity memberEntity = memberService.login(loginRequest);
        if (memberEntity == null){
            // 로그인 실패
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            // 로그인 실패
            return "/members/login";
        }
        // 로그인 성공 => 세션 성공
        httpServletRequest.getSession(true);  // Session이 없으면 생성
        // 세션에 userId를 넣어줌
        assert memberEntity != null;
        session.setAttribute("userId", memberEntity.getMno());
        session.setMaxInactiveInterval(1800); // Session이 30분동안 유지



        return "redirect:/";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        HttpSession session = request.getSession(false);  // Session이 없으면 null return
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/members/login";
    }

    @GetMapping("/members/mypage")
    public String mypage(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        if (userId == null) {
            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
            return "redirect:/members/login";
        }
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");


        MemberEntity loginUser = memberService.getLoginUserByMno(userId);

        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
            model.addAttribute("email", loginUser.getMemberEmail());
            model.addAttribute("role", loginUser.getRole());
        }
        return "/members/mypage";
    }
    @GetMapping("/members/changePassword")
    public String changePassword(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        if (userId == null) {
            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
            return "redirect:/members/login";
        }

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        model.addAttribute("joinRequest", new JoinRequest());
        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
        }

        return "/members/changePassword";
    }
    @PostMapping("/members/changePassword")
    public String changePasswordSubmit(@ModelAttribute ChangePasswordRequest changePasswordRequest, Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        if (userId == null) {
            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
            return "redirect:/members/login";
        }

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
        }

        boolean isChanged = memberService.changePassword(changePasswordRequest, loginUser);

        if (isChanged) {
            model.addAttribute("message", "비밀번호가 변경되었습니다.");
            return "/main.html";
        } else {
            model.addAttribute("message", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
            return "/members/changePassword";
        }
    }
    @GetMapping("/booking/recentBooking")
    public String recentBooking(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginUser = memberService.getLoginUserByMno(userId);
        if (userId != null) {
            // 사용자 ID를 사용하여 티켓 목록을 가져옴
            List<Reservation> tickets = ticketService.getTicketsByEmail(memberService.getEmailByMno(userId));
            model.addAttribute("tickets", tickets);
        } else {
            // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리디렉션
            return "redirect:/members/login";
        }
        if(loginUser != null) {
            model.addAttribute("name", loginUser.getMemberName());
            model.addAttribute("email", loginUser.getMemberEmail());
            model.addAttribute("role", loginUser.getRole());
        }
        return "/booking/recentBooking";
    }

}

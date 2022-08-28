package kr.inhatc.spring.member.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.inhatc.spring.member.dto.MemberFormDto;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 등록 페이지로 이동
     * @param model
     * @return
     */
    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    /**
     * 회원 정보 등록하기
     * @param memberFormDto
     * @return
     */
    @PostMapping(value = "/new")
    public String memberForm(MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try{
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 패스워드를 확인해주세요.");
        return "/member/memberLoginForm";
    }
}

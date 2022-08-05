package kr.inhatc.spring.temp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafController {

    @GetMapping(value = "/ex01")
    public String ex01(Model model){
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleaf/ex01";
    }


}

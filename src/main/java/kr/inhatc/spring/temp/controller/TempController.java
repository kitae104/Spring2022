package kr.inhatc.spring.temp.controller;

import kr.inhatc.spring.temp.dto.TempDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // @Controller + @ResponseBody
public class TempController {

    //@GetMapping(value = "/")
    public String HelloWorld(){
        return "Hello World";
    }

    @GetMapping(value = "/test")
    public TempDto test(){
        TempDto tempDto = new TempDto();
        tempDto.setAge(20);
        tempDto.setName("kim");

        return tempDto;
    }

    // 반응 안함.. 컨트롤러 변경 필요!
//    @GetMapping(value = "/ex01")
//    public String ex01(Model model){
//        model.addAttribute("data", "타임리프 예제 입니다.");
//        return "temp/ex01";
//    }

}

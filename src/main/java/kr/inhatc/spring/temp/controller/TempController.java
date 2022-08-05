package kr.inhatc.spring.temp.controller;

import kr.inhatc.spring.temp.dto.TempDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // @Controller + @ResponseBody
public class TempController {

    @GetMapping(value = "/")
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
}

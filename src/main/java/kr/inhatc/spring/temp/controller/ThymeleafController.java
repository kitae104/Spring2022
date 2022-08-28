package kr.inhatc.spring.temp.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.inhatc.spring.shop.item.dto.ItemDto;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafController {

    @GetMapping(value = "/ex01")
    public String ex01(Model model){
        model.addAttribute("data", "타임리프 예제 입니다.");
        return "thymeleaf/ex01";
    }

    @GetMapping(value = "/ex02")
    public String ex02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);

        return "thymeleaf/ex02";
    }

    @GetMapping(value = "/ex03")
    public String ex03(Model model){
        List<ItemDto> itemDtoList = getItemDtoList();

        model.addAttribute("itemDtoList", itemDtoList);

        return "thymeleaf/ex03";
    }

    private static List<ItemDto> getItemDtoList() {
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    @GetMapping(value = "/ex04")
    public String ex04(Model model) {
        List<ItemDto> itemDtoList = getItemDtoList();

        model.addAttribute("itemDtoList", itemDtoList);

        return "thymeleaf/ex04";
    }

    @GetMapping(value = {"/ex05", "/ex07"})
    public void ex05() {
    }

    @GetMapping(value = "/ex06")
    public void ex06(String param1, String param2, Model model) {
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
    }
}

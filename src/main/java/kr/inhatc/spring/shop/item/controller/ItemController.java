package kr.inhatc.spring.shop.item.controller;

import kr.inhatc.spring.shop.item.dto.ItemDto;
import kr.inhatc.spring.shop.item.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class ItemController {

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());   // 빈 폼 Dto 정보를 전달.
        return "item/itemForm";
    }
}

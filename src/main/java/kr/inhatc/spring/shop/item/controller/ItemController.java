package kr.inhatc.spring.shop.item.controller;

import kr.inhatc.spring.shop.item.dto.ItemDto;
import kr.inhatc.spring.shop.item.dto.ItemFormDto;
import kr.inhatc.spring.shop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());   // 빈 폼 Dto 정보를 전달.
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        log.info("================ 상품 등록 페이지 이동 ");

        if(bindingResult.hasErrors()){  // 상품 등록 시 필수 값이 없는 경우
            return "item/itemForm";     // 오류시 상품 등록 페이지로 돌아감
        }

        // 상품 등록시 첫번째 이미지(필수값)가 없으면 에러 메시지 출력
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";     // 오류시 상품 등록 페이지로 돌아감
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);     // 상품 저장
        } catch(Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";     // 오류시 상품 등록 페이지로 돌아감
        }

        return "redirect:/";    // 정상 등록시 메인 페이지로 이동
    }
}

package kr.inhatc.spring.shop.item.controller;

import kr.inhatc.spring.shop.item.dto.ItemDto;
import kr.inhatc.spring.shop.item.dto.ItemFormDto;
import kr.inhatc.spring.shop.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
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

    /**
     * 상품 조회 하기<br>
     * @param itemId
     * @param model
     * @return
     */
    @GetMapping(value = "/admin/item/{itemId}")     //DB에서 해당 상품 번호에 대한 정보를 먼저 확인한 후 수행
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDetail(itemId);	// 상품 상세 정보 조회
            log.info("===========> " + itemFormDto);
            model.addAttribute("itemFormDto", itemFormDto);         		// 뷰로 조회된 정보 전달
        } catch(EntityNotFoundException e){
            // 조회된 정보가 없는 경우 에러 메시지와 빈 form 형태로 전달
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }
    
    @PostMapping(value = "/admin/item/{itemId}") 
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, 
    						@RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
    	
    	if(bindingResult.hasErrors()) {
    		return "item/itemForm";
    	}
    	
    	if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
    		model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
    		return "item/itemForm"; 		
    	}

    	try
		{
			itemService.updateItem(itemFormDto, itemImgFileList);
		} 
    	catch (Exception e)
		{
			model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
			return "item/itemForm";
		}    	
    	
    	return "redirect:/";
    }
    
    
}

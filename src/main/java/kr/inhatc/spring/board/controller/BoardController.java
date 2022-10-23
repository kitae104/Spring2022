package kr.inhatc.spring.board.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.repository.CustomBoardRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/board")
public class BoardController {
  
  private final CustomBoardRepository customBoardRepository;

  /**
   * 검색어에 따른 리스트 보여주기  
   * @param searchVal
   * @param pageable
   * @param model
   * @return
   */
  @GetMapping("/list")
  public String list(String searchVal, Pageable pageable, Model model){
    
    Page<BoardDto> results = customBoardRepository.selectBoardList(searchVal, pageable);
    
    model.addAttribute("list", results);
    model.addAttribute("maxPage", 5);
    model.addAttribute("searchVal", searchVal);

    pageModelPut(results, model);
    
    return "board/list"; 
  }

  /**
   * 페이지 관련 정보 처리 
   * @param results
   * @param model
   */
  private void pageModelPut(Page<BoardDto> results, Model model) {
    model.addAttribute("totalCount", results.getTotalElements());           // 전체 갯수 
    model.addAttribute("size",  results.getPageable().getPageSize());       // 한페이지 크기 
    model.addAttribute("number",  results.getPageable().getPageNumber());   // 현재 페이지 번호 
  }

  @GetMapping("/write")
  public String write(){
      return "board/write"; 
  }

  @GetMapping("/update")
  public String update(){
      return "board/update";
  }
}

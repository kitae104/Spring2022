package kr.inhatc.spring.board.controller;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import kr.inhatc.spring.board.dto.BoardDeleteDto;
import kr.inhatc.spring.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.repository.CustomBoardRepository;
import kr.inhatc.spring.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "/board")
public class BoardController {
  
  private final CustomBoardRepository customBoardRepository;

  private final BoardService boardService; 
  
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
    model.addAttribute("number", results.getPageable().getPageNumber());   // 현재 페이지 번호
  }

  @GetMapping("/write") 
  public String write(Model model){
    model.addAttribute("boardDto", new BoardDto()); 
    return "board/write"; 
  }
   
  @PostMapping("/write")
  public String save(@Valid BoardDto boardDto, BindingResult result) {
    
    log.info("=======================> " + boardDto);
    
    //유효성검사 걸릴시 
    if(result.hasErrors()){
        return "board/write";
    }
    
    boardService.saveBoard(boardDto);
    return "redirect:/board/list";
  }

  @GetMapping("/update/{boardId}")
  public String detail(@PathVariable Long boardId, Model model){
    Board board = boardService.selectBoardDetail(boardId);
    BoardDto boardDto = new BoardDto();
    boardDto.setId(boardId);
    boardDto.setUsername(board.getMember().getName());
    boardDto.setTitle(board.getTitle());
    boardDto.setContent(board.getContent());
    model.addAttribute("boardDto", boardDto);

    return "board/update";
  }

  @PostMapping("/update/{boardId}")
  public String update(@Valid BoardDto boardDto, BindingResult result){

    log.info("=======================> " + boardDto);

    //유효성검사 걸릴시
    if(result.hasErrors()){
      return "board/update";
    }

    boardService.saveBoard(boardDto);
    return "redirect:/board/list";
  }

  /**
   * 게시판  
   * @param
   * @return
   */
  @ResponseBody
  @PostMapping("/delete")   
  public String delete(@RequestBody BoardDeleteDto dto) {
    
    log.info("=======================> 삭제할 항목 수 : " + dto);
    List<Long> idList = dto.getIdList();
    for (Long id : idList) {
      boardService.deleteBoard(id);
    }

    return "redirect:/board/list";
  }
}

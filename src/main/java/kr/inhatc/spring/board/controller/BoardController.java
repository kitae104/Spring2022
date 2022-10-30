package kr.inhatc.spring.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.inhatc.spring.board.dto.BoardDeleteDto;
import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.dto.BoardFileDto;
import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.board.repository.CustomBoardRepository;
import kr.inhatc.spring.board.service.BoardFileService;
import kr.inhatc.spring.board.service.BoardService;
import kr.inhatc.spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "/board")
public class BoardController {
  
  private final CustomBoardRepository customBoardRepository;

  private final BoardService boardService;

  private final BoardFileService fileService;
  
  private final MemberRepository memberRepository;
  
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
  public String write(Model model, @AuthenticationPrincipal User user){
    // 사용자 이름 가져오기 
    log.info(">>>> User : " + user.getUsername());
    String username = memberRepository.findByEmail(user.getUsername()).getName();
    model.addAttribute("username", username);
    model.addAttribute("boardDto", new BoardDto()); 
    return "board/write"; 
  }
   
  @PostMapping("/write")
  public String save(@Valid BoardDto boardDto, BindingResult result) {
    
    log.info(">>>>> write(boardDto) : " + boardDto);
    
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
    log.info(">>>>>>>>>>>>>>>>>>>> 업데이트 board : " + board.getMember().getName());
	
	BoardDto boardDto = BoardDto.builder()
            .id(boardId)
            .title(board.getTitle())
            .content(board.getContent())    
            .username(board.getMember().getName()) 
            .build();
    
	log.info(">>>>>>>>>>>>>>>>>>>> 업데이트 boardDto : " + boardDto);
    model.addAttribute("boardDto", boardDto); 

    List<BoardFileDto> boardFiles = customBoardRepository.selectBoardFileDetail(boardId);
    log.info("========> 불러온 파일 갯수 : " + boardFiles.size());

    model.addAttribute("boardFiles", boardFiles);
    return "board/update";
  }

  @PutMapping("/update/{boardId}")
  public String update(@Valid BoardDto boardDto, BindingResult result){

    log.info("========> 게시판 업데이트 : " + boardDto);

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
  @PostMapping("/delete")   
  public String delete(@RequestParam List<String> boardIds) {
    
    log.info(">>>> 삭제 리스트 갯수 : " + boardIds.size());
    
    for(int i=0; i<boardIds.size(); i++){
      Long id = Long.valueOf(boardIds.get(i));
      boardService.deleteBoard(id);
    }

    return "redirect:/board/list"; 
  }

  @PostMapping("/fileDelete")
  public String boardFileDelete(@RequestParam Long fileId, @RequestParam Long boardId){
    log.info(">>>> 파일 삭제 : " );
    //게시판 파일삭제
    fileService.deleteBoardFile(fileId);

    return "redirect:/board/update/"+boardId;
  }
}

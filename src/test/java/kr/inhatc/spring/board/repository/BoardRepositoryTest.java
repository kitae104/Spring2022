package kr.inhatc.spring.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;

@SpringBootTest
class BoardRepositoryTest {

  @Autowired
  BoardRepository boardRepository;

  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName(value = "게시판_등록")
  public void insertTest(){

      //등록된글 다지우기
      boardRepository.deleteAll();

      //given
      String title = "타이틀1";
      String content = "내용1";

      List<Member> memberList = memberRepository.findAll();
      Member member = memberList.get(0);

      BoardDto boardDto = new BoardDto(title, content);
      Board board = boardDto.toEntity(member);
      boardRepository.save(board);

      //when
      List<Board> boardList = boardRepository.findAll();

      //then
      Board boards = boardList.get(0);
      assertThat(boards.getTitle()).isEqualTo(title);
      assertThat(boards.getContent()).isEqualTo(content);
  }
  
  @Test
  @DisplayName(value = "게시판_수정")
  public void updateTest(){

      //given
      List<Board> boardList = boardRepository.findAll();
      Board boards = boardList.get(0);
      String title = "변경된 타이틀";
      String content = "변경된 내용";

      boards.update(title, content);
      boardRepository.save(boards);

      //when
      List<Board> boardList2 = boardRepository.findAll();

      //then
      Board boards2 = boardList2.get(0);
      assertThat(boards2.getTitle()).isEqualTo(title);
      assertThat(boards2.getContent()).isEqualTo(content);
  }
}

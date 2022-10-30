package kr.inhatc.spring.board.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.board.service.BoardService;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.shop.constant.ItemSellStatus;
import kr.inhatc.spring.shop.item.entity.Item;

@SpringBootTest
class BoardControllerTest {

  @Autowired
  private BoardService boardService;
  
  
}

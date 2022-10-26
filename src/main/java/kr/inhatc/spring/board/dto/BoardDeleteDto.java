package kr.inhatc.spring.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class BoardDeleteDto {
    List<Long> idList;
}

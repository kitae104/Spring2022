package kr.inhatc.spring.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}

package kr.inhatc.spring.board.repository;

import kr.inhatc.spring.board.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}

package kr.inhatc.spring.board.repository;

import kr.inhatc.spring.board.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long>
{

}

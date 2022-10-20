package kr.inhatc.spring.excel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.excel.entity.ExcelTutorial;

/**
 * 엑셀 레포지토리<br>
 * save(), findOne(), findById(), findAll(), count(), delete(), deleteById()…  사용 가능 
 * @author 김기태
 *
 */
public interface ExcelTutorialRepository extends JpaRepository<ExcelTutorial, Long>
{

}

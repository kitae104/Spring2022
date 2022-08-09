package kr.inhatc.spring.member.repository;

import kr.inhatc.spring.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByEmail(String email);    // 회원 중복 검사를 위해 사용
}

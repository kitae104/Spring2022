package kr.inhatc.spring.member.service;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;    // 빈 주입, @Autowired와 동일

    /**
     * 중복 확인 후 사용자를 저장하기
     * @param member
     * @return
     */
    public Member saveMember(Member member){
        validateDuplicateMember(member);                // 중복 여부 확인
        return memberRepository.save(member);           // 사용자 등록
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());    // 추후에 Optional로 처리
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}

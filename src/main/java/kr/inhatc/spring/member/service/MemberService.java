package kr.inhatc.spring.member.service;

import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

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

    /**
     * 로그인 사용자 확인
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        System.out.println("===========>" + member.getEmail());

        if(member == null){
            System.out.println("1111");
            throw new UsernameNotFoundException(email); // 해당 사용자가 없는 경우
        }

        System.out.println("===========>" + member.getEmail() + ", " + member.getRole().toString() );
        return User.builder()                       // User 객체 생성하기
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}

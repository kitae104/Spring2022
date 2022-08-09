package kr.inhatc.spring.member.entity;

import kr.inhatc.spring.constant.Role;
import kr.inhatc.spring.member.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

/**
 * 회원 정보 관리
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique=true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)  // 문자열 형태로 사용
    private Role role;

    /**
     * 회원을 생성하는 메소드<br>
     * 코드 변경시 한 곳만 수정하면 되는 장점을 가짐
     * @param memberFormDto
     * @param passwordEncoder
     * @return
     */
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());  // 비밀 번호 암호화
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}

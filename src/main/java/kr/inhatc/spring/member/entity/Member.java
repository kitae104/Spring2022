package kr.inhatc.spring.member.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.member.constant.Role;
import kr.inhatc.spring.member.dto.MemberFormDto;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 회원 정보 관리
 */
@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

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

    @OneToMany(mappedBy = "member") // 연관관계 설정 - 양방향 
    private List<Board> boards = new ArrayList<>();
    
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

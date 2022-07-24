package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class MemberDto {

    public MemberDto(Member member) {
        this.id = member.getId();
        this.age = member.getAge();
        this.username = member.getUsername();
    }

    private Long id;
    private Long age;
    private String username;

}

package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

    /*
    * 회원가입
    * */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        repository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = repository.findByName(member.getName());
        if (!findMember.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /*
    * 전체 회원 조회
    * */
    public List<Member> findMembers() {
        return repository.findAll();
    }

    public Member findMember(Long memberId) {
        return repository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member findMember = repository.findOne(id);
        findMember.setName(name); //변경 감지로 인한 엔티티 수정
    }
}

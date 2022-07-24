package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.DataJpaApplication;
import study.datajpa.entity.Member;

import java.util.List;

@Transactional
@SpringBootTest(classes = DataJpaApplication.class)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    public void paging_test() {
        memberRepository.save(new Member("memberH", 15L));
        memberRepository.save(new Member("memberE", 20L));
        memberRepository.save(new Member("memberA", 26L));
        memberRepository.save(new Member("memberB", 26L));
        memberRepository.save(new Member("memberC", 26L));
        memberRepository.save(new Member("memberD", 26L));
        memberRepository.save(new Member("memberF", 26L));
        memberRepository.save(new Member("memberG", 26L));
        memberRepository.save(new Member("member1", 26L));
        memberRepository.save(new Member("member2", 26L));
        memberRepository.save(new Member("member3", 26L));
        memberRepository.save(new Member("member4", 26L));
        memberRepository.save(new Member("member5", 26L));
        memberRepository.save(new Member("member6", 26L));
        memberRepository.save(new Member("member7", 26L));
        memberRepository.save(new Member("member8", 26L));
        memberRepository.save(new Member("member9", 26L));
        memberRepository.save(new Member("member10", 26L));
        memberRepository.save(new Member("member11", 26L));
        memberRepository.save(new Member("member12", 26L));
        memberRepository.save(new Member("member13", 26L));
        memberRepository.save(new Member("member14", 26L));

        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(Sort.Direction.ASC, "username"));
        Page<Member> pages = memberRepository.findByAge(26L, pageRequest);

        List<Member> members = pages.getContent();
        long totalElements = pages.getTotalElements();
        int number = pages.getNumber();

        pages.isFirst();
        pages.hasNext();
        pages.hasPrevious();
        pages.isLast();

        for (Member member : members) {
            System.out.println(member.getUsername());
        }
        System.out.println("total pages num : " + totalElements);
        System.out.println("current page : " + number);
    }

}
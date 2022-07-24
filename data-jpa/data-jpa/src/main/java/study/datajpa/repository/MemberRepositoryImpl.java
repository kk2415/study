package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom(Long age) {
        return em.createQuery("select m from Member m where m.age = :age", Member.class)
                .setParameter("age", age)
                .getResultList();
    }
}


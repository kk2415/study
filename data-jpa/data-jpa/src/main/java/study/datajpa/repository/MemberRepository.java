package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAge(String username, Long age);

    Optional<Member> findByUsername(String username);

    // @Query 애노테이션으로 쿼리문 작성
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") Long age);


    // 컬렉션을 파라미터로 받음
    @Query("select m from Member m where m.username in :usernames")
    List<Member> findByUsername(@Param("usernames") List<String> usernames);


    //페이징
    @Query(value = "select m from Member m left join m.team",
            countQuery = "select count(m) from Member m") //카운트 쿼리는 쿼리가 복잡해지면 성능에 문제가 생기니 이런 식으로 따로 쿼리 작성
    Page<Member> findByAge(Long age, Pageable pageable);
    /**
     * PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
     */


    //벌크성 수정 쿼리
    @Modifying(clearAutomatically = true) //em.clear() 작업을 자동으로 해줌
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") Long age);
}

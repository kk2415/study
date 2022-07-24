package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
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
    @Modifying(clearAutomatically = true) //em.clear() 작업을 자동으로 해줌, 실제 DB랑 영속성 컨텍스트랑 동기화
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age") //변경감지가 아니라 바로 DB 내용을 업데이트
    int bulkAgePlus(@Param("age") Long age);


    //멤버에 연관된 객체에 프록시가 아닌 fetch join으로 한 번에 다 끌고와서 그 데이터를 이용하여 실제 객체를 넣고 싶을 때
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"}) //JPQL에 fetch join추가하고 싶을 때
    @Query("select m from Member m")
    List<Member> findMemberFetchJoin();


    /**
     * 영속성 컨텍스트의 스냅샷을 안만들어서 변경감지가 불가능하게 만듬
     * */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);


}

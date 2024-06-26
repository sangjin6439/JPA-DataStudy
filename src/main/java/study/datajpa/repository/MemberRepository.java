package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import study.datajpa.domain.Member;
import study.datajpa.dto.MemberDto;
import study.datajpa.dto.UsernameOnlyDto;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // limit 쿼리
    List<Member> findTop3By();

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username FROM Member m")
    List<String> findUsernameList();

    //DTO를 조회할 때에는 new를 사용해야
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션

    Member findMemberByUsername(String username);//단건

    Optional<Member> findOptionalByUsername(String username);//단건

    //카운트 쿼리를 풀어서 사용할 수 있다. 대부분 카운트 쿼리에서 성능 차이가 남. 데이터가 많으면 이런식으로 따로 분리해서 구현하기
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    /* 벌크 연산시 주의 사항!!
    executeUpdate를 호출함 -> 변경할  때 사용, bulk쿼리에서는 영속성컨텍스트와 DB의 값이 달라 원하는 결과가 안나온다(영속성 컨텍스트의 결과가 나옴).
     이럴때는 em.clear로 영컨의 값을 다 날려도 좋고 (clearAutomatically = true)로 자동으로 날려도 좋다.
    */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age>=:age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team"))
//    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    //읽기 전용일 때에만 사용(실무에서 거의 사용 X, 스냅샷을 저장하지 않게해서 성능은 올라가지만 그렇게 영향 X)
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String name);

//    <T>List<T> findProjectionsByUsername(@Param("username") String username,Class<T> type);

    @Query(value = "select username from member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);
}

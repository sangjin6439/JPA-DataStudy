package study.datajpa.repository;

import java.util.List;

import study.datajpa.domain.Member;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();

}

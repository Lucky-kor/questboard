package com.questboard.member.service;

import com.questboard.member.entity.Member;
import com.questboard.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
  private final MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  public Member createMember(Member member) {
    verifyExistsEmail(member.getEmail());

    // 패스워드 암호화 필요
    // Member Role 저장 필요.
    Member savedMember = memberRepository.save(member);

    return savedMember;
  }

  @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
  public Member updateMember(Member member) {
    Member findMember = findVerifiedMember(member.getMemberId());

    Optional.ofNullable(member.getName())
        .ifPresent(findMember::setName);
    Optional.ofNullable(member.getPhone())
        .ifPresent(findMember::setPhone);
    Optional.ofNullable(member.getMemberStatus())
        .ifPresent(findMember::setMemberStatus);

    return memberRepository.save(findMember);
  }

  @Transactional(readOnly = true)
  public Member findMember(long memberId) {
    return findVerifiedMember(memberId);
  }

  public Page<Member> findMembers(int page, int size) {
    return memberRepository.findAll(PageRequest.of(page, size,
        Sort.by("memberId").descending()));
  }

  public void deleteMember(long memberId) {
    Member findMember = findVerifiedMember(memberId);

    findMember.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);
    memberRepository.save(findMember);
  }

  @Transactional(readOnly = true)
  public Member findVerifiedMember(long memberId) {
    Optional<Member> optionalMember =
        memberRepository.findById(memberId);
    Member findMember =
        optionalMember.orElseThrow(() ->
            // 예외처리 추가 필요
            new RuntimeException());
    return findMember;
  }

  private void verifyExistsEmail(String email) {
    Optional<Member> member = memberRepository.findByEmail(email);
    if (member.isPresent())
      // 예외처리 추가 핸들링 필요
      throw new RuntimeException();
  }
}

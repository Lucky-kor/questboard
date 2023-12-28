package com.questboard.member.controller;

import com.questboard.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
@Validated
@Slf4j
public class MemberController {
  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @PostMapping
  public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) {
    Member member = mapper.memberPostToMember(requestBody);
    member.setStamp(new Stamp()); // homework solution 추가

    Member createdMember = memberService.createMember(member);
    URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, createdMember.getMemberId());

    return ResponseEntity.created(location).build();
  }

  @PatchMapping("/{member-id}")
  public ResponseEntity patchMember(
      @PathVariable("member-id") @Positive long memberId,
      @Valid @RequestBody MemberDto.Patch requestBody) {
    requestBody.setMemberId(memberId);

    Member member =
        memberService.updateMember(mapper.memberPatchToMember(requestBody));

    return new ResponseEntity<>(
        new SingleResponseDto<>(mapper.memberToMemberResponse(member)),
        HttpStatus.OK);
  }

  @GetMapping("/{member-id}")
  public ResponseEntity getMember(
      @PathVariable("member-id") @Positive long memberId) {
    Member member = memberService.findMember(memberId);
    return new ResponseEntity<>(
        new SingleResponseDto<>(mapper.memberToMemberResponse(member))
        , HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity getMembers(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size) {
    Page<Member> pageMembers = memberService.findMembers(page - 1, size);
    List<Member> members = pageMembers.getContent();
    return new ResponseEntity<>(
        new MultiResponseDto<>(mapper.membersToMemberResponses(members),
            pageMembers),
        HttpStatus.OK);
  }

  @DeleteMapping("/{member-id}")
  public ResponseEntity deleteMember(
      @PathVariable("member-id") @Positive long memberId) {
    memberService.deleteMember(memberId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}

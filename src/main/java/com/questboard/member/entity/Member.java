package com.questboard.member.entity;

import com.questboard.answer.entity.Answer;
import com.questboard.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column(nullable = false, updatable = false, unique = true)
  private String email;

  @Column(length = 100, nullable = false)
  private String password;

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 13, nullable = false, unique = true)
  private String phone;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 20, nullable = false)
  private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

  // 추가
  @ElementCollection(fetch = FetchType.EAGER)
  private final List<String> roles = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "member")
  private List<Answer> answers = new ArrayList<>();

  public void setQuestion(Question question) {
    questions.add(question);
    if(question.getMember() != this) {
      question.setMember(this);
    }
  }

  public void setAnswer(Answer answer) {
    answers.add(answer);
    if(answer.getMember() != this) {
      answer.setMember(this);
    }
  }

//  @OneToMany(mappedBy = "member")
//  private List<Order> orders = new ArrayList<>();
//
//  @OneToOne(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//  private Stamp stamp;

  public Member(String email) {
    this.email = email;
  }

  public Member(String email, String name, String phone) {
    this.email = email;
    this.name = name;
    this.phone = phone;
  }
  public enum MemberStatus {
    MEMBER_ACTIVE("활동중"),
    MEMBER_SLEEP("휴면 상태"),
    MEMBER_QUIT("탈퇴 상태");

    @Getter
    private String status;

    MemberStatus(String status) {
      this.status = status;
    }
  }

}

package com.questboard.answer.entity;

import com.questboard.member.entity.Member;
import com.questboard.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long answerId;

  @Column(length = 50, nullable = false)
  private String title;

  @Column(length = 255, nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @OneToOne
  @JoinColumn(name = "QUESTION_ID")
  private Question question;

  public void setMember(Member member) {
    this.member = member;
    if(!member.getAnswers().contains(this)) {
      member.setAnswer(this);
    }
  }

  public void setQuestion(Question question) {
    this.question = question;
    if(question.getAnswer() != this) {
      question.setAnswer(this);
    }
  }
}

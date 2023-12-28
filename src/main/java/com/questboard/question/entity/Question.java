package com.questboard.question.entity;

import com.questboard.answer.entity.Answer;
import com.questboard.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long questionId;

  @Column(length = 50, nullable = false)
  private String title;

  @Column(length = 255, nullable = false)
  private String content;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 20, nullable = false)
  private Question.QuestionStatus questionStatus = Question.QuestionStatus.QUESTION_REGISTERED;

  @Enumerated(value = EnumType.STRING)
  @Column(length = 20, nullable = false)
  private Question.QuestionScope questionScope;

  @ManyToOne
  @JoinColumn(name = "MEMBER_ID")
  private Member member;

  @OneToOne(mappedBy = "question")
  private Answer answer;

  public void setMember(Member member) {
    this.member = member;
    if(!member.getQuestions().contains(this)) {
      member.setQuestion(this);
    }
  }

  public void setAnswer(Answer answer) {
    this.answer = answer;
    if(answer.getQuestion() != this) {
      answer.setQuestion(this);
    }
  }

  public enum QuestionStatus {
    QUESTION_REGISTERED("질문 등록 상태"),
    QUESTION_ANSWERED("답변 완료 상태"),
    QUESTION_DELETED("질문 삭제 상태"),
    QUESTION_DEACTIVED("질문 비활성화 상태");

    @Getter
    private String status;

    QuestionStatus(String status) {
      this.status = status;
    }
  }

  public enum QuestionScope {
    QUESTION_PUBLIC("공개글"),
    QUESTION_SECRET("비공개글");

    @Getter
    private String status;

    QuestionScope(String status) { this.status = status; }
  }
}

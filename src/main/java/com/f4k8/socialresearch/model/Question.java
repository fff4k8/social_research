package com.f4k8.socialresearch.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "questions")
public class Question implements Serializable {
  public Question() { }
  @Id
  @GeneratedValue(generator = "question_id_sequence", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "question_id_sequence",
      sequenceName = "question_id_sequence",
      allocationSize = 20
  )
  private Integer id;

  @NotNull
  private String description;
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Quiz quiz;

  @NotNull
  @OneToMany( mappedBy = "question",  cascade = CascadeType.ALL, orphanRemoval = true)
  private final Set<Answer> answers = new HashSet<>();

  public void addAnswer(Answer answer) {
    answers.add(answer);
    answer.setQuestion(this);
  }
  public void removeAnswer(Answer answer) {
    answers.remove(answer);
    answer.setQuestion(null);
  }

  public int countPersons() {
    return answers.stream().mapToInt(a -> a.getPersons().size()).sum();
  }

  public Answer getAnswerById(int id) {
    return answers.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
  }


  public Set<Answer> getAnswers() {
    return answers;
  }
  public Quiz getQuiz() {
    return quiz;
  }
  public void setQuiz(Quiz quiz) {
    this.quiz = quiz;
  }
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

}

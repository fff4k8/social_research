package com.f4k8.socialresearch.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "quizzes")
public class Quiz implements Serializable {
  public Quiz(){}
  @Id
  @GeneratedValue(generator = "quiz_id_sequence", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "quiz_id_sequence",
      sequenceName = "quiz_id_sequence",
      allocationSize = 1
  )
  private Integer id;

  @NotNull
  private String description;

  @NotNull
  @OneToMany( mappedBy = "quiz", fetch = FetchType.LAZY, orphanRemoval = true,cascade = CascadeType.ALL)
  private final Set<Question> questions = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "quizzes")
  private final Set<Person> persons = new HashSet<>();



  public Set<Question> getQuestions() {
    return questions;
  }
  public void addQuestion(Question question) {
    questions.add(question);
    question.setQuiz(this);
  }
  public void removeQuestion(Question question) {
    questions.remove(question);
    question.setQuiz(null);
  }
  public Question getQuestionById(int id){
    return questions.stream().filter(q -> q.getId() == id).collect(Collectors.toList()).get(0);
  }


  public Set<Person> getPersons() {
    return persons;
  }
  public void addPerson(Person person) {
    this.persons.add(person);
   person.getQuizzes().add(this);
  }
  public void removePerson(Person person) {
    this.persons.remove(person);
    person.getQuizzes().remove(this);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Quiz)) {
      return false;
    }
    Quiz quiz = (Quiz) o;
    return id.equals(quiz.id);
  }

  @Override
  public int hashCode() {
    return 123*id.hashCode();
  }
}


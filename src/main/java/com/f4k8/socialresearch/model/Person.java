package com.f4k8.socialresearch.model;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
public class Person implements Serializable {
  public Person() { }

  @Id  @Size(min=7, max=32)
  @Column(name = "email", updatable = false, nullable = false)
  private String email;

  @NotNull @Size(min=3, max=32)
  private String area;

  @NotNull @Size(min=3, max=32) @Min(100) @Max(Integer.MAX_VALUE)
  private Integer areaPopulation;

  @NotNull @Min(18) @Max(99)
  private Short age;

  @NotNull @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @NotNull @Enumerated(value = EnumType.STRING)
  private Education education;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "person", orphanRemoval = true,
      fetch = FetchType.LAZY, optional = false)
  private PersonDetails personDetails ;


  public PersonDetails getPersonDetails() {
    return personDetails;
  }

  public void setPersonDetails(PersonDetails personDetails) {
    this.personDetails = personDetails;
    personDetails.setPerson(this);
    personDetails.setEmail(this.getEmail());
  }

  public enum Gender{M, F}
  public enum Education{NONE, MID, HIGH}

  @ManyToMany(fetch = FetchType.LAZY)
  private  Set<Quiz> quizzes = new HashSet<>();

  public Set<Quiz> getQuizzes() {
    return quizzes;
  }

  public void addQuiz(Quiz quiz) {
    this.quizzes.add(quiz);
  }

  public void removeQuiz(Quiz quiz) {
    quizzes.remove(quiz);
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "persons")
  private final Set<Answer> answers = new HashSet<>();

  public Set<Answer> getAnswers() {
    return answers;
  }

  public void addAnswer(Answer answer) {
    this.answers.add(answer);
    answer.getPersons().add(this);
  }
  public void removeAnswer(Answer answer) {
    answers.remove(answer);
    answer.getPersons().remove(this);
  }



  public Short getAge() {
    return age;
  }
  public void setAge(Short age) {
    this.age = age;
  }
  public Gender getGender() {
    return gender;
  }
  public void setGender(Gender gender) {
    this.gender = gender;
  }
  public Education getEducation() {
    return education;
  }
  public void setEducation(Education education) {
    this.education = education;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getArea() {
    return area;
  }
  public void setArea(String area) {
    this.area = area;
  }
  public Integer getAreaPopulation() {
    return areaPopulation;
  }
  public void setAreaPopulation(Integer areaPopulation) {
    this.areaPopulation = areaPopulation;
  }
}

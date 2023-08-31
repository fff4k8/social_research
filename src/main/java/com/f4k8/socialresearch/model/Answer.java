package com.f4k8.socialresearch.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "answers")
public class Answer implements Serializable {
  public Answer() { }
  @Id
  @GeneratedValue(generator = "answer_id_sequence", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
      name = "answer_id_sequence",
      sequenceName = "answer_id_sequence",
      allocationSize = 30
  )
  private Integer id;

  @NotNull
  private String description;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Question question;


  @ManyToMany(fetch = FetchType.LAZY)
  private final Set<Person> persons = new HashSet<>();


  public void addPerson(Person person) {
    this.persons.add(person);
    person.getAnswers().add(this);
  }

  public void removePerson(Person person) {
    this.persons.remove(person);
    person.getAnswers().remove(this);
  }

  public Set<Person> getPersons() {
    return persons;
  }


  public int getAvgAge() {
    return (int) persons.stream().mapToInt(Person::getAge).average().orElse(0);
  }

  public String getStringAvgAge() {
    return "Age avg: " + getAvgAge();
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
  public Question getQuestion() {
    return question;
  }
  public void setQuestion(Question question) {
    this.question = question;
  }


}

package com.f4k8.socialresearch.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "persons_details")
public class PersonDetails {

  public PersonDetails() { }

  public enum Role{USER, ADMIN}

  @NotNull @Enumerated(value = EnumType.STRING)
  private Role role;
  public Role getRole() {
    return role;
  }
  public void setRole(Role role) {
    this.role = role;
  }


  @Id
  @Column(name = "email")
  private String email;


  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @MapsId("email")
  @JoinColumn(name = "email")
  private Person person;


  @NotNull @Size(min=3, max=32)
  private String firstName;
  @NotNull @Size(min=3, max=32)
  private String lastName;

  @NotNull @Size(min=7, max=32)
  private String password;


  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public Person getPerson() {
    return person;
  }
  public void setPerson(Person person) {
    this.person = person;
    this.email = person.getEmail();
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

}


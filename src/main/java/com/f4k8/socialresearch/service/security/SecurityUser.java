package com.f4k8.socialresearch.service.security;

import com.f4k8.socialresearch.model.Person;
import com.f4k8.socialresearch.model.PersonDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


public class SecurityUser implements UserDetails {

  private Person person;

  public void setPerson(Person person) {
    this.person = person;
  }

  public Person getPerson() {
    return this.person;
  }

  public SecurityUser(Person person) {
    this.person = person;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + person.getPersonDetails().getRole().name()));
  }

  @Override
  public String getPassword() {
    return person.getPersonDetails().getPassword();
  }

  @Override
  public String getUsername() {
    return person.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public PersonDetails.Role getRole() {
    return person.getPersonDetails().getRole();
  }
}

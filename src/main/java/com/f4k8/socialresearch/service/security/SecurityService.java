package com.f4k8.socialresearch.service.security;

import com.f4k8.socialresearch.model.Person;
import com.f4k8.socialresearch.model.PersonDetails;
import com.f4k8.socialresearch.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;


@Service
public class SecurityService implements UserDetailsService {

  @PersistenceUnit
  EntityManagerFactory emf;
  @Autowired
  private PersonRepository personRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

  Person person = personRepository.findPersonWithQuizzes(email)
           .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));

    return new SecurityUser(person);
  }

  public boolean registerPerson(Person person) {
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    //TODO: candidate for caching
    long exist = // checking if user exists
        em.createQuery("select count(*) from Person p WHERE p.email = ?1", Long.class)
            .setParameter(1, person.getEmail()).getSingleResult();

    if (exist == 1) {
      em.getTransaction().rollback();
      em.clear();
      em.close();
      return false;
    }

    PersonDetails pd = person.getPersonDetails();
    pd.setPassword(new BCryptPasswordEncoder(12).encode(pd.getPassword()));

    pd.setRole(PersonDetails.Role.USER);   // default role

    em.persist(person);
    em.getTransaction().commit();
    em.clear();
    em.close();

    return true;
  }
}

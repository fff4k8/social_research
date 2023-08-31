
package com.f4k8.socialresearch.repository;


import com.f4k8.socialresearch.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

  Optional<Person> findByEmail(String email);


  @Transactional(readOnly = true)
  @Query("SELECT DISTINCT p FROM Person p "
     + "JOIN FETCH p.personDetails d "
     + "LEFT JOIN FETCH p.quizzes q "
     + "WHERE p.email = ?1")
 Optional<Person> findPersonWithQuizzes(String email);

}


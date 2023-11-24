
package com.f4k8.socialresearch.repository;


import com.f4k8.socialresearch.model.Person;
import com.f4k8.socialresearch.model.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {


  @Query("SELECT DISTINCT p FROM Person p "
     + "JOIN FETCH p.personDetails d "
     + "LEFT JOIN FETCH p.quizzes q "
     + "WHERE p.email = ?1")
 Optional<Person> findPersonWithQuizzes(String email);


@Query("SELECT new com.f4k8.socialresearch.model.UserDto(p.email, d.password) " +
        "FROM Person p JOIN p.personDetails d")
List<UserDto> findAllUsersDto();


}


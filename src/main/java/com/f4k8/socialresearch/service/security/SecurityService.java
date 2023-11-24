package com.f4k8.socialresearch.service.security;

import com.f4k8.socialresearch.model.Person;
import com.f4k8.socialresearch.model.PersonDetails;
import com.f4k8.socialresearch.model.UserDto;
import com.f4k8.socialresearch.repository.PersonRepository;
import com.f4k8.socialresearch.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;



@Service
public class SecurityService implements UserDetailsService {

    @PersistenceUnit
    EntityManagerFactory emf;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    CacheService cacheService;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository.findPersonWithQuizzes(email)
                .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        return new SecurityUser(person);
    }


    public boolean registerPerson(Person person) {

        // checks if username already exists in cache
        UserDto user = cacheService.findUserDto(person.getEmail());
        if (user != null) return false; // false if exists


        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        PersonDetails pd = person.getPersonDetails();
        pd.setPassword(new BCryptPasswordEncoder(12).encode(pd.getPassword()));

        pd.setRole(PersonDetails.Role.USER);   // default role

        em.persist(person);
        em.getTransaction().commit();
        em.clear();
        em.close();

        // saving to cache
        UserDto userDto = new UserDto(person.getEmail(), pd.getPassword());
        cacheService.saveUserDto(userDto);

        return true;
    }









}

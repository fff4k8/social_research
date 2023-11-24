package com.f4k8.socialresearch.service;

import com.f4k8.socialresearch.model.Quiz;
import com.f4k8.socialresearch.model.UserDto;
import com.f4k8.socialresearch.repository.PersonRepository;
import com.f4k8.socialresearch.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.Map;



@Service
public class CacheService {

    @Autowired
    CacheManager cacheManager;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    QuizRepository quizRepository;


    @EventListener(ApplicationReadyEvent.class)
    public void cacheInit() {

        // getting all usersDto on startup
        List<UserDto> users = personRepository.findAllUsersDto();
        Cache cache = cacheManager.getCache("usersDto");

        users.forEach(u -> {
            cache.put(u.getEmail(), u);
        });

        // getting all quizzes on startup
        List<Quiz> quizzes = quizRepository.findAllQuizzesQA();
        Cache cache2 = cacheManager.getCache("quizzes");

        quizzes.forEach(q -> {
            cache2.put(q.getId(), q);
        });
    }


    public UserDto findUserDto(String key) {
        Cache cache = cacheManager.getCache("usersDto");
        try {
            return (UserDto) cache.get(key).get();  // could be null
        } catch (NullPointerException ex) {
            return null; // returning null is provided for auth/controller logic
        }
    }

    public void saveUserDto(UserDto userDto) {
        cacheManager.getCache("usersDto")
                .put(userDto.getEmail(), userDto);
    }


    public Collection<Quiz> findAllQuizzes() {
        Cache cache = cacheManager.getCache("quizzes");
        Map<Integer, Quiz> map = (Map<Integer, Quiz>) cache.getNativeCache();
        Collection<Quiz> quizzes = map.values();
        return quizzes;
    }

    public Quiz findQuizById(int id) {
        Cache cache = cacheManager.getCache("quizzes");
        Quiz quiz = (Quiz) cache.get(id).get();
        return quiz;
    }

    public void saveQuiz(Quiz quiz) {
        cacheManager.getCache("quizzes")
                .put(quiz.getId(), quiz);
    }

}

package com.f4k8.socialresearch.service;

import com.f4k8.socialresearch.model.Answer;
import com.f4k8.socialresearch.model.Question;
import com.f4k8.socialresearch.model.Quiz;
import com.f4k8.socialresearch.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Service
public class QuizService {

  @PersistenceUnit
  EntityManagerFactory emf;
  @Autowired
  QuizRepository quizRepository;
  @Autowired
  CacheService cacheService;

  public Collection<Quiz> findAllQuizzes() {
    return cacheService.findAllQuizzes();
  }

  public Quiz findById(int id){
    return cacheService.findQuizById(id);
  }

  @Transactional(readOnly = true)
  public Quiz findByIdEagerPersons(int id){
    return quizRepository.findByIdEagerPersons(id);
  }

    // Parsing <Q,A> pairs from web-controller
    public void saveQuiz(@NotNull MultiValueMap<String, String> map) {
    EntityManager em = emf.createEntityManager();

    Quiz quiz = new Quiz(); // create

    System.out.println(map.toString());

    String quiz_title = map.getFirst("quiz_title");
    System.out.println("saving quiz title: " + quiz_title);

    quiz.setDescription(quiz_title); // set

    int num_questions = Integer.parseInt(map.getFirst("num_questions"));

    for (int i = 0; i < num_questions; i++) {
      String qVal = map.getFirst("question_" + i);
      System.out.println("saving question number " + i + ": " + qVal);

      int num_answers = Integer.parseInt(map.getFirst("num_answers_" + i));

      Question question = new Question(); // create

      question.setDescription(qVal); // set

      for (int u = 0; u < num_answers; u++) {
        String aVal = map.getFirst("answer_" + i + "_" + u);
        System.out.println("saving answer_" + i + "_" + u + ": " + aVal);

        Answer answer = new Answer(); // create
        answer.setDescription(aVal); // set
        question.addAnswer(answer); //add
      }
      quiz.addQuestion(question); //add
    }
    em.getTransaction().begin();
    em.persist(quiz); //save
    em.getTransaction().commit();
    em.clear();
    em.close();

    cacheService.saveQuiz(quiz); // updating cache
  }

}
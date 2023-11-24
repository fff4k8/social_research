package com.f4k8.socialresearch.repository;
import com.f4k8.socialresearch.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Integer> {

  @Query("SELECT DISTINCT q FROM Quiz q "
      + "JOIN FETCH q.questions que "
      + "JOIN FETCH que.answers ans "
      + "LEFT JOIN FETCH ans.persons p "
      + "WHERE q.id = ?1")
  Quiz findByIdEagerPersons(int id);

  @Query("SELECT q FROM Quiz q WHERE q.id = ?1")
  Quiz findByIdLazy(int id);

  @Query("SELECT DISTINCT q FROM Quiz q "
          + "JOIN FETCH q.questions que "
          + "JOIN FETCH que.answers ans ")
  List<Quiz> findAllQuizzesQA();
}


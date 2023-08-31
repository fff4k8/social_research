package com.f4k8.socialresearch.service;

import com.f4k8.socialresearch.model.Answer;
import com.f4k8.socialresearch.model.Person;
import com.f4k8.socialresearch.model.Quiz;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Service
public class PersonService {

  @PersistenceUnit
  EntityManagerFactory emf;

  /** insertQuizAnswers(Person person, List<Answer> answers, Quiz quiz, int batchSize)
                 Inserts answers into answers_persons table
                       using prepared statement native query
                             and adding Persons Quiz after all
   */
  public void insertQuizAnswers(Person person, List<Answer> answers, Quiz quiz, int batchSize) {
    EntityManager em = emf.createEntityManager();
    String answerQuery = "INSERT INTO answers_persons (answers_id, persons_email) VALUES (?, ?)";
    String quizQuery = "INSERT INTO persons_quizzes (persons_email, quizzes_id) VALUES (?, ?)";
    Session session = em.unwrap(Session.class);
    session.setJdbcBatchSize(batchSize);

    em.getTransaction().begin();

    session.doWork(connection -> {
      connection.setAutoCommit(false);
      try (PreparedStatement psa = connection.prepareStatement(answerQuery);
           PreparedStatement psq = connection.prepareStatement(quizQuery)) {
        psq.setString(1, person.getEmail());
        psq.setInt(2, quiz.getId());

        final int[] i = {1};
        answers.forEach(answer -> {
          try {
            psa.setInt(1, answer.getId());
            psa.setString(2, person.getEmail());
            psa.addBatch();

            if (i[0] % batchSize == 0) {
              psa.executeBatch();
              psa.clearBatch();
            }
            i[0]++;
          } catch (SQLException e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            session.close();
          }
        });
        psa.executeBatch();
        psq.executeUpdate();

        connection.commit();
        em.getTransaction().commit();
        psa.clearBatch();
        psa.close();
        connection.close();

      } catch (SQLException e) {
        e.printStackTrace();
        connection.rollback();
        em.getTransaction().rollback();
        connection.close();
        em.clear();
        em.close();
      }
    });

    em.clear();
    em.close();
    // updating Person at the end
    person.addQuiz(quiz);
  }

}
















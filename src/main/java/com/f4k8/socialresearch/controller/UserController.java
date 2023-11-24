package com.f4k8.socialresearch.controller;

import com.f4k8.socialresearch.model.*;
import com.f4k8.socialresearch.service.PersonService;
import com.f4k8.socialresearch.service.QuizService;
import com.f4k8.socialresearch.service.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {

  @Autowired
  QuizService quizService;
  @Autowired
  PersonService personService;


  @GetMapping("/") // redirect depends on user role
  public String homePage(@AuthenticationPrincipal SecurityUser securityUser) {
    if (securityUser.getRole().equals(PersonDetails.Role.ADMIN)) {
      return "redirect:/admin/";
    }
    return "redirect:/quizzes";
  }


  @GetMapping("/quizzes")
  public String get_quizzes(@AuthenticationPrincipal SecurityUser securityUser, Model model) {

    List<Quiz> quizList = new ArrayList<>(quizService.findAllQuizzes());

    // clear answered quizzes
    quizList.removeAll(securityUser.getPerson().getQuizzes());
    model.addAttribute(quizList);

    return "quizzes";
  }



  @GetMapping("/quiz_form") // get quiz by id
  public String get_quiz(@RequestParam Integer id, Model model, HttpSession session)  {

    Quiz quiz = quizService.findById(id);

    model.addAttribute(quiz);
    session.setAttribute("quiz_id", id);
    return "quiz_form";
  }



  @PostMapping(path = "/submit_answers",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.TEXT_HTML_VALUE)
  public String submit_answers(@RequestBody MultiValueMap<String, String> mmap,
                               @AuthenticationPrincipal SecurityUser securityUser,
                               HttpSession session) {
    Map<String, String> map = mmap.toSingleValueMap();

    // websession cache
    int id = (int) session.getAttribute("quiz_id");
    Quiz quiz = quizService.findById(id);
    // websession cache
    Person person = securityUser.getPerson();

    if (person.getQuizzes().contains(quiz)) {
      return "redirect:/quizzes";
    }

    List<Answer> answers = new ArrayList<>();
    map.forEach((k, v) -> {
      // gets q,a from already loaded quiz entity
      Question question = quiz.getQuestionById(Integer.parseInt(k));
      Answer answer = question.getAnswerById(Integer.parseInt(v));
      answers.add(answer);
    });
    personService.insertQuizAnswers(person, answers, quiz, 30);
    session.removeAttribute("quiz_id");
    return "redirect:/quizzes";
  }

}




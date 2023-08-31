package com.f4k8.socialresearch.controller;


import com.f4k8.socialresearch.model.Quiz;
import com.f4k8.socialresearch.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @Autowired
  QuizService quizService;

  @GetMapping("/")
  public String homeAdmin() {
    return "admin_page";
  }

  @GetMapping("/quizzes")
  public String quizzes(HttpSession session, Model model) {

    //TODO: candidate for caching
    @SuppressWarnings("unchecked")
    List<Quiz> quizList = (List<Quiz>) session.getAttribute("quizList");
    if (quizList == null) {
      quizList = quizService.findAllQuizzes();
      session.setAttribute("quizList", quizList);
    }
    model.addAttribute(quizList);
    return "quizzes";
  }

  // 1. create new quiz
  @GetMapping("/create_quiz")
  public String quiz() {
    return "create_quiz";
  }

  // 2. submit new quiz
  @PostMapping(path = "/quiz_submit",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.TEXT_HTML_VALUE)
  public String quiz_submit(@RequestBody MultiValueMap<String, String> map) {
   Quiz quiz = quizService.saveQuiz(map); // 3. processing values
    return "redirect:/";
  }

  // 3. view results
  @GetMapping("/quiz_result") //get quiz result by id
  public String quiz_result(@RequestParam Integer id, Model model) {
    Quiz quiz = quizService.findByIdEagerPersons(id);
    model.addAttribute(quiz);
    return "quiz_result";
  }



}

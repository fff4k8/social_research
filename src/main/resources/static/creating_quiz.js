
   function addQuestions(){

            // Generate a dynamic number of inputs
            let number = document.getElementById("num_questions").value;

            // Get the element where the inputs will be added to
            let container = document.getElementById("questions_container");

            // Remove every children it had before
            while (container.hasChildNodes()) {
               container.removeChild(container.lastChild);
          }

            for (let i=0; i < number; i++){


          let answers_container = document.createElement("div");
           answers_container.id = "answers_container_" + i;

                // Append a node with a random text
                answers_container.appendChild(document.createTextNode("Question " + (i+1) + ": "));
                  answers_container.appendChild(document.createElement("br"));
                // Create an <input> element, set its type and name attributes
                let textarea = document.createElement("textarea");
                textarea.rows="4";
                textarea.cols="50";
                textarea.name = "question_" + i;
                textarea.required = true;

                  answers_container.appendChild(textarea);
                 answers_container.appendChild(document.createElement("br"));
                  answers_container.appendChild(document.createElement("br"));

                 let answer_1 = document.createElement("input");
                answer_1.type = "text";
                answer_1.name = "answer_" + i + "_" + 0;
                answer_1.id = "answer_q_" + i + "_id_" + 0;
                 answer_1.required = true;
             let answer_2 = document.createElement("input");
                answer_2.type = "text";
                answer_2.name = "answer_" + i + "_" + 1;
                answer_2.id = "answer_q_" + i + "_id_" + 1;
                  answer_2.required = true;


             let button_p = document.createElement("button");
                   button_p.id = "bu_+_id_" + i;
                   button_p.name = "bu_+_name_" + i;
                   button_p.innerHTML = "+";
                    button_p.type = "button";
             let button_m = document.createElement("button");
                   button_m.id = "bu_-_id_" + i;
                   button_m.name = "bu_-_name_" + i;
                   button_m.innerHTML = "-";
                   button_m.type = "button";



                      let num_answers = document.createElement("input");
                         num_answers.type = "hidden";
                         num_answers.value = 2;
                         num_answers.name = "num_answers_" + i;
                         num_answers.id = "num_answers_id_" + i;


                   button_p.onclick = function() {
                               num_answers.value++;
                                   let answer = document.createElement("input");
                                     answer.type = "text";
                                    answer.name = "answer_" + i + "_" + (parseInt(num_answers.value) - 1);
                                    answer.id = "answer_q_" + i + "_id_" + (parseInt(num_answers.value) - 1);

                                    answer.required = true;

                                   answers_container.appendChild(document.createTextNode("Answer " + (num_answers.value) + ": "));
                                   answers_container.appendChild(answer);
                                   answers_container.appendChild(document.createElement("br"));

                     };

                   button_m.onclick = function() {
                        if(parseInt(num_answers.value)>2){
                        num_answers.value--;
                          answers_container.removeChild(answers_container.lastChild);
                           answers_container.removeChild(answers_container.lastChild);
                            answers_container.removeChild(answers_container.lastChild);
                            }
                     };

                answers_container.appendChild(button_p);
                  answers_container.appendChild(button_m);
                       answers_container.appendChild(num_answers);

                 answers_container.appendChild(document.createElement("br"));
                  answers_container.appendChild(document.createElement("br"));

                 answers_container.appendChild(document.createTextNode("Answer 1: "));
                answers_container.appendChild(answer_1);
                     answers_container.appendChild(document.createElement("br"));

                 answers_container.appendChild(document.createTextNode("Answer 2: "));
                  answers_container.appendChild(answer_2);
                 answers_container.appendChild(document.createElement("br"));

                  container.appendChild(answers_container);

                  container.appendChild(document.createElement("br"));

            }
        }


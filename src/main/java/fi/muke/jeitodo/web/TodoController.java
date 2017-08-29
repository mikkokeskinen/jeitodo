package fi.muke.jeitodo.web;

import fi.muke.jeitodo.domain.Todo;
import fi.muke.jeitodo.domain.TodoRepository;
import fi.muke.jeitodo.domain.User;
import fi.muke.jeitodo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/add")
    public @ResponseBody String addNewTodo(Principal userDetails, @RequestParam String title) {
        String email = userDetails.getName();
        User user = userRepository.findByEmail(email);

        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setUser((User) user);
        todoRepository.save(todo);

        return "Saved";
    }

    @GetMapping(path = "/")
    public ModelAndView home(Principal userDetails) {
        User user = userRepository.findByEmail(userDetails.getName());
        List<Todo> todos = todoRepository.findIncompleteForUser(user);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("user", user);
        modelAndView.addObject("todos", todos);
        modelAndView.setViewName("todos");

        return modelAndView;
    }
}

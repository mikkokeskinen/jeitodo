package fi.muke.jeitodo.web;

import fi.muke.jeitodo.domain.Todo;
import fi.muke.jeitodo.domain.TodoRepository;
import fi.muke.jeitodo.domain.User;
import fi.muke.jeitodo.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/todo")
public class TodoController {
    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    public TodoController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/")
    public ModelAndView home(Principal userDetails) {
        User user = userRepository.findByEmail(userDetails.getName());
        List<Todo> incompleteTodos = todoRepository.findIncompleteForUser(user);
        List<Todo> completeTodos = todoRepository.findCompleteForUser(user);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("user", user);
        modelAndView.addObject("incompleteTodos", incompleteTodos);
        modelAndView.addObject("completeTodos", completeTodos);
        modelAndView.setViewName("todos");

        return modelAndView;
    }

    @PostMapping(path = "/")
    public String save(HttpServletRequest request, Principal userDetails) {
        User user = userRepository.findByEmail(userDetails.getName());

        if (request.getParameter("todo_id") != null) {
            Todo todo = todoRepository.findOne(Integer.parseInt(request.getParameter("todo_id")));

            if (todo.getUser() != user) {
                throw new ResourceNotFoundException();
            }

            if (request.getParameter("mark_done") != null) {
                todo.setCompleted(!todo.isCompleted());
                todo.setCompletedAt(todo.isCompleted() ? new Date() : null);
                todoRepository.save(todo);
            }

            if (request.getParameter("delete") != null) {
                todoRepository.delete(todo);
            }
        }

        if (request.getParameter("title") != null) {
            Todo todo = new Todo();
            todo.setTitle(request.getParameter("title"));
            todo.setUser(user);
            todoRepository.save(todo);
        }

        return "redirect:/todo/";
    }
}

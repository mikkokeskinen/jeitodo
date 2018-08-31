package fi.muke.jeitodo.web;

import fi.muke.jeitodo.domain.Todo;
import fi.muke.jeitodo.domain.TodoRepository;
import fi.muke.jeitodo.domain.User;
import fi.muke.jeitodo.domain.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String home(Model model, Principal userDetails) {
        User user = userRepository.findByEmail(userDetails.getName());
        List<Todo> incompleteTodos = todoRepository.findIncompleteForUser(user);
        List<Todo> completeTodos = todoRepository.findCompleteForUser(user);

        model.addAttribute("user", user);
        model.addAttribute("incompleteTodos", incompleteTodos);
        model.addAttribute("completeTodos", completeTodos);

        return "todos";
    }

    @PostMapping(path = "/")
    public String save(HttpServletRequest request, Principal userDetails) {
        User user = userRepository.findByEmail(userDetails.getName());

        if (request.getParameter("todo_id") != null) {
            Todo todo = todoRepository.findById(Integer.parseInt(request.getParameter("todo_id"))).orElse(null);

            if (todo != null) {
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

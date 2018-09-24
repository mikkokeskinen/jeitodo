package fi.muke.jeitodo.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
public class TodoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void findIncompleteForUser() {
        User u1 = new User();
        u1.setEmail("test@example.com");
        u1.setPassword("password");
        entityManager.persist(u1);

        Todo todo1 = new Todo();
        todo1.setUser(u1);
        todo1.setCompleted(false);

        Todo todo2 = new Todo();
        todo2.setUser(u1);
        todo2.setCompleted(true);

        entityManager.persist(todo1);
        entityManager.persist(todo2);

        entityManager.flush();

        List<Todo> todos = todoRepository.findIncompleteForUser(u1);

        assertThat(todos.size()).isEqualTo(1);
    }

    @Test
    public void createdIsPopulated() {
        Todo todo = new Todo();
        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo.getCreated()).isNotNull();
    }

    @Test
    public void updatedIsUpdated() {
        Todo todo = new Todo();
        todoRepository.save(todo);
        entityManager.flush();

        Date updated = todo.getUpdated();

        todo.setTitle("Test");
        todoRepository.save(todo);
        entityManager.flush();

        assertThat(todo.getUpdated()).isAfter(updated);
    }

    @Test
    public void completedAtIsNotPopulated() {
        Todo todo = new Todo();
        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo.getCompletedAt()).isNull();
    }
}

package fi.muke.jeitodo.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND completed = false")
    public List<Todo> findIncompleteForUser(@Param("user") User user);
}

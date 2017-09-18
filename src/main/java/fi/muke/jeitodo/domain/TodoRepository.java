package fi.muke.jeitodo.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Integer> {
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND completed = false ORDER BY t.created DESC")
    public List<Todo> findIncompleteForUser(@Param("user") User user);

    @Query("SELECT t FROM Todo t WHERE t.user = :user AND completed = true ORDER BY t.updated DESC")
    public List<Todo> findCompleteForUser(@Param("user") User user);
}

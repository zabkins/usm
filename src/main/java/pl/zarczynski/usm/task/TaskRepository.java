package pl.zarczynski.usm.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.task.entity.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("SELECT t FROM Task t LEFT JOIN FETCH t.subTasks st WHERE t.user = :user")
	Page<Task> findAllByUser(User user, Pageable pageable);

	@Query("SELECT t FROM Task t LEFT JOIN FETCH t.subTasks st WHERE t.id = :id AND t.user = :user")
	Optional<Task> findByIdAndUserWithSubtasks(Long id, User user);

	@Query("SELECT t FROM Task t WHERE t.id = :id AND t.user = :user")
	Optional<Task> findByIdAndUser(Long id, User user);
}

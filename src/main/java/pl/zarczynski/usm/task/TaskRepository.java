package pl.zarczynski.usm.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.zarczynski.usm.configuration.user.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	@Query("SELECT t FROM Task t LEFT JOIN FETCH t.subTasks st WHERE t.user = :user")
	List<Task> findAllByUser(User user);
}

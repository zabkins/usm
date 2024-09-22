package pl.zarczynski.usm.subtask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.subtask.entity.SubTask;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

	@Query(value = "SELECT st FROM SubTask st JOIN st.task t WHERE t.user = :user AND st.id =:subTaskId")
	Optional<SubTask> findSubTaskByIdAndUser(Long subTaskId, User user);

	@Query(value = "SELECT st FROM SubTask st JOIN st.task t WHERE t.user = :user AND t.id = :taskId")
	List<SubTask> findAllSubTasksForGivenTask(Long taskId, User user);
}

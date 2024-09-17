package pl.zarczynski.usm.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sub_task")
public class SubTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private Boolean isDone;
	@ManyToOne
	@JoinColumn(name = "task_id")
	private Task task;

}
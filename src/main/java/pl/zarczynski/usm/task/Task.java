package pl.zarczynski.usm.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "task")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime dateFrom;
	private LocalDateTime dateTo;
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
	private List<SubTask> subTasks;
}

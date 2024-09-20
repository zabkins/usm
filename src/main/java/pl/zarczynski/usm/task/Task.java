package pl.zarczynski.usm.task;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;
import pl.zarczynski.usm.configuration.user.User;
import pl.zarczynski.usm.task.subtask.SubTask;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "task")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@ToString.Exclude
	private User user;
	private String name;
	private String description;
	@CreationTimestamp
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss z")
	private ZonedDateTime createdAt;
	@UpdateTimestamp
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss z")
	private ZonedDateTime updatedAt;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss z")
	private ZonedDateTime startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss z")
	private ZonedDateTime finishDate;
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
	@ToString.Exclude
	private List<SubTask> subTasks;

	@Override
	public final boolean equals (Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		Task task = (Task) o;
		return getId() != null && Objects.equals(getId(), task.getId());
	}

	@Override
	public final int hashCode () {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}

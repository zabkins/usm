package pl.zarczynski.usm.configuration.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.zarczynski.usm.common.DateHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Table(name = "users")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false)
	private Integer id;

	@Column(nullable = false)
	private String fullName;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(nullable = false)
	@ToString.Exclude
	private String password;

	@CreationTimestamp
	@Column(updatable = false, name = "created_at")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss z")
	@ToString.Exclude
	private ZonedDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss z")
	@ToString.Exclude
	private ZonedDateTime updatedAt;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return List.of();
	}

	@Override
	public String getPassword () {
		return password;
	}

	@Override
	public String getUsername () {
		return email;
	}

	@ToString.Include(name = "createdAt")
	private String formatCreatedAtDate() {
		return DateHelper.parseDate(createdAt);
	}

	@ToString.Include(name = "updatedAt")
	private String formatUpdatedAtDate() {
		return DateHelper.parseDate(updatedAt);
	}

	@Override
	public final boolean equals (Object o) {
		if (this == o) return true;
		if (o == null) return false;
		Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
		if (thisEffectiveClass != oEffectiveClass) return false;
		User user = (User) o;
		return getId() != null && Objects.equals(getId(), user.getId());
	}

	@Override
	public final int hashCode () {
		return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
	}
}

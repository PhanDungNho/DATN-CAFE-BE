package cafe.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Accounts {
	@Id
	@Column(name = "username", nullable = false, length = 255)
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "fullname", nullable = false, length = 255)
	private String fullname;

	@Column(name = "phone", nullable = false, length = 15)
	private String phone;

	@Column(name = "email", nullable = false, length = 255)
	private String email;

	@Column(name = "amountpaid", nullable = false)
	private Double amountpaid;

	@Column(name = "active", nullable = false, length = 255)
	private Boolean active;

	@OneToMany(mappedBy = "username")
	private Set<UserRoles> userRoles;
}

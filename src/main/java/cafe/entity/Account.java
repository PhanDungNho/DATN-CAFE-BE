package cafe.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Accounts")
public class Account {
	@Id
	@Column(name = "username", nullable = false, length = 255)
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "full_name", nullable = false, length = 255)
	private String fullName;

	@Column(name = "phone", nullable = true, length = 15)
	private String phone;

	@Column(name = "email", nullable = false, length = 255)
	private String email;
	
	@Column(name = "image", length = 255)
	private String image;

	@Column(name = "amount_paid", nullable = false)
	private Double amountPaid;

	@Column(name = "active", nullable = false, length = 255)
	private Boolean active;

	@JsonIgnore
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
	private List<Authority> authorities;
    
    @PrePersist
	public void prePersist() {
    	active = true;
    	amountPaid =0D;
    	enabled = false; // Mặc định tài khoản chưa được kích hoạt
	}
    // Thêm trường OTP để lưu mã OTP
    @Column(name = "otp", length = 6)
    private String otp;

    // Thêm trường enabled để lưu trạng thái xác thực của tài khoản
    @Column(name = "enabled", nullable = true)
    private Boolean enabled;
    
   

 
}

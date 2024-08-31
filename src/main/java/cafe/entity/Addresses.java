package cafe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Addresses {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "street", nullable = false, length = 255)
	private String street;
	
	@Column(name = "wardcode", nullable = false)
	private int wardcode;
	
	@Column(name = "districtcode", nullable = false)
	private int districtcode;
	
	@Column(name = "citycode", nullable = false)
	private int citycode;
	
	 @Column(columnDefinition = "TEXT")
	private String fulladdresstext;
	
	@Column(name = "isdefault", nullable = false)
	private Boolean isdefault;
	
	@Column(name = "active", nullable = false)
	private Boolean active;
	
	@ManyToOne
	@JoinColumn(name = "account", nullable = false)
	private Account account;
}

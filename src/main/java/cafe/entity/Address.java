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
@Table(name = "Addresses")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "street", nullable = false, length = 255)
	private String street;
	
	@Column(name = "ward_code", nullable = false)
	private int wardCode;
	
	@Column(name = "district_code", nullable = false)
	private int districtCode;
	
	@Column(name = "city_code", nullable = false)
	private int cityCode;
	
 
	 @Column(name = "full_address", columnDefinition = "nvarchar(max)")
	private String fullAddress;
	
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault;
	
	@Column(name = "active", nullable = false)
	private Boolean active = true;
	
	@ManyToOne
	@JoinColumn(name = "account", nullable = false)
	private Account account;
}

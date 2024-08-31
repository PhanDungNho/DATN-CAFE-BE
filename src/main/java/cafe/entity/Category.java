package cafe.entity;



import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
@Id
@GeneratedValue(strategy = GenerationType. IDENTITY)
@Column(name = "id", nullable = false)
private Long id;

@Column (name ="name", nullable = false, length = 255)
private String name;

@Column (name ="image", nullable = true, length = 255)
private String image;

@Column(name = "active")
private Boolean active;

@JsonIgnore
@OneToMany(mappedBy = "category")
private List<Product> products;

}
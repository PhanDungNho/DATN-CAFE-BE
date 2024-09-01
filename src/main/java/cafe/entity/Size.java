package cafe.entity;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
@SuppressWarnings("serial")
@Entity
@Data
@Table(name = "size")
public class Size    {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "active")
    private Boolean active;
    
    @JsonIgnore
    @OneToMany(mappedBy = "size")
    private List<ProductVariant> productvariants;
    

    // Getters and Setters
}
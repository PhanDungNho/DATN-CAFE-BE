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
@Table(name = "product")
public class Product    {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "categoryid")
    private Category category;
    
    @Column(name = "active")
    private Boolean active;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description ;
    

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductVariant> productvariants;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Image> images;

    // Getters and Setters
}
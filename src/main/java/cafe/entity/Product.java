package cafe.entity;


import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "Products")
public class Product    {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
    @Column(name = "slug", nullable = true, length = 255)
    private String slug;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(name = "active")
    private Boolean active;

    @Column(name = "description", columnDefinition = "nvarchar(max)")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductVariant> productVariants;
    
//    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductToppings> productToppings;
    
    
    

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;
    
    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }
    // Getters and Setters
}
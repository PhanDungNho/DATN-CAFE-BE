package cafe.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name="moment_price",nullable = false)
    private BigDecimal momentPrice;

    @Column(columnDefinition = "TEXT")
    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.EAGER)
    private List<OrderDetailTopping> orderDetailToppings;
    

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        OrderDetail orderDetail = (OrderDetail) o;
//        return Objects.equals(id, orderDetail.getId());
//    } 
//    @Override
//    public int hashCode() {
//        return Objects.hash(id); // Chỉ sử dụng id hoặc các thuộc tính cơ bản khác
//    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); 
    }

}


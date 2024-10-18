package cafe.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import cafe.enums.TransactionMethod;
import cafe.enums.TransactionStatus;
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
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderid", nullable = true)
    private Order order;

    @Column(name = "partner_code")
    private String partnerCode;

    @Column(name = "order_id")  // Đổi tên cột để tránh trùng với "orderid"
    private String orderId;

    @Column(name = "request_id")
    private String requestId;

    @Column
    private BigDecimal amount;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "trans_id")
    private String transId;

    @Column(name = "result_code")
    private Integer resultCode;

    @Column
    private String message;

    @Column(name = "pay_type")
    private String payType;

    @Column(name = "response_time")
    private Date responseTime;
    

    @Column(name = "extra_data")
    private String extraData;

    @Column
    private String signature;
    
    @Column(name = "pay_url")
    private String payUrl;

}

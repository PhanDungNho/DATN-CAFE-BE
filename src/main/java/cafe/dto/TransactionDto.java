//package cafe.dto;
//
//import java.io.Serializable;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
//import lombok.Data;
//
//@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class TransactionDto {
//    private Long id;
//    @NotNull(message = "Active status is required")
//    private Boolean active;
//    @NotNull(message = "Citycode is required")
//    private Integer citycode;
//    @NotNull(message = "Districtcode is required")
//    private Integer districtcode;
//    @Size(max = 65535, message = "Fulladdresstext must be less than or equal to 65535 characters")
//    private String fulladdresstext;
//    @NotNull(message = "Isdefault status is required")
//    private Boolean isdefault;
//    @NotBlank(message = "Street name is required")
//    @Size(max = 255, message = "Street must be less than or equal to 255 characters")
//    private String street;
//    @NotNull(message = "Wardcode is required")
//    private Integer wardcode;
//    @NotNull(message = "Account is required")
//    private AccountDto account;
//}

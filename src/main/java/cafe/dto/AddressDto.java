package cafe.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private Long id;
    @NotNull(message = "Active status is required")
    private Boolean active;
    @NotNull(message = "Citycode is required")
    private Integer cityCode;
    @NotNull(message = "Districtcode is required")
    private Integer districtCode;
    @Size(max = 65535, message = "Fulladdresstext must be less than or equal to 65535 characters")
    private String fullAddress;
    @NotNull(message = "Isdefault status is required")
    private Boolean isDefault;
    @NotBlank(message = "Street name is required")
    @Size(max = 255, message = "Street must be less than or equal to 255 characters")
    private String street;
    @NotNull(message = "Wardcode is required")
    private Integer wardCode;
    
    private String account;
    private AccountDto accountDto;
}

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
    private Boolean active;
    private Integer cityCode;
    private Integer districtCode;
    private String fullAddress;
    private Boolean isDefault;
    private String street;
    private Integer wardCode;
    
    private String account;
    private AccountDto accountDto;
}

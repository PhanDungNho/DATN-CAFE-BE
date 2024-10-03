package cafe.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Bao gồm các trường không null
// DTO là lớp trung gian, thường để bảo mật, này nọ
public class ProductDto implements Serializable {

    private long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than or equal to 255 characters")
    private String name;

    @NotNull(message = "Product active status is required")
    private Boolean active;

    @Size(max = 65535, message = "Description must be less than or equal to 65535 characters")
    private String description;

    // Chỉ sử dụng khi tạo mới hoặc cập nhật sản phẩm
    private Long categoryid; 

    private List<ProductVariantDto> productVariants; // Danh sách biến thể sản phẩm
    private List<ImageDto> images; // Danh sách hình ảnh sản phẩm
    private List<MultipartFile> imageFiles;
    
    // Chỉ sử dụng khi trả về thông tin sản phẩm
    private CategoryDto category;
}
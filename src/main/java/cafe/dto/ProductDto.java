package cafe.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude;

import cafe.entity.Image;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor// Bao gồm các trường không null
// DTO là lớp trung gian, thường để bảo mật, này nọ
public class ProductDto implements Serializable {

    private long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be less than or equal to 255 characters")
    private String name;

    @NotNull(message = "Product active status is required")
    private Boolean active;
	 
	private Integer ordering; 

    @Size(max = 65535, message = "Description must be less than or equal to 65535 characters")
    private String description;
    
    @Size(max = 65535, message = "Description must be less than or equal to 65535 characters")
    private String slug;

    // Chỉ sử dụng khi tạo mới hoặc cập nhật sản phẩm
    private Long categoryId; 

    private List<ProductVariantDto> productVariants;
    private List<ProductToppingDto> productToppings;	
    
//    private Long productVariantId;
//    private Long ProductToppingId;
    
//    private List<ImageDto> images; // Danh sách hình ảnh sản phẩm
    @NotEmpty(message = "At least one image is required")
    private List<MultipartFile> imageFiles;
    private List<Image> images;

    
    private List<ImageDto> imagesDto;
    // Thêm trường tổng số lượng bán
    private Long totalSales;  
    // Chỉ sử dụng khi trả về thông tin sản phẩm
    private CategoryDto category;
    
    @Override
    public int hashCode() {
        return Objects.hash(id);  // Chỉ sử dụng id hoặc trường duy nhất, không sử dụng các quan hệ để tránh vòng lặp
    }
}
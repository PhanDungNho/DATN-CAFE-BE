package cafe.dto;

import java.io.Serializable;

import cafe.entity.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ImageDto implements Serializable {

    private Long id;
    private String uid;

    @NotBlank(message = "Image name is required")
    @Size(max = 255, message = "Image name must be less than or equal to 255 characters")
    private String name;
    
    private String fileName;
    private String url;
    private String status;
    private String response = "{\"status:\" \"sucess\"}";
    private Long productId;
    
    public static ImageDto convert(Image image) {
        ImageDto dto = new ImageDto();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setFileName(image.getFileName());
        dto.setProductId(image.getProduct().getId());
        return dto;
    }
}

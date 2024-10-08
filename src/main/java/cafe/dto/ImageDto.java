package cafe.dto;

import java.io.Serializable;

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
    
    private String filename;
    private String url;
    private String status;
    private String response = "{\"status:\" \"sucess\"}";
}

package cafe.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Data
public class CarDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private List<ImageeDto> images; // Sử dụng ImageeDto ở đây
    private List<MultipartFile> imageFiles;
}

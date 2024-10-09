package cafe.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cafe.entity.Imagee;

@Data
public class CarDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private List<ImageeDto> images; // Sử dụng ImageeDto ở đây
    private List<MultipartFile> imageFiles;
    private List<Imagee> imagees;
}

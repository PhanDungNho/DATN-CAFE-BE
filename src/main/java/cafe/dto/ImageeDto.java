package cafe.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ImageeDto implements Serializable {
    private Long id;
    private String url;
}

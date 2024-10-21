package cafe.modal;

import cafe.entity.Image;
import cafe.entity.Product;
import lombok.Data;

@Data
public class ImageResponse {
	private Long id;
	private String name;
	private String filename;
	private String url;
	private Product product;
	
	public static ImageResponse convert(Image entity) {
		ImageResponse response = new ImageResponse();
		
		response.setId(entity.getId());
		response.setName(entity.getName());
		response.setFilename(entity.getFileName());
		response.setUrl(entity.getUrl());
		response.setProduct(entity.getProduct());
		
		return response;
	}
}

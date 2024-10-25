package cafe.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cafe.dto.CategoryDto;
import cafe.dto.ImageDto;
import cafe.dto.ProductDto;
import cafe.dto.ProductToppingDto;
import cafe.dto.ProductVariantDto;
import cafe.dto.SizeDto;
import cafe.dto.ToppingDto;
import cafe.entity.Category;
import cafe.entity.Image;
import cafe.entity.Product;
import cafe.entity.ProductToppings;
import cafe.entity.ProductVariant;
import cafe.exception.EntityException;
import cafe.exception.FileStorageException;
import cafe.modal.OrderResponse;
import cafe.modal.ProductResponse;
import cafe.service.FileStorageService;
import cafe.service.ImageService;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin("*")
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	ImageService imageService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createProduct(@ModelAttribute ProductDto dto, BindingResult result,
			@RequestPart List<MultipartFile> imageFiles) {
		ResponseEntity<?> response = mapValidationErrorService.mapValidationField(result);
	    if (response != null) {
	        return response;
	    }

	    if (dto.getProductToppings() == null) {
	        dto.setProductToppings(new ArrayList<>());
	    }

	    if (dto.getProductVariants() == null) {
	        dto.setProductVariants(new ArrayList<>());
	    }

	    try {
	        Product product = productService.insertProduct(dto);
	        ProductDto respDto = new ProductDto();
	        respDto.setSlug(null);
	        
	        List<ProductToppingDto> productToppingDto = product.getProductToppings().stream()
	        		.map(this::convertProductTopingDto)	  
	        		.collect(Collectors.toList());
	        respDto.setProductToppings(productToppingDto);
	        
	        List<ProductVariantDto> productVariantDto = product.getProductVariants().stream()
	        		.map(this::convertProductVariantDto)
	        		.collect(Collectors.toList());
	        respDto.setProductVariants(productVariantDto);
	        
	        BeanUtils.copyProperties(product, respDto);

	        return new ResponseEntity<>(respDto, HttpStatus.CREATED);
	    } catch (EntityException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	    }
	}
	
	private ProductToppingDto convertProductTopingDto(ProductToppings productTopping) {
	    if (productTopping == null) {
	        return null; // Hoặc có thể ném một exception tùy theo yêu cầu
	    }

	    ProductToppingDto dto = new ProductToppingDto();
	    dto.setId(productTopping.getId());
	    dto.setProductId(productTopping.getProduct().getId());
	    dto.setToppingId(productTopping.getTopping().getId());

	    return dto;
	}
	
	private ProductVariantDto convertProductVariantDto(ProductVariant productVariant) {
	    if (productVariant == null) {
	        return null; // Hoặc ném một exception tùy theo yêu cầu
	    }

	    ProductVariantDto dto = new ProductVariantDto();
	    dto.setId(productVariant.getId());
	    dto.setActive(productVariant.getActive());
	    dto.setProductId(productVariant.getProduct().getId());
	    dto.setSizeId(productVariant.getSize().getId());
	    dto.setPrice(productVariant.getPrice());
	   
	    return dto;
	}
	
	@PatchMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @ModelAttribute ProductDto dto,
			@RequestPart(required = false) List<MultipartFile> imageFiles) {

		Product updatedProduct = productService.updateProduct(id, dto);

		// Cập nhật lại thông tin DTO sau khi cập nhật sản phẩm
		dto.setImageFiles(null);
		dto.setImages(updatedProduct.getImages());

		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@PatchMapping("/{id}/toggle-active")
	public ResponseEntity<?> toggleActive(@PathVariable Long id) {
		Product updatedProduct = productService.toggleActive(id);

		// Tạo ProductDto để trả về thông tin sản phẩm sau khi đã toggle active
		ProductDto responseDto = new ProductDto();
		responseDto.setId(updatedProduct.getId());
		responseDto.setName(updatedProduct.getName());
		responseDto.setActive(updatedProduct.getActive());
		responseDto.setDescription(updatedProduct.getDescription());

		// Map Category entity sang CategoryDto
		if (updatedProduct.getCategory() != null) {
			CategoryDto categoryDto = new CategoryDto();
			categoryDto.setId(updatedProduct.getCategory().getId());
			categoryDto.setName(updatedProduct.getCategory().getName());
			responseDto.setCategory(categoryDto);
		}
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> getProducts() {
		List<Product> products = productService.findAll();
		List<ProductDto> productDtos = products.stream().map(product -> {
			ProductDto dto = new ProductDto();
			dto.setId(product.getId());
			dto.setName(product.getName());
			dto.setActive(product.getActive());
			dto.setDescription(product.getDescription());

			// Ánh xạ danh mục (category) nếu tồn tại
			if (product.getCategory() != null) {
				CategoryDto categoryDto = new CategoryDto();
				categoryDto.setId(product.getCategory().getId());
				categoryDto.setName(product.getCategory().getName());
				dto.setCategory(categoryDto);
			}

			// Ánh xạ danh sách product variants
			List<ProductVariantDto> variantDtos = product.getProductVariants().stream().map(variant -> {
				ProductVariantDto variantDto = new ProductVariantDto();
				variantDto.setId(variant.getId());
				variantDto.setActive(variant.getActive());
				variantDto.setProductId(variant.getProduct().getId());
				variantDto.setSizeId(variant.getSize().getId());

				// Ánh xạ thông tin ProductDto vào ProductVariantDto
				ProductDto productDto = new ProductDto();
				productDto.setId(variant.getProduct().getId());
				productDto.setName(variant.getProduct().getName());
				variantDto.setProduct(productDto);

				// Ánh xạ thông tin SizeDto vào ProductVariantDto
				SizeDto sizeDto = new SizeDto();
				sizeDto.setId(variant.getSize().getId());
				sizeDto.setName(variant.getSize().getName());
				sizeDto.setActive(variant.getSize().getActive());
				variantDto.setSize(sizeDto);

				// Ánh xạ giá sản phẩm biến thể
				variantDto.setPrice(variant.getPrice());

				return variantDto;
			}).toList();
			
			List<ProductToppingDto> productToppingDtos = product.getProductToppings().stream().map(productTopping -> {
				ProductToppingDto productToppingDto = new ProductToppingDto();
				productToppingDto.setId(productTopping.getId());
				productToppingDto.setProductId(productTopping.getProduct().getId());
				productToppingDto.setToppingId(productTopping.getTopping().getId());
				
				ToppingDto toppingDto = new ToppingDto();
				toppingDto.setId(productTopping.getTopping().getId());
				toppingDto.setName(productTopping.getTopping().getName());
				toppingDto.setPrice(productTopping.getTopping().getPrice());
				toppingDto.setActive(productTopping.getTopping().getActive());
				toppingDto.setImage(productTopping.getTopping().getImage());
				
				productToppingDto.setTopping(toppingDto);
				
				return productToppingDto;
			}).toList();

			List<Image> imageDtos = product.getImages().stream().map(images -> {
				Image imageDto = new Image();
				imageDto.setId(images.getId());
				imageDto.setName(images.getName());
				imageDto.setFileName(images.getFileName());
				imageDto.setUrl(images.getUrl());
				imageDto.setProduct(images.getProduct());

				return imageDto;
			}).toList();

			dto.setProductVariants(variantDtos);
			dto.setImages(imageDtos);
			dto.setProductToppings(productToppingDtos);

			return dto;
		}).sorted(Comparator.comparing(ProductDto::getId).reversed()).toList();

		return new ResponseEntity<>(productDtos, HttpStatus.OK);
	}

	// cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getProducts(
			@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(productService.findAll(pageable), HttpStatus.OK);
	}

	@GetMapping("/{id}/get")
	public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
		return productService.findById(id).map(product -> ResponseEntity.ok(ProductResponse.convert(product)))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ProductResponse()));
	}

	@GetMapping("/find")
	public ResponseEntity<?> getProductByName(@RequestParam("query") String query) {
		return new ResponseEntity<>(productService.findProductByName(query), HttpStatus.OK);
	}

	@GetMapping("/images/{filename:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request) {
		Resource resource = fileStorageService.loadLogoFileResource(filename);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			throw new EntityException("Could not determine file type");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@Transactional
	@DeleteMapping("/images/{filename}")
	public ResponseEntity<?> deleteImageByFilename(@PathVariable String filename) {
		imageService.deleteImageByFilename(filename);
		fileStorageService.deleteLogoFile(filename);

		return new ResponseEntity<>("removed", HttpStatus.OK);
	}

	@PostMapping(value = "/images/one", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile imageFile) {

		var fileInfo = fileStorageService.storeUploadedProductImageFile(imageFile);
		ImageDto dto = new ImageDto();
		BeanUtils.copyProperties(fileInfo, dto);

		dto.setStatus("done");
		dto.setUrl("http://localhost:8081/api/v1/products/images/" + fileInfo.getFileName());
		System.out.println(fileInfo.getFileName());

		System.out.println(dto.getUrl());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

}

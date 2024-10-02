package cafe.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.CategoryDto;
import cafe.dto.ProductDto;
import cafe.entity.Category;
import cafe.entity.Product;

import cafe.service.MapValidationErrorService;
import cafe.service.ProductService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin // cho phép truy cập tất cả
public class ProductController {

	@Autowired
	ProductService productService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Product product = productService.save(productDto);
		// Tạo ProductDto từ Product và trả về
		ProductDto responseDto = new ProductDto();
		responseDto.setId(product.getId());
		responseDto.setName(product.getName());
		responseDto.setActive(product.getActive());
		responseDto.setDescription(product.getDescription());
		// Map Category entity sang CategoryDto
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(product.getCategory().getId());
		categoryDto.setName(product.getCategory().getName());
		responseDto.setCategory(categoryDto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	// cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
	    // Gọi service để cập nhật sản phẩm với ProductDto
	    Product updatedProduct = productService.update(id, dto);
	    
	    // Tạo ProductDto để trả về thông tin sản phẩm đã cập nhật
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

	        if (product.getCategory() != null) {
	            CategoryDto categoryDto = new CategoryDto();
	            categoryDto.setId(product.getCategory().getId());
	            categoryDto.setName(product.getCategory().getName());
	            dto.setCategory(categoryDto);
	        }

	        return dto;
	    }).toList();

	    return new ResponseEntity<>(productDtos, HttpStatus.OK);
	}

	// cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getProducts(
			@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(productService.findAll(pageable), HttpStatus.OK);
	}  
//
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getCategories(@PathVariable("id") Long id) {
		return new ResponseEntity<>(productService.findById(id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
		productService.deleteById(id);
		return new ResponseEntity<>("Product with Id: " + id + " was deleted", HttpStatus.OK);
	}
}

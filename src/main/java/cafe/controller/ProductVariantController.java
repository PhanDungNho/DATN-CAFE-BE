package cafe.controller;

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
import cafe.dto.ProductVariantDto;
import cafe.dto.SizeDto;
import cafe.entity.Category;
import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.entity.Size;
import cafe.entity.exception.EntityException;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductService;
import cafe.service.ProductVariantService;
import cafe.service.SizeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/productvariants")
@CrossOrigin
public class ProductVariantController {

	@Autowired
	ProductVariantService productVariantService;
	@Autowired
	ProductService productService;
	@Autowired
	SizeService sizeService;
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createProductVariant(@Valid @RequestBody ProductVariantDto productVariantDto, BindingResult result) {
	    // Kiểm tra lỗi validation
	    ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
	    if (responseEntity != null) {
	        return responseEntity;
	    }

	    // Lưu ProductVariant thông qua service
	    ProductVariant productVariant = productVariantService.save(productVariantDto);

	    // Tạo ProductVariantDto từ ProductVariant và trả về
	    ProductVariantDto responseDto = new ProductVariantDto();
	    responseDto.setId(productVariant.getId());
	    responseDto.setActive(productVariant.getActive());
	    responseDto.setPrice(productVariant.getPrice());

	    // Map Product entity sang ProductDto
	    ProductDto productDto = new ProductDto();
	    productDto.setId(productVariant.getProduct().getId());
	    productDto.setName(productVariant.getProduct().getName());
	    productDto.setActive(productVariant.getProduct().getActive());
	    productDto.setDescription(productVariant.getProduct().getDescription());
	    responseDto.setProductid(productDto.getId());

	    // Map Size entity sang SizeDto
	    SizeDto sizeDto = new SizeDto();
	    sizeDto.setId(productVariant.getSize().getId());
	    sizeDto.setName(productVariant.getSize().getName());
	    responseDto.setSizeid(sizeDto.getId());

	    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}


	//cập nhật
	// cập nhật ProductVariant
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateProductVariant(@PathVariable Long id, @Valid @RequestBody ProductVariantDto dto, BindingResult result) {
	    ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
	    if (responseEntity != null) {
	        return responseEntity;
	    }

	    // Gọi service để cập nhật ProductVariant
	    ProductVariant updatedProductVariant = productVariantService.update(id, dto);

	    // Tạo ProductVariantDto để trả về thông tin ProductVariant đã cập nhật
	    ProductVariantDto responseDto = new ProductVariantDto();
	    responseDto.setId(updatedProductVariant.getId());
	    responseDto.setPrice(updatedProductVariant.getPrice());
	    responseDto.setActive(updatedProductVariant.getActive());

	    // Map Product và Size sang ProductDto và SizeDto
	    if (updatedProductVariant.getProduct() != null) {
	        responseDto.setProductid(updatedProductVariant.getProduct().getId());
	    }
	    if (updatedProductVariant.getSize() != null) {
	        responseDto.setSizeid(updatedProductVariant.getSize().getId());
	    }

	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}


//	@PatchMapping("/{id}")
//	public ResponseEntity<?> updateProductVariant(@PathVariable Long id, @RequestBody ProductVariantDto dto) {
//		ProductVariant entity = new ProductVariant();
//		BeanUtils.copyProperties(dto, entity);
//		entity = productVariantService.update(id, entity);
//		dto.setId(entity.getId());
//		return new ResponseEntity<>(dto, HttpStatus.CREATED);
//
//	}

	@GetMapping()
	public ResponseEntity<?> getProductVariants() {
		return new ResponseEntity<>(productVariantService.findAll(), HttpStatus.OK);
	}
	
	//cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getProducts(
			@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(productVariantService.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getCategories(@PathVariable("id") Long id){
		return new ResponseEntity<>(productVariantService.findById(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
		productVariantService.deleteById(id);
		return new ResponseEntity<>("Product with Id: "+id+ " was deleted",HttpStatus.OK);
	}
}

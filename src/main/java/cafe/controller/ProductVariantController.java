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
import cafe.entity.Category;
import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductVariantService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/productvariants")
@CrossOrigin
public class ProductVariantController {

	@Autowired
	ProductVariantService productVariantService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createProductVariant(@Valid @RequestBody ProductVariantDto dto, BindingResult result) {

		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		//		if(true) {
//			throw new CategoryException("Category is error");
//		}
		ProductVariant entity = new ProductVariant();
		BeanUtils.copyProperties(dto, entity);
		entity = productVariantService.save(entity);

		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}
//Test code
	//cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> UpdateProductVariant(@PathVariable Long id, @RequestBody ProductVariantDto dto) {
		ProductVariant entity = new ProductVariant();
		BeanUtils.copyProperties(dto, entity);
		entity = productVariantService.update(id, entity);
		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

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

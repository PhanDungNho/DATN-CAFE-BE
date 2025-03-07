package cafe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.CategoryDto;
import cafe.entity.Category;
import cafe.service.CategoryService;
import cafe.service.MapValidationErrorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin("*")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto dto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);
		entity = categoryService.save(entity);

		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	//cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);
		entity = categoryService.update(id, entity);
		
		 Map<String, String> response = new HashMap<>();
		    response.put("message", "Size " + entity.getId() + " đã được update.");

		    return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}
	
	
	@PatchMapping("/{id}/toggle-active")
	public ResponseEntity<Map<String, String>> updateCategoryActive(@PathVariable Long id) {
	    Category updatedCate = categoryService.toggleActive(id);
	    
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Size " + (updatedCate.getActive() ? "activated" : "deactivated") + " successfully.");

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}

 
	@GetMapping()
	public ResponseEntity<?> getCategories() {
		return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
	}
	
	//cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getCategories(
			@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(categoryService.findAll(pageable), HttpStatus.OK);
	}
	
 
	
	@GetMapping("/find")
	public ResponseEntity<?> getCategoryByName(@RequestParam("query") String query){
		return new ResponseEntity<>(categoryService.findCategoryByName(query), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getCategories(@PathVariable("id") Long id){
		return new ResponseEntity<>(categoryService.findById(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
		categoryService.deleteById(id);
		return new ResponseEntity<>("Category with Id: "+id+ " was deleted",HttpStatus.OK);
	}
}

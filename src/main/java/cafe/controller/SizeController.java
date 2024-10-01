package cafe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.SizeDto;
import cafe.entity.Category;
import cafe.entity.Size;
import cafe.service.MapValidationErrorService;
import cafe.service.SizeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sizes")
@CrossOrigin
public class SizeController {

	@Autowired
	SizeService sizeService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createSize(@Valid @RequestBody SizeDto dto, BindingResult result) {

		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Size entity = new Size();
		BeanUtils.copyProperties(dto, entity);
		entity = sizeService.save(entity);

		dto.setId(entity.getId());
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Đã thêm sản phẩm thành công.");

	    return new ResponseEntity<>(response, HttpStatus.OK);
 

	}

	//cập nhật
//	@PatchMapping("/{id}")
//	public ResponseEntity<?> updateSize(@PathVariable Long id, @RequestBody SizeDto dto) {
//		Size entity = new Size();
//		BeanUtils.copyProperties(dto, entity);
//		entity = sizeService.update(id, entity);
//		dto.setId(entity.getId());
//		return new ResponseEntity<>(dto, HttpStatus.CREATED);
//	}

	@PatchMapping("/{id}/toggle-active")
	public ResponseEntity<Map<String, String>> updateCategoryActive(@PathVariable Long id) {
	    Size updatedSize = sizeService.toggleActive(id);
	    
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Size " + (updatedSize.getActive() ? "activated" : "deactivated") + " successfully.");

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<?> getSizes() {
		return new ResponseEntity<>(sizeService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> getSizes(
			@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(sizeService.findAll(pageable), HttpStatus.OK);
	}
	
	
	@GetMapping("/find")
	public ResponseEntity<?> getCategoryByName(@RequestParam("query") String query){
		return new ResponseEntity<>(sizeService.findSizeByName(query), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getSizes(@PathVariable("id") Long id){
		return new ResponseEntity<>(sizeService.findById(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSizes(@PathVariable("id") Long id){
		sizeService.deleteById(id);
		return new ResponseEntity<>("Size with Id: "+id+ " was deleted",HttpStatus.OK);
	}

}

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.AccountDto;
import cafe.dto.ToppingDto;
import cafe.entity.Account;
import cafe.entity.Size;
import cafe.entity.Topping;
import cafe.service.MapValidationErrorService;
import cafe.service.ToppingService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/toppings")
@CrossOrigin
public class ToppingController {
	@Autowired
	private ToppingService toppingService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

//	@PostMapping()
//	public ResponseEntity<?> createTopping(@Valid @RequestBody ToppingDto dto, BindingResult reslut) {
//		ResponseEntity<?> response = mapValidationErrorService.mapValidationField(reslut);
//
//		if (response != null) {
//			return response;
//		}
//
//		Topping entity = new Topping();
//		BeanUtils.copyProperties(dto, entity);
//		entity = toppingService.save(entity);
//
//		return new ResponseEntity<>(dto, HttpStatus.CREATED);
//	}
	
	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTopping(@Valid @ModelAttribute ToppingDto dto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Topping topping = toppingService.insertTopping(dto);
 
		dto.setImage(topping.getImage());
		// trả về người dùng responseDto
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@GetMapping()
	public ResponseEntity<?> getToppings() {
		return new ResponseEntity<>(toppingService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{id}/get")
	public ResponseEntity<?> getTopping(@PathVariable Long id) {
		return new ResponseEntity<>(toppingService.findById(id), HttpStatus.OK);
	}
	
	@GetMapping("/find")
	public ResponseEntity<?> getToppingsByName(@RequestParam("query") String query, 
			@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(toppingService.findToppingsByName(query, pageable), HttpStatus.OK);
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> getTopping(
			@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(toppingService.findAll(pageable), HttpStatus.OK);
	}
	@PatchMapping("/{id}/toggle-active")
	public ResponseEntity<Map<String, String>> updateToppingActive(@PathVariable Long id) {
	    Topping updatedTopping = toppingService.toggleActive(id);
	    
	    Map<String, String> response = new HashMap<>();
	    response.put("message", "Topping " + (updatedTopping.getActive() ? "activated" : "deactivated") + " successfully.");

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateTopping(
			@PathVariable("id") Long id,
			@RequestBody ToppingDto dto) {

		Topping entity = new Topping();
		BeanUtils.copyProperties(dto, entity);

		entity = toppingService.update(id, entity);
		
		dto.setId(entity.getId());

		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
}

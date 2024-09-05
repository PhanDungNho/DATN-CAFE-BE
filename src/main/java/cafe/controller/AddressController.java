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

import cafe.dto.AddressDto;
import cafe.dto.CategoryDto;
import cafe.entity.Address;
import cafe.entity.Category;
import cafe.service.AddressService;
import cafe.service.CategoryService;
import cafe.service.MapValidationErrorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/address")
@CrossOrigin
public class AddressController {
	@Autowired
	AddressService addressService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createAddress(@Valid @RequestBody AddressDto dto, BindingResult result) {

		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		//		if(true) {
//			throw new CategoryException("Category is error");
//		}
		Address entity = new Address();
		BeanUtils.copyProperties(dto, entity);
		entity = addressService.save(entity);

		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	//cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressDto dto) {
		Address entity = new Address();
		BeanUtils.copyProperties(dto, entity);
		entity = addressService.update(id, entity);
		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	@GetMapping()
	public ResponseEntity<?> getAddress() {
		return new ResponseEntity<>(addressService.findAll(), HttpStatus.OK);
	}
	
	//cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getAddress(
			@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(addressService.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping("/{id}/get")
	public ResponseEntity<?> getAddress(@PathVariable("id") Long id){
		return new ResponseEntity<>(addressService.findById(id),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id){
		addressService.deleteById(id);
		return new ResponseEntity<>("Address with Id: "+id+ " was deleted",HttpStatus.OK);
	}
}

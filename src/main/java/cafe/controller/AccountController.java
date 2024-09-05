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

import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.entity.Size;
import cafe.service.AccountService;
import cafe.service.MapValidationErrorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@PostMapping
	public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDto dto, BindingResult result) {

		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		//		if(true) {
//			throw new CategoryException("Category is error");
//		}
		Account entity = new Account();
		BeanUtils.copyProperties(dto, entity);
		entity = accountService.save(entity);

		dto.setUsername(entity.getUsername());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}
	
	//cập nhật
		@PatchMapping("/{username}")
		public ResponseEntity<?> updateAccounts(@PathVariable String username, @RequestBody AccountDto dto) {
			Account entity = new Account();
			BeanUtils.copyProperties(dto, entity);
			entity = accountService.update(username, entity);
			dto.setUsername(entity.getUsername());
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		}
	
	@GetMapping()
	public ResponseEntity<?> getAccounts() {
		return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
	}
	
	//cái này để phân trang
		@GetMapping("/page")
		public ResponseEntity<?> getAccounts(
				@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
			return new ResponseEntity<>(accountService.findAll(pageable), HttpStatus.OK);
		}
		
		@GetMapping("/{id}/get")
		public ResponseEntity<?> getAccounts(@PathVariable("id") String username){
			return new ResponseEntity<>(accountService.findById(username),HttpStatus.OK);
		}
		
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deleteAccounts(@PathVariable("id") String username){
			accountService.deleteById(username);
			return new ResponseEntity<>("Role with Id: "+username+ " was deleted",HttpStatus.OK);
		}
}

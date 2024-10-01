package cafe.controller;


import java.util.List;
import java.util.Optional;

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

import cafe.dto.AccountDto;
import cafe.dto.AuthorityDto;
import cafe.dto.CategoryDto;
import cafe.dto.ProductDto;
import cafe.dto.RoleDto;
import cafe.entity.Authority;
import cafe.entity.Product;

import cafe.service.AccountService;
import cafe.service.AuthorityService;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductService;
import cafe.service.RoleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/authorities")
@CrossOrigin
public class AuthorityController {

	@Autowired
	AuthorityService authorityService;
	
	@GetMapping
	public List<Authority> findAll(@RequestParam("admin") Optional<Boolean> admin){
		if(admin.orElse(false)) {
			return authorityService.findAuthorityOfAdministrator();
		}
		return authorityService.findAll();
	}
	
	//trao quyền
	@PostMapping
	public ResponseEntity<?> post(@RequestBody AuthorityDto dto) {
		 authorityService.create(dto);
		 AuthorityDto responseDto = new AuthorityDto();
		 responseDto.setId(dto.getId());
		 responseDto.setRoleid(dto.getRoleid());
		 responseDto.setUsername(dto.getUsername());
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
		
	}
	
	// tước quyền
	@DeleteMapping("{id}")
	public  ResponseEntity<?>  delete(@PathVariable("id") Long id) {
		 authorityService.delete(id);
		 return new ResponseEntity<>("Access revoked", HttpStatus.OK);
		 
		
	}
}

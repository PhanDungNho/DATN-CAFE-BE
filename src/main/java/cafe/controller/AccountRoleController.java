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

import cafe.dto.AccountDto;
import cafe.dto.AccountRoleDto;
import cafe.dto.CategoryDto;
import cafe.dto.ProductDto;
import cafe.dto.RoleDto;
import cafe.entity.AccountRole;
import cafe.entity.Product;
import cafe.service.AccountRoleService;
import cafe.service.AccountService;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductService;
import cafe.service.RoleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accountroles")
@CrossOrigin
public class AccountRoleController {

	@Autowired
	AccountRoleService accountRoleService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> createAccountRole(@Valid @RequestBody AccountRoleDto accountRoleDto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		AccountRole accountRole = accountRoleService.save(accountRoleDto);
		// Tạo ProductDto từ Product và trả về
		AccountRoleDto responseDto = new AccountRoleDto();
		responseDto.setId(accountRole.getId());
		
		AccountDto accountDto = new AccountDto();
		accountDto.setUsername(accountRole.getAccount().getUsername());
		accountDto.setPassword(accountRole.getAccount().getPassword());
		accountDto.setAmountpaid(accountRole.getAccount().getAmountpaid());
		accountDto.setEmail(accountRole.getAccount().getEmail());
		accountDto.setFullname(accountRole.getAccount().getFullname());
		accountDto.setPhone(accountRole.getAccount().getPhone());
		accountDto.setActive(accountRole.getAccount().getActive());
		
		responseDto.setAccount(accountDto.getUsername());
		
		
		RoleDto roleDto = new RoleDto();
		roleDto.setId(accountRole.getRole().getId());
		roleDto.setActive(accountRole.getRole().getActive());
		roleDto.setRolename(accountRole.getRole().getRolename());
		
		responseDto.setRoleid(roleDto.getId());
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	// cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateAccountRole(@PathVariable Long id, @RequestBody AccountRoleDto dto) {
	    // Gọi service để cập nhật sản phẩm với ProductDto
	    AccountRole updatedAccountRole = accountRoleService.update(id, dto);
	    
	    AccountRoleDto responseDto = new AccountRoleDto();
		responseDto.setId(updatedAccountRole.getId());

	    // Map Category entity sang CategoryDto
	    if (updatedAccountRole.getAccount() != null) {
	    	AccountDto accountDto = new AccountDto();
			accountDto.setUsername(updatedAccountRole.getAccount().getUsername());
			accountDto.setPassword(updatedAccountRole.getAccount().getPassword());
			accountDto.setAmountpaid(updatedAccountRole.getAccount().getAmountpaid());
			accountDto.setEmail(updatedAccountRole.getAccount().getEmail());
			accountDto.setFullname(updatedAccountRole.getAccount().getFullname());
			accountDto.setPhone(updatedAccountRole.getAccount().getPhone());
			accountDto.setActive(updatedAccountRole.getAccount().getActive());
			
			responseDto.setAccount(accountDto.getUsername());
	    }
	    
	    if (updatedAccountRole.getRole() != null) {
	    	RoleDto roleDto = new RoleDto();
			roleDto.setId(updatedAccountRole.getRole().getId());
			roleDto.setActive(updatedAccountRole.getRole().getActive());
			roleDto.setRolename(updatedAccountRole.getRole().getRolename());
			
			responseDto.setRoleid(roleDto.getId());
	    }

	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
//		@PatchMapping("/{id}/toggle-active")
//		public ResponseEntity<?> toggleActive(@PathVariable Long id) {
//		    Product updatedProduct = productService.toggleActive(id);
//	
//		    // Tạo ProductDto để trả về thông tin sản phẩm sau khi đã toggle active
//		    ProductDto responseDto = new ProductDto();
//		    responseDto.setId(updatedProduct.getId());
//		    responseDto.setName(updatedProduct.getName());
//		    responseDto.setActive(updatedProduct.getActive());
//		    responseDto.setDescription(updatedProduct.getDescription());
//	
//		    // Map Category entity sang CategoryDto
//		    if (updatedProduct.getCategory() != null) {
//		        CategoryDto categoryDto = new CategoryDto();
//		        categoryDto.setId(updatedProduct.getCategory().getId());
//		        categoryDto.setName(updatedProduct.getCategory().getName());
//		        responseDto.setCategory(categoryDto);
//		    }
//		    return new ResponseEntity<>(responseDto, HttpStatus.OK);
//		}

	@GetMapping()
		public ResponseEntity<?> getAccountRole() {
	    List<AccountRole> accountRoles = accountRoleService.findAll();

	    List<AccountRoleDto> accountRoleDtos = accountRoles.stream().map(accountrole -> {
	    	AccountRoleDto dto = new AccountRoleDto();
	        dto.setId(accountrole.getId());
	        
	        if (accountrole.getAccount() != null) {
	            AccountDto accountDto = new AccountDto();
	            accountDto.setUsername(accountrole.getAccount().getUsername());
	            accountDto.setPassword(accountrole.getAccount().getPassword());
	            accountDto.setFullname(accountrole.getAccount().getFullname());
	            accountDto.setActive(accountrole.getAccount().getActive());
	            accountDto.setAmountpaid(accountrole.getAccount().getAmountpaid());
	            accountDto.setPhone(accountrole.getAccount().getPhone());
	            accountDto.setEmail(accountrole.getAccount().getEmail());
	            dto.setAccount(accountDto.getUsername());
	        }
	        
	        if(accountrole.getRole() != null) {
	        	RoleDto roleDto = new RoleDto();
	        	roleDto.setId(accountrole.getRole().getId());
	        	roleDto.setActive(accountrole.getRole().getActive());
	        	roleDto.setRolename(accountrole.getRole().getRolename());
	        	dto.setRoleid(roleDto.getId());
	        }

	        return dto;
	    }).toList();

	    return new ResponseEntity<>(accountRoleDtos, HttpStatus.OK);
	}
	
		//cái này để phân trang
		@GetMapping("/page")
		public ResponseEntity<?> getRoles(
				@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
			return new ResponseEntity<>(accountRoleService.findAll(pageable), HttpStatus.OK);
		}
		
		@GetMapping("/{id}/get")
		public ResponseEntity<?> getRoles(@PathVariable("id") Long id){
			return new ResponseEntity<>(accountRoleService.findById(id),HttpStatus.OK);
		}
		
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deleteRoles(@PathVariable("id") Long id){
			accountRoleService.deleteById(id);
			return new ResponseEntity<>("AccountRole with Id: "+id+ " was deleted",HttpStatus.OK);
		}
}

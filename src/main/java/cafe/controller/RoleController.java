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
import cafe.dto.RoleDto;
import cafe.dto.SizeDto;
import cafe.entity.Account;
import cafe.entity.Role;
import cafe.entity.Size;
import cafe.service.MapValidationErrorService;
import cafe.service.RoleService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin
public class RoleController {

	@Autowired
	RoleService roleService;
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@PostMapping
	public ResponseEntity<?> createRole(@Valid @RequestBody RoleDto dto, BindingResult result) {

		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Role entity = new Role();
		BeanUtils.copyProperties(dto, entity);
		entity = roleService.save(entity);

		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}
	
		//cập nhật
		@PatchMapping("/{id}")
		public ResponseEntity<?> updateSize(@PathVariable Long id, @RequestBody RoleDto dto) {
			Role entity = new Role();
			BeanUtils.copyProperties(dto, entity);
			entity = roleService.update(id, entity);
			dto.setId(entity.getId());
			return new ResponseEntity<>(dto, HttpStatus.CREATED);
		}
	
		//tim tat ca
		@GetMapping()
		public ResponseEntity<?> getRoles() {
			return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
		}
	
		//cái này để phân trang
		@GetMapping("/page")
		public ResponseEntity<?> getRoles(
				@PageableDefault(size=5,sort="name",direction = Sort.Direction.ASC) Pageable pageable) {
			return new ResponseEntity<>(roleService.findAll(pageable), HttpStatus.OK);
		}
		
		@GetMapping("/{id}/get")
		public ResponseEntity<?> getRoles(@PathVariable("id") Long id){
			return new ResponseEntity<>(roleService.findById(id),HttpStatus.OK);
		}
		
		@DeleteMapping("/{id}")
		public ResponseEntity<?> deleteRoles(@PathVariable("id") Long id){
			roleService.deleteById(id);
			return new ResponseEntity<>("Role with Id: "+id+ " was deleted",HttpStatus.OK);
		}
		
		
		@PatchMapping("/{id}/toggle-active")
		public ResponseEntity<?> toggleActive(@PathVariable("id") Long id) {
		    // Call the service to toggle the account's active status
		    Role updatedRole = roleService.toggleActive(id);

		    // Check if the account was found
		    if (updatedRole == null) {
		        return new ResponseEntity<>("Role not found", HttpStatus.NOT_FOUND);
		    }

		    // Create AccountDto to return updated account information
		    RoleDto responseDto = new RoleDto();
		    responseDto.setId(updatedRole.getId());
		    responseDto.setActive(updatedRole.getActive());
		    responseDto.setRolename(updatedRole.getRolename());
		    // Note: Remove the password from the response for security reasons

		    return new ResponseEntity<>(responseDto, HttpStatus.OK);
		}
}

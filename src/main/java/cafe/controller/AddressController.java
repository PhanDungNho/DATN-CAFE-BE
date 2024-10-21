package cafe.controller;

import java.util.List;

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
import cafe.dto.AddressDto;
import cafe.entity.Address;
import cafe.service.AddressService;
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
	public ResponseEntity<?> createProduct(@Valid @RequestBody AddressDto addressDto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Address address = addressService.save(addressDto);

		AddressDto responseDto = new AddressDto();
		responseDto.setId(address.getId());
		responseDto.setActive(address.getActive());
		responseDto.setCityCode(address.getCityCode());
		responseDto.setDistrictCode(address.getDistrictCode());
		responseDto.setFullAddress(address.getFullAddress());
		responseDto.setIsDefault(address.getIsDefault());
		responseDto.setStreet(address.getStreet());
		responseDto.setWardCode(address.getWardCode());
		// Map Category entity sang CategoryDto
		AccountDto accountDto = new AccountDto();
		accountDto.setUsername(address.getAccount().getUsername());
		accountDto.setPassword(address.getAccount().getPassword());
		accountDto.setAmountPaid(address.getAccount().getAmountPaid());
		accountDto.setEmail(address.getAccount().getEmail());
		accountDto.setFullName(address.getAccount().getFullName());
		accountDto.setPhone(address.getAccount().getPhone());
		accountDto.setActive(address.getAccount().getActive());
		
		responseDto.setAccount(accountDto.getUsername());
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	// cập nhật
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateAdress(@PathVariable Long id, @RequestBody AddressDto dto) {
	    // Gọi service để cập nhật sản phẩm với ProductDto
	    Address updatedAddress = addressService.update(id, dto);
	    
	    // Tạo ProductDto để trả về thông tin sản phẩm đã cập nhật
	    AddressDto responseDto = new AddressDto();
		responseDto.setId(updatedAddress.getId());
		responseDto.setActive(updatedAddress.getActive());
		responseDto.setCityCode(updatedAddress.getCityCode());
		responseDto.setDistrictCode(updatedAddress.getDistrictCode());
		responseDto.setFullAddress(updatedAddress.getFullAddress());
		responseDto.setIsDefault(updatedAddress.getIsDefault());
		responseDto.setStreet(updatedAddress.getStreet());
		responseDto.setWardCode(updatedAddress.getWardCode());

	    // Map Category entity sang CategoryDto
	    if (updatedAddress.getAccount() != null) {
	    	AccountDto accountDto = new AccountDto();
			accountDto.setUsername(updatedAddress.getAccount().getUsername());
			accountDto.setPassword(updatedAddress.getAccount().getPassword());
			accountDto.setAmountPaid(updatedAddress.getAccount().getAmountPaid());
			accountDto.setEmail(updatedAddress.getAccount().getEmail());
			accountDto.setFullName(updatedAddress.getAccount().getFullName());
			accountDto.setPhone(updatedAddress.getAccount().getPhone());
			accountDto.setActive(updatedAddress.getAccount().getActive());
			responseDto.setAccount(accountDto.getUsername());
	    }

	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	@PatchMapping("/{id}/toggle-active")
	public ResponseEntity<?> toggleActive(@PathVariable Long id) {
	    Address updatedAddress = addressService.toggleActive(id);

	    // Tạo ProductDto để trả về thông tin sản phẩm sau khi đã toggle active
	    AddressDto responseDto = new AddressDto();
		responseDto.setId(updatedAddress.getId());
		responseDto.setActive(updatedAddress.getActive());
		responseDto.setCityCode(updatedAddress.getCityCode());
		responseDto.setDistrictCode(updatedAddress.getDistrictCode());
		responseDto.setFullAddress(updatedAddress.getFullAddress());
		responseDto.setIsDefault(updatedAddress.getIsDefault());
		responseDto.setStreet(updatedAddress.getStreet());
		responseDto.setWardCode(updatedAddress.getWardCode());

	    // Map Category entity sang CategoryDto
		if (updatedAddress.getAccount() != null) {
	    	AccountDto accountDto = new AccountDto();
			accountDto.setUsername(updatedAddress.getAccount().getUsername());
			accountDto.setPassword(updatedAddress.getAccount().getPassword());
			accountDto.setAmountPaid(updatedAddress.getAccount().getAmountPaid());
			accountDto.setEmail(updatedAddress.getAccount().getEmail());
			accountDto.setFullName(updatedAddress.getAccount().getFullName());
			accountDto.setPhone(updatedAddress.getAccount().getPhone());
			accountDto.setActive(updatedAddress.getAccount().getActive());
			responseDto.setAccount(accountDto.getUsername());
	    }
	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> getProducts() {
	    List<Address> addresses = addressService.findAll();

	    List<AddressDto> addressDtos = addresses.stream().map(address -> {
	    	AddressDto dto = new AddressDto();
	        dto.setId(address.getId());
	        dto.setActive(address.getActive());
	        dto.setCityCode(address.getCityCode());
	        dto.setDistrictCode(address.getDistrictCode());
	        dto.setFullAddress(address.getFullAddress());
	        dto.setIsDefault(address.getIsDefault());
	        dto.setStreet(address.getStreet());
	        dto.setWardCode(address.getWardCode());

	        if (address.getAccount() != null) {
	        	 AccountDto accountDto = new AccountDto();
		            accountDto.setUsername(address.getAccount().getUsername());
		            accountDto.setPassword(address.getAccount().getPassword());
		            accountDto.setFullName(address.getAccount().getFullName());
		            accountDto.setActive(address.getAccount().getActive());
		            accountDto.setAmountPaid(address.getAccount().getAmountPaid());
		            accountDto.setPhone(address.getAccount().getPhone());
		            accountDto.setEmail(address.getAccount().getEmail());
		            dto.setAccount(accountDto.getUsername());
	        }
	        return dto;
	    }).toList();
	    return new ResponseEntity<>(addressDtos, HttpStatus.OK);
	}

	// cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getAddress(
			@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(addressService.findAll(pageable), HttpStatus.OK);
	}

	@GetMapping("/{id}/get")
	public ResponseEntity<?> getAddress(@PathVariable("id") Long id) {
		return new ResponseEntity<>(addressService.findById(id), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
		addressService.deleteById(id);
		return new ResponseEntity<>("Address with Id: " + id + " was deleted", HttpStatus.OK);
	}
}

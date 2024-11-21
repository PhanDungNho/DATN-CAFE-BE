package cafe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import cafe.dto.AddressDto;
import cafe.entity.Address;
import cafe.exception.EntityException;
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

//	@PostMapping
//    public ResponseEntity<?> createAddress(@Valid @RequestBody AddressDto addressDto, BindingResult result) {
//        if (result.hasErrors()) {
//            return new ResponseEntity<>("Validation errors", HttpStatus.BAD_REQUEST);
//        }
//
//        Address address = addressService.save(addressDto);
//        AddressDto responseDto = mapAddressToDto(address);
//        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
//    }
	
	@PostMapping
	public ResponseEntity<?> createAddress(@Valid @RequestBody AddressDto addressDto, BindingResult result) {
	    if (result.hasErrors()) {
        return new ResponseEntity<>("Validation errors", HttpStatus.BAD_REQUEST);
	    }

	    // Construct fullAddress from street, wardCode, and cityCode if fullAddress is null
	    if (addressDto.getFullAddress() == null || addressDto.getFullAddress().isEmpty()) {
	        String fullAddress = String.format("%s,%s,%s,%s",
	                                           addressDto.getStreet(), 
	                                           addressDto.getWardCode(), 
	                                           addressDto.getDistrictCode(),
	                                           addressDto.getCityCode());
	        addressDto.setFullAddress(fullAddress);
	    }

	    Address address = addressService.save(addressDto);
	    AddressDto responseDto = mapAddressToDto(address);
	    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}


    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody AddressDto dto) {
        Address updatedAddress = addressService.update(id, dto);
        AddressDto responseDto = mapAddressToDto(updatedAddress);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    private AddressDto mapAddressToDto(Address address) {
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
            dto.setAccount(address.getAccount().getUsername());
        }
        return dto;
    }
   
    @PatchMapping("/{id}/setIsDefault")
    public ResponseEntity<?> setIsDefault(@PathVariable Long id) {
        Address updatedAddress = addressService.setIsDefault(id);

        AddressDto responseDto = mapAddressToDto(updatedAddress);
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
	        dto.setFullAddress(address.getFullAddress());
	        dto.setIsDefault(address.getIsDefault());
	        dto.setStreet(address.getStreet());

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


	@GetMapping("/{username}/get")
    public ResponseEntity<?> findAddressesByAccountUsername(@PathVariable String username) {
        List<Address> addresses = addressService.findAddressesByAccountUsername(username);
        return ResponseEntity.ok(addresses);
    }

	
	@GetMapping("/find")
	public ResponseEntity<List<Address>> getAddressByName(@RequestParam("query") String query, Authentication authentication) {
	    // Lấy tên tài khoản từ session hoặc JWT token
	    String username = authentication.getName();  // Hoặc từ token nếu dùng JWT
	    List<Address> addresses = addressService.findAddressesByFullAddressAndAccount(query, username);
	    return ResponseEntity.ok(addresses);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
	    try {
	        addressService.deleteById(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (EntityException e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	    } catch (SecurityException e) {
	        return new ResponseEntity<>("Unauthorized to delete this address.", HttpStatus.UNAUTHORIZED);
	    }
	}


	
}

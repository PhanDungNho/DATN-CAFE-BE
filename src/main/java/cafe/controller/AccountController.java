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
import cafe.dto.CategoryDto;
import cafe.dto.ProductDto;
import cafe.entity.Account;
import cafe.entity.Product;
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
	public ResponseEntity<?> createAccount(@Valid @RequestBody AccountDto accountDto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Account account = accountService.save(accountDto);
		// Tạo ProductDto từ Product và trả về
		AccountDto responseDto = new AccountDto();
		responseDto.setUsername(account.getUsername());
		responseDto.setActive(account.getActive());
		responseDto.setAmountpaid(account.getAmountpaid());
		responseDto.setEmail(account.getEmail());
		responseDto.setFullname(account.getFullname());
		responseDto.setPassword(account.getPassword());
		responseDto.setPhone(account.getPhone());
		
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@PatchMapping("/{username}")
	public ResponseEntity<?> updateAccount(@PathVariable("username") String username, @RequestBody AccountDto dto) {
	    // Fetch the existing account from the service
	    Account existingAccount = accountService.findById(username);

	    // Check if the account exists
	    if (existingAccount == null) {
	        return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
	    }

	    // Update the fields of the existing account
	    existingAccount.setActive(dto.getActive());
	    existingAccount.setEmail(dto.getEmail());
	    existingAccount.setFullname(dto.getFullname());
	    existingAccount.setPassword(dto.getPassword());
	    existingAccount.setPhone(dto.getPhone());
	    existingAccount.setAmountpaid(dto.getAmountpaid());

	    // Persist the changes
	    Account updatedAccount = accountService.update(existingAccount);

	    // Prepare the response DTO
	    AccountDto responseDto = new AccountDto();
	    responseDto.setUsername(updatedAccount.getUsername());
	    responseDto.setActive(updatedAccount.getActive());
	    responseDto.setAmountpaid(updatedAccount.getAmountpaid());
	    responseDto.setEmail(updatedAccount.getEmail());
	    responseDto.setFullname(updatedAccount.getFullname());
	    responseDto.setPassword(updatedAccount.getPassword());
	    responseDto.setPhone(updatedAccount.getPhone());

	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	
	@PatchMapping("/{username}/toggle-active")
	public ResponseEntity<?> toggleActive(@PathVariable("username") String username) {
	    // Call the service to toggle the account's active status
	    Account updatedAccount = accountService.toggleActive(username);

	    // Check if the account was found
	    if (updatedAccount == null) {
	        return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
	    }

	    // Create AccountDto to return updated account information
	    AccountDto responseDto = new AccountDto();
	    responseDto.setUsername(updatedAccount.getUsername());
	    responseDto.setActive(updatedAccount.getActive());
	    responseDto.setEmail(updatedAccount.getEmail());
	    responseDto.setFullname(updatedAccount.getFullname());
	    responseDto.setPhone(updatedAccount.getPhone());
	    responseDto.setAmountpaid(updatedAccount.getAmountpaid());
	    
	    // Note: Remove the password from the response for security reasons

	    return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> getAccounts() {
	    List<Account> accounts = accountService.findAll();

	    List<AccountDto> accountDtos = accounts.stream().map(account -> {
	    	AccountDto dto = new AccountDto();
	        dto.setUsername(account.getUsername());
	        dto.setActive(account.getActive());
	        dto.setAmountpaid(account.getAmountpaid());
	        dto.setEmail(account.getEmail());
	        dto.setPassword(account.getPassword());
	        dto.setFullname(account.getFullname());
	        dto.setPhone(account.getPhone());

	        return dto;
	    }).toList();
	    return new ResponseEntity<>(accountDtos, HttpStatus.OK);
	}

	// cái này để phân trang
	@GetMapping("/page")
	public ResponseEntity<?> getAccounts(
			@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return new ResponseEntity<>(accountService.findAll(pageable), HttpStatus.OK);
	}

	@GetMapping("/{username}/get")
	public ResponseEntity<?> getAccounts(@PathVariable("username") String username) {
		return new ResponseEntity<>(accountService.findById(username), HttpStatus.OK);
	}

	@DeleteMapping("/{username}")
	public ResponseEntity<?> deleteAccounts(@PathVariable("username") String username) {
		accountService.deleteById(username);
		return new ResponseEntity<>("Product with Id: " + username + " was deleted", HttpStatus.OK);
	}
}

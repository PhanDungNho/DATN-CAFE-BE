package cafe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.exception.EntityException;
import cafe.service.AccountService;
import cafe.service.FileStorageService;
import cafe.service.MapValidationErrorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@CrossOrigin(origins = "*")
public class AccountController {

	@Autowired
	AccountService accountService;
	
	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAccount(@Valid @ModelAttribute AccountDto accountDto, BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Account account = accountService.insertAccountAdmin(accountDto);
		accountDto.setPassword(null);
		accountDto.setImage(account.getImage());
		// trả về người dùng responseDto
		return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
	}



	@PatchMapping(value = "/{username}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateAccount(@PathVariable String username, @Valid @ModelAttribute AccountDto dto,
			BindingResult result) {
		ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationField(result);
		if (responseEntity != null) {
			return responseEntity;
		}
		Account account = accountService.update(username, dto);
		dto.setPassword(null);
		dto.setImage(account.getImage());
		dto.setUsername(account.getUsername());

		return new ResponseEntity<>(account, HttpStatus.OK);
	}

	@PatchMapping("/{username}/toggle-active")
	public ResponseEntity<?> toggleActive(@PathVariable String username) {
		// Call the service to toggle the account's active status
		Account updatedAccount = accountService.toggleActive(username);
		// Check if the account was found
		if (updatedAccount == null) {
			return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
		}
		// Create AccountDto to return updated account information
		AccountDto responseDto = new AccountDto();
		BeanUtils.copyProperties(updatedAccount, responseDto, "password");

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@GetMapping("/find/phone")
	public ResponseEntity<?> getAccountByPhone(@RequestParam("query") String query) {
		return new ResponseEntity<>(accountService.findAccountByPhone(query), HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> getAccounts( @RequestParam("admin") Optional<Boolean> admin) {
		List<Account> accounts;
		if(admin.orElse(false)) {
			accounts = accountService.getAdministrators();
		}else {
			accounts = accountService.findAll();	
		}
		
		List<AccountDto> accountDtos = accounts.stream().map(account -> {
			AccountDto dto = new AccountDto();
			BeanUtils.copyProperties(account, dto, "password");
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



	@GetMapping("/find")
	public ResponseEntity<?> getAccountByName(@RequestParam("query") String query) {
		return new ResponseEntity<>(accountService.findAccountByName(query), HttpStatus.OK);
	}
	
	@GetMapping("/findadmin")
	public ResponseEntity<?> getAccountByNameAdmin(@RequestParam("query") String query) {
		return new ResponseEntity<>(accountService.findAccountByNameAdmin(query), HttpStatus.OK);
	}
	
	
	@GetMapping("/image/{filename:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request) {
		Resource resource = fileStorageService.loadLogoFileResource(filename);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			throw new EntityException("Could not determine file type");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}

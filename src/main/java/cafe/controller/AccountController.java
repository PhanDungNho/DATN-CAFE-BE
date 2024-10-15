package cafe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import org.springframework.web.bind.annotation.RestController;

import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.entity.Size;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import cafe.dto.AccountDto;
import cafe.entity.Account;
import cafe.entity.Topping;
import cafe.exception.EntityException;
import cafe.service.AccountService;
import cafe.service.FileStorageService;
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


@CrossOrigin(origins = "*")
public class AccountController {

	@Autowired
	AccountService accountService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createAccount(@Valid @ModelAttribute AccountDto accountDto, BindingResult result) {
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
		Account account = accountService.insertAccount(accountDto);
		accountDto.setPassword(null);
		accountDto.setImage(account.getImage());
		accountDto.setUsername(account.getUsername());
		// trả về người dùng responseDto
		return new ResponseEntity<>(accountDto, HttpStatus.CREATED);
	}

	@PatchMapping(value ="/{username}",consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateAccount(@PathVariable String username,@Valid @ModelAttribute AccountDto dto,BindingResult result) {
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
	public ResponseEntity<Map<String, String>> updateAccountActive(@PathVariable String username) {
		Account updatedAccount = accountService.toggleActive(username);
		Map<String, String> response = new HashMap<>();
		response.put("message",
				"Account " + (updatedAccount.getActive() ? "activated" : "deactivated") + " successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<?> getAccounts() {

		return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
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

	/// phân quyền
	@GetMapping("/admin")
	public List<Account> findAll(@RequestParam("admin") Optional<Boolean> admin) {
		if (admin.orElse(false)) {
			return accountService.getAdministrators();
		}
		return accountService.findAll();
	}
	
	@GetMapping("/find")
	public ResponseEntity<?> getAccountByName(@RequestParam("query") String query) {
		return new ResponseEntity<>(accountService.findAccountByName(query), HttpStatus.OK);
	}
	
	@GetMapping("/find/phone")
	public ResponseEntity<?> getAccountByPhone(@RequestParam("query") String query) {
		return new ResponseEntity<>(accountService.findAccountByPhone(query), HttpStatus.OK);
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
}

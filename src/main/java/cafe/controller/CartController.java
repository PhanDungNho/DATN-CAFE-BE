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

import cafe.dto.CartDto;
import cafe.entity.Account;
import cafe.entity.Cart;
import cafe.entity.ProductVariant;
import cafe.service.AccountService;
import cafe.service.CartService;
import cafe.service.CategoryService;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductVariantService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carts")
@CrossOrigin
public class CartController {

	@Autowired
	CartService cartService;

	@Autowired
	AccountService accountService;

	@Autowired
	ProductVariantService productVariantService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> CreateCartDetail(@Valid @RequestBody CartDto dto) {
		Cart entity = new Cart();
		Account account = accountService.findById(dto.getUsername());
		entity.setAccount(account);
		ProductVariant productvariant = productVariantService.findById(dto.getProductvariantid());
		entity.setProductvariant(productvariant);
		entity.setQuantity(dto.getQuantity());
		entity = cartService.save(entity);
		dto.setId(entity.getId());
		return new ResponseEntity<>(dto, HttpStatus.CREATED);

	}

	@PatchMapping("/{id}")
	public ResponseEntity<String> updateQuantityCart(@PathVariable Long id, @RequestBody CartDto dto) {
	    Cart entity = new Cart();
	    BeanUtils.copyProperties(dto, entity);
	    entity = cartService.updateQuantity(id, entity);
	    dto.setId(entity.getId());
	 
	    return new ResponseEntity<>("Cart is updated", HttpStatus.OK);
	}

	@GetMapping("/{username}")
	public ResponseEntity<?> getCartByAccount(@PathVariable String username) {
		return new ResponseEntity<>(cartService.getCartsByUsername(username), HttpStatus.OK);
	}

//	@GetMapping("/{id}/get")
//	public ResponseEntity<?> getCategories(@PathVariable("id") Long id){
//		return new ResponseEntity<>(categoryService.findById(id),HttpStatus.OK);
//	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProductFromCart(@PathVariable("id") Long id) {
		cartService.deleteById(id);
		return new ResponseEntity<>("Category with Id: " + id + " was deleted", HttpStatus.OK);
	}
}

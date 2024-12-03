package cafe.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import cafe.dto.CartDetailDto;
import cafe.dto.CartDetailToppingDto;
import cafe.dto.ProductVariantDto;
import cafe.dto.ToppingDto;
import cafe.entity.Account;
import cafe.entity.CartDetail;
import cafe.entity.CartDetailTopping;
import cafe.entity.ProductVariant;
import cafe.modal.CartDetailResponse;
import cafe.modal.OrderResponse;
import cafe.service.AccountService;
import cafe.service.CartDetailToppingService;
import cafe.service.CartService;
import cafe.service.CategoryService;
import cafe.service.MapValidationErrorService;
import cafe.service.ProductVariantService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cartDetails")
@CrossOrigin
public class CartDetailController {

	@Autowired
	CartService cartService;

	@Autowired
	AccountService accountService;

	@Autowired
	ProductVariantService productVariantService;

	@Autowired
	MapValidationErrorService mapValidationErrorService;

	@PostMapping
	public ResponseEntity<?> CreateCartDetail(@Valid @RequestBody CartDetailDto dto) {
		CartDetail cartDetail = cartService.insertCartDetail(dto);

		CartDetailDto respDto = new CartDetailDto();
		respDto.setId(cartDetail.getId());

		if (cartDetail.getAccount() != null) {
			respDto.setUsername(cartDetail.getAccount().getUsername());
		}

		if (cartDetail.getProductVariant() != null) {
			respDto.setProductVariantId(cartDetail.getProductVariant().getId());
		}

		respDto.setQuantity(cartDetail.getQuantity());
		respDto.setNote(cartDetail.getNote());
		respDto.setAccount(cartDetail.getAccount());

		// Thiết lập danh sách topping cho cart detail
		List<CartDetailToppingDto> cartDetailToppings = cartDetail.getCartDetailToppings().stream()
				.map(this::convertToCartDetailResponse).collect(Collectors.toList());

		respDto.setCartDetailToppings(cartDetailToppings);

		return new ResponseEntity<>(respDto, HttpStatus.CREATED);
	}

	// Phương thức chuyển đổi CartDetailTopping sang DTO
	private CartDetailToppingDto convertToCartDetailResponse(CartDetailTopping cartDetailTopping) {
		if (cartDetailTopping == null) {
			return null;
		}

		CartDetailToppingDto dto = new CartDetailToppingDto();

		dto.setId(cartDetailTopping.getId());
		dto.setQuantity(cartDetailTopping.getQuantity());
		dto.setToppingId(cartDetailTopping.getTopping().getId());

		// Thiết lập topping từ cartDetailTopping
		if (cartDetailTopping.getTopping() != null) {
			ToppingDto toppingDto = new ToppingDto();
			toppingDto.setId(cartDetailTopping.getTopping().getId());
			toppingDto.setName(cartDetailTopping.getTopping().getName());
			toppingDto.setPrice(cartDetailTopping.getTopping().getPrice());
			toppingDto.setActive(cartDetailTopping.getTopping().getActive());
			toppingDto.setImage(cartDetailTopping.getTopping().getImage());

			dto.setTopping(toppingDto);
		}

		return dto;
	}

	@PatchMapping("/{id}")
	public ResponseEntity<String> updateQuantityCart(@PathVariable Long id, @RequestBody CartDetailDto dto) {
		CartDetail entity = new CartDetail();
		BeanUtils.copyProperties(dto, entity);
		entity = cartService.updateQuantity(id, entity);
		dto.setId(entity.getId());

		return new ResponseEntity<>("Cart is updated", HttpStatus.OK);
	}

	@GetMapping("/{username}")
	public ResponseEntity<?> getCartByAccount(@PathVariable String username) {
		List<CartDetail> carts = cartService.getCartsByUsername(username);
		List<CartDetailResponse> cartResponses = carts.stream().map(CartDetailResponse::convert).toList();
		return new ResponseEntity<>(cartResponses, HttpStatus.OK);
	}

//	@GetMapping("/{id}/get")
//	public ResponseEntity<?> getCategories(@PathVariable("id") Long id){
//		return new ResponseEntity<>(categoryService.findById(id),HttpStatus.OK);
//	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProductFromCart(@PathVariable("id") Long id) {
		cartService.deleteById(id);
		return new ResponseEntity<>("Cart Detail with Id: " + id + " was deleted", HttpStatus.OK);
	}

}

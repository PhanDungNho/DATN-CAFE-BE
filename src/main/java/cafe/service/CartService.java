package cafe.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.CartDetailDto;
import cafe.dto.ProductDto;
import cafe.entity.Account;
import cafe.entity.CartDetail;
import cafe.entity.CartDetailTopping;
import cafe.entity.Category;
import cafe.entity.OrderDetail;
import cafe.entity.OrderDetailTopping;
import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.entity.Topping;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.repository.CartDetailToppingRepository;
import cafe.repository.CartRepository;
import cafe.repository.ProductVariantRepository;
import cafe.repository.ToppingRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ProductVariantRepository productVariantRepository;

	@Autowired
	private ToppingRepository toppingRepository;

	public CartDetail save(CartDetail entity) {
		return cartRepository.save(entity);
	}

	public CartDetail insertCartDetail(CartDetailDto dto) {
		CartDetail cartDetail = new CartDetail();
		Account account = accountRepository.findById(dto.getUsername()).get();
		ProductVariant productVariant = productVariantRepository.getById(dto.getProductVariantId());
		cartDetail.setAccount(account);
		cartDetail.setProductVariant(productVariant);
		cartDetail.setQuantity(dto.getQuantity());
		cartDetail.setNote(dto.getNote());

		List<CartDetailTopping> cartDetailToppings = dto.getCartDetailToppings().stream().map(toppingDto -> {
			CartDetailTopping cartDetailTopping = new CartDetailTopping();
			Topping topping = toppingRepository.findById(toppingDto.getTopping().getId())
					.orElseThrow(() -> new EntityException("Topping not found"));
			cartDetailTopping.setTopping(topping);
			cartDetailTopping.setQuantity(toppingDto.getQuantity());
			cartDetailTopping.setCartDetail(cartDetail);
			return cartDetailTopping;
		}).collect(Collectors.toList());

		cartDetail.setCartDetailToppings(cartDetailToppings);

		return cartRepository.save(cartDetail);
	}

	public CartDetail updateQuantity(Long id, CartDetail entity) {
		Optional<CartDetail> existed = cartRepository.findById(id);
		if (existed.isEmpty()) {
			throw new EntityException("Category id " + id + " does not exist");
		}
		try {
			CartDetail existedCart = existed.get();
			existedCart.setQuantity(entity.getQuantity());
			return cartRepository.save(existedCart);
		} catch (Exception ex) {
			throw new EntityException("Category is updated failed");
		}

	}

	public List<CartDetail> getCartsByUsername(String username) {
		return cartRepository.findByAccountUsername(username);
	}

	public Page<CartDetail> findAll(Pageable pageable) {
		return cartRepository.findAll(pageable);
	}

	public void deleteById(Long id) {
		CartDetail existed = findById(id);
		cartRepository.delete(existed);
	}

	public List<CartDetail> findAll() {
		return cartRepository.findAll();
	}

	public CartDetail findById(Long id) {
		Optional<CartDetail> found = cartRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Cart with id " + id + " does not exist");
		}
		return found.get();
	}

}

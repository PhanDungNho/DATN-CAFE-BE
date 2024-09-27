package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Cart;
import cafe.entity.Category;
import cafe.exception.EntityException;
import cafe.repository.CartRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;

	public Cart save(Cart entity) {
		return cartRepository.save(entity);
	}
	
	 
	public Cart updateQuantity(Long id, Cart entity) {
		Optional<Cart> existed = cartRepository.findById(id);
		if (existed.isEmpty()) {
			throw new EntityException("Category id " + id + " does not exist");
		}
		try {
			Cart existedCart = existed.get();
			existedCart.setQuantity(entity.getQuantity());
			return cartRepository.save(existedCart);
		} catch (Exception ex) {
			throw new EntityException("Category is updated failed");
		}

	}

	public List<Cart> getCartsByUsername(String username) {
		return cartRepository.findByAccountUsername(username);
	}

	public Page<Cart> findAll(Pageable pageable) {
		return cartRepository.findAll(pageable);
	}

	public void deleteById(Long id) {
		Cart existed = findById(id);
		cartRepository.delete(existed);
	}

	public List<Cart> findAll() {
		return cartRepository.findAll();
	}

	public Cart findById(Long id) {
		Optional<Cart> found = cartRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Cart with id " + id + " does not exist");
		}
		return found.get();
	}
}

package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import cafe.entity.CartDetail;
import cafe.exception.EntityException;
import cafe.repository.CartRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;

	public CartDetail save(CartDetail entity) {
		return cartRepository.save(entity);
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

package cafe.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cafe.entity.CartDetail;
import cafe.entity.CartDetailTopping;
import cafe.exception.EntityException;
import cafe.repository.CartDetailToppingRepository;
import jakarta.transaction.Transactional;

@Service
public class CartDetailToppingService {

	@Autowired
	private CartDetailToppingRepository cartDetailToppingRepository;

	@Transactional
	public void deleteById(Long id) {
		cartDetailToppingRepository.deleteByIdCartDetailTopping(id);
	}

}

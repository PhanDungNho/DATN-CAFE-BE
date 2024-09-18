package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Topping;
import cafe.entity.exception.EntityException;
import cafe.repository.ToppingRepository;

@Service
public class ToppingService {
	@Autowired
	private ToppingRepository toppingRepository;

	public Topping save(Topping entity) {
		
		return toppingRepository.save(entity);
	}

	public List<Topping> findAll() {
		return toppingRepository.findAll();
	}

	public Topping findById(Long id) {
		Optional<Topping> found = toppingRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Topping with id " + id + " does not exist");
		}
		return found.get();
	}
	
	
	
	public Page<Topping> findAll(Pageable pageable) {
		return toppingRepository.findAll(pageable);
	}

	public Topping update(Long id, Topping topping) {
		Optional<Topping> found = toppingRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Topping with id " + id + " does not exist");
		}
		
		Topping entity = found.get();
		BeanUtils.copyProperties(topping, entity, "id");
		
		return toppingRepository.save(entity);
	}
	
//	public Topping delete(Long id, Topping topping) {
//		Optional<Topping> found = toppingRepository.findById(id);
//		if (found.isEmpty()) {
//			throw new EntityException("Topping with id " + id + " does not exist");
//		}
//		
//		Topping entity = found.get();
//		
//		try {
//			
//			entity.setActive(topping.getActive());
//			
//		} catch (Exception e) {
//			throw new EntityException("Product is updated failed");
//		}
//		
//		return toppingRepository.delete(entity);
//	} 
}

package cafe.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.AccountDto;
import cafe.dto.ToppingDto;
import cafe.entity.Account;
import cafe.entity.Category;
import cafe.entity.Size;
import cafe.entity.Topping;
import cafe.exception.EntityException;
import cafe.repository.ToppingRepository;

@Service
public class ToppingService {
	@Autowired
	private ToppingRepository toppingRepository;
	@Autowired
	private FileStorageService fileStorageService;
	public Topping save(Topping entity) {
		
		return toppingRepository.save(entity);
	}
	
	public Topping insertTopping(ToppingDto dto) {

		 
		Topping entity = new Topping();
		BeanUtils.copyProperties(dto, entity);
	 

		if (dto.getImageFile() != null) {
			String filename = fileStorageService.storeLogoFile(dto.getImageFile());
			entity.setImage(filename);
			dto.setImage(filename);
		}
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
	
//	public Page<ToppingDto> findToppingsByName(String name, Pageable pageable){
//		var list = toppingRepository.findByNameContainsIgnoreCase(name, pageable);
//		
//		var newList = list.getContent().stream()
//				.map(item -> {
//					ToppingDto dto = new ToppingDto();
//					BeanUtils.copyProperties(item, dto, "orderdetailtopping");
//					
//					return dto;
//				}).collect(Collectors.toList());
//		
//		var newPage = new PageImpl<>(newList, list.getPageable(), list.getTotalElements());
//		
//		return newPage;
//	}
//	
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
	
	public Topping toggleActive(Long id) {
		Optional<Topping> optionalTopping = toppingRepository.findById(id);
		if (optionalTopping.isEmpty()) {
			throw new EntityException("Topping with id " + id + " do not exist");
		}
		Topping topping = optionalTopping.get();
		topping.setActive(!topping.getActive());
		return toppingRepository.save(topping);
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
	public List<Topping> findCategoryByName(String name){
		List<Topping> list = toppingRepository.findByNameContainsIgnoreCase(name);
		return list;
	}

}

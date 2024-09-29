package cafe.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.CategoryDto;
import cafe.entity.Category;
import cafe.exception.EntityException;
import cafe.repository.CategoryRepository;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	public Category save(Category entity) {
		return categoryRepository.save(entity);
	}

	public Category update(Long id, Category entity) {
		Optional<Category> existed = categoryRepository.findById(id);
		if (existed.isEmpty()) {
			// neu k tim thay thi tra ve ngoai le
			throw new EntityException("Category id " + id + " does not exist");

		}
		try {
			// neu tìm thấy trong csdl thì sẽ truy cập tới đối tượng Catrgory
			Category existedCategory = existed.get();
			existedCategory.setName(entity.getName());
			existedCategory.setActive(entity.getActive());
			return categoryRepository.save(existedCategory);
			// thì tiến hành cập nhật thủ công bth
		} catch (Exception ex) {
			// nếu có lỗi sẽ ném ra ngoại lệ
			throw new EntityException("Category is updated failed");
		}

	}

	public CategoryDto updateCategoryActive(Long id, Boolean active) {
		// Tìm category theo id
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new EntityException("Category not found with id: " + id));

		// Chỉ cập nhật trường active
		category.setActive(active);

		// Lưu thay đổi vào cơ sở dữ liệu
		categoryRepository.save(category);

		// Chuyển đổi từ entity sang DTO và trả về kết quả
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(category.getId());
		categoryDto.setName(category.getName()); // Giữ lại giá trị name
		categoryDto.setActive(category.getActive());

		return categoryDto;
	}

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	
//	public Page<CategoryDto> findCategoryByName(String name, Pageable pageable){
//		var list = categoryRepository.findByNameContainsIgnoreCase(name, pageable);
//		
//		var newList = list.getContent().stream()
//				.map(item -> {
//					CategoryDto dto = new CategoryDto();
//					BeanUtils.copyProperties(item, dto);
//					
//					return dto;
//				}).collect(Collectors.toList());
//		
//		var newPage = new PageImpl<>(newList, list.getPageable(), list.getTotalElements());
//		
//		return newPage;
//	}

	public Category findById(Long id) {
		Optional<Category> found = categoryRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Category with id " + id + " does not exist");
		}
		return found.get();
	}

	public void deleteById(Long id) {
		Category existed = findById(id);
		categoryRepository.delete(existed);
	}

	public Category toggleActive(Long id) {
		Optional<Category> optionalCate = categoryRepository.findById(id);
		if (optionalCate.isEmpty()) {
			throw new EntityException("Category with id " + id + " do not exist");
		}
		Category cate = optionalCate.get();
		cate.setActive(!cate.getActive());
		return categoryRepository.save(cate);
	}
	
	public List<Category> findCategoryByName(String name){
		List<Category> list = categoryRepository.findByNameContainsIgnoreCase(name);
		return list;
	}

	
}

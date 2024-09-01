package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Category;
import cafe.entity.exception.EntityException;
import cafe.repository.CategoryRepository;

 

@Service
public class CategoryService {
	@Autowired
	private  CategoryRepository categoryRepository;

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

	public List<Category> findAll() {
		return categoryRepository.findAll();
	}

	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

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
}

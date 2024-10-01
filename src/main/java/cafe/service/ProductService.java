package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.ProductDto;
import cafe.entity.Category;
import cafe.entity.Product;
import cafe.exception.EntityException;
import cafe.repository.CategoryRepository;
import cafe.repository.ProductRepository;

 

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;


	
	 public Product save(ProductDto productDto) {
	        // Tạo mới một Product entity từ ProductDto
	        Product product = new Product();
	        product.setName(productDto.getName());
	        product.setActive(productDto.getActive());
	        product.setDescription(productDto.getDescription());

	        // Tìm Category từ categoryId và set vào Product
	        Category category = categoryRepository.findById(productDto.getCategoryid())
	            .orElseThrow(() -> new EntityException("Category id " + productDto.getCategoryid() + " does not exist"));
	        product.setCategory(category);

	        // Lưu sản phẩm vào cơ sở dữ liệu
	        return productRepository.save(product);
	}
	 
	
	 public Product update(Long id, ProductDto dto) {
		    Optional<Product> existed = productRepository.findById(id);
		    if (existed.isEmpty()) {
		        throw new EntityException("Product id " + id + " does not exist");
		    }

		    Product existedProduct = existed.get();
		    existedProduct.setName(dto.getName());
		    existedProduct.setActive(dto.getActive());
		    existedProduct.setDescription(dto.getDescription());

		    // Chỉ gán Category nếu có categoryid
		    if (dto.getCategoryid() != null) {
		        Optional<Category> category = categoryRepository.findById(dto.getCategoryid());
		        if (category.isPresent()) {
		            existedProduct.setCategory(category.get());
		        } else {
		            throw new EntityException("Category id " + dto.getCategoryid() + " does not exist");
		        }
		    }

		    return productRepository.save(existedProduct);
		}

	 
	 // để bật tắt active
	   public Product toggleActive(Long id) {
	        Optional<Product> optionalProduct = productRepository.findById(id);
	        if (optionalProduct.isEmpty()) {
	            throw new EntityException("Product with id " + id + " does not exist");
	        }

	        Product product = optionalProduct.get();
	        product.setActive(!product.getActive()); // Đảo ngược trạng thái active
	        return productRepository.save(product); // Lưu thay đổi vào cơ sở dữ liệu
	    }
	 

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Page<Product> findAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public Product findById(Long id) {
		Optional<Product> found = productRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Product with id " + id + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(Long id) {
		Product existed = findById(id);
		productRepository.delete(existed);
	}
}

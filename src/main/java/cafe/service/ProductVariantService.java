package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.exception.EntityException;
import cafe.repository.ProductVariantRepository;

 

@Service
public class ProductVariantService {
	@Autowired
	private ProductVariantRepository productVariantRepository;

	public ProductVariant save(ProductVariant entity) {
		return productVariantRepository.save(entity);
	}

	public ProductVariant update(Long id, ProductVariant entity) {
		Optional<ProductVariant> existed = productVariantRepository.findById(id);
		if (existed.isEmpty()) {
			// neu k tim thay thi tra ve ngoai le
			throw new EntityException("ProductVariant id " + id + " does not exist");
		}
		try {
			// neu tìm thấy trong csdl thì sẽ truy cập tới đối tượng Catrgory
			ProductVariant existedProductVariant = existed.get();
			existedProductVariant.setProduct(entity.getProduct());
			existedProductVariant.setPrice(entity.getPrice());
			existedProductVariant.setSize(entity.getSize());
			existedProductVariant.setActive(entity.getActive());
			return productVariantRepository.save(existedProductVariant);
			// thì tiến hành cập nhật thủ công bth
		} catch (Exception ex) {
			// nếu có lỗi sẽ ném ra ngoại lệ
			throw new EntityException("Product is updated failed");
		}

	}

	public List<ProductVariant> findAll() {
		return productVariantRepository.findAll();
	}

	public Page<ProductVariant> findAll(Pageable pageable) {
		return productVariantRepository.findAll(pageable);
	}

	public ProductVariant findById(Long id) {
		Optional<ProductVariant> found = productVariantRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Product with id " + id + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(Long id) {
		ProductVariant existed = findById(id);
		productVariantRepository.delete(existed);
	}
}

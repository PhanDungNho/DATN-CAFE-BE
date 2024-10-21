package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.ProductVariantDto;
import cafe.entity.Account;
import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.entity.Size;
import cafe.exception.EntityException;
import cafe.repository.ProductRepository;
import cafe.repository.ProductVariantRepository;
import cafe.repository.SizeRepository;

@Service
public class ProductVariantService {
	@Autowired
	private ProductVariantRepository productVariantRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SizeRepository sizeRepository;

	public ProductVariant save(ProductVariantDto dto) {
		ProductVariant entity = new ProductVariant();
		BeanUtils.copyProperties(dto, entity, "size");

	    // Lấy thông tin product từ database
	    Product product = productRepository.findById(dto.getProductId())
	            .orElseThrow(() -> new EntityException("Product not found"));
	    entity.setProduct(product);

	    // Lấy thông tin size từ database
	    Size size = sizeRepository.findById(dto.getSizeId())
	            .orElseThrow(() -> new EntityException("Size not found"));
	    entity.setSize(size);

		return productVariantRepository.save(entity);
	}

	public ProductVariant update(Long id, ProductVariantDto dto) {
		Optional<ProductVariant> existed = productVariantRepository.findById(id);
		if (existed.isEmpty()) {
			throw new EntityException("ProductVariant id " + id + " does not exist");
		}
		try {
			ProductVariant existedProductVariant = existed.get();
			BeanUtils.copyProperties(dto, existedProductVariant);
			
			Product product = productRepository.findById(dto.getProductid())
					.orElseThrow(() -> new EntityException("Product not found"));
			existedProductVariant.setProduct(product);

			Size size = sizeRepository.findById(dto.getSizeid())
					.orElseThrow(() -> new EntityException("Size not found"));
			existedProductVariant.setSize(size);
			
			return productVariantRepository.save(existedProductVariant);
		} catch (Exception ex) {
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
	
	public List<ProductVariant> searchProductVariantsByProductName(String productName) {
		List<ProductVariant> list = productVariantRepository.findByProductNameContainsIgnoreCase(productName);
        return list;
    }

	public ProductVariant toggleActive(Long id) {
		Optional<ProductVariant> variant = productVariantRepository.findById(id);
		if (variant.isEmpty()) {
			throw new EntityException("Product Variant with id: " + id + "does not exist");
		}

		ProductVariant productVariant = variant.get();
		productVariant.setActive(!productVariant.getActive());

		return productVariantRepository.save(productVariant);
	}

}

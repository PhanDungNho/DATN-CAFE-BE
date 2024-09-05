package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.ProductVariantDto;
import cafe.entity.Product;
import cafe.entity.ProductVariant;
import cafe.entity.Size;
import cafe.entity.exception.EntityException;
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
	        // Tìm Product dựa trên productid từ DTO
	        Product product = productRepository.findById(dto.getProductid())
	                .orElseThrow(() -> new EntityException("Product not found"));

	        // Tìm Size dựa trên sizeid từ DTO
	        Size size = sizeRepository.findById(dto.getSizeid())
	                .orElseThrow(() -> new EntityException("Size not found"));

	        // Tạo một thực thể ProductVariant mới
	        ProductVariant productVariant = new ProductVariant();
	        productVariant.setActive(dto.getActive());
	        productVariant.setPrice(dto.getPrice());
	        productVariant.setProduct(product);  // Gán Product vào ProductVariant
	        productVariant.setSize(size);        // Gán Size vào ProductVariant

	        // Lưu vào cơ sở dữ liệu
	        return productVariantRepository.save(productVariant);
	    }

	  
	  public ProductVariant update(Long id, ProductVariantDto dto) {
		    Optional<ProductVariant> existed = productVariantRepository.findById(id);
		    if (existed.isEmpty()) {
		        throw new EntityException("ProductVariant id " + id + " does not exist");
		    }

		    ProductVariant existedProductVariant = existed.get();

		    // Truy vấn product và size từ cơ sở dữ liệu
		    Optional<Product> productOpt = productRepository.findById(dto.getProductid());
		    if (productOpt.isEmpty()) {
		        throw new EntityException("Product id " + dto.getProductid() + " does not exist");
		    }
		    
		    Optional<Size> sizeOpt = sizeRepository.findById(dto.getSizeid());
		    if (sizeOpt.isEmpty()) {
		        throw new EntityException("Size id " + dto.getSizeid() + " does not exist");
		    }

		    // Cập nhật thông tin ProductVariant
		    existedProductVariant.setProduct(productOpt.get());
		    existedProductVariant.setSize(sizeOpt.get());
		    existedProductVariant.setPrice(dto.getPrice());
		    existedProductVariant.setActive(dto.getActive());

		    // Lưu lại ProductVariant đã cập nhật
		    return productVariantRepository.save(existedProductVariant);
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

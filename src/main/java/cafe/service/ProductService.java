package cafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.NaturalId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cafe.dto.ProductDto;
import cafe.dto.ProductToppingDto;
import cafe.dto.ProductVariantDto;
import cafe.dto.UploadedFileInfo;
import cafe.entity.Category;
import cafe.entity.Image;
import cafe.entity.Product;
import cafe.entity.ProductToppings;
import cafe.entity.ProductVariant;
import cafe.entity.Size;
import cafe.entity.Topping;
import cafe.exception.EntityException;
import cafe.repository.CategoryRepository;
import cafe.repository.ImageRepository;
import cafe.repository.ProductRepository;
import cafe.repository.ProductToppingRepository;
import cafe.repository.ProductVariantRepository;
import cafe.repository.SizeRepository;
import cafe.repository.ToppingRepository;
import jakarta.transaction.Transactional;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private ToppingRepository toppingRepository;

	@Autowired
	private SizeRepository sizeRepository;

	@Autowired
	private ProductToppingRepository productToppingRepository;

	@Autowired
	private ProductVariantRepository productVariantRepository;

//	public Product save(ProductDto productDto) {
//		// Tạo mới một Product entity từ ProductDto
//		Product product = new Product();
//		product.setName(productDto.getName());
//		product.setActive(productDto.getActive());
//		product.setDescription(productDto.getDescription());
//
//		// Tìm Category từ categoryId và set vào Product
//		Category category = categoryRepository.findById(productDto.getCategoryid()).orElseThrow(
//				() -> new EntityException("Category id " + productDto.getCategoryid() + " does not exist"));
//		product.setCategory(category);
//
//		// Lưu sản phẩm vào cơ sở dữ liệu
//		return productRepository.save(product);
//	}

	public Product insertProduct(ProductDto dto) {
		Product product = new Product();
		BeanUtils.copyProperties(dto, product);

		// Validate category ID
		if (dto.getCategoryId() == null) {
			throw new EntityException("Category ID must not be null");
		}

		// Find category once
		Category cate = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new EntityException("Category not found"));
		product.setCategory(cate);

		// Handle images
		if (dto.getImageFiles() != null && !dto.getImageFiles().isEmpty()) {
			List<Image> images = dto.getImageFiles().stream().map(file -> {
				String filename = fileStorageService.storeLogoFile(file);
				Image image = new Image();
				image.setFileName(filename);
				image.setUrl(filename);
				image.setName(file.getName());
				image.setProduct(product);
				return image;
			}).collect(Collectors.toList());
			product.setImages(images);
		}

		// Save product first
		Product savedProduct = productRepository.save(product);

		// Handle product toppings
		List<ProductToppings> productToppings = dto.getProductToppings().stream().map(productToppingDto -> {
			if (productToppingDto.getToppingId() == null) {
				throw new EntityException("Topping ID must not be null");
			}
			ProductToppings productTopping = new ProductToppings();
			BeanUtils.copyProperties(productToppingDto, productTopping);

			productTopping.setProduct(savedProduct); // Use the saved product

			Topping topping = toppingRepository.findById(productToppingDto.getToppingId())
					.orElseThrow(() -> new EntityException("Topping not found"));
			productTopping.setTopping(topping);

			return productTopping;
		}).collect(Collectors.toList());
		savedProduct.setProductToppings(productToppings);
		productToppingRepository.saveAll(productToppings);

		// Handle product variants
		List<ProductVariant> productVariants = dto.getProductVariants().stream().map(productVariantDto -> {
			if (productVariantDto.getSizeId() == null) {
				throw new EntityException("Size ID must not be null");
			}
			ProductVariant productVariant = new ProductVariant();
			BeanUtils.copyProperties(productVariantDto, productVariant);

			productVariant.setProduct(savedProduct); // Use the saved product
			Size size = sizeRepository.findById(productVariantDto.getSizeId())
					.orElseThrow(() -> new EntityException("Size not found"));
			productVariant.setSize(size);

			return productVariant;
		}).collect(Collectors.toList());
		savedProduct.setProductVariants(productVariants);
		productVariantRepository.saveAll(productVariants);

		return savedProduct; // Return the saved product
	}

//	public Product update(Long id, ProductDto dto) {
//		Optional<Product> existed = productRepository.findById(id);
//		if (existed.isEmpty()) {
//			throw new EntityException("Product id " + id + " does not exist");
//		}
//
//		Product existedProduct = existed.get();
//		existedProduct.setName(dto.getName());
//		existedProduct.setActive(dto.getActive());
//		existedProduct.setDescription(dto.getDescription());
//
//		// Chỉ gán Category nếu có categoryid
//		if (dto.getCategoryid() != null) {
//			Optional<Category> category = categoryRepository.findById(dto.getCategoryid());
//			if (category.isPresent()) {
//				existedProduct.setCategory(category.get());
//			} else {
//				throw new EntityException("Category id " + dto.getCategoryid() + " does not exist");
//			}
//		}
//
//		return productRepository.save(existedProduct);
//	}

	@Transactional
	public Product updateProduct(Long id, ProductDto dto) {
		// Tìm sản phẩm theo ID
		Product product = productRepository.findById(id).orElseThrow(() -> new EntityException("Product not found"));

		// Cập nhật thông tin cơ bản của sản phẩm
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setActive(dto.getActive());

		// Cập nhật danh mục nếu có
		if (dto.getCategoryId() != null) {
			Category cate = categoryRepository.findById(dto.getCategoryId())
					.orElseThrow(() -> new EntityException("Category not found"));
			product.setCategory(cate);
		}

		// Cập nhật hình ảnh
		if (dto.getImageFiles() != null && !dto.getImageFiles().isEmpty()) {
			List<Image> images = dto.getImageFiles().stream().map(file -> {
				String filename = fileStorageService.storeLogoFile(file);
				Image image = new Image();
				image.setFileName(filename);
				image.setUrl(filename);
				image.setName(file.getName());
				image.setProduct(product);
				return image;
			}).collect(Collectors.toList());
			product.setImages(images);
		}

		// Cập nhật hoặc thêm mới các biến thể sản phẩm
		if (dto.getProductVariants() != null) {
			List<ProductVariant> updatedVariants = new ArrayList<>();
			for (ProductVariantDto variantDto : dto.getProductVariants()) {

				ProductVariant variant;

				// Kiểm tra xem biến thể có ID hay không (0 nghĩa là thêm mới)
				if (variantDto.getId() == 0) {
					// Tạo mới productVariant nếu ID bằng 0
					variant = new ProductVariant();
					variant.setProduct(product); // Liên kết với sản phẩm hiện tại
				} else {
					// Cập nhật biến thể hiện có
					variant = productVariantRepository.findById(variantDto.getId())
							.orElseThrow(() -> new EntityException("ProductVariant not found"));
				}

				// Cập nhật các thuộc tính cho productVariant
				variant.setActive(variantDto.getActive());
				variant.setPrice(variantDto.getPrice());

				// Cập nhật size nếu có
				if (variantDto.getSizeId() != null) {
					Size size = sizeRepository.findById(variantDto.getSizeId())
							.orElseThrow(() -> new EntityException("Size not found"));
					variant.setSize(size);
				}

				updatedVariants.add(variant);
			}

			// Cập nhật lại danh sách productVariant cho sản phẩm
			product.setProductVariants(updatedVariants);
		}

		// Cập nhật topping nếu có
		if (dto.getProductToppings() != null
				&& dto.getProductToppings().stream().anyMatch(toppingDto -> toppingDto.getToppingId() != null)) {
			// Xóa tất cả topping hiện tại cho sản phẩm
			productToppingRepository.deleteByProductId(product.getId());

			List<ProductToppings> updatedToppings = new ArrayList<>();

			for (ProductToppingDto productToppingDto : dto.getProductToppings()) {
				// Kiểm tra toppingId không null
				if (productToppingDto.getToppingId() == null) {
					throw new EntityException("Topping ID must not be null");
				}

				ProductToppings productTopping = new ProductToppings();
				BeanUtils.copyProperties(productToppingDto, productTopping);

				Topping topping = toppingRepository.findById(productToppingDto.getToppingId())
						.orElseThrow(() -> new EntityException("Topping not found"));

				productTopping.setTopping(topping);
				productTopping.setProduct(product);

				updatedToppings.add(productTopping); // Thêm topping mới vào danh sách
			}

			// Cập nhật danh sách topping cho sản phẩm
			product.setProductToppings(updatedToppings);
			productToppingRepository.saveAll(updatedToppings); // Lưu tất cả topping đã cập nhật
		} else {
			// Nếu không có topping nào, bạn có thể thiết lập danh sách topping rỗng
			productToppingRepository.deleteByProductId(product.getId());
			product.setProductToppings(new ArrayList<>());
		}

		return productRepository.save(product);
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

//	public Product findById(Long id) {
//		Optional<Product> found = productRepository.findById(id);
//		if (found.isEmpty()) {
//			throw new EntityException("Product with id " + id + " does not exist");
//		}
//		
//		return found.get();
//	}

	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

//	public void deleteById(Long id) {
//		Product existed = findById(id);
//		productRepository.delete(existed);
//	}

	public List<Product> findProductByName(String name) {
		List<Product> list = productRepository.findByNameContainsIgnoreCase(name);
		return list;
	}
	public List<Product> findProductsByName(String name) {
	    return productRepository.findByNameContainsIgnoreCase(name);
	
	@Transactional
	public List<Product> updateOrdering(List<ProductDto> productDtos) {
	    List<Product> products = productDtos.stream().map(dto -> {
	        Product product = productRepository.findById(dto.getId())
	            .orElseThrow(() -> new EntityException("Product with id " + dto.getId() + " does not exist"));
	        
	        product.setOrdering(dto.getOrdering());
	        
	        return product;
	    }).collect(Collectors.toList());

	    return productRepository.saveAll(products); 
	}
}

package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.dto.AddressDto;
import cafe.dto.ProductDto;
import cafe.entity.Account;
import cafe.entity.Address;
import cafe.entity.Category;
import cafe.entity.Product;
import cafe.exception.EntityException;
import cafe.repository.AccountRepository;
import cafe.repository.AddressRespository;
import cafe.repository.CategoryRepository;

@Service
public class AddressService {
	@Autowired
	private  AddressRespository addressRespository;
	@Autowired
	private  AccountRepository accountRepository;
	
	public Address save(AddressDto addressDto) {
        // Tạo mới một Product entity từ ProductDto
		Address address = new Address();
        address.setActive(addressDto.getActive());
        address.setCityCode(addressDto.getCityCode());
        address.setDistrictCode(addressDto.getDistrictCode());
        address.setFullAddress(addressDto.getFullAddress());
        address.setIsDefault(addressDto.getIsDefault());
        address.setStreet(addressDto.getStreet());
        address.setWardCode(addressDto.getWardCode());
        
        // Tìm Category từ categoryId và set vào Product
        Account account = accountRepository.findById(addressDto.getAccount())
            .orElseThrow(() -> new EntityException("Account id " + addressDto.getAccount() + " does not exist"));
        address.setAccount(account);

        // Lưu sản phẩm vào cơ sở dữ liệu
        return addressRespository.save(address);
}
 

		public Address update(Long id, AddressDto dto) {
	    Optional<Address> existed = addressRespository.findById(id);
	    if (existed.isEmpty()) {
	        throw new EntityException("Address id " + id + " does not exist");
	    }

	    Address existedAddress = existed.get();
	    existedAddress.setActive(dto.getActive());
	    existedAddress.setCityCode(dto.getCityCode());
	    existedAddress.setDistrictCode(dto.getDistrictCode());
	    existedAddress.setFullAddress(dto.getFullAddress());
	    existedAddress.setIsDefault(dto.getIsDefault());
	    existedAddress.setStreet(dto.getStreet());
	    existedAddress.setWardCode(dto.getWardCode());

	    // Chỉ gán Category nếu có categoryid
	    if (dto.getAccount() != null) {
	        Optional<Account> acOptional = accountRepository.findById(dto.getAccount());
	        if (acOptional.isPresent()) {
	        	existedAddress.setAccount(acOptional.get());
	        } else {
	            throw new EntityException("Address id " + dto.getAccount() + " does not exist");
	        }
	    }

	    return addressRespository.save(existedAddress);
	}

 
 // để bật tắt active
   public Address toggleActive(Long id) {
        Optional<Address> optionalAddress = addressRespository.findById(id);
        if (optionalAddress.isEmpty()) {
            throw new EntityException("Adrees with id " + id + " does not exist");
        }

        Address address = optionalAddress.get();
        address.setActive(!address.getActive()); // Đảo ngược trạng thái active
        return addressRespository.save(address); // Lưu thay đổi vào cơ sở dữ liệu
    }
 

	public List<Address> findAll() {
		return addressRespository.findAll();
	}
	
	public Page<Address> findAll(Pageable pageable) {
		return addressRespository.findAll(pageable);
	}
	
	public Address findById(Long id) {
		Optional<Address> found = addressRespository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Address with id " + id + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(Long id) {
		Address existed = findById(id);
		addressRespository.delete(existed);
	}
}

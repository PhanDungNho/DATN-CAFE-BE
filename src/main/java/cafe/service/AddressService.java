package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	@Transactional
	public Address save(AddressDto addressDto) {
	    // Đặt isDefault mặc định là false nếu không nhận từ Frontend
	    if (addressDto.getIsDefault() == null) {
	        // Kiểm tra nếu danh sách địa chỉ hiện tại là rỗng
	        boolean isFirstAddress = addressRespository.count() == 0;
	        addressDto.setIsDefault(isFirstAddress);
	    }

	    Address address = new Address();
	    mapDtoToAddress(addressDto, address);
	    return addressRespository.save(address);
	}

    @Transactional
    public Address update(Long id, AddressDto dto) {
        Address address = addressRespository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("Address not found with id: " + id)
        );

        if (dto.getIsDefault()) {
            // Đặt các địa chỉ cũ của tài khoản về isDefault = false nếu cập nhật địa chỉ mới là mặc định
            resetDefaultAddress(dto.getAccount());
        }

        mapDtoToAddress(dto, address);
        return addressRespository.save(address);
    }

    private void resetDefaultAddress(String username) {
        List<Address> defaultAddresses = addressRespository.findByAccountUsernameAndIsDefaultTrue(username);
        for (Address addr : defaultAddresses) {
            addr.setIsDefault(false);
            addressRespository.save(addr);
        }
    }

    private void mapDtoToAddress(AddressDto dto, Address address) {
        address.setStreet(dto.getStreet());
        address.setWardCode(dto.getWardCode());
        address.setDistrictCode(dto.getDistrictCode());
        address.setCityCode(dto.getCityCode());
        address.setFullAddress(dto.getFullAddress());
        address.setIsDefault(dto.getIsDefault());
        address.setActive(dto.getActive());

        if (dto.getAccount() != null) {
            Account account = accountRepository.findByUsername(dto.getAccount())
                .orElseThrow(() -> new IllegalArgumentException("Account not found with username: " + dto.getAccount()));
            address.setAccount(account);
        }
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
	
	 public List<Address> findAddressesByAccountUsername(String username) {
	        Account account = accountRepository.findByUsername(username)
	                .orElseThrow(() -> new EntityException("Account with username " + username + " does not exist"));
	        return addressRespository.findByAccount(account);
	    }
	
	 public void deleteById(Long id) {
		    Address address = addressRespository.findById(id)
		            .orElseThrow(() -> new EntityException("Address with id " + id + " does not exist"));
		    addressRespository.delete(address);
		}
	
	public List<Address> findAddressesByFullAddress(String fullAddress) {
	    return addressRespository.findByFullAddressContainingIgnoreCase(fullAddress);
	}
	
	public Address setIsDefault(Long addressId) {
	    // Tìm địa chỉ với id, nếu không tìm thấy thì trả về null
	    Address addressToUpdate = addressRespository.findById(addressId).orElse(null);

	    // Nếu không tìm thấy địa chỉ, trả về null hoặc xử lý theo yêu cầu
	    if (addressToUpdate == null) {
	        return null; // Hoặc xử lý theo cách khác nếu cần
	    }

	    // Đặt tất cả địa chỉ khác của cùng tài khoản thành false
	    addressRespository.findByAccountUsernameAndIsDefaultTrue(addressToUpdate.getAccount().getUsername()).forEach(address -> {
	        address.setIsDefault(false);
	        addressRespository.save(address);
	    });

	    // Cập nhật isDefault của địa chỉ được chọn thành true
	    addressToUpdate.setIsDefault(true);
	    return addressRespository.save(addressToUpdate);
	}


}

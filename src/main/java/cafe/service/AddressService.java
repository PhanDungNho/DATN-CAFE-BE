package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Address;
import cafe.entity.exception.EntityException;
import cafe.repository.AddressRespository;
import cafe.repository.CategoryRepository;

@Service
public class AddressService {
	@Autowired
	private  AddressRespository addressRespository;

	public Address save(Address entity) {
		return addressRespository.save(entity);
	}

	public Address update(Long id, Address entity) {
		Optional<Address> existed = addressRespository.findById(id);
		if (existed.isEmpty()) {
			// neu k tim thay thi tra ve ngoai le
			throw new EntityException("Address id " + id + " does not exist");

		}
		try {
			// neu tìm thấy trong csdl thì sẽ truy cập tới đối tượng Address
			Address existedAddress = existed.get();
			existedAddress.setActive(entity.getActive());
			existedAddress.setCitycode(entity.getCitycode());
			existedAddress.setDistrictcode(entity.getDistrictcode());
			existedAddress.setFulladdresstext(entity.getFulladdresstext());
			existedAddress.setIsdefault(entity.getIsdefault());
			existedAddress.setStreet(entity.getStreet());
			existedAddress.setWardcode(entity.getWardcode());
			return addressRespository.save(existedAddress);
			// thì tiến hành cập nhật thủ công bth
		} catch (Exception ex) {
			// nếu có lỗi sẽ ném ra ngoại lệ
			throw new EntityException("Address is updated failed");
		}

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

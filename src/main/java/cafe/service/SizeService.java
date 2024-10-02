package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Category;
import cafe.entity.Size;
import cafe.exception.EntityException;
import cafe.repository.SizeRepository;

 

@Service
public class SizeService {
	@Autowired
	private SizeRepository sizeRepository;

	public Size save(Size entity) {
		return sizeRepository.save(entity);
	}

	public Size update(Long id, Size entity) {
		Optional<Size> existed = sizeRepository.findById(id);
		if (existed.isEmpty()) {
			// neu k tim thay thi tra ve ngoai le
			throw new EntityException("Size id " + id + " does not exist");

		}
		try {
			// neu tìm thấy trong csdl thì sẽ truy cập tới đối tượng Catrgory
			Size existedSize = existed.get();
			existedSize.setName(entity.getName());
			existedSize.setActive(entity.getActive());
			return sizeRepository.save(existedSize);
			// thì tiến hành cập nhật thủ công bth
		} catch (Exception ex) {
			// nếu có lỗi sẽ ném ra ngoại lệ
			throw new EntityException("Size is updated failed");
		}

	}

	public List<Size> findAll() {
		return sizeRepository.findAll();
	}

	public Page<Size> findAll(Pageable pageable) {
		return sizeRepository.findAll(pageable);
	}

	public Size findById(Long id) {
		Optional<Size> found = sizeRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Size with id " + id + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(Long id) {
		Size existed = findById(id);
		sizeRepository.delete(existed);
	}
	
	public Size toggleActive(Long id) {
		Optional<Size> optionalSize = sizeRepository.findById(id);
		if (optionalSize.isEmpty()) {
			throw new EntityException("Size with id " + id + " do not exist");
		}
		Size size = optionalSize.get();
		size.setActive(!size.getActive());
		return sizeRepository.save(size);
	}
	public List<Size> findSizeByName(String name){
		List<Size> list = sizeRepository.findByNameContainsIgnoreCase(name);
		return list;
	}
}

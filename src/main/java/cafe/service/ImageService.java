package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Image;
import cafe.exception.EntityException;
import cafe.entity.Image;
import cafe.repository.ImageRepository;
 
 

@Service
public class ImageService {
	@Autowired
	private ImageRepository imageRepository;

	public Image save(Image entity) {
		return imageRepository.save(entity);
	}

	public void deleteImageByFilename(String filename) {
		imageRepository.deleteByFileName(filename);
	}
//	public Image update(int id, Image entity) {
//		Optional<Image> existed = imageRepository.findById(id);
//		if (existed.isEmpty()) {
//			// neu k tim thay thi tra ve ngoai le
//			throw new EntityException("Image id " + id + " does not exist");
//
//		}
//		try {
//			// neu tìm thấy trong csdl thì sẽ truy cập tới đối tượng Catrgory
//			Image existedImage = existed.get();
//			existedImage.setName(entity.getName());
//			existedImage.setActive(entity.getActive());
//			return imageRepository.save(existedImage);
//			// thì tiến hành cập nhật thủ công bth
//		} catch (Exception ex) {
//			// nếu có lỗi sẽ ném ra ngoại lệ
//			throw new EntityException("Image is updated failed");
//		}
//
//	}

	public List<Image> findAll() {
		return imageRepository.findAll();
	}

	public Page<Image> findAll(Pageable pageable) {
		return imageRepository.findAll(pageable);
	}

//	public Image findById(Integer id) {
//		Optional<Image> found = imageRepository.findById(id);
//		if (found.isEmpty()) {
//			throw new EntityException("Image with id " + id + " does not exist");
//		}
//		return found.get();
//	}
	
//	public void deleteById(Integer id) {
//		Image existed = findById(id);
//		imageRepository.delete(existed);
//	}
}

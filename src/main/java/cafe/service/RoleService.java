package cafe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cafe.entity.Product;
import cafe.entity.Role;
import cafe.entity.Size;
import cafe.exception.EntityException;
import cafe.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;
	
	public Role save(Role entity) {
		return roleRepository.save(entity);
	}
	
	
	public Role update(Long id, Role entity) {
		Optional<Role> existed = roleRepository.findById(id);
		if (existed.isEmpty()) {
			// neu k tim thay thi tra ve ngoai le
			throw new EntityException("Role id " + id + " does not exist");

		}
		try {
			Role existedRole = existed.get();
			existedRole.setRolename(entity.getRolename());
			existedRole.setActive(entity.getActive());
			return roleRepository.save(existedRole);
			// thì tiến hành cập nhật thủ công bth
		} catch (Exception ex) {
			// nếu có lỗi sẽ ném ra ngoại lệ
			throw new EntityException("Role is updated failed");
		}

	}
	
	
	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	public Role findById(Long id) {
		Optional<Role> found = roleRepository.findById(id);
		if (found.isEmpty()) {
			throw new EntityException("Role with id " + id + " does not exist");
		}
		return found.get();
	}
	
	public void deleteById(Long id) {
		Role existed = findById(id);
		roleRepository.delete(existed);
	}
	
		// để bật tắt active
	   	public Role toggleActive(Long id) {
	        Optional<Role> optionalRole = roleRepository.findById(id);
	        if (optionalRole.isEmpty()) {
	            throw new EntityException("Role with id " + id + " does not exist");
	        }

	        Role role = optionalRole.get();
	        role.setActive(!role.getActive()); // Đảo ngược trạng thái active
	        return roleRepository.save(role); // Lưu thay đổi vào cơ sở dữ liệu
	    }
}

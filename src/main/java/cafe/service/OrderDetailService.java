package cafe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cafe.entity.OrderDetail;
import cafe.repository.OrderDetailRepository;

@Service
public class OrderDetailService {
	@Autowired
	OrderDetailRepository detailRepository;
	
	public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return detailRepository.findByOrderId(orderId);
    }

}

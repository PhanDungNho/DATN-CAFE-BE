package cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cafe.service.CartDetailToppingService;

@RestController
@RequestMapping("/api/v1/cartDetailToppings")
@CrossOrigin
public class CartDetailToppingController {
	@Autowired
	CartDetailToppingService cartDetailToppingService;
	
	@DeleteMapping("/{id}")
	public void deleteCartDetailTopping(@PathVariable Long id) {
	    System.out.println("Deleting cart detail topping with ID: " + id); 
	    cartDetailToppingService.deleteById(id);
	}

}

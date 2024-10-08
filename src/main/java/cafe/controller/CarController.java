package cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 

import cafe.dto.CarDto;
import cafe.entity.Car;
import cafe.exception.EntityException;
import cafe.service.CarService;
import cafe.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/cars")
@CrossOrigin
public class CarController {
    @Autowired
    private CarService carService;
	@Autowired
	private  FileStorageService fileStorageService;

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCar(@ModelAttribute CarDto carDto) {
        Car car = carService.addCar(carDto);
        return new ResponseEntity<>("hay", HttpStatus.CREATED);
    }
    
    
    
	@GetMapping("/logo/{filename:.+}")
	public ResponseEntity<?> downloadFile(@PathVariable String filename, HttpServletRequest request) {
		Resource resource = fileStorageService.loadLogoFileResource(filename);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			throw new EntityException("Could not determine file type");
		}
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}
}

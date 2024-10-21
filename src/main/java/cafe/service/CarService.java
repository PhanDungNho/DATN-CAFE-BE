package cafe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cafe.dto.CarDto;
import cafe.entity.Car;
import cafe.entity.Imagee;
import cafe.repository.CarRepository;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Car addCar(CarDto carDto) {
        Car car = new Car();
        car.setName(carDto.getName());
        car.setDescription(carDto.getDescription());

        // Thiết lập danh sách ảnh và upload ảnh
        if (carDto.getImageFiles() != null && !carDto.getImageFiles().isEmpty()) {
            List<Imagee> images = carDto.getImageFiles().stream()
                .map(file -> {
                    String filename = fileStorageService.storeLogoFile(file); // Lưu ảnh và lấy tên file
                    Imagee imagee = new Imagee();
                    imagee.setUrl(filename);
                    imagee.setCar(car); // Thiết lập car cho từng image
                    return imagee;
                })
                .collect(Collectors.toList());
            car.setImages(images);
        }

        return carRepository.save(car);
    }
}


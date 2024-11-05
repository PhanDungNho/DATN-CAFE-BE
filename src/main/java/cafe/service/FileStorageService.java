package cafe.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import cafe.dto.UploadedFileInfo;
import cafe.exception.FileNotFoundException;
import cafe.exception.FileStorageException;
import cafe.config.FileStorageProperties;
 

@Service
public class FileStorageService {

    private Path fileLogoStorageLocation;
    private Path fileProductImageStorageLocation;
    private String UPLOAD_DIR; // Đảm bảo biến này được khai báo

    // Khởi tạo đường dẫn
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileLogoStorageLocation = Paths.get(fileStorageProperties.getUploadLogoDir())
                .toAbsolutePath().normalize();
        this.fileProductImageStorageLocation = Paths.get(fileStorageProperties.getUploadProductImageDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileLogoStorageLocation);
            Files.createDirectories(fileProductImageStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored", ex);
        }
    }

    // 1. Hàm lưu trữ file
    public String storeLogoFile(MultipartFile file) {
        return storeFile(fileLogoStorageLocation, file);
    }

    public UploadedFileInfo storeUploadedProductImageFile(MultipartFile file) {
        return storeUploadedFile(fileProductImageStorageLocation, file);
    }

    private String storeFile(Path location, MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uuid.toString() + "." + ext;
        try {
            if (filename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }
            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
        }
    }

    private UploadedFileInfo storeUploadedFile(Path location, MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uuid.toString() + "." + ext;
        try {
            if (filename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }
            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            UploadedFileInfo info = new UploadedFileInfo();
            info.setFileName(filename);
            info.setUid(uuid.toString());
            info.setName(StringUtils.getFilename(file.getOriginalFilename()));
            return info;
        } catch (Exception ex) {
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", ex);
        }
    }

    // 2. Hàm tải file
    public Resource loadLogoFileResource(String name) {
        return loadFileAsResource(fileLogoStorageLocation, name);
    }

    public Resource loadProductImageFileAsResource(String filename) {
        return loadFileAsResource(fileProductImageStorageLocation, filename);
    }

    private Resource loadFileAsResource(Path location, String filename) {
        try {
            Path filePath = location.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + filename);
            }
        } catch (Exception exception) {
            throw new FileNotFoundException("File not found " + filename, exception);
        }
    }

    // 3. Hàm xóa file
    public void deleteLogoFile(String filename) {
        deleteFile(fileLogoStorageLocation, filename);
    }

    public void deleteProductImageFile(String filename) {
        deleteFile(fileProductImageStorageLocation, filename);
    }

    private void deleteFile(Path location, String filename) {
        try {
            Path filePath = location.resolve(filename).normalize();
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found " + filename);
            }
            Files.delete(filePath);
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + filename, e);
        }
    }
    
    
    public String storeFileFromUrl(String imageUrl, Path location) {
        UUID uuid = UUID.randomUUID(); 
        String ext = "jpg"; 
        String filename = uuid.toString() + "." + ext; 
        try {
      
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();

       
            Path targetLocation = location.resolve(filename);

     
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename; 
        } catch (Exception e) {
            throw new FileStorageException("Could not store file from URL " + imageUrl + ". Please try again!", e);
        }
    }
    
    public String storeLogoFileFromUrl(String imageUrl) {
        return storeFileFromUrl(imageUrl, fileLogoStorageLocation); // Lưu ảnh logo từ URL
    }
    
    //hieunguyen
    public void deleteFile(String filename) {
        Path filePath = Paths.get(UPLOAD_DIR + filename); // Đường dẫn đến tệp
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Không thể xóa tệp: " + e.getMessage());
        }
    }

}


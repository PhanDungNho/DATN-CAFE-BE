package cafe.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

	private final Path rootLocation = Paths.get(System.getProperty("user.dir"), "uploads");

	@GetMapping("/logo/{filename:.+}")
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		try {
			// Tạo đường dẫn đến file
			Path file = rootLocation.resolve("logo").resolve(filename).normalize();

			// Log đường dẫn để kiểm tra
			System.out.println("Đường dẫn tệp: " + file.toAbsolutePath().toString());

			// Kiểm tra xem file có tồn tại và có thể đọc được không
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
						.contentType(MediaType.IMAGE_JPEG) // Đảm bảo đây là loại nội dung phù hợp
						.body(resource);
			} else {
				throw new FileNotFoundException("Không thể đọc tệp!");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ByteArrayResource("Không tìm thấy tệp".getBytes()));
		}
	}
}

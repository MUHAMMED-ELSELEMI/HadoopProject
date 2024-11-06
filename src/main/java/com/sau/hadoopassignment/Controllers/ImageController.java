package com.sau.hadoopassignment.Controllers;

import com.sau.hadoopassignment.DTOs.EmployeeDTO;
import com.sau.hadoopassignment.Services.EmployeeService;
import com.sau.hadoopassignment.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees/{empno}/images")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ByteArrayResource> getEmployeeImage(@PathVariable Integer empno) throws IOException {
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(empno);
        if (!employee.isPresent() || employee.get().getImg() == null) {
            return ResponseEntity.notFound().build();
        }

        String imagePath = employee.get().getFullImagePath(); // Use the image name from EmployeeDTO
        byte[] imageBytes = imageService.getImageFromHDFS(imagePath);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + empno + ".jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(imageBytes.length)
                .body(resource);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadImage(@PathVariable Integer empno, @RequestParam("file") MultipartFile file) {
        try {
            String imageName = imageService.uploadImage(file); // Upload image and get its name
            employeeService.updateEmployeeImage(empno, imageName); // Update the employee with the new image name
            return ResponseEntity.ok(imageName); // Return the image name
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

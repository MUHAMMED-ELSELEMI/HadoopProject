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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/employees/{empno}/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private EmployeeService employeeService;

    // Get employee image and display it
    @GetMapping
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable Integer empno) throws IOException {
        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(empno);
        if (!employee.isPresent() || employee.get().getImg() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }

        String imagePath = employee.get().getFullImagePath();
        byte[] imageBytes = imageService.getImageFromHDFS(imagePath);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);  // Set content type based on the image type

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


    // Upload image for employee
    @PostMapping(consumes = {"multipart/form-data"})
    public String uploadImage(@PathVariable Integer empno, @RequestParam("file") MultipartFile file, Model model) {
        try {
            String imageName = imageService.uploadImage(file); // Upload image and get its name
            employeeService.updateEmployeeImage(empno, imageName); // Update the employee with the new image name
            model.addAttribute("success", "Image uploaded successfully!");
            return "redirect:/employees/" + empno + "/images"; // Redirect to the employee's image page
        } catch (IOException e) {
            model.addAttribute("error", "Error uploading image: " + e.getMessage());
            return "error"; // Return to error page in case of upload failure
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            return "error"; // Return to error page in case of other exceptions
        }
    }
}

package com.sau.hadoopassignment.Services;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    @Autowired
    private FileSystem hdfsFileSystem;

    public byte[] getImageFromHDFS(String imagePath) throws IOException {
        Path path = new Path(imagePath);
        try (InputStream inputStream = hdfsFileSystem.open(path);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            StreamUtils.copy(inputStream, outputStream);
            return outputStream.toByteArray();
        }
    }
    public void deleteImage(String imageName) throws IOException {
        Path path = new Path("/user/hadoop/images/" + imageName);
        if (hdfsFileSystem.exists(path)) {
            hdfsFileSystem.delete(path, false); // `false` means non-recursive deletion
        }
    }

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        String filePath = "/user/hadoop/images/" + file.getOriginalFilename();
        Path path = new Path(filePath);

        try (InputStream inputStream = file.getInputStream();
             FSDataOutputStream outputStream = hdfsFileSystem.create(path)) {

            byte[] buffer = new byte[4096]; // Use a larger buffer for better performance
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        return file.getOriginalFilename(); // Return the HDFS path for confirmation
    }



}

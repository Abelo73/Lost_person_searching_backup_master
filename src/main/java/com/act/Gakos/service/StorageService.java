package com.act.Gakos.service;

import com.act.Gakos.entity.UploadImage;
import com.act.Gakos.repository.StorageRepository;
import com.act.Gakos.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.oauth2.resourceserver.OpaqueTokenDsl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.util.Optional;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);
    private static final String UPLOAD_DIRECTORY = "uploads/images";





    public String uploadImage(MultipartFile file) {
        logger.info("Received upload request for file: {}", file.getOriginalFilename());
        try {
            // Create the upload directory if it doesn't exist
            File uploadDir = new File(UPLOAD_DIRECTORY);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Create a file object for the uploaded image
            File imageFile = new File(uploadDir, file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(file.getBytes());
            }

            // Save image details in the database
            UploadImage imageData = storageRepository.save(UploadImage.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .path(imageFile.getAbsolutePath()) // Save the path to the database
                    .build());

            logger.info("Image saved successfully at {}.", imageFile.getAbsolutePath());
            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            logger.error("File upload failed: {}", e.getMessage(), e);
            return "File upload failed: " + e.getMessage();
        }
    }

    public byte[] downloadImage(String fileName) {
        Optional<UploadImage> imageData = storageRepository.findByName(fileName);
        return ImageUtils.decompressImage(imageData.get().getImageData());
    }

    public byte[] getImageById(Long id) {
        Optional<UploadImage> image = storageRepository.findById(id);
        return ImageUtils.decompressImage(image.get().getImageData());
    }
}




//    public byte[] downloadImage(String fileName){
//        Optional<UploadImage> imagedata =  storageRepository.findByName(fileName);
//        byte[] images =  ImageUtils.decompressImage(imagedata.get().getImageData());
//        return images;
//    }
//
//
//    public byte[] getImageById(Long id){
//    Optional<UploadImage> image = storageRepository.findById(id);
//    byte[] images = ImageUtils.decompressImage(image.get().getImageData());
//    return images;
//    }
//}

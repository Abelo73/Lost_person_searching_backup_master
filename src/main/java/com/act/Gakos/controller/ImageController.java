package com.act.Gakos.controller;

import com.act.Gakos.entity.UploadImage;
import com.act.Gakos.repository.StorageRepository;
import com.act.Gakos.service.StorageService;
import com.act.Gakos.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ImageController {
    @Autowired
    private StorageService service;


    @Autowired
    private StorageRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);



//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
//        String uploadImage  =  service.uploadImage(file);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam(name = "file") MultipartFile file) {
        logger.info("Requests for uplaoding image: {} ", file);
        String uploadImage = service.uploadImage(file);

        logger.info("Upload image: {} ", uploadImage);

        Map<String, String> response = new HashMap<>();
        response.put("message", "File uploaded successfully");
        response.put("fileName", uploadImage);

        logger.info("Successfully uploaded image."+ response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData = service.downloadImage(fileName);

        return ResponseEntity.status(HttpStatus.OK)
                .body(imageData);
    }


//    @GetMapping("/image/{id}")
//    public ResponseEntity<Resource> getImage(@PathVariable Long id) throws MalformedURLException {
//        Optional<UploadImage> imageOptional = repository.findById(id);
//        if (imageOptional.isPresent()) {
//            UploadImage image = imageOptional.get();
//            Path path = Paths.get(image.getPath());
//            Resource resource = new UrlResource(path.toUri());
//
//            return ResponseEntity.ok()
//                    .contentType(MediaType.parseMediaType(image.getType()))
//                    .body(resource);
//        }
//        return ResponseEntity.notFound().build();
//    }


//    @GetMapping("/image/{id}")
//    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
//        Optional<UploadImage> imageOptional = repository.findById(id);
//        return imageOptional.map(uploadImage -> ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(uploadImage.getImageData())).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/image/{id}")
    public ResponseEntity<UploadImage> getImage(@PathVariable Long id) {
        Optional<UploadImage> imageOptional = repository.findById(id);
        return imageOptional.map(uploadImage -> {
            UploadImage response = new UploadImage(
                    uploadImage.getName(),
                    uploadImage.getPath(),
                    uploadImage.getImageData()
            );
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON) // Change to JSON content type
                    .body(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


//    @GetMapping("/image/{id}")
//    public ResponseEntity<?> getImageById(@PathVariable Long id) {
//        Optional<UploadImage> image = repository.findById(id);
//        if (image.isPresent()) {
//            byte[] images = ImageUtils.decompressImage(image.get().getImageData());
//            return ResponseEntity.status(HttpStatus.OK)
//                    .contentType(MediaType.parseMediaType(image.get().getType())) // Set the image content type
//                    .body(images);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Image not found");
//        }
//    }

}
package com.project.shopapp.controllers;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping("")
    public ResponseEntity<String> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return ResponseEntity.ok("getProducts here");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok(String.format("Product with id = %d", id));
    }

    @PostMapping("")
    //Neu tham so truyen vao la object ? => Data Transfer Obect = Request Object
    public ResponseEntity<?> insertProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
        @PathVariable("id") Long id,
            @RequestParam("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = productService.getProductById(id);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                //Kiem tra kich thuoc file va dinh dang
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File is too large! Maximun size is 10MB");
                }
                String contentType = file.getContentType();
                if (!contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unsupported content type");
                }
                //Luu file va cap nhat thumbnail trong DTO
                String fileName = storeFile(file);
                //Luu vao doi tuong product trong DB
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder().imageUrl(fileName).build()
                );
                //luu vao bang product_images
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        //Them UUID vao truoc ten file de dam bao ten file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Duong dan den thu muc ma ban muon luu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        //Kiem tra va tao thu muc neu no khong ton tai
        if (!uploadDir.toFile().exists()) {
            Files.createDirectories(uploadDir);
        }
        //Duong dan day du den file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //Sao chep file vao thu muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok("This is updateProduct " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok("This is deleteProduct " + id);
    }

}

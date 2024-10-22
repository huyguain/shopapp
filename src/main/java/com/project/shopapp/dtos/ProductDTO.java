package com.project.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data //toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @NotBlank(message = "Product's name cannot be empty")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String name;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Discount must be less or than equal to 10,000000")
    private Float price;
    private String thumbnail;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category_id")
    private Long categoryId;
}

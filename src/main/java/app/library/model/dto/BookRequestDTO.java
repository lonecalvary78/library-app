package app.library.model.dto;

import app.library.validation.ISBN;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Data required to create a new book")
public class BookRequestDTO {
    
    @Schema(description = "Title of the book", example = "The Hobbit", required = true)
    @NotBlank(message = "Title is required")
    private String title;
    
    @Schema(description = "Author of the book", example = "J.R.R. Tolkien", required = true)
    @NotBlank(message = "Author is required")
    private String author;
    
    @Schema(
        description = "ISBN (International Standard Book Number) of the book - must be a valid 10-digit or 13-digit ISBN", 
        example = "9780547928227", 
        required = true
    )
    @NotBlank(message = "ISBN is required")
    @ISBN
    private String isbn;
} 
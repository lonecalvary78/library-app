package app.library.model.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Book data returned from API operations")
public class BookResponseDTO {
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;
    
    @Schema(description = "Title of the book", example = "The Hobbit")
    private String title;
    
    @Schema(description = "Author of the book", example = "J.R.R. Tolkien")
    private String author;
    
    @Schema(description = "ISBN of the book", example = "9780547928227")
    private String isbn;
    
    @Schema(description = "Flag indicating if the book is currently borrowed", example = "false")
    private boolean borrowed;
    
    @Schema(description = "ID of the borrower if the book is borrowed", example = "123", nullable = true)
    private Long borrowerId;
    
    @Schema(description = "Name of the borrower if the book is borrowed", example = "John Doe", nullable = true)
    private String borrowerName;
    
    @Schema(description = "Timestamp when the book was borrowed", example = "2023-06-15T10:30:00Z", nullable = true)
    private Instant borrowedAt;
    
    @Schema(description = "Timestamp when the book was added to the system", example = "2023-05-01T09:15:00Z")
    private Instant createdAt;
} 
package app.library.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data required to borrow a book")
public class BookBorrowRequestDTO {
    
    @Schema(
        description = "ID of the borrower who is borrowing the book", 
        example = "1", 
        required = true
    )
    @NotNull(message = "Borrower ID is required")
    private Long borrowerId;
} 
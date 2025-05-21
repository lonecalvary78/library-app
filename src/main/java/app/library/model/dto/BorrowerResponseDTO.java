package app.library.model.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Borrower data returned from API operations")
public class BorrowerResponseDTO {
    @Schema(description = "Unique identifier of the borrower", example = "1")
    private Long id;
    
    @Schema(description = "Name of the borrower", example = "John Doe")
    private String name;
    
    @Schema(description = "Email address of the borrower", example = "john.doe@example.com")
    private String email;
    
    @Schema(description = "Timestamp when the borrower was registered", example = "2023-05-01T09:15:00Z")
    private Instant createdAt;
} 
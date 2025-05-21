package app.library.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Data required to register a new borrower")
public class BorrowerRequestDTO {
    
    @Schema(description = "Name of the borrower", example = "John Doe", required = true)
    @NotBlank(message = "Name is required")
    private String name;
    
    @Schema(
        description = "Email address of the borrower (must be unique)", 
        example = "john.doe@example.com", 
        required = true
    )
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
} 
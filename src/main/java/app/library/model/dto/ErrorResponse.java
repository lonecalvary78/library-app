package app.library.model.dto;

import java.time.Instant;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response structure")
public class ErrorResponse {
    
    @Schema(description = "Timestamp when the error occurred", example = "2023-06-15T10:30:00Z")
    private Instant timestamp;
    
    @Schema(description = "HTTP status code", example = "400")
    private int status;
    
    @Schema(description = "Error type", example = "Validation Error")
    private String error;
    
    @Schema(description = "Error message", example = "Invalid input")
    private String message;
    
    @Schema(description = "Detailed validation errors (for validation errors only)", nullable = true)
    private Map<String, String> errors;
} 
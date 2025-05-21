package app.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.library.model.dto.BorrowerRequestDTO;
import app.library.model.dto.BorrowerResponseDTO;
import app.library.model.dto.ErrorResponse;
import app.library.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/borrowers")
@RequiredArgsConstructor
@Tag(name = "Borrowers", description = "Borrower management APIs")
public class BorrowerController {
    
    private final BorrowerService borrowerService;
    
    @Operation(
        summary = "Register a new borrower", 
        description = "Registers a new borrower with the provided details. Email addresses must be unique."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Borrower registered successfully", 
                    content = @Content(schema = @Schema(implementation = BorrowerResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "A borrower with this email already exists", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BorrowerResponseDTO> createBorrower(
            @Parameter(description = "Borrower data to register", required = true) 
            @Valid @RequestBody BorrowerRequestDTO borrowerRequestDTO) {
        BorrowerResponseDTO createdBorrower = borrowerService.createBorrower(borrowerRequestDTO);
        return new ResponseEntity<>(createdBorrower, HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all borrowers", 
        description = "Retrieves all borrowers registered in the library"
    )
    @ApiResponse(responseCode = "200", description = "Borrowers retrieved successfully", 
                content = @Content(schema = @Schema(implementation = BorrowerResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<BorrowerResponseDTO>> getAllBorrowers() {
        List<BorrowerResponseDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }
    
    @Operation(
        summary = "Get a borrower by ID", 
        description = "Retrieves a specific borrower by their ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Borrower retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = BorrowerResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Borrower not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BorrowerResponseDTO> getBorrowerById(
            @Parameter(description = "ID of the borrower to retrieve", required = true) 
            @PathVariable Long id) {
        BorrowerResponseDTO borrower = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(borrower);
    }
} 
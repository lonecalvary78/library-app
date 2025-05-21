package app.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.library.model.dto.BookBorrowRequestDTO;
import app.library.model.dto.BookRequestDTO;
import app.library.model.dto.BookResponseDTO;
import app.library.model.dto.ErrorResponse;
import app.library.service.BookService;
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
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management APIs")
public class BookController {
    
    private final BookService bookService;
    
    @Operation(
        summary = "Create a new book", 
        description = "Creates a new book with the provided details. Multiple books with the same ISBN are allowed as different copies."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Book created successfully", 
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "A book with the same ISBN exists but with different title/author", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @Parameter(description = "Book data to create", required = true) 
            @Valid @RequestBody BookRequestDTO bookRequestDTO) {
        BookResponseDTO createdBook = bookService.createBook(bookRequestDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all books", 
        description = "Retrieves all books in the library"
    )
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully", 
                content = @Content(schema = @Schema(implementation = BookResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @Operation(
        summary = "Get a book by ID", 
        description = "Retrieves a specific book by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book retrieved successfully", 
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Book not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true) 
            @PathVariable Long id) {
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }
    
    @Operation(
        summary = "Borrow a book", 
        description = "Marks a book as borrowed by a specific borrower. A book can only be borrowed by one person at a time."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book borrowed successfully", 
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Book or borrower not found", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "Book is already borrowed", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}/borrow")
    public ResponseEntity<BookResponseDTO> borrowBook(
            @Parameter(description = "ID of the book to borrow", required = true) 
            @PathVariable Long id, 
            @Parameter(description = "Borrower information", required = true) 
            @Valid @RequestBody BookBorrowRequestDTO borrowRequest) {
        BookResponseDTO borrowedBook = bookService.borrowBook(id, borrowRequest);
        return ResponseEntity.ok(borrowedBook);
    }
    
    @Operation(
        summary = "Return a book", 
        description = "Marks a borrowed book as returned and available for borrowing again"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book returned successfully", 
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Book not found or not currently borrowed", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}/return")
    public ResponseEntity<BookResponseDTO> returnBook(
            @Parameter(description = "ID of the book to return", required = true) 
            @PathVariable Long id) {
        BookResponseDTO returnedBook = bookService.returnBook(id);
        return ResponseEntity.ok(returnedBook);
    }
}

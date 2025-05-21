package app.library.service;

import java.util.List;

import app.library.model.dto.BookBorrowRequestDTO;
import app.library.model.dto.BookRequestDTO;
import app.library.model.dto.BookResponseDTO;

public interface BookService {
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);
    
    List<BookResponseDTO> getAllBooks();
    
    BookResponseDTO getBookById(Long id);
    
    BookResponseDTO borrowBook(Long bookId, BookBorrowRequestDTO borrowRequest);
    
    BookResponseDTO returnBook(Long bookId);
} 
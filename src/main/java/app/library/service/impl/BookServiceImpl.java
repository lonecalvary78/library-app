package app.library.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.library.exception.ResourceAlreadyExistsException;
import app.library.exception.ResourceNotFoundException;
import app.library.model.dto.BookBorrowRequestDTO;
import app.library.model.dto.BookRequestDTO;
import app.library.model.dto.BookResponseDTO;
import app.library.model.entity.Book;
import app.library.model.entity.Borrower;
import app.library.repository.BookRepository;
import app.library.repository.BorrowerRepository;
import app.library.service.BookService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    
    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        // Validate that the ISBN and title/author combination are consistent
        boolean bookExists = bookRepository.existsByIsbnAndTitleAndAuthor(
                bookRequestDTO.getIsbn(), 
                bookRequestDTO.getTitle(), 
                bookRequestDTO.getAuthor());
        
        if (!bookExists) {
            // Check if books with this ISBN exist but with different title/author
            List<Book> existingBooks = bookRepository.findByIsbn(bookRequestDTO.getIsbn());
            if (!existingBooks.isEmpty()) {
                Book existingBook = existingBooks.get(0);
                if (!existingBook.getTitle().equals(bookRequestDTO.getTitle()) || 
                    !existingBook.getAuthor().equals(bookRequestDTO.getAuthor())) {
                    throw new ResourceAlreadyExistsException(
                            "A book with ISBN " + bookRequestDTO.getIsbn() + 
                            " already exists but with different title or author");
                }
            }
        }
        
        Book book = new Book();
        book.setTitle(bookRequestDTO.getTitle());
        book.setAuthor(bookRequestDTO.getAuthor());
        book.setIsbn(bookRequestDTO.getIsbn());
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDto(book);
    }

    @Override
    @Transactional
    public BookResponseDTO borrowBook(Long bookId, BookBorrowRequestDTO borrowRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        if (book.getBorrower() != null) {
            throw new ResourceAlreadyExistsException("Book is already borrowed");
        }
        
        Borrower borrower = borrowerRepository.findById(borrowRequest.getBorrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + borrowRequest.getBorrowerId()));
        
        book.setBorrower(borrower);
        book.setBorrowedAt(Instant.now());
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }

    @Override
    @Transactional
    public BookResponseDTO returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        
        if (book.getBorrower() == null) {
            throw new ResourceNotFoundException("Book is not currently borrowed");
        }
        
        book.setBorrower(null);
        book.setBorrowedAt(null);
        
        Book savedBook = bookRepository.save(book);
        return convertToDto(savedBook);
    }
    
    private BookResponseDTO convertToDto(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setCreatedAt(book.getCreatedAt());
        
        if (book.getBorrower() != null) {
            dto.setBorrowed(true);
            dto.setBorrowerId(book.getBorrower().getId());
            dto.setBorrowerName(book.getBorrower().getName());
            dto.setBorrowedAt(book.getBorrowedAt());
        } else {
            dto.setBorrowed(false);
        }
        
        return dto;
    }
} 
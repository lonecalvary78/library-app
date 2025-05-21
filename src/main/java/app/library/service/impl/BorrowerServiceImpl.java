package app.library.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.library.exception.ResourceAlreadyExistsException;
import app.library.exception.ResourceNotFoundException;
import app.library.model.dto.BorrowerRequestDTO;
import app.library.model.dto.BorrowerResponseDTO;
import app.library.model.entity.Borrower;
import app.library.repository.BorrowerRepository;
import app.library.service.BorrowerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Override
    @Transactional
    public BorrowerResponseDTO createBorrower(BorrowerRequestDTO borrowerRequestDTO) {
        if (borrowerRepository.existsByEmail(borrowerRequestDTO.getEmail())) {
            throw new ResourceAlreadyExistsException("Borrower with email " + borrowerRequestDTO.getEmail() + " already exists");
        }
        
        Borrower borrower = new Borrower();
        borrower.setName(borrowerRequestDTO.getName());
        borrower.setEmail(borrowerRequestDTO.getEmail());
        
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return convertToDto(savedBorrower);
    }

    @Override
    public List<BorrowerResponseDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BorrowerResponseDTO getBorrowerById(Long id) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found with id: " + id));
        return convertToDto(borrower);
    }
    
    private BorrowerResponseDTO convertToDto(Borrower borrower) {
        BorrowerResponseDTO dto = new BorrowerResponseDTO();
        dto.setId(borrower.getId());
        dto.setName(borrower.getName());
        dto.setEmail(borrower.getEmail());
        dto.setCreatedAt(borrower.getCreatedAt());
        return dto;
    }
} 
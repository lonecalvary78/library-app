package app.library.service;

import java.util.List;

import app.library.model.dto.BorrowerRequestDTO;
import app.library.model.dto.BorrowerResponseDTO;

public interface BorrowerService {
    BorrowerResponseDTO createBorrower(BorrowerRequestDTO borrowerRequestDTO);
    
    List<BorrowerResponseDTO> getAllBorrowers();
    
    BorrowerResponseDTO getBorrowerById(Long id);
} 
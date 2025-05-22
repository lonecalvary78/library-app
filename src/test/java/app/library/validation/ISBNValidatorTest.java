package app.library.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintValidatorContext;

class ISBNValidatorTest {

    private ISBNValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ISBNValidator();
        context = null; // We don't use the context in our validator implementation
    }

    @ParameterizedTest
    @DisplayName("Test valid ISBNs")
    @ValueSource(strings = {
        "0-306-40615-2",   // Valid ISBN-10 with hyphens
        "0306406152",      // Valid ISBN-10 without hyphens
        "0-13-149505-0",   // Another valid ISBN-10
        "978-0-306-40615-7", // Valid ISBN-13 with hyphens
        "9780306406157",    // Valid ISBN-13 without hyphens
        "978-3-16-148410-0", // Another valid ISBN-13
        "039-3-04-002-X"     // Valid ISBN-10 with X check digit
    })
    void shouldValidateCorrectISBNs(String isbn) {
        boolean result = validator.isValid(isbn, context);
        
        if (!result) {
            System.out.println("Failed ISBN: " + isbn);
            String cleanedISBN = isbn.replaceAll("[\\s-]", "");
            System.out.println("Cleaned ISBN: " + cleanedISBN);
        }
        
        assertTrue(result);
    }

    @Test
    @DisplayName("Test specific problematic ISBN")
    void testSpecificProblematicISBN() {
        String isbn = "0-13-149505-0";
        String cleanedISBN = isbn.replaceAll("[\\s-]", "");
        System.out.println("Testing ISBN: " + isbn);
        System.out.println("Cleaned ISBN: " + cleanedISBN);
        
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(cleanedISBN.charAt(i));
            int weight = 10 - i;
            int contribution = digit * weight;
            sum += contribution;
            System.out.println("Digit " + i + ": " + digit + " × " + weight + " = " + contribution);
        }
        
        char lastChar = cleanedISBN.charAt(9);
        int lastValue = Character.getNumericValue(lastChar);
        sum += lastValue;
        System.out.println("Last digit: " + lastValue);
        System.out.println("Sum: " + sum);
        System.out.println("Sum % 11: " + (sum % 11));
        System.out.println("Is divisible by 11: " + (sum % 11 == 0));
        
        boolean isValid = validator.isValid(isbn, context);
        System.out.println("Validator result: " + isValid);
        
        assertTrue(isValid);
    }

    @ParameterizedTest
    @DisplayName("Test invalid ISBNs")
    @ValueSource(strings = {
        "0-306-40615-3",   // Invalid ISBN-10 check digit
        "0306406159",      // Invalid ISBN-10 check digit
        "978-0-306-40615-8", // Invalid ISBN-13 check digit
        "9780306406158",    // Invalid ISBN-13 check digit
        "123456789",       // Too short
        "12345678901234",  // Too long
        "abcdefghij",      // Non-numeric
        "123456789X0"      // Invalid format with X not in last position
    })
    void shouldRejectInvalidISBNs(String isbn) {
        assertFalse(validator.isValid(isbn, context));
    }

    @Test
    @DisplayName("Test null, empty, and whitespace values")
    void shouldAcceptNullOrEmptyValues() {
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("  ", context));
    }
} 
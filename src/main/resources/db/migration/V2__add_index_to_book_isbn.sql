-- Add index on ISBN to improve search performance
CREATE INDEX idx_book_isbn ON book(isbn); 
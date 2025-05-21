-- Create borrower table
CREATE TABLE IF NOT EXISTS borrower (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP
);

-- Create book table
CREATE TABLE IF NOT EXISTS book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(50) NOT NULL,
    borrower_id BIGINT,
    borrowed_at TIMESTAMP,
    created_at TIMESTAMP,
    CONSTRAINT fk_borrower FOREIGN KEY (borrower_id) REFERENCES borrower(id)
); 
package com.stc.library.model;

public class Book {

    private String title;
    private String author;
    private int publicationYear;
    private int edition;
    private String publisher;
    private int copyNumber;
    private int totalCopies;
    private boolean isAvailable;
    private String isbn;
    private int borrowCount;

    public Book(String title, String author, int publicationYear, int edition, String publisher, int copyNumber, boolean isAvailable, String isbn) {
        validateTitle(title);
        validateAuthor(author);
        validatePublicationYear(publicationYear);
        this.edition = edition;
        this.publisher = publisher;
        validateCopyNumber(copyNumber);
        this.isAvailable = isAvailable;
        this.isbn = isbn;
        this.borrowCount = 0;
        this.totalCopies = copyNumber; 
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public int getEdition() {
        return edition;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getCopyNumber() {
        return copyNumber;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        if (borrowCount < 0) {
            throw new IllegalArgumentException("Borrow count cannot be negative.");
        }
        this.borrowCount = borrowCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setCopyNumber(int copyNumber) {
        validateCopyNumber(copyNumber);
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTotalCopies(int totalCopies) {
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative.");
        }
        this.totalCopies = totalCopies;
    }

    private void validatePublicationYear(int publicationYear) {
        int currentYear = java.time.Year.now().getValue();
        if (publicationYear < 1450 || publicationYear > currentYear) {
            throw new IllegalArgumentException("Publication year must be between 1450 and " + currentYear);
        }
        this.publicationYear = publicationYear;
    }

    private void validateCopyNumber(int copyNumber) {
        if (copyNumber < 0) {
            throw new IllegalArgumentException("Copy number cannot be negative.");
        }
        this.copyNumber = copyNumber;
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty.");
        }
        this.title = title;
    }

    private void validateAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty.");
        }
        this.author = author;
    }

/// Borrow A Book -- MELEK

    /**
     * Borrows the given number of copies of this book.
     * Verifies availability before borrowing, decreases the available copies,
     * updates the borrow statistics and marks the book as unavailable when no
     * copies are left.
     *
     * @param numberOfBorrowedCopies how many copies to borrow (must be positive)
     * @throws IllegalArgumentException if the amount is not positive or there
     *                                  are not enough copies available
     */
    public void borrowABook(int numberOfBorrowedCopies) {
        if (numberOfBorrowedCopies <= 0) {
            throw new IllegalArgumentException("Number of copies to borrow must be positive.");
        }
        if (!isAvailable || this.copyNumber < numberOfBorrowedCopies) {
            throw new IllegalArgumentException(
                    "Not enough copies available to borrow. Available copies: " + this.copyNumber + ".");
        }

        setCopyNumber(this.copyNumber - numberOfBorrowedCopies);
        this.borrowCount += numberOfBorrowedCopies;

        if (this.copyNumber == 0) {
            this.isAvailable = false;
        }
    }

    public void returnABook(int numberOfReturnedCopies) {
        if (numberOfReturnedCopies <= 0) {
            throw new IllegalArgumentException("Number of copies to return must be positive.");
        }
        
        if (this.copyNumber + numberOfReturnedCopies > this.totalCopies) {
            int maxAllowedToReturn = this.totalCopies - this.copyNumber;
            throw new IllegalArgumentException("Cannot return more copies than the total owned by the library. Max allowed to return: " + maxAllowedToReturn);
        }

        setCopyNumber(this.copyNumber + numberOfReturnedCopies);
        this.isAvailable = true;
    }

}

package com.stc.library.model;

public class Book {

    private String title;
    private String author;
    private int publicationYear;
    private int edition;
    private String publisher;
    private int copyNumber;
    private boolean isAvailable;
    private String isbn;

    public Book(String title, String author, int publicationYear, int edition, String publisher, int copyNumber, boolean isAvailable, String isbn) {
        validateTitle(title);
        validateAuthor(author);
        validatePublicationYear(publicationYear);
        this.edition = edition;
        this.publisher = publisher;
        validateCopyNumber(copyNumber);
        this.isAvailable = isAvailable;
        this.isbn = isbn;
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

    public boolean isAvailable() {
        return isAvailable;
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
        this.copyNumber = copyNumber;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

}

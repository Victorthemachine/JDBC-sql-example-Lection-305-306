/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectTemplates.simpleLibApp;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tanya
 */
public class Book {
    
    private final String title;
    private final Set<String> authors;
    private final String published;
    private final String publisher;
    private final String ISBN;
    private final Set<Copy> Copies = new HashSet<>();
    
    private int copyId = 0;

    public Book(String title, Set<String> author, String published, String publisher, String ISBN) {
        this.title = title;
        this.authors = author;
        this.published = published;
        this.publisher = publisher;
        this.ISBN = ISBN;
    }

    public Set<String> getAuthors() {
        return Collections.unmodifiableSet(authors);
    }

    public void addAuthor(String author) {
        this.authors.add(author);
    }
    
    public String getPublished() {
        return published;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTitle() {
        return title;
    }

    public Set<Copy> getCopies() {
        return Collections.unmodifiableSet(Copies);
    }
    
    public Copy createCopy() {
        copyId++;
        Copy copy = new Copy(this.ISBN, this, copyId);
        Copies.add(copy);
        return copy;
    }
    
    public void removeCopy(Copy copie) {
        Copies.remove(copie);
    }
    
    private void addCopy(Copy copykatze) {
        Copies.add(copykatze);
    }    

    public String getISBN() {
        return ISBN;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectTemplates.simpleLibApp;

import java.util.Set;

/**
 *
 * @author Tanya
 */
public class BookBuilder {

    private String title;
    private Set <String> author;
    private String published;
    private String publisher;
    private String ISBN;

    public BookBuilder() {
    }

    public BookBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder setAuthor(Set<String> author) {
        this.author = author;
        return this;
    }

    public BookBuilder setPublished(String published) {
        this.published = published;
        return this;
    }

    public BookBuilder setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public BookBuilder setISBN(String ISBN) {
        this.ISBN = ISBN;
        return this;
    }

    public Book build() {
        return new Book(title, author, published, publisher, ISBN);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectTemplates.simpleLibApp;

/**
 *
 * @author Tanya
 */
public class Copy {

    private final String registration;
    private final Book book;
    private final int id;

    public Copy(String registration, Book book, int id) {
        this.registration = registration;
        this.book = book;
        this.id = id;
        Library.getInstance().add(this);
    }

/*    @Override
    protected void finalize() throws Throwable {
        book.removeCopy(this);
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }
  */  
    public String getRegistration() {
        return registration;
    }

    public Book getBook() {
        return book;
    }

    @Override
    public String toString() {
        return "Copy{" + "evidenceNummer=" + registration + ", weissy=" + book + '}';
    }

}

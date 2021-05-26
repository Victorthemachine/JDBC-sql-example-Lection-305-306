/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectTemplates.simpleLibApp;

import java.util.Date;

/**
 *
 * @author Tanya
 */
public class Borrowing {

    private final Date published;
    private final Copy copy;
    private final Reader reader;

    public Borrowing(Date published, Copy copy, Reader reader) {
        this.published = published;
        this.copy = copy;
        this.reader = reader;
        Library.getInstance().add(this);
    }

    public Date getPublished() {
        return published;
    }

    public Copy getCopy() {
        return copy;
    }

    public Reader getReader() {
        return reader;
    }

}

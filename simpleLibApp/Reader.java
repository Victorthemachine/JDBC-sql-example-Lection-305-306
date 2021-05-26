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
public class Reader {

    private final String registration;
    private final String name;

    public Reader(String registration, String name) {
        this.registration = registration;
        this.name = name;
        Library.getInstance().add(this);
    }

    public String getRegistration() {
        return registration;
    }

    public String getName() {
        return name;
    }

}

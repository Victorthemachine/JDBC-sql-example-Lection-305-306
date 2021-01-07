/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgclassesandothersmallthings.pgClasses;

/**
 *
 * @author Tanya
 */
public class Config {
    
    public static final String API = "jdbc";
    public static final String CONN = "mysql";
    public static final String HOST = "localhost";
    public static final String DATABASE = "dk-3xx_air";
    public static final String USER = "dk-3xx";
    public static final String PASSWORD = "xxx";
    public static final String URL = API + ":" + CONN + "://" + HOST + "/" + DATABASE;
    
}

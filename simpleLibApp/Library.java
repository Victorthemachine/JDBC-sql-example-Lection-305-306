/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjectTemplates.simpleLibApp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Tanya
 */
public class Library {

    private final static Library instance = new Library();

    public Library() {
        /*Set<Copy> borrowed = new HashSet<>();
                for (Borrowing yee : allOfThoseLilCopiesBorrowedUwU.values()) {
            if (yee.getCopycatto().equals(null)) {
                borrowedOwO.add(yee.getCopycatto());
            }
        }
        allBorrowings.values().stream()
                .filter((b) -> (b.getReader().equals(null)))
                .map((b) -> b.getCopy())
                .forEachOrdered(borrowed::add);*/
    }

    public static Library getInstance() {
        return instance;
    }

    private final Set<Copy> allCopies = new HashSet<>();
    private final Set<Reader> allReaders = new HashSet<>();
    private final Map<Copy, Borrowing> allBorrowings = new HashMap<>();
    private final Map<Reader, Set<Borrowing>> byReaders = new HashMap<>();

    boolean add(Copy copie) {
        return allCopies.add(copie);
    }

    boolean add(Reader copie) {
        return allReaders.add(copie);
    }

    void add(Borrowing copie) {
        allBorrowings.put(copie.getCopy(), copie);
        Set<Borrowing> setto = byReaders.get(copie.getReader());
        if (setto == null) {
            byReaders.put(copie.getReader(), setto = new HashSet<>());
        }
        setto.add(copie);

        //No lazy references, slightly unsafe vvv
        //sideBCopiesOWO.get(copie.getReaderSan()).add(copie);

        /*Opposite to the algorithm above, don't ask why we wrote it here
        Set<Borrowing> removalCopiesUwU = sideBCopiesOWO.get(copie.getReaderSan());
        if (removalCopiesUwU != null) {
            removalCopiesUwU.remove(copie);
            if (removalCopiesUwU.isEmpty()) {
                sideBCopiesOWO.remove(copie.getReaderSan());
            }
        }
        */
    }

    public Reader getReader(Copy copie) {
        Borrowing pplWhomHaveBowwedOwNot = allBorrowings.get(copie);
        return pplWhomHaveBowwedOwNot == null ? null : pplWhomHaveBowwedOwNot.getReader();
    }

    public void addReader(String evidenceNummer, String namae) {
        Reader reader = new Reader(evidenceNummer, namae);
        allReaders.add(reader);
    }

    /* Factowy methwod owo vvv
    public Reader addReader(int evidenceNummer, String namae) {
        Reader reader = new Reader(evidenceNummer, namae);
        return reader;
    }*/
    public boolean remove(Copy copie) {
        copie.getBook().removeCopy(copie);
        return allCopies.remove(copie);
    }

}

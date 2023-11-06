package it.unicam.cs.asdl2223.mp1;

/**
 * Un oggetto di quest classe rappresenta un fattore primo di un numero naturale
 * con una certa molteplicità.
 * 
 * @author Luca Tesei (template) // TODO INSERIRE NOME, COGNOME ED EMAIL
 *         xxxx@studenti.unicam.it DELLO STUDENTE (implementazione)
 *
 */
public class Factor implements Comparable<Factor> {

    /*
     * Numero primo corrispondente a questo fattore
     */
    private final int primeValue;

    /*
     * Molteplicità del numero primo di questo fattore, deve essere maggiore o
     * uguale a 1.
     */
    private final int multiplicity;

    // TODO definire ulteriori variabili istanza che si ritengono necessarie per
    // implementare tutti i metodi

    /**
     * Crea un fattore primo di un numero naturale, formato da un numero primo e
     * dalla sua molteplicità.
     * 
     * @param primeValue,
     *                          numero primo
     * @param multiplicity,
     *                          valore della molteplicità, deve essere almeno 1
     * @throws IllegalArgumentException
     *                                      se la molteplicità è minore di 1
     *                                      oppure se primeValue è minore o
     *                                      uguale di 0.
     */
    public Factor(int primeValue, int multiplicity) {
        // TODO implementare
        this.primeValue = primeValue;
        this.multiplicity = multiplicity;
    }

    /**
     * @return the primeValue
     */
    public int getPrimeValue() {
        return primeValue;
    }

    /**
     * @return the multiplicity
     */
    public int getMultiplicity() {
        return multiplicity;
    }

    /*
     * Calcola l'hashcode dell'oggetto in accordo ai valori usati per definire
     * il metodo equals.
     */
    @Override
    public int hashCode() {
        // TODO implentare
        return -1;
    }

    /*
     * Due oggetti Factor sono uguali se e solo se hanno lo stesso numero primo
     * e la stessa molteplicità
     */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        return false;
    }

    /*
     * Un Factor è minore di un altro se contiene il numero primo minore. Se due
     * Factor hanno lo stesso numero primo allora il più piccolo dei due è
     * quello ce ha minore molteplicità.
     */
    @Override
    public int compareTo(Factor o) {
        // TODO implementare
        return 0;
    }

    /*
     * Il fattore viene reso con la stringa primeValue^multiplicity
     */
    @Override
    public String toString() {
        // TODO implementare
        return "";
    }

    // TODO inserire eventuali metodi accessori privati per fini di
    // implementazione

}

package it.unicam.cs.asdl2223.mp1;


/**
 * Il crivello di Eratostene è un modo per determinare tutti i numeri primi da
 * {@code 1} a un certo intero {@code n} assegnato. Il crivello può essere
 * semplicemente rappresentato da un array di booleani in cui la posizione i è
 * true se e solo se il numero i è primo. Per costruirlo si segue un semplice
 * algoritmo che parte dalla posizione 2 e la mette a true. Dopodiché vengono
 * messe a false tutte le posizioni multiple di 2. Poi viene determinata la
 * prima posizione successiva a 2 che non è stato messa a false, in questo caso
 * 3. Si passa quindi a mettere a false tutte le posizioni multiple di 3. Si
 * ripete la procedura per la posizione non false successiva, che sarà 5. E così
 * via fino ad aver visitato tutte le caselle minori o uguali alla capacità del
 * crivello.
 * 
 * Un oggetto di questa classe permette di conoscere direttamente se un numero
 * tra 2 e la capacità del crivello è primo tramite il metodo isPrime(int).
 * 
 * Inoltre un oggetto di questa classe fornisce la funzionalità di elencare
 * tutti i numeri primi da 2 alla capacità del crivello tramite una serie di
 * chiamate al metodo nextPrime(). L'elenco può essere fatto ripartire in
 * qualsiasi momento chiamando il metodo restartPrimeIteration() e si interrompe
 * non appena il metodo hasNextPrime() restituisce false.
 * 
 * @author Luca Tesei (template)
 *         Twinkal Sikri, twinkal.sikri@studenti.unicam.it (implementazione)
 *
 */
public class CrivelloDiEratostene {
    /*
     * Array di booleani che rappresenta il crivello. La posizione i dell'arraya
     * è true se e solo se il numero i è primo altrimenti la posizione i deve
     * essere false. Le posizioni 0 e 1 dell'array non sono significative e non
     * vengono usate. L'ultima posizione dell'array deve essere uguale alla
     * capacità del crivello.
     */
    private boolean[] crivello;

    private int IndiceRST;
    //indice usato per partenza iterazione

    /*
     * Capacità del crivello, immutabile
     */
    private final int capacity;

    // TODO definire ulteriori variabili istanza che si ritengono necessarie per
    // implementare tutti i metodi

    /**
     * Costruisce e inizializza il crivello di Eratostene fino alla capacità
     * data. La capacità deve essere almeno 2.
     *
     * @param capacity
     *                     capacità del crivello, almeno 2
     *
     * @throws IllegalArgumentException
     *                                      se il numero {@code capacity} è
     *                                      minore di {@code 2}
     */
    public CrivelloDiEratostene(int capacity) {
        if (capacity < 2)
            throw new IllegalArgumentException("La capienza è minore di 2");
        this.capacity = capacity;
        this.crivello = new boolean[this.capacity + 1];
        for (int i = 2; i <= this.capacity; i++)
            this.crivello [i]= true;
        // scorro gli elementi degli array e li imposto a true
        for (int i = 2; i <= this.capacity; i++)
            //scorro nuovamente l'array ma stavolta partendo da 2
                for (int j = i * i; j <= this.capacity; j += i)
                    this.crivello [j]=false;
                // imposto a false tutti gli elementi, da 1 a n, che non sono primi,
                // sfruttando lo scorrimento degli indici.
        IndiceRST = 2;
    }


    /**
     * Restituisce la capacità di questo crivello, cioè il numero massimo di
     * entrate.
     *
     * @return la capacità di questo crivello
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Controlla se un numero è primo. Può rispondere solo se il numero passato
     * come parametro è minore o uguale alla capacità di questo crivello.
     *
     * @param n
     *              il numero da controllare
     * @return true se il numero passato è primo, false altrimenti
     * @throws IllegalArgumentException
     *                                      se il numero passato {@code n}
     *                                      eccede la capacità di questo
     *                                      crivello o se è un numero minore di
     *                                      2.
     */
    public boolean isPrime(int n) {
        if (n < 2 || n > this.capacity)
            throw new IllegalArgumentException("Il numero in input è maggiore della capienza o è mionore di 2");
        return this.crivello[n];
        //sfrutto il crivello per sapere se il numero in input è primo;
    }

    /**
     * Indica se l'elenco corrente dei numeri primi di questo crivello ha ancora
     * un numero disponibile da elencare o se l'elenco è giunto al termine
     * perché sono già stati elencati tutti i numeri primi minori uguali alla
     * capacità. Se il metodo restituisce true, può essere fatta una ulteriore
     * chiamata al metodo nextPrime() per ottenere il numero successivo
     * nell'elenco. Se il metodo restituisce false non si potrà più chiamare il
     * metodo nextPrime() fino a quando l'elenco non viene fatto ripartire
     * tramite il metodo restartPrimeIteration().
     *
     * @return true se c'è ancora un numero primo nell'elenco dei numeri primi
     *         di questo livello, false se sono già stati elencati tutti i
     *         numeri primi di questo crivello.
     */
    public boolean hasNextPrime() {
        for (int i = IndiceRST; i <= this.capacity; i++)
            if (this.crivello[i] == true)
                // indica se un numero è primo, se è true nell'array crivello
                return true;
        return false;
    }

    /**
     * Restituisce il prossimo numero primo in questo crivello nell'elenco
     * corrente. L'elenco parte sempre dal numero 2 e si interrompe non appena
     * il metodo hasNextPrime() diventa false. Il metodo lancia l'eccezione
     * IllegalStateException se si prova a chiedere il prossimo numero primo
     * quando l'elenco corrente è terminato. L'elenco può essere fatto ripartire
     * in qualsiasi momento chiamando il metodo restartPrimeIteration().
     *
     * @return il prossimo numero primo nell'elenco corrente
     * @throws IllegalStateException
     *                                   se l'elenco è terminato e non è stato
     *                                   ancora fatto ripartire.
     */
    public int nextPrime() {
        if (!hasNextPrime()) {
            throw new IllegalStateException("Non c'è un altro numero successivo, l'elenco è terminato");
        }
        while(!this.isPrime(IndiceRST))
            IndiceRST++;
        return IndiceRST++;
        //incrementando l'indice, nel crviello, se esso è un numero primo viene tornato
    }

    /**
     * Fa ripartire da 2 l'elenco corrente dei numeri primi fino alla capacità
     * di questo crivello. Questo metodo può essere chiamato in qualsiasi
     * momento, anche se l'elenco corrente non è ancora terminato. L'effetto è
     * comunque di ricominciare da 2.
     */
    public void restartPrimeIteration() {
        IndiceRST = 2;
    }

    // TODO inserire eventuali metodi accessori privati per fini di
    // implementazione

}

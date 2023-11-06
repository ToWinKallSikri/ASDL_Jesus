package it.unicam.cs.asdl2223.mp3;

import java.util.ArrayList;

/**
 * 
 * Class that solves an instance of the El Mamun's Caravan problem using
 * dynamic programming.
 * 
 * Template: Daniele Marchei and Luca Tesei
 * Implementation: Twinkal Sikri, twinkal.sikri@studenti.unicam.it
 *
 */
public class ElMamunCaravanSolver {

    // the expression to analyse
    private final Expression expression;

    // table to collect the optimal solution for each sub-problem,
    // protected just for Junit Testing purposes
    protected Integer[][] table;

    // table to record the chosen optimal solution among the optimal solution of
    // the sub-problems, protected just for JUnit Testing purposes
    protected Integer[][] tracebackTable;

    // flag indicating that the problem has been solved at least once
    private boolean solved;

    /**
     * Create a solver for a specific expression.
     * 
     * @param expression
     *                       The expression to work on
     * @throws NullPointerException
     *                                  if the expression is null
     */
    public ElMamunCaravanSolver(Expression expression) {
        // se l'espressione che viene passata è null, lancio una NullPointerException
        if (expression == null)
            throw new NullPointerException("Creazione di solver con expression null");
        // costruttore dei vari parametri utilizzati nell'implementazione
        this.expression = expression;

        int size = this.expression.size();

        this.table = new Integer[size][size];

        this.tracebackTable = new Integer[size][size];

        this.solved = false;
    }

    /**
     * Returns the expression that this solver analyse.
     * 
     * @return the expression of this solver
     */
    public Expression getExpression() {
        return this.expression;
    }

    /**
     * Solve the problem on the expression of this solver by using a given
     * objective function.
     * 
     * @param function
     *                     The objective function to be used when deciding which
     *                     candidate to choose
     * @throws NullPointerException
     *                                  if the objective function is null
     */
    public void solve(ObjectiveFunction function) {

        // se la funzione che viene passata è null, lancio una NullPointerException
        if ( function == null) {
            throw new NullPointerException("La funzione è nulla");
        }
        // creo una lista di supporto che verrà usata poi, per la memorizzazione dei valori calcolati
        ArrayList<Integer> subSeqValues;

        // variabile d'appoggio creata per avere la size dell'espressione e lavorarci sopra
        int size = this.expression.size();

        // riempio tutte le posizioni [i][i] della tabella con i digit che troviamo e
        // nelle loro corrispettive posizioni. Stiamo quindi riempiendo la diagonale con m[i][i].
        for (int i = 0; i < size; i += 2) {
            table[i][i] = (Integer) expression.get(i).getValue();
        }

        // for per determinare la lunghezza delle sotto sequenze da calcolare
        for (int expLen = 0; expLen < size - 2; expLen += 2) {
            // for per scorrere l'indice "i" nelle varie espressioni
            for (int i  = 0; i < size - expLen -2; i += 2) {
                // assegnamento dell'indice "j" nell'espressione
                int j = i + expLen + 2;
                // inizializzo la lista di supporto
                subSeqValues = new ArrayList<Integer>();
                // for per far scorrere l'indice k nell'espressione data
                for (int k = 0; i + k + 2 <= j; k += 2) {

                    // variabile d'appoggio creata per indicare la posizione nella matrice
                    // del primo valore da moltiplicare/addizionare
                    Integer x = this.table [i][i + k];
                    // variabile d'appoggio creata per indicare la posizione nella matrice
                    // del secondo valore da moltiplicare/addizionare
                    Integer y = this.table [i+ k + 2][j];

                    // condizioni per fare l'addizione o la moltiplicazione fra le due variabili create,
                    // dipende dall'operatore che mi trovo davanti quando scorro nell'espressione
                    if (this.expression.get(i + k + 1).getValue().equals("+")) {
                        subSeqValues.add(x + y);

                    } else if (this.expression.get(i + k + 1).getValue().equals("*")) {
                        subSeqValues.add(x * y);
                    }
                }

                // sfrutto le dinamiche di "function" e nella posizione [i][j] della table,
                // inserisco il valore migliore che ho, fra quelli calcolati in tutte le sotto sequenze
                table[i][j] = function.getBest(subSeqValues);

                // sfrutto le dinamiche di "function" e nella posizione [i][j] della tracebackTable,
                // inserisco il valore migliore che ho (in questo caso si tratta del K, ovvero la posizione della parentesi),
                // fra quelli calcolati in tutte le sotto sequenze
                tracebackTable [i][j] = function.getBestIndex(subSeqValues) * 2;
            }
        }
        // quando l'espressione è stata risolta, imposto il flag a true
        this.solved = true;
    }


    /**
     * Returns the current optimal value for the expression of this solver. The
     * value corresponds to the one obtained after the last solving (which used
     * a particular objective function).
     * 
     * @return the current optimal value
     * @throws IllegalStateException
     *                                   if the problem has never been solved
     */
    public int getOptimalSolution() {
        // se il problema non è stato ancora mai risolto, lancio una IllegalStateException
        if (!isSolved()) {
            throw new IllegalStateException("Il problema non è mai stato ancora risolto");
        }
        // la soluzione migliore (min or max), si trova sempre all'apice della nostra tabella,
        // in questo caso, la si trova, appunto, nella posizione [0][expression.size() - 1]
        return this.table[0][expression.size() - 1];
    }

    /**
     * Returns an optimal parenthesization corresponding to an optimal solution
     * of the expression of this solver. The parenthesization corresponds to the
     * optimal value obtained after the last solving (which used a particular
     * objective function).
     * 
     * If the expression is just a digit then the parenthesization is the
     * expression itself. If the expression is not just a digit then the
     * parethesization is of the form "(<parenthesization>)". Examples: "1",
     * "(1+2)", "(1*(2+(3*4)))"
     * 
     * @return the current optimal parenthesization for the expression of this
     *         solver
     * @throws IllegalStateException
     *                                   if the problem has never been solved
     */
    public String getOptimalParenthesization() {
        // se il problema non è stato ancora mai risolto, lancio una IllegalStateException
        if (!isSolved()) {
            throw new IllegalStateException("Il problema non è ancora mai stato ancora risolto");
        }
        // richiamo il metodo traceBack creato appositamente per effettuare ricorsivamente la parentesizzazione,
        // dandogli come indici su cui lavorare la "i" impostata a 0, ovvero l'inizio, e "j"
        // che corrisponde alla fine della sotto sequenza/espressione.
        return traceBack(0, this.expression.size() - 1);
    }

    /**
     * Determines if the problem has been solved at least once.
     * 
     * @return true if the problem has been solved at least once, false
     *         otherwise.
     */
    public boolean isSolved() {
        return this.solved;
    }

    @Override
    public String toString() {
        return "ElMamunCaravanSolver for " + expression;
    }

    /*
     * metodo creato per fare il traceBack, lavorando sulla tracebackTable, ricorsivamente
     */
    private String traceBack (int i, int j) {
        // se l'indice "i" e l'indice "j", coincidono, ritorno il singolo numero
        if ( i == j) {
            return expression.get(i).toString();
        }
        // trovo l'operatore in posizione i + this.tracebackTable[i][j] + 1 e lo salvo in una variabile d'appoggio
        String Operator = this.expression.get(i + this.tracebackTable[i][j] + 1).toString();

        // ricorsione del traceBack, che di volta in volta, crea una parentesizzazione in base alla sotto sequenza in cui mi trovo
        return "(" + traceBack(i, i + this.tracebackTable[i][j]) + Operator + traceBack(i + this.tracebackTable[i][j] + 2, j) + ")";
    }
}

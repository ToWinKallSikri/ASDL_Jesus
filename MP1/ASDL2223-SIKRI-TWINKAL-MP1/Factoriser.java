package it.unicam.cs.asdl2223.mp1;

/**
 * Un fattorizzatore è un agente che fattorizza un qualsiasi numero naturale nei
 * sui fattori primi.
 * 
 * @author Luca Tesei (template)
 *         Twinkal Sikri, twinkal.sikri@studenti.unicam.it DELLO STUDENTE (implementazione)
 *
 */
public class Factoriser {

    private int dividendo;
    // variabile utilizzata per definire il numero da dividere
    private int remaningLeap;
    // variabile utilizzata per definire a ogni ciclo il nuovo "dividendo"
    private int divisore;
    // variabile utilizzata per dividere, incrementata a ogni ciclo

    // TODO definire ulteriori variabili istanza che si ritengono necessarie per
    // implementare tutti i metodi

    /**
     * Fattorizza un numero restituendo la sequenza crescente dei suoi fattori
     * primi. La molteplicità di ogni fattore primo esprime quante volte il
     * fattore stesso divide il numero fattorizzato. Per convenzione non viene
     * mai restituito il fattore 1. Il minimo numero fattorizzabile è 1. In
     * questo caso viene restituito un array vuoto.
     * 
     * @param n
     *              un numero intero da fattorizzare
     * @return un array contenente i fattori primi di n
     * @throws IllegalArgumentException
     *                                      se si chiede di fattorizzare un
     *                                      numero minore di 1.
     */
    public Factor[] getFactors(int n) {
        if (n < 1)
            throw new IllegalArgumentException("Il numero da fattorizzare non può essere minore di 1");
        dividendo = n;
        divisore = 2;
        remaningLeap = dividendo;
        Factor[] factors = new Factor[(int) Math.sqrt(n)];
        while (remaningLeap > 1) {
            // continua la fattorizzazione fin quando serve
            while (remaningLeap % divisore == 0) {
            // ciclo di divisione, fin quando il resto è ancora maggiore di 1
                if (remaningLeap == dividendo) {
                //caso in cui il rimanente da dividere è uguale al dividendo
                    for (int i = 0; i < factors.length; i++)
                        factors[i] = new Factor(remaningLeap, 1);
                        //ho trovato il fattore uguale al dividendo che devo inserire nell'array
                        break;
                        //break per uscire dal ciclo e tornare l'array
                } else {
                    for (int i = 0; i < factors.length; i++)
                        factors[i] = new Factor(divisore, Occorrenze(divisore,factors));
                    //aggiungo i nuovi fattori all'array
                    remaningLeap = remaningLeap / divisore;
                    //creo il nuovo "dividendo"
                }
            }
        }
        divisore++; //incremento divisore fino a raggiungimento delle condizioni
        return factors;
    }
     /*
      * Metodo ideale per avere la molteplicità di un numero in base alle sue occorrenze nell'Array factors
      */
    public int Occorrenze (int divisore, Factor [] factors) {
        int counter = 0;
        for (int i = 0; i< factors.length; i++) {
            if  ( factors[i].equals(divisore)) {
                counter++;
            }
        }
        return counter;
    }

    // TODO inserire eventuali metodi accessori privati per fini di
    // implementazione

}


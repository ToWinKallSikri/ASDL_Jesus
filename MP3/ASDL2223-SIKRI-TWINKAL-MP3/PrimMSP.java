package it.unicam.cs.asdl2223.mp3;

//TODO completare gli import necessari

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

import java.util.ArrayList;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author Luca Tesei (template)
 *         Twinkal Sikri,twinkal.sikri@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMSP<L> {

    /*
     * Lista con lo scopo di essere una queue per l'algoritmo.
     */

    private final ArrayList<GraphNode<L>> coda;


    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMSP() {
        // inizializzo la lista che avrà la funzione di coda nell'algoritmo
        ArrayList<GraphNode<L>> Queue = new ArrayList<GraphNode<L>>();
        this.coda = Queue;
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {

        // se il grafo passato, o il nodo sorgente passato, sono null lancio una NullPointerException
        if (g == null || s == null) {
            throw new NullPointerException("Il grafo passato o il nodo sono null");
        }

        // se il grafo non contiene il nodo sorgente passato, o se il grafo passato è orientato,
        // lancio una IllegalArgumentException
        if (g.getNode(s) == null || g.isDirected()) {
            throw new IllegalArgumentException("Il grafo non contiene il nodo passato oppure il grafo passato è diretto");
        }

        // for per scorrere tutti gli archi del grafo e controllarli
        for (GraphEdge<L> edgeWeightDump : g.getEdges()) {
            // controllo che il tutti gli archi del grafo siano pesati e che non abbiano peso negativo
            if (!edgeWeightDump.hasWeight()|| edgeWeightDump.getWeight() < 0) {
                // se le condizioni non sono rispettate lancio una IllegalArgumentException
                throw new IllegalArgumentException();
            }
        }

        // imposto il previous di tutti i nodi a null, il loro colore a bianco
        // e il floating (la sua distanza) a infinito
        for (GraphNode<L> nodeSetter : g.getNodes()) {

            nodeSetter.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            nodeSetter.setPrevious(null);
            nodeSetter.setColor(0);

        }

        // il nodo sorgente ha ovviamente distanza 0
        s.setFloatingPointDistance(0);

        // tutti i nodi del grafo vengono aggiunti alla coda
        coda.addAll(g.getNodes());

        // creo un nodo null che sarà quello in cui verrà salvato di volta in volta,
        // l'elemento minore da eliminare dalla coda
        GraphNode<L> savedNode = null;

        // ripeto finché la coda non è vuota
        while(!coda.isEmpty()) {

            savedNode = coda.get(0);
            //scorro tutti i nodi messi nella coda e prendo quello con la distanza minore
            for (GraphNode<L> n : coda) {
                // se la distanza del nodo che scorro è maggiore di quella del nodo successivo,
                // assegno quest'ultimo al nodo di supporto e ripeto finché la coda non è vuota
                if(savedNode.getFloatingPointDistance() > n.getFloatingPointDistance()) {
                    savedNode = n;
                }
            }
            // ogni volta coloro di nero il nodo esplorato e lo rimuovo dalla coda
            savedNode.setColor(2);
            coda.remove(savedNode);

            // scorro tutti i nodi adiacenti al nodo eliminato dalla coda
            for(GraphNode<L> adjDumpNode : g.getAdjacentNodesOf(savedNode)) {

                // se la coda contiene il nodo, quindi se questo nodo non è di colore nero, e se
                // il peso dell'arco tra il nodo, e il nodo minore, è minore della distanza del nodo,
                // modifico il precedente del nodo e la sua distanza
                if(adjDumpNode.getColor() == 0 && g.getEdge(adjDumpNode,savedNode).getWeight() < adjDumpNode.getFloatingPointDistance()) {

                    adjDumpNode.setFloatingPointDistance(g.getEdge(adjDumpNode,savedNode).getWeight());

                    adjDumpNode.setPrevious(savedNode);
                }

            }

        }

    }

}

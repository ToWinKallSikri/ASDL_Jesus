package it.unicam.cs.asdl2223.mp2;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * Class that provides an implementation of a "dynamic" min-priority queue based
 * on a ternary heap. "Dynamic" means that the priority of an element already
 * present in the queue may be decreased, so possibly this element may become
 * the new minumum element. The elements that can be inserted may be of any
 * class implementing the interface <code>PriorityQueueElement</code>. This
 * min-priority queue does not have capacity restrictions, i.e., it is always
 * possible to insert new elements and the number of elements is unbound.
 * Duplicated elements are permitted while <code>null</code> elements are not
 * permitted.
 * 
 * @author Template: Luca Tesei
 *         Implementation: Twinkal Sikri, twinkal.sikri@studenti.unicam.it
 *
 *
 */
public class TernaryHeapMinPriorityQueue {

    /*
     * ArrayList for representing the ternary heap. Use all positions, including
     * position 0 (the JUnit tests will assume so). You have to adapt
     * child/parent indexing formulas consequently.
     */
    private ArrayList<PriorityQueueElement> heap;

    // TODO implement: possibly insert other private fields that may be needed
    // for implementation

    /**
     * Create an empty queue.
     */
    public TernaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Return the current size of this queue.
     * 
     * @return the number of elements currently in this queue.
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Add an element to this min-priority queue. The current priority
     * associated with the element will be used to place it in the correct
     * position in the ternary heap. The handle of the element will also be set
     * accordingly.
     * 
     * @param element
     *                    the new element to add
     * @throws NullPointerException
     *                                  if the element passed is null
     */
    public void insert(PriorityQueueElement element) {
        // se l'elemento è nullo lancio una NullPointerException
        if (element == null) {
            throw new NullPointerException("Elemento inserito nullo.");
        }
        // inserisco l'indice dell'elemento
        element.setHandle(size());
        // aggiungo l'elemento all'heap
        heap.add(element);
        //index dell'ultimo nodo
        int last = heap.size() - 1;
        //Sistemo min-heap con il nuovo elemento
        while (last > 0) {
            // creo un nuovo indice di appoggio per salvare l'ultimo elemento dell'heap
            int lastParent = parent(last);
            // prendo l' elemento nuovo e la sua priority, poi verifico se questa sia minore
            // di quella dell'ultimo nodo dell'heap
            if (element.getPriority() < heap.get(lastParent).getPriority()) {
                // se la condizione sopra si verifica devo scambiare i due elementi
                swapElement(last, lastParent);
                // eseguo ricorsivamente l'operazione fin quando last non arriva a 0, ovvero il primo indice
                last = lastParent;
            } else break;
        }

    	
    }

    /**
     * Returns the current minimum element of this min-priority queue without
     * extracting it. This operation does not affect the ternary heap.
     * 
     * @return the current minimum element of this min-priority queue
     * 
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement minimum() {
        // se l'heap è vuoto lancio l'eccezione NoSuchElementException
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Lista min-priority vuota");
        }
    // ritorno l'elemento in posizione 0 (ovvero l'elemento minimo dell'albero)
    return this.heap.get(0);
    }

    /**
     * Extract the current minimum element from this min-priority queue. The
     * ternary heap will be updated accordingly.
     * 
     * @return the current minimum element
     * @throws NoSuchElementException
     *                                    if this min-priority queue is empty
     */
    public PriorityQueueElement extractMinimum() {
        // se l'heap è vuoto lancia l'eccezione NoSuchElementexception
        if (heap.isEmpty()) {
            throw new NoSuchElementException("Lista min-priority vuota");
        }
        // creo una variabile d'appoggio a cui assegno il minimo dell'heap (primo elemento dell'heap)
        PriorityQueueElement minToBeExtracted = minimum();
        // scambio il minimo con la fine della lista (ultima foglia del heap)
        swapElement(0, heap.size() - 1);
        // rimuovo l'ultima foglia del heap
        heap.remove(heap.size() - 1);
        // faccio l'heapify, visto che ho rimosso (estratto) un elemento
        heapify(0);
        // setto l'handle dell'elemento minimo a 0
        minToBeExtracted.setHandle(0);
        // ritorno il minimo estratto
        return minToBeExtracted;
    }

    /**
     * Decrease the priority associated to an element of this min-priority
     * queue. The position of the element in the ternary heap must be changed
     * accordingly. The changed element may become the minimum element. The
     * handle of the element will also be changed accordingly.
     * 
     * @param element
     *                        the element whose priority will be decreased, it
     *                        must currently be inside this min-priority queue
     * @param newPriority
     *                        the new priority to assign to the element
     * 
     * @throws NoSuchElementException
     *                                      if the element is not currently
     *                                      present in this min-priority queue
     * @throws IllegalArgumentException
     *                                      if the specified newPriority is not
     *                                      strictly less than the current
     *                                      priority of the element
     */
    public void decreasePriority(PriorityQueueElement element,
            double newPriority) {
        // la priorità dell'elemento in questione è minore della nuova priorità minima
        // lancia un IllegalArgumentException
        if (element.getPriority() < newPriority ){
            throw new IllegalArgumentException();
        }
        // inizializzo un flag che mi aiuta a cercare l'elemento nell'heap
        boolean flag = false;
        // indice usato nella ricerca dell'elemento nell'heap
        int auxInd = 0;

        //cerco l'elemento nel heap e se c'è acquisisco l'indice
        while (auxInd < heap.size()) {
            if (this.heap.get(auxInd).getPriority() == element.getPriority()) {
                flag = true;
                break;
            }
            // fin quando non trovo l'elemento, continuo a ciclare (il limite è la size dell'heap)
            auxInd++;
        }
        // se l'elemento non è contenuto nell'heap lancia un NoSuchElementException
        if (!flag) {
            throw new NoSuchElementException("L'elemento non è presente nell'heap");
        }
        // verifico la priorità
        if (!(newPriority < heap.get(auxInd).getPriority())) {
            throw new IllegalArgumentException("Nuova priorità non abbastanza bassa");
        }
        // imposto la nuova priorità
        this.heap.get(auxInd).setPriority(newPriority);
        //index del nodo dell'elemento
        int last = parent(auxInd);
        //Ricostruisco min-heap con il nuovo elemento
        for (int i = last; i >= 0; i--) {
            heapify(i);
        }
    }

    /**
     * Erase all the elements from this min-priority queue. After this operation
     * this min-priority queue is empty.
     */
    public void clear() {
        this.heap.clear();
    }

    /*
     * metodo per fare l'heapify nell'heap
     */
    private void heapify (int y) {
        // mi da' l'indice sinistro
        int left = this.sx(y);
        // mi da' l'indice centrale
        int center = this.mid(y);
        // mi da' l'indice destro
        int right = this.dx(y);
        // verifico se ho a che fare con una foglia
        if(leafTest(y))
            return;
        //caso in cui il nodo y ha solo una foglia
        if (left == size() - 1) {
            // prendo l' elemento a sinistra e la sua priority, poi verifico se questa sia minore
            // di quella del nodo che ho preso in considerazione
            if (heap.get(left).getPriority() < heap.get(y).getPriority()) {
                // se il nodo in questione è maggiore di quello a sinistra faccio lo scambio
                swapElement(y, left);
        }
        //caso in cui il nodo y ha solo due foglie
        } else if (left == size() - 2) {
                if (heap.get(left).getPriority() < heap.get(y).getPriority()
                        || heap.get(center).getPriority() < heap.get(y).getPriority()) {

                    if (heap.get(left).getPriority() < heap.get(center).getPriority()) {
                        swapElement(y, left);
                    } else {
                        swapElement(y, center);
                    }
                }
            //caso in cui il nodo y è completo (tre foglie)
        } else if  (heap.get(left).getPriority() < heap.get(y).getPriority()
                    || heap.get(center).getPriority() < heap.get(y).getPriority()
                    || heap.get(right).getPriority() < heap.get(y).getPriority()) {
                //Cerco indice della foglia minore tra tutte
                int min = left;
                if (heap.get(center).getPriority() < heap.get(min).getPriority()) {
                    min = center;
                }
                if (heap.get(right).getPriority() < heap.get(min).getPriority()) {
                    min = right;
                }
        swapElement(y, min);
        heapify(min);
        }

    }

    /*
     * metodo per trovare l'indice sinistro dell'heap
     */
    private int sx (int i) {
        return (3*i)+1;
    }

    /*
     * metodo per trovare l'indice centrale dell'heap
     */
    private int mid (int i) {
        return (3*i)+2;
    }

    /*
     * metodo per trovare l'indice destro dell'heap
     */
    private int dx (int i) {
        return (3*i)+3;
    }

    /*
     * metodo per trovare il parent
     */
    private int parent (int i) {
        // se l'indice è 0, ritorno 0
        if (i == 0) {
            return 0;
        } else {
            // calcolo indice padre
            return (i - 1) / 3;
        }
    }

    /*
     * metodo per verificare se l'indice in questione è una leaf (elemento senza figli e l'ultimo dell'albero)
     */
    private boolean leafTest (int i) {
        // ritorna true se l'indice della foglia
        // in questione è >= al size dell'heap evinco che si tratta di una foglia
        if (sx(i) >= this.size())
            return true;
        return false;
    }

    /*
     * metodo per scambiare due nodi
     */
    private void swapElement(int FirstP, int SecondP) {
        // element e handle ausiliari
        PriorityQueueElement AuxElem1, AuxHandle2;
        //copio il primo elemento con Handle del secondo elemento
        AuxElem1 = this.heap.get(FirstP);
        AuxElem1.setHandle(SecondP);
        //copio il secondo elemento con Handle del primo elemento
        AuxHandle2 = this.heap.get(SecondP);
        AuxHandle2.setHandle(FirstP);
        //eseguo lo scambio fra i due nodi
        this.heap.set(FirstP, AuxHandle2);
        this.heap.set(SecondP, AuxElem1);
    }


    /*
     * This method is only for JUnit testing purposes.
     */
    protected ArrayList<PriorityQueueElement> getTernaryHeap() {
        return this.heap;
    }

}

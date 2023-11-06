/**
 *
 */
package it.unicam.cs.asdl2223.mp2;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the Java SE Double-ended Queue (Deque) interface
 * (<code>java.util.Deque</code>) based on a double linked list. This deque does
 * not have capacity restrictions, i.e., it is always possible to insert new
 * elements and the number of elements is unbound. Duplicated elements are
 * permitted while <code>null</code> elements are not permitted. Being
 * <code>Deque</code> a sub-interface of
 * <code>Queue<code>, this class can be used also as an implementaion of a <code>Queue</code>
 * and of a <code>Stack</code>.
 * <p>
 * The following operations are not supported:
 * <ul>
 * <li><code>public <T> T[] toArray(T[] a)</code></li>
 * <li><code>public boolean removeAll(Collection<?> c)</code></li>
 * <li><code>public boolean retainAll(Collection<?> c)</code></li>
 * <li><code>public boolean removeFirstOccurrence(Object o)</code></li>
 * <li><code>public boolean removeLastOccurrence(Object o)</code></li>
 * </ul>
 *
 * @author Template: Luca Tesei
 *         Implementation: Twinkal Sikri, twinkal.sikri@studenti.unicam.it
 */
public class ASDL2223Deque<E> implements Deque<E> {

    /*
     * Current number of elements in this deque
     */
    private int size;

    /*
     * Pointer to the first element of the double-linked list used to implement this
     * deque
     */
    private Node<E> first;

    /*
     * Pointer to the last element of the double-linked list used to implement this
     * deque
     */
    private Node<E> last;

    /*
     * Counter del numero delle modifiche
     */
    private int currentMods;

    /**
     * Constructs an empty deque.
     */
    public ASDL2223Deque() {
        // costruttore della classe
        this.size = 0;
        this.first = null;
        this.last = null;
        this.currentMods = 0;
    }

    @Override
    public boolean isEmpty() {
        // se size == 0, ovvero se la lista è vuota, ritorna true;
        return this.first == null;
    }

    @Override
    public Object[] toArray() {
        // creo un nuovo array di oggetti
        Object[] array = new Object[this.size];
        // creo una variabile i per far scorrere gli indici dell'array appena creato
        int i = 0;
        // uso un for-each per iterare tutti i nodi ed inserirli nell'array
        for (E node : this) {
            // ad ogni indice viene associato un nodo
            array[i] = node;
            // incremento l'indice ad ogni iterazione
            i++;
        }
        // ritorno l'array appena creato
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // se la collezione è null lancio una NullPointerException
        if (c == null) {
            throw new NullPointerException("Collection con valori nulli o vuota.");
        }
        // creo una variabile counter da confrontare con la size della collezione
        int counter = 0;
        // uso un for-each per iterare tutti i nodi della collezione di cui voglio verificare la presenza
        for (Object node : c) {
            // se la deque non contiene uno dei nodi ritorno false
            if (!this.contains(node)) {
                return false;
            // goni volta che contiene uno dei nodi della collezione aumento il counter
            } else {
                counter++;
            }
        }
        // se il counter corrisponde con la size della collezione ritorno true
        return counter == c.size();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // se la collezione è null lancio una NullPointerException
        if (c == null) {
            throw new NullPointerException("Collection con valori nulli o vuota.");
        }
        // uso un for-each per iterare tutti i nodi della collezione che voglio aggiungere alla deque
        for (E node : c)
            // richiamo il metodo addLast, in modo tale da aggiungere ogni nodo alla fien, uno dopo l'altro
            addLast(node);
        // alla fine dell'operazione, se l'aggiunta è stata eseguita correttamente ritorno true
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public void clear() {
        // imposto il first e il last a null e sfrutto la caratteristica del garbage collector di Java, per reimpostare
        // a null la lista
        this.last = null;
        this.first = null;
        // imposto la size a 0
        size = 0;
        // aumento il counter delle modifiche
        currentMods++;
    }

    @Override
    public void addFirst(E e) {
        // se l'elemento e è null lancio una NullPointerException
        if (e == null) {
            throw new NullPointerException("Elemento nullo.");
        }
        // creo un nuovo nodo da aggiungere all'inizio della lista
        Node newNode = new Node(null, e, this.first);
        // istruzioni per quando la lista è vuota
        if (this.isEmpty()) {
            // imposto il nuovo nodo come first
            this.first = newNode;
            // imposto il nuovo nodo come last
            this.last = newNode;
            // incremento il counter della size della lista
            size++;
            // incremento il counter delle modifiche nella lista
            currentMods++;
            // istruzioni se la lista non è vuota
        } else {
            // imposto il puntatore prev del first al nuovo nodo
            this.first.prev = newNode;
            // faccio divenatre first il nuovo nodo
            this.first = newNode;
            // incremento il counter della size della lista
            size++;
            // incremento il counter delle modifiche nella lista
            currentMods++;
        }
    }

    @Override
    public void addLast(E e) {
        // se l'elemento e è null lancio una NullPointerException
        if (e == null) {
            throw new NullPointerException("Elemento nullo.");
        }
        // metodo equivalente all'add
        add(e);
    }

    @Override
    public boolean offerFirst(E e) {
        // metodo equivalente ad addFirst
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        // metodo equivalente all'add
        return add(e);
    }

    @Override
    public E removeFirst() {
        // se la mia lista è vuota lancio un'eccezione NoSuchElementException
        if (this.isEmpty())
            throw new NoSuchElementException("La lista è vuota");
        // creo un nuovo nodo d'appoggio per ritornare l'item del nodo della lista che vado a togliere (il primo)
        Node newFirstNode = this.first;
        // istruzioni se la lisa contiene un solo elemento
        if (this.size == 1) {
            // imposto il first a null
            this.first = null;
            // imposto il last a null
            this.last = null;
            // decremento il size counter della lista
            size--;
            // incremento il counter del numero delle modifiche
            currentMods++;
            // ritorno l'elemento del nodo che ho rimosso
            return (E) newFirstNode.item;
        } else {
            // faccio diventare "first" l'elemento dopo
            this.first = this.first.next;
            // imposto a null il puntatore del ex-FirstElement
            this.first.prev.next = null;
            // imposto a null il puntatore prev del nuovo FirstElement
            this.first.prev = null;
            // decremento il size counter della lista
            size--;
            // incremento il counter del numero delle modifiche
            currentMods++;
        }
        // ritorno l'elemento del nuovo first
        return (E) newFirstNode.item;
    }

    @Override
    public E removeLast() {
        // se la mia lista è vuota lancio un'eccezione NoSuchElementException
        if (this.isEmpty()) {
            throw new NoSuchElementException("Deque vuota");
        }
        // creo un nuovo nodo d'appoggio per ritornare l'item del nodo della lista che vado a togliere (l'ultimo)
        Node newLastNode = this.last;
        // istruzioni se la lisa contiene un solo elemento
        if (this.size == 1) {
            // imposto il first a null
            this.first = null;
            // imposto il last a null
            this.last = null;
            // decremento il size counter della lista
            size--;
            // incremento il counter del numero delle modifiche
            currentMods++;
            // ritorno l'elemento del nodo che ho rimosso
            return (E) newLastNode.item;
        } else {
            // faccio diventare "last" l'elemento prima
            this.last = this.last.prev;
            // imposto a null il puntatore prev del ex-LastNode
            this.last.next.prev = null;
            // imposto a null il puntatore next del nuovo LastNode
            this.last.next = null;
            // decremento il size counter della lista
            size--;
            // incremento il counter del numero delle modifiche
            currentMods++;
        }
        // ritorno l'elemento del nuovo last
        return (E) newLastNode.item;
    }

    @Override
    public E pollFirst() {
        // se il primo nodo della lista è nullo, ritorno null
        if (this.first == null) {
            return null;
        }
        // equivalente al metodo removeFirst
        return this.removeFirst();
    }

    @Override
    public E pollLast() {
        // se l'ultimo nodo della lista è nullo, ritorno null
        if (this.last == null) {
            return null;
        }
        // equivalente al metodo removeLast
        return removeLast();
    }

    @Override
    public E getFirst() {
        // se la mia lista è vuota lancio un'eccezione NoSuchElementException
        if (this.isEmpty()) {
            throw new NoSuchElementException("Deque vuota");
        }
        // ritorno l'item del primo nodo della lista
        return this.first.item;
    }

    @Override
    public E getLast() {
        // se la mia lista è vuota lancio un'eccezione NoSuchElementException
        if (this.isEmpty()) {
            throw new NoSuchElementException("Deque vuota");
        }
        // ritorno l'item dell'ultimo nodo della lista
        return this.last.item;
    }

    @Override
    public E peekFirst() {
        // se la mia lista è vuota ritorno null
        if (this.isEmpty())
            return null;
        // ritorno l'item del primo nodo della lista
        return this.first.item;
    }

    @Override
    public E peekLast() {
        // se la mia lista è vuota ritorno null
        if (this.isEmpty())
            return null;
        // ritorno l'item dell'ultimo nodo della lista
        return this.last.item;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException("This class does not implement this service.");
    }

    @Override
    public boolean add(E e) {
        // se l'elemento e è null lancio una NullPointerException
        if (e == null)
            throw new NullPointerException("L'elemento è nullo");
        // creo il nuovo nodo che ha il pointer prev che punta verso l'ultimo elemento in lista
        Node newNode = new Node(this.last, (E) e, null);
        // se la lista è vuota imposto il nodo in testa alla lista e lo faccio diventare il first
        if (this.isEmpty()) {
            // imposto il nuovo nodo come first
            this.first = newNode;
            // imposto il nuovo nodo come last
            this.last = newNode;
            // aumento il counter della size della lista
            size++;
            // aumento il counter delle modifiche
            currentMods++;
            // istruzioni se la lista non è vuota
        } else {
            // il nuovo nodo è puntato dall'ultimo elemento della lista
            this.last.next = newNode;
            // il nuovo nodo, diventa il nuovo last della lista
            this.last = newNode;
            // aumento il counter della size della lista
            size++;
            // aumento il counter delle modifiche
            currentMods++;
        }
        // se l'add è stato fatto correttamente ritorna true
        return true;
    }

    @Override
    public boolean offer(E e) {
        // equivalente al metodo add
        return add(e);
    }

    @Override
    public E remove() {
        // se la mia lista è vuota lancio l'eccezione NoSuchElementException
        if (this.isEmpty()) {
            throw new NoSuchElementException("Deque vuota");
        }
        // creo un nuovo nodo e lo imposto a first
        Node FirstNode = this.first;
        // istruzioni se la lisa contiene un solo elemento
        if (this.size == 1) {
            // imposto il first a null
            this.first = null;
            // imposto il last a null
            this.last = null;
            // decremento il size counter della lista
            size--;
            // incremento il counter del numero delle modifiche
            currentMods++;
            // ritorno l'elemento del nodo che ho rimosso
            return (E) FirstNode.item;
        } else {
            // faccio diventare "first" l'elemento dopo
            this.first = this.first.next;
            // imposto a null il puntatore del ex-FirstElement
            this.first.prev.next = null;
            // imposto a null il puntatore prev del nuovo FirstElement
            this.first.prev = null;
            size--;
            currentMods++;
            // ritorno l'elemento del nuovo first
        }
        return (E) FirstNode.item;
    }

    @Override
    public E poll() {
        // se la mia lista è vuota ritorno null
        if (this.isEmpty())
            return null;
        // metodo equivalente a removeFirst
        return this.removeFirst();
    }

    @Override
    public E element() {
        // ritorno l'item del primo elemento della lista
        return this.first.item;
    }

    @Override
    public E peek() {
        // se la mia lista è vuota ritorno null
        if (this.isEmpty())
            return null;
        // ritorno l'item del primo elemento della lista
        return this.element();
    }

    @Override
    public void push(E e) {
        // metodo equivalente all'addFirst
        addFirst(e);
    }

    @Override
    public E pop() {
        // metodo equivalente al removeFirst
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        // se l'oggetto è null lancio una nuova NullPointerException
        if (o == null) {
            throw new NullPointerException("L'object è nullo");
        }
        // se la lista non contiene l'oggetto in questione, ritorno false
        if (!this.contains(o)) {
            return false;
        }
        // creo un nuovo nodo d'appoggio, lo imposto a first
        Node firstNode = this.first;
        // creo un nuovo nodo che contiene l'oggetto da rimuovere
        Node remNode = new Node(null, o, null);
        // l'item del nodo d'appoggio corrisponde all'oggetto da rimuovere eseguo queste istruzioni
        if (remNode.item.equals(firstNode.item)) {
            // uso il metodo remove per togliere il nodo in questione (il primo della lista)
            remove();
            // ritorno true se l'operazione è andata a buon fine
            return true;
        }
        // creo un for che mi consente di ciclare tutti i nodi della lista
        for (int i = 0; i < this.size; i++) {
            // se il next del nodo d'appoggio è null, so che si tratta dell'ultimo nodo della lista
            if (firstNode.next == null) {
                // uso il metodo removeLast per rimuovere l'ultimo nodo della lista
                removeLast();
                // ritorno true se l'operazione è andata a buon fine
                return true;
            }
            // operazioni da fare mentre si sta iterando
            // se l'item del nodo d'appoggio equivale all'oggetto che voglio togliere eseguo queste operazioni
            if (firstNode.item.equals(remNode.item)) {
                // imposto il next del nodo precedente al nodo d'appoggio come next del nodo in question
                firstNode.prev.next = firstNode.next;
                // imposto il prev del nodo dopo al nodo d'appoggio come prev del nodo in questione
                firstNode.next.prev = firstNode.prev;
                // imposto il prev del nodo d'appoggio a null
                firstNode.prev = null;
                // imposto il next del nodo d'appoggio a null
                firstNode.next = null;
                // decremento il counter della size della lista
                size--;
                // incremento il counter delle modifiche
                currentMods++;
                // se l'operazione è andata a buon fine, e il nodo corretto è stato rimosso, ritorno true;
                return true;
            }
            // per ciclare nella lista fin quando non trovo il nodo giusto
            firstNode = firstNode.next;
        }
        // ritorno false se non ho trovato nessun nodo
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // se l'oggetto è null lancio una NullPointerException
        if (o == null) {
            throw new NullPointerException("L'object è nullo");
        }
        // creo un nuovo nodo d'appoggio per la ricerca
        Node searchNode = new Node(null, o, null);
        // creo iterator per andare avanti nella lista
        Itr itSearch = new Itr();
        // uso un ciclo for, per ciclare fin quando non trovo il nodo corretto
        for (int i = 0; i < size; i++) {
            // fin quando l'iteratore ha un next continuo la ricerca
            if (itSearch.hasNext()) {
                // se l'iterator trova un nodo di cui l'item corrisponde
                // a quello che sto cercando ritorno true
                if (itSearch.next().equals(searchNode.item))
                    return true;
            }
        }
        // ritorno false se l'oggetto non è contenuto dalla lista
        return false;
    }

    @Override
    public int size() {
        // ritorna la size della lista
        return this.size;
    }

    /*
     * Class for representing the nodes of the double-linked list used to implement
     * this deque. The class and its members/methods are protected instead of
     * private only for JUnit testing purposes.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        protected Node<E> prev;

        protected Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    /*
     * Class for implementing an iterator for this deque. The iterator is fail-fast:
     * it detects if during the iteration a modification to the original deque was
     * done and, if so, it launches a <code>ConcurrentModificationException</code>
     * as soon as a call to the method <code>next()</code> is done.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private final int numeroModificheAtteso;

        Itr() {
            this.lastReturned = null;
            this.numeroModificheAtteso = ASDL2223Deque.this.currentMods;
        }

        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return ASDL2223Deque.this.first != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;
        }

        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != ASDL2223Deque.this.currentMods) {
                throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException("Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = ASDL2223Deque.this.first;
                return ASDL2223Deque.this.first.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DescItr();
    }

    /*
     * Class for implementing a descendign iterator for this deque. The iterator is
     * fail-fast: it detects if during the iteration a modification to the original
     * deque was done and, if so, it launches a
     * <code>ConcurrentModificationException</code> as soon as a call to the method
     * <code>next()</code> is done.
     */
    private class DescItr implements Iterator<E> {

        private Node<E> lastReturned;

        private final int numeroModificheAtteso;

        DescItr() {
            this.lastReturned = null;
            this.numeroModificheAtteso = ASDL2223Deque.this.currentMods;
        }

        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return ASDL2223Deque.this.last != null;
            else
                // almeno un prev è stato fatto
                return lastReturned.prev != null;
        }

        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != ASDL2223Deque.this.currentMods) {
                throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException("Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono alla fine e la lista non è vuota
                this.lastReturned = ASDL2223Deque.this.last;
                return ASDL2223Deque.this.last.item;
            } else {
                // non sono alla fine, ma c’è ancora qualcuno
                lastReturned = lastReturned.prev;
                return lastReturned.item;
            }

        }

    }


    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getFirstNode() {
        return this.first;
    }

    /*
     * This method is only for JUnit testing purposes.
     */
    protected Node<E> getLastNode() {
        return this.last;
    }

}

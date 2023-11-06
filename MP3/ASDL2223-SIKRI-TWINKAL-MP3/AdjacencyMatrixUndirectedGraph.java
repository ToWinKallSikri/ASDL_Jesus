package it.unicam.cs.asdl2223.mp3;


import java.util.*;

// TODO completare gli import necessari

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * <p>
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * <p>
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * <p>
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * <p>
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Luca Tesei (template)
 * Twinkal Sikri, twinkal.sikri@studenti.unicam.it (implementazione)
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /*
     * Variabile di supporto per conteggio degli archi nella matrice
     */
    private int edgesCounter;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        // inizializzo la matrice e la lista che conterrà i nodi
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        // inizializzo la variabile di supporto che utilizzo per tenere il conto degli archi
        edgesCounter = 0;
    }

    @Override
    public int nodeCount() {
        // ritorno la size della lista contenente l'insieme dei nodi
        return nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        // ritorno la variabile contenente la quantità di archi
        return edgesCounter;
    }

    @Override
    public void clear() {
        // per fare il clear del grafo, resetto le liste e imposto il counter degli archi a 0
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
        edgesCounter = 0;
    }

    @Override
    public boolean isDirected() {
        // il grafo non è diretto, quindi ritorno false
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        // se il nodo è null, lancio una NullPointerException
        if (node == null) {
            throw new NullPointerException("Nodo nullo.");
        }

        // se il nodo è già contenuto nel grafo ritorno false
        if (this.nodesIndex.containsKey(node)) {
            return false;
        }

        // Aggiungo un nodo nella lista dei nodi
        this.nodesIndex.put(node, this.matrix.size());

        // Aggiungo una lista vuota nella matrice
        this.matrix.add(new ArrayList<GraphEdge<L>>());

        // Sistemo la matrice
        for (ArrayList<GraphEdge<L>> edgeArrayList : this.matrix) {
            for (int i = edgeArrayList.size(); i < this.nodeCount(); i++) {
                edgeArrayList.add(null);
            }
        }
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        // se la label passata è null, lancio una NullPointerException
        if (label == null) {
            throw new NullPointerException("Nodo nullo.");
        }
        // creo un nodo d'appoggio, con la label passata, da aggiungere quindi
        GraphNode<L> amgNode = new GraphNode<>(label);
        // se il nodo con la label data è gia contenuto nella lista dei nodi, ritorno false
        if (this.nodesIndex.containsKey(amgNode)) {
            return false;
        }

        // Aggiunta nodo nella lista dei nodi
        this.nodesIndex.put(amgNode, this.matrix.size());

        // Aggiunta lista vuota nella matrice
        this.matrix.add(new ArrayList<GraphEdge<L>>());

        // Sistemo la matrice
        for (ArrayList<GraphEdge<L>> edgeArrayList : this.matrix) {
            for (int i = edgeArrayList.size(); i < this.nodeCount(); i++) {
                edgeArrayList.add(null);
            }
        }
        return true;
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        // se la label passata è null, lancio una NullPointerException
        if (node == null) {
            throw new NullPointerException("Nodo nullo");
        }
        // se il nodo da rimuovere non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Nodo non trovato nella lista");
        }
        // scorro tutti le liste del matrix rimuovendo ogni edge collegato al node da rimuovere
        for (ArrayList<GraphEdge<L>> sentryList : this.matrix) {
            // condizione di verifica, per vedere se la lista degli archi collegati al nodo non sia null
            if (sentryList.get(nodesIndex.get(node)) != null) {
                // decremento il numero degli edge
                edgesCounter--;
            }
            // elimino la lista di archi collegati al nodo dalla matrice
            // (cast indispensabile per la corretta rimozione della lista)
            sentryList.remove((int) nodesIndex.get(node));
        }
        // elimino la lista di archi dalla matrice
        matrix.remove((int) nodesIndex.get(node));

        // for per sistemare la lista dei nodi e la loro indicizzazione
        for (GraphNode<L> a : nodesIndex.keySet()) {
            // cambio l'indice di tutti i nodi con indice superiore a quello da eliminare
            if (nodesIndex.get(a) > nodesIndex.get(node)) {
                nodesIndex.put(a, nodesIndex.get(a) - 1);
            }
        }
        // elimino il nodo
        nodesIndex.remove(node);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        // se la label è nulla, lancio una NullPointerException
        if (label == null) {
            throw new NullPointerException("Label nulla");
        }
        // creo un nodo d'appoggio con la label passata, per la fruibilità del codice
        GraphNode<L> remLabNode = this.getNode(label);
        // se il nodo con la label in questione non è presente nella lista dei nodi,
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(remLabNode)) {
            throw new IllegalArgumentException("Il nodo collegato alla label, non è in lista");
        }
        // scorro tutti le liste del matrix rimuovendo ogni edge collegato al node da rimuovere
        for (ArrayList<GraphEdge<L>> sentryList : matrix) {
            // condizione di verifica, per vedere se la lista degli archi collegati al nodo non sia null
            if (sentryList.get(nodesIndex.get(remLabNode)) != null) {
                // decremento il numero degli edge
                edgesCounter--;
            }
            // elimino la lista di archi collegati al nodo dalla matrice
            // (cast indispensabile per la corretta rimozione della lista)
            sentryList.remove((int) nodesIndex.get(remLabNode));
        }
        // elimino la lista di archi dalla matrice
        matrix.remove((int) nodesIndex.get(remLabNode));

        // for per sistemare la lista dei nodi e la loro indicizzazione
        for (GraphNode<L> a : nodesIndex.keySet()) {
            // cambio l'indice di tutti i nodi con indice superiore a quello da eliminare
            if (nodesIndex.get(a) > nodesIndex.get(remLabNode)) {
                nodesIndex.put(a, nodesIndex.get(a) - 1);
            }
        }
        // elimino il nodo
        nodesIndex.remove(remLabNode);
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // se l'indice del nodo non è contenuto nella lista, lancio un'IndexOutOfBoundException
        if (!nodesIndex.containsValue((Integer) i)) {
            throw new IndexOutOfBoundsException("Indice del nodo non contenuto nella lista");
        }
        // creo un nodo d'appoggio con l'indice dato, per una maggiore fruibilità del codice
        GraphNode<L> remIndNode = getNode(i);
        // scorro tutti le liste del matrix rimuovendo ogni edge collegato al node da rimuovere
        for (ArrayList<GraphEdge<L>> sentryList : matrix) {
            // condizione di verifica, per vedere se la lista degli archi collegati al nodo non sia null
            if (sentryList.get(nodesIndex.get(remIndNode)) != null) {
                // decremento il numero degli edge
                edgesCounter--;
            }
            // elimino la lista di archi collegati al nodo dalla matrice
            // (cast indispensabile per la corretta rimozione della lista)
            sentryList.remove((int) nodesIndex.get(remIndNode));
        }
        // elimino la lista di archi dalla matrice
        matrix.remove((int) nodesIndex.get(remIndNode));
        // for per sistemare la lista dei nodi e la loro indicizzazione
        for (GraphNode<L> a : nodesIndex.keySet()) {
            // cambio l'indice di tutti i nodi con indice superiore a quello da eliminare
            if (nodesIndex.get(a) > nodesIndex.get(remIndNode)) {
                nodesIndex.put(a, nodesIndex.get(a) - 1);
            }
        }
        // elimino il nodo
        nodesIndex.remove(remIndNode);
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        // se l'etichetta del nodo è null, lancio un NullPointerException
        if (node.getLabel() == null) {
            throw new NullPointerException("L'etichetta dek nodo è nulla");
        }
        // cerco, con un for, il nodo nella lista dei nodi
        for (GraphNode<L> search : nodesIndex.keySet()) {
            // se l'etichetta del nodo che voglio,
            // coincide con una di quelle dei nodi presenti in lista, ritorno quel nodo
            if (search.getLabel().equals(node.getLabel())) {
                return search;
            }
        }
        // se non trovo nulla, ritorno null
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        // se l'etichetta del nodo è null, lancio un NullPointerException
        if (label == null) {
            throw new NullPointerException("La label è nulla");
        }
        // creo uun nodo d'appoggio con la label data, per la fruibilità del codice
        GraphNode<L> supp = new GraphNode<L>(label);
        // cerco, con un for, il nodo nella lista dei nodi
        for (GraphNode<L> search : nodesIndex.keySet()) {
            // se il nodo che voglio, coincide con uno dei nodi presenti in lista,
            // ritorno quel nodo
            if (search.equals(supp)) {
                return search;
            }
        }
        // se non trovo nulla, ritorno null
        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // se l'indice del nodo non è contenuto nella lista dei nodi, lancio una NullPointerException
        if (i >= this.nodeCount() || i < 0) {
            throw new IndexOutOfBoundsException("Indice non corretto");
        }
        // cerco, con un for, l'indice del nodo nella lista dei nodi
        for (GraphNode<L> x : nodesIndex.keySet()) {
            // se l'indice del nodo che voglio, coincide con uno di quelli dei nodi presenti in lista,
            // ritorno quel nodo
            if (getNodeIndexOf(x) == i) {
                return x;
            }
        }
        // se non trovo nulla, ritorno null
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        // se il nodo è null, lancio una NullPointerException
        if (node == null) {
            throw new NullPointerException("Nodo nullo");
        }
        // richiamo il metodo sottostante, che ritorna il nodo sfruttando la label di questo
        return getNodeIndexOf(node.getLabel());
    }

    @Override
    public int getNodeIndexOf(L label) {
        // se la label è null, lancio una NullPointerException
        if (label == null) {
            throw new NullPointerException("Label nulla");
        }
        // Cerco tra la lista di nodi, il nodo che
        // ha la label coincidente e ritorno il proprio index
        for (GraphNode<L> sentryNode : this.getNodes()) {
            if (sentryNode.getLabel().equals(label))
                return this.nodesIndex.get(sentryNode);
        }
        // se non trovo la label nella lista dei nodi, lancio un'IllegalArgumentException
        throw new IllegalArgumentException("Label assente");
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // creo un set dove inserire tutti i nodi
        Set<GraphNode<L>> nodeSet = new HashSet<>();
        // for per scorrere tutta la lista dei nodi
        for (GraphNode<L> node : nodesIndex.keySet()) {
            // se il nodo non è null, lo aggiungo al set
            if (node != null) {
                nodeSet.add(node);
            }
        }
        return nodeSet;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // se l'arco da aggiungere è null, lancio una NullPointerException
        if (edge == null) {
            throw new NullPointerException("Edge nullo");
        }
        // se uno dei due nodi dell'arco, non è contenuto nella lista dei nodi, o se l'arco è diretto,
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()) ||
                edge.isDirected()) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // creo due nodi d'appoggio, con i nodi dell'arco, per la fruibilità del codice
        Integer xNode_1 = nodesIndex.get(edge.getNode1());

        Integer xNode_2 = nodesIndex.get(edge.getNode2());
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(edge)) {
            return false;
        }
        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(edge.getNode1(), edge.getNode2(), false, edge.getWeight());

        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);
        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // se uno dei due nodi passati è null, lancio una NullPointerException
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Uno o entrambi i nodi, sono null");
        }
        // creo due variabili d'appoggio con l'indice dei due nodi
        Integer xNode_1 = nodesIndex.get(node1);

        Integer xNode_2 = nodesIndex.get(node2);
        // creo un arco d'appoggio con i due nodi dati
        GraphEdge<L> susEdge = new GraphEdge<>(node1, node2, false);
        // se uno dei due nodi non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(susEdge)) {
            return false;
        }
        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(node1, node2, false);

        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);
        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }


    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // se uno dei due nodi passati è null, lancio una NullPointerException
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Uno o entrambi i nodi, sono null");
        }
        // creo due variabili d'appoggio con l'indice dei due nodi
        Integer xNode_1 = nodesIndex.get(node1);

        Integer xNode_2 = nodesIndex.get(node2);
        // creo un arco d'appoggio con i due nodi dati
        GraphEdge<L> susEdge = new GraphEdge<>(node1, node2, false);
        // se uno dei due nodi non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(susEdge)) {
            return false;
        }

        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(node1, node2, false, weight);
        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);
        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // se una delle due label passate è null, lancio una NullPointerException
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Una delle due etichette è null");
        }
        // creo due variabili d'appoggio con l'indice dei due nodi che contengono le label
        int xNode_1 = nodesIndex.get(getNode(label1));

        int xNode_2 = nodesIndex.get(getNode(label2));
        // creo un arco d'appoggio con i due nodi dati
        GraphEdge<L> susEdge = new GraphEdge<>(getNode(xNode_1), getNode(xNode_2), false);
        // se uno dei due nodi non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsValue(xNode_1) || !nodesIndex.containsValue(xNode_2)) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(susEdge)) {
            return false;
        }
        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(getNode(xNode_1), getNode(xNode_2), false);
        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);

        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        // se una delle due label passate è null, lancio una NullPointerException
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Una delle due etichette è null");
        }
        // creo due variabili d'appoggio con l'indice dei due nodi che contengono le label
        Integer xNode_1 = nodesIndex.get(getNode(label1));

        Integer xNode_2 = nodesIndex.get(getNode(label2));
        // creo un arco d'appoggio con i due nodi delle label
        GraphEdge<L> susEdge = getEdge(xNode_1, xNode_2);
        // se uno dei due nodi non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsValue(xNode_1) || !nodesIndex.containsValue(xNode_2)) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(susEdge)) {
            return false;
        }
        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(getNode(xNode_1), getNode(xNode_2), false, weight);
        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);
        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }

    @Override
    public boolean addEdge(int i, int j) {
        // se uno dei due indici passati non è corretto lancio una IndexOutOfBoundException
        if (i > nodeCount() || i < 0 || j > nodeCount() || j < 0) {
            throw new IndexOutOfBoundsException("Indici non corretti");
        }
        // creo due variabili d'appoggio con l'indice dei due nodi che hanno indice coincidente a quelli passati
        Integer xNode_1 = nodesIndex.get(getNode(i));

        Integer xNode_2 = nodesIndex.get(getNode(j));
        // creo un arco d'appoggio con i due nodi dati
        GraphEdge<L> susEdge = new GraphEdge<>(getNode(i), getNode(j), false);
        // se uno dei due nodi non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsValue(xNode_1) || !nodesIndex.containsValue(xNode_2)) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(susEdge)) {
            return false;
        }
        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(getNode(xNode_1), getNode(xNode_2), false);
        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);
        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // se uno dei due indici passati non è corretto lancio una IndexOutOfBoundException
        if (i >= nodeCount() || i < 0 || j >= nodeCount() || j < 0) {
            throw new IndexOutOfBoundsException("Indici non corretti");
        }
        // creo due variabili d'appoggio con l'indice dei due nodi che hanno indice coincidente a quelli passati
        Integer xNode_1 = nodesIndex.get(getNode(i));

        Integer xNode_2 = nodesIndex.get(getNode(j));
        // creo un arco d'appoggio con i due nodi dati
        GraphEdge<L> susEdge = getEdge(xNode_1, xNode_2);
        // se uno dei due nodi non è contenuto nella lista, lancio un'IllegalArgumentException
        if (!nodesIndex.containsValue(xNode_1) || !nodesIndex.containsValue(xNode_2)) {
            throw new IllegalArgumentException("Parametri dell'edge non corretti");
        }
        // se lo slot dove devo inserire l'arco è già occupato e
        // l'arco che lo occupa è quello che voglio inserire, ritorno false
        if (matrix.get(xNode_1).get(xNode_2) != null && matrix.get(xNode_1).get(xNode_2).equals(susEdge)) {
            return false;
        }
        // creo un arco d'appoggio per la fruibilità del codice
        GraphEdge<L> newEdge = new GraphEdge<L>(getNode(xNode_1), getNode(xNode_2), false, weight);
        // aggiungo l'arco nella matrice, in entrambe le posizioni
        matrix.get(xNode_1).set(xNode_2, newEdge);

        matrix.get(xNode_2).set(xNode_1, newEdge);
        // incremento il counter degli archi
        edgesCounter++;

        return true;
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // se l'arco da rimuovere è null, o se uno dei due nodi collegati all'arco è null,
        // lancio una NullPointerException
        if (edge == null || edge.getNode1() == null || edge.getNode2() == null) {
            throw new NullPointerException("Arco da rimuovere non corretto");
        }
        // se uno dei due nodi dell'arco da rimuovere non è contenuto nella lista dei nodi,
        // o se l'arco da rimuovere non è contenuto nella matrice, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(edge.getNode1()) || !nodesIndex.containsKey(edge.getNode2()) ||
                !matrix.get(getNodeIndexOf(edge.getNode1())).contains(edge)) {
            throw new IllegalArgumentException("Arco da rimuovere indefinito");
        }
        // creo due variabili d'appoggio con l'indice dei nodi dell'arco da rimuovere
        Integer xNode_1 = nodesIndex.get(edge.getNode1());

        Integer xNode_2 = nodesIndex.get(edge.getNode2());
        // rimuovo l'arco settandolo a null, nelle due corrispettive posizioni nella matrice
        matrix.get(xNode_1).set(xNode_2, null);

        matrix.get(xNode_2).set(xNode_1, null);
        // decremento il numero degli edge
        edgesCounter--;

        return;
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        //  se uno dei due nodi collegati all'arco da rimuovere è null,
        // lancio una NullPointerException
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Uno o entrambi i nodi sono null");
        }
        // creo un arco d'appoggio con i due nodi passati
        GraphEdge<L> remEdge = new GraphEdge<L>(node1, node2, false);
        // se uno dei due nodi dell'arco da rimuovere non è contenuto nella lista dei nodi,
        // o se l'arco da rimuovere non è contenuto nella matrice, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2) ||
                !matrix.get(getNodeIndexOf(node1)).contains(remEdge) ||
                !matrix.get(getNodeIndexOf(node2)).contains(remEdge)) {
            throw new IllegalArgumentException("Arco da rimuovere indefinito");
        }
        // creo due variabili d'appoggio con l'indice dei nodi dell'arco da rimuovere
        Integer xNode_1 = nodesIndex.get(node1);

        Integer xNode_2 = nodesIndex.get(node2);
        // rimuovo l'arco settandolo a null, nelle due corrispettive posizioni nella matrice
        matrix.get(xNode_1).set(xNode_2, null);

        matrix.get(xNode_2).set(xNode_1, null);
        // decremento il numero degli edge
        edgesCounter--;

        return;
    }

    @Override
    public void removeEdge(L label1, L label2) {
        // se una delle label dei nodi collegati all'arco è null,
        // lancio una NullPointerException
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Arco da rimuovere non corretto");
        }
        // creo due nodi d'appoggio con l'etichetta dei nodi dell'arco da rimuovere,
        // per la fruibilità del codice
        GraphNode<L> labNode_1 = getNode(label1);

        GraphNode<L> labNode_2 = getNode(label2);
        // se uno dei due nodi dell'arco da rimuovere non è contenuto nella lista dei nodi,
        // o se l'arco da rimuovere non è contenuto nella matrice, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(labNode_1) || !nodesIndex.containsKey(getNode(labNode_2)) ||
                !matrix.get(getNodeIndexOf(labNode_1)).contains(getEdge(labNode_1, labNode_2))) {
            throw new IllegalArgumentException("Arco da rimuovere indefinito");
        }
        // creo due variabili d'appoggio con l'indice dei nodi dell'arco da rimuovere
        Integer xNode_1 = nodesIndex.get(labNode_1);

        Integer xNode_2 = nodesIndex.get(labNode_2);
        // rimuovo l'arco settandolo a null, nelle due corrispettive posizioni nella matrice
        matrix.get(xNode_1).set(xNode_2, null);

        matrix.get(xNode_2).set(xNode_1, null);
        // decremento il numero degli edge
        edgesCounter--;

        return;
    }

    @Override
    public void removeEdge(int i, int j) {
        // se uno dei due indici passati non è corretto lancio una IndexOutOfBoundException
        if (i > nodeCount() || i < 0 || j > nodeCount() || j < 0) {
            throw new IndexOutOfBoundsException("Indici non corretti");
        }
        // creo due nodi d'appoggio con l'indice dei nodi dell'arco da rimuovere,
        // per la fruibilità del codice
        GraphNode<L> indNode_1 = getNode(i);

        GraphNode<L> indNode_2 = getNode(j);
        // se uno dei due nodi dell'arco da rimuovere non è contenuto nella lista dei nodi,
        // o se l'arco da rimuovere non è contenuto nella matrice, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(indNode_1) || !nodesIndex.containsKey(getNode(indNode_2)) ||
                !matrix.get(getNodeIndexOf(indNode_1)).contains(getEdge(indNode_1, indNode_2))) {
            throw new IllegalArgumentException("Arco da rimuovere indefinito");
        }
        // creo due variabili d'appoggio con l'indice dei nodi dell'arco da rimuovere
        Integer xNode_1 = nodesIndex.get(indNode_1);

        Integer xNode_2 = nodesIndex.get(indNode_2);
        // rimuovo l'arco settandolo a null, nelle due corrispettive posizioni nella matrice
        matrix.get(xNode_1).set(xNode_2, null);

        matrix.get(xNode_2).set(xNode_1, null);
        // decremento il numero degli edge
        edgesCounter--;

        return;
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        // se l'arco che voglio è null, lancio una NullPointerException
        if (edge == null) {
            throw new NullPointerException("Edge nullo");
        }
        // creo due nodi d'appoggio, con i nodi dell'arco che voglio,
        // per la fruibilità del codice
        GraphNode<L> auxNode_1 = edge.getNode1();

        GraphNode<L> auxNode_2 = edge.getNode2();
        // se uno dei due nodi dell'arco che voglio, non è contenuto nella lista dei nodi
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(auxNode_1) || !nodesIndex.containsKey(auxNode_2)) {
            throw new IllegalArgumentException("Nodi non contenuti nel grafo");
        }
        // creo due variabili d'appoggio con i nodi dell'arco che voglio
        Integer xNode_1 = nodesIndex.get(edge.getNode1());

        Integer xNode_2 = nodesIndex.get(edge.getNode2());

        // for per scorrere gli archi nella matrice e quando trovo quello giusto, lo ritorno
        for (ArrayList<GraphEdge<L>> edgeTest : matrix) {
            if (edgeTest.contains(edge))
                return matrix.get(xNode_1).get(xNode_2);
        }
        // se non trovo nulla, ritorno null
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // se l'arco che voglio è null, lancio una NullPointerException
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Uno, o entrambi, i nodi dell'arco sono null");
        }
        // se uno dei due nodi dell'arco che voglio, non è contenuto nella lista dei nodi
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Nodi non contenuti nel grafo");
        }
        // creo un nuovo arco d'appoggio con i nodi che gli passo
        GraphEdge<L> newEdge = new GraphEdge<>(node1, node2, isDirected());
        // richiamo il metodo getEdge e gli passo come parametro il nuovo arco
        return getEdge(newEdge);

    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // se l'arco che voglio è null, lancio una NullPointerException
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Una, o entrambe, le label dei nodi dell'arco sono null");
        }
        // se uno dei due nodi dell'arco che voglio, non è contenuto nella lista dei nodi
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(getNode(label1)) || !nodesIndex.containsKey(getNode(label2))) {
            throw new IllegalArgumentException("Nodi non contenuti nel grafo");
        }
        // creo un nuovo arco d'appoggio con i nodi che gli passo
        GraphEdge<L> newEdge = new GraphEdge<>(getNode(label1), getNode(label2), isDirected());
        // richiamo il metodo getEdge e gli passo come parametro il nuovo arco
        return getEdge(newEdge);

    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // se i nodi correlati agli indici che gli passo non sono contenuti nella lista,
        // lancio una NullPointerException
        if (!nodesIndex.containsKey(getNode(i)) || !nodesIndex.containsKey(getNode(j))) {
            throw new IllegalArgumentException("Nodi non contenuti nel grafo");
        }
        // creo un nuovo arco con i nodi correlati agli indici che gli passo
        GraphEdge<L> newEdge = new GraphEdge<>(getNode(i), getNode(j), isDirected());
        // richiamo il metodo getEdge e gli passo come parametro il nuovo arco
        return getEdge(newEdge);

    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // se il nodo passato è null, lancio una NullPointerException
        if (node == null) {
            throw new NullPointerException("Nodo nullo");
        }
        // se la lista dei nodi, non contiene il nodo passato,
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Nodo assente");
        }
        // creo un set di nodi, dove aggiungo tutti i nodi adiacenti a quello passato
        Set<GraphNode<L>> adjNodeSet = new HashSet<>();
        // for per scorrere gli archi del nodo passato, nella matrice
        for (GraphEdge<L> supp : matrix.get(nodesIndex.get(node))) {
            // se l'arco non è nullo, faccio l'add al set verificando che il nodo che
            // voglio aggiungere non sia uguale a quello che gli passo
            if (supp != null) {
                if (supp.getNode1().equals(node)) {
                    adjNodeSet.add(supp.getNode2());
                } else {
                    adjNodeSet.add(supp.getNode1());
                }
            }
        }
        // ritorno il set
        return adjNodeSet;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        // se la label passata è null, lancio una NullPointerException
        if (label == null) {
            throw new NullPointerException("Label nulla");
        }
        // se la lista dei nodi, non contiene il nodo con l'etichetta passata,
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(getNode(label))) {
            throw new IllegalArgumentException("Nodo assente");
        }
        // creo un nodo d'appoggio con la label passata
        GraphNode<L> labNode = getNode(label);
        // creo un set di nodi, dove aggiungo tutti i nodi adiacenti a quello passato
        Set<GraphNode<L>> adjNodeSet = new HashSet<>();
        // for per scorrere gli archi del nodo passato, nella matrice
        for (GraphEdge<L> supp : matrix.get(nodesIndex.get(labNode))) {
            // se l'arco non è nullo, faccio l'add al set verificando che il nodo che
            // voglio aggiungere non sia uguale a quello che gli passo
            if (supp != null) {
                if (supp.getNode1().equals(labNode)) {
                    adjNodeSet.add(supp.getNode2());
                } else {
                    adjNodeSet.add(supp.getNode1());
                }
            }
        }
        // ritorno il set
        return adjNodeSet;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        // se l'indice non è corretto lancio un'IndexOutOfBoundException
        if (i < 0 || i >= nodeCount()) {
            throw new IndexOutOfBoundsException("Indice non corretto");
        }
        // se la lista dei nodi, non contiene il nodo con l'indice passato,
        // lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(getNode(i))) {
            throw new IllegalArgumentException("Nodo assente");
        }
        // creo un nodo d'appoggio con l'indice che viene passato
        GraphNode<L> indNode = getNode(i);
        // creo un set di nodi, dove aggiungo tutti i nodi adiacenti a quello passato
        Set<GraphNode<L>> adjNodeSet = new HashSet<>();
        // for per scorrere gli archi del nodo passato, nella matrice
        for (GraphEdge<L> supp : matrix.get(nodesIndex.get(indNode))) {
            // se l'arco non è nullo, faccio l'add al set verificando che il nodo che
            // voglio aggiungere non sia uguale a quello che gli passo
            if (supp != null) {
                if (supp.getNode1().equals(indNode)) {
                    adjNodeSet.add(supp.getNode2());
                } else {
                    adjNodeSet.add(supp.getNode1());
                }
            }
        }
        // ritorno il set
        return adjNodeSet;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        // se il nodo di cui voglio gli archi è nullo, lancio una NullPointerException
        if (node == null) {
            throw new NullPointerException("Nodo nullo");
        }
        // se la lista dei nodi, non contiene il nodo passato, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Nodo assente");
        }
        // creo un set dove verranno inseriti tutti gli archi correlati al nodo
        Set<GraphEdge<L>> graphEdgeSet = new HashSet<>();
        // for per scorrere gli archi del nodo passato, nella matrice
        for (GraphEdge<L> supp : matrix.get(getNodeIndexOf(node))) {
            // se l'arco non è nullo, faccio l'add al set
            if (supp != null) {
                graphEdgeSet.add(supp);
            }
        }
        // ritorno il set di archi
        return graphEdgeSet;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // se la label del nodo di cui voglio gli archi è nulla, lancio una NullPointerException
        if (label == null) {
            throw new NullPointerException("Label nulla");
        }
        // creo un nodo d'appoggio con a label passata
        GraphNode<L> labNode = getNode(label);
        // se la lista dei nodi, non contiene il nodo passato, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(labNode)) {
            throw new IllegalArgumentException("Nodo assente");
        }
        // creo un set dove verranno inseriti tutti gli archi correlati al nodo
        Set<GraphEdge<L>> graphEdgeSet = new HashSet<>();
        // for per scorrere gli archi del nodo passato, nella matrice
        for (GraphEdge<L> supp : matrix.get(getNodeIndexOf(labNode))) {
            // se l'arco non è nullo, faccio l'add al set
            if (supp != null) {
                graphEdgeSet.add(supp);
            }
        }
        // ritorno il set di archi
        return graphEdgeSet;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // se l'indice non è corretto lancio un'IndexOutOfBoundException
        if (i < 0 || i > nodeCount()) {
            throw new IndexOutOfBoundsException("Indice non corretto");
        }
        // creo un nodo d'appoggio con l'indice che viene passato
        GraphNode<L> indNode = getNode(i);
        // se la lista dei nodi, non contiene il nodo passato, lancio un'IllegalArgumentException
        if (!nodesIndex.containsKey(indNode)) {
            throw new IllegalArgumentException("Nodo assente");
        }
        // creo un set dove verranno inseriti tutti gli archi correlati al nodo
        Set<GraphEdge<L>> graphEdgeSet = new HashSet<>();
        // for per scorrere gli archi del nodo passato, nella matrice
        for (GraphEdge<L> supp : matrix.get(getNodeIndexOf(indNode))) {
            // se l'arco non è nullo, faccio l'add al set
            if (supp != null) {
                graphEdgeSet.add(supp);
            }
        }
        // ritorno il set di archi
        return graphEdgeSet;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // creo un nuovo set dove inserire tutti gli archi della matrice
        Set<GraphEdge<L>> allEdges = new HashSet<>();
        // for per scorrere tutti i nodi della lista
        for (GraphNode<L> xNode : nodesIndex.keySet()) {
            // per ogni nodo della lista, con questo for scorro tutti gli archi della lista
            // a loro collegata
            for (GraphEdge<L> xEdge : matrix.get(nodesIndex.get(xNode))) {
                // se l'arco non è null, lo aggiungo al set creato prima
                if (xEdge != null) {
                    allEdges.add(xEdge);
                }
            }
        }
        return allEdges;
    }

}

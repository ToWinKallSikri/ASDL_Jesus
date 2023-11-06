/**
 * 
 */
package it.unicam.cs.asdl2223.mp1;

/**
 * Un oggetto di questa classe rappresenta una lampada che ha un appoggio
 * circolare. Implementa l'interfaccia ShelfItem, ma come lunghezza e larghezza
 * ha il diametro della base. Ridefinisce il metodo di default per calcolare la
 * superficie occupata restituiendo l'area del cerchio che corrisponde alla
 * base. Una lampada è identificata dal nome e dal nome del brand.
 * 
 *  @author Luca Tesei (template)
 *          Twinkal Sikri, twinkal.sikri@studenti.unicam.it (implementazione)
 *
 */
public class RoundLamp implements ShelfItem {

    private final double diameter;

    private final double weight;

    private final String name;

    private final String brandName;


    /**
     * @param diameter
     *                      diametro della base in cm
     * @param weight
     *                      peso in grammi
     * @param name
     *                      nome del modello della lampada
     * @param brandName
     *                      nome del brand della lampada
     */
    public RoundLamp(double diameter, double weight, String name,
            String brandName) {
        this.diameter = diameter;
        this.weight = weight;
        this.name = name;
        this.brandName = brandName;
    }

    /*
     * Restituisce l'area del cerchio corrispondente alla base
     */
    @Override
    public double getOccupiedSurface() {
        double OccupiedSurface = (getRadius()*getRadius())*Math.PI;
        return OccupiedSurface;
    }
    /*
     * Restituisce il raggio della circonferenza
     */
    public double getRadius() {
        return this.diameter/2;
    }
    /*
     * Restituisce il diametro della base
     */
    @Override
    public double getLength() {
        return this.diameter;
    }

    /*
     * Restituisce il diametro della base
     */
    @Override
    public double getWidth() {
        return this.diameter;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /*
     * Due lampade sono considerate uguali se hanno lo stesso nome e lo stesso
     * nome del brand.
     */
    @Override
    public boolean equals(Object obj) {
        RoundLamp o = null;
        if (this == obj) return true;
        if (obj instanceof RoundLamp)
            o = (RoundLamp) obj;
        else return false;
        return this.name.equals(o.name) && this.brandName.equals(o.brandName);
    }

    /*
     * L'hashcode viene calcolato usando gli stessi campi usati per definire
     * l'uguaglianza
     */
    @Override
    public int hashCode() {
        int hash = 1 + 31 * name.hashCode();
        hash = hash + 31 * brandName.hashCode();
        return hash;
    }


}

public class Electronica extends Producte implements Comparable<Electronica> {
    int diesGarantia;

    //-----------------------------------------------------------------------------
    // Constructor
    public Electronica(String nom, float preu, String CODI_DE_BARRES, int diesGarantia) {
        super(nom, preu, CODI_DE_BARRES);
        this.diesGarantia = diesGarantia;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Getters
    public int getDiesGarantia() {
        return diesGarantia;
    }

    @Override
    public float getPreu() {
        return calcularPreu();
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Setters
    public void setDiesGarantia(int diesGarantia) {
        this.diesGarantia = diesGarantia;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Sobreescrivim el mètode abstracte de Producte i fem l'operacio de l'enunciat
    @Override
    public float calcularPreu() {
        // Fem l'operació de l'enunciat
        float operacioGarantia = super.getPreu() + super.getPreu()*(diesGarantia/365)*0.1f;
        return operacioGarantia;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Electrònica{" + "\n" +
                super.toString() + "\n" +
                "       Dies de garantia = '" + diesGarantia + '\'' + "\n" +
                "       Preu final = " + getPreu() + "\n" +
                "}";
    }
    //-----------------------------------------------------------------------------

    // -----------------------------------------------------------------------------
    // Metode de comparació
    // La classe String ja te un mètode de comparació per default.
    // Només hem de cridar al mètode de comparació (compareTo) per comparar dos strings
    @Override
    public int compareTo(Electronica e1) {
        return this.getNom().compareTo(e1.getNom());
    }
    // ----------------------------------------------------------------------------
}

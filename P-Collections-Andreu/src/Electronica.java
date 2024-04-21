public class Electronica extends Producte {
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

}

public class Textil extends Producte {
    public enum enumCompositioTextil {ALGODON, POLIESTER, LINO, SEDA, LANA, NYLON, OTRO;};
    enumCompositioTextil composicioTextil;

    //-----------------------------------------------------------------------------
    // Constructor
    public Textil(String nom, float preu, String CODI_DE_BARRES, enumCompositioTextil composicioTextil) {
        super(nom, preu, CODI_DE_BARRES);
        this.composicioTextil = composicioTextil;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Getters
    public enumCompositioTextil getComposicioTextil() {
        return composicioTextil;
    }

    @Override
    public float getPreu() {
        return calcularPreu();
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Setters
    public void setComposicioTextil(String composicioTextil) {
        this.composicioTextil = enumCompositioTextil.valueOf(composicioTextil);
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Sobreescrivim el mètode abstracte de Producte i el deixem igual, no varia de la classe superior
    @Override
    public float calcularPreu() {
        return super.getPreu();
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Textil{" + "\n" +
                super.toString() + "\n" +
                "       Composició textil = '" + composicioTextil + '\'' + "\n" +
                "       Preu final (no varia) = " + calcularPreu() + "\n" +
                "}";
    }
    //-----------------------------------------------------------------------------
}

public class Textil extends Producte {
    private String composicioTextil;

    //-----------------------------------------------------------------------------
    // Constructor
    public Textil(String nom, float preu, String CODI_DE_BARRES, String composicioTextil) {
        super(nom, preu, CODI_DE_BARRES);
        this.composicioTextil = composicioTextil;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Getters
    public String getComposicioTextil() {
        return composicioTextil;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Setters
    public void setComposicioTextil(String composicioTextil) {
        this.composicioTextil = composicioTextil;
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
                "       ComposicioTextil = '" + composicioTextil + '\'' + "\n" +
                "       Preu final (no varia) = " + getPreu() + "\n" +
                "}";
    }
    //-----------------------------------------------------------------------------
}

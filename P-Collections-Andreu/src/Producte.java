public abstract class Producte {
    private String nom;
    private float preu;
    private final String CODI_DE_BARRES;

    //-------------------------------------------------------------------------------
    // Constructor
    public Producte(String nom, float preu, String CODI_DE_BARRES) {
        this.nom = nom;
        this.preu = preu;
        this.CODI_DE_BARRES = CODI_DE_BARRES;
    }
    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // Getters
    public String getNom() {
        return nom;
    }

    public float getPreu() {
        return preu;
    }

    public String getCODI_DE_BARRES() {
        return CODI_DE_BARRES;
    }
    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPreu(float preu) {
        this.preu = preu;
    }
    //-------------------------------------------------------------------------------

    // Classe abstracta per calcular el preu
    public abstract float calcularPreu();
}

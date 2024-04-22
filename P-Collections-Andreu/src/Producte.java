import java.util.HashMap;
import java.util.Random;
import java.util.regex.*;


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

    public float getPreu(){
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

    //------------------------------------------------------------------------
    @Override
    public String toString() {
        return  "       nom='" + nom + '\'' + "\n" +
                "       preu=" + preu + "\n" +
                "       Codi_de_barres='" + CODI_DE_BARRES + '\'';
    }
    //------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // Classe abstracta per calcular el preu
    public abstract float calcularPreu();
    //-------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    public boolean comprovarCodiDeBarres(String CODI_DE_BARRES) {
        Pattern patron = Pattern.compile("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(202[5-9]|20[3-9][0-9])$");
        Matcher mat = patron.matcher(CODI_DE_BARRES);
        return mat.matches();
    }
    //-------------------------------------------------------------------------------



}

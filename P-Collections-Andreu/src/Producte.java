import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.*;


public abstract class Producte {
    private String nom;
    private float preu;
    private final String CODI_DE_BARRES;
    private int quantitat;


    //-------------------------------------------------------------------------------
    // Constructor
    public Producte(String nom, float preu, String CODI_DE_BARRES) {
        this.nom = nom;
        this.preu = preu;
        this.CODI_DE_BARRES = CODI_DE_BARRES;
        this.quantitat = 1;
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

    public int getQuantitat() {
        return quantitat;
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

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }
    //-------------------------------------------------------------------------------

    //------------------------------------------------------------------------
    @Override
    public String toString() {
        return  "       nom='" + nom + '\'' + "\n" +
                "       quantitat=" + quantitat + "\n" +
                "       preu=" + preu + "\n" +
                "       Codi_de_barres='" + CODI_DE_BARRES + '\'';
    }
    //------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // Classe abstracta per calcular el preu
    public abstract float calcularPreu();
    //-------------------------------------------------------------------------------





}

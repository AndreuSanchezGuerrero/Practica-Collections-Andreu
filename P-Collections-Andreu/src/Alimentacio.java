import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Alimentacio extends Producte implements Comparable<Alimentacio> {
    private String DATACADUCITAT; // dd-mm-yyyy
    Scanner scan = new Scanner(System.in);

    //------------------------------------------------------------------------
    // Constructor
    public Alimentacio(String nom, float preu, String CODI_DE_BARRES, String dataCaducitat) {
        super(nom, preu, CODI_DE_BARRES);
        this.DATACADUCITAT = dataCaducitat;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // calcularPreu() -> Sobreescrivim el mètode obstracte de Producte i fem l'operació de l'enunciat
    @Override
    public float calcularPreu() {
        // Obtenim la data actual
        LocalDate dataActual = LocalDate.now();

        // Guardem la DATACADUCITAT en format LocalDate per poder operar amb ella i la parsegem al format dd-mm-yyyy.
        LocalDate dataParsejada = LocalDate.parse(DATACADUCITAT, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // Operació de l'enunciat
        int resta = dataActual.until(dataParsejada).getDays() +1;
        float preuFinal = super.getPreu() - super.getPreu() * (1.0f / resta) + (super.getPreu() * 0.1f);
        return preuFinal;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // Getters
    public String getDATACADUCITAT() {
        return DATACADUCITAT;
    }

    @Override
    public float getPreu() {
        return calcularPreu();
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Alimentacio{" + "\n" +
                super.toString() + "\n" +
                "       dataCaducitat = '" + DATACADUCITAT + '\'' + "\n" +
                "       Preu final = " + getPreu() + "\n" +
                "}";
    }
    //------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Metode de comparació
    // La classe String ja te un mètode de comparació per default.
    // Només hem de cridar al mètode de comparació (compareTo) per comparar dos strings
    @Override
    public int compareTo(Alimentacio a1) {
        return this.getNom().compareTo(a1.getNom());
    }
    //-----------------------------------------------------------------------------
}

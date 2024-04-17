import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Alimentacio extends Producte {
    private String dataCaducitat; // dd-mm-yyyy

    //------------------------------------------------------------------------
    // Constructor
    public Alimentacio(String nom, float preu, String CODI_DE_BARRES, String dataCaducitat) {
        super(nom, preu, CODI_DE_BARRES);
        this.dataCaducitat = dataCaducitat;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // Sobreescrivim el mètode
    @Override
    public float calcularPreu() {
        LocalDate dataActual = LocalDate.now();
        LocalDate dataParsejada = LocalDate.parse(dataCaducitat, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // Operació de l'enunciat
        int resta = dataActual.until(dataParsejada).getDays() +1;
        float preuFinal = super.getPreu() - super.getPreu() * (1.0f / resta) + (super.getPreu() * 0.1f);
        return preuFinal;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // Getters
    public String getDataCaducitat() {
        return dataCaducitat;
    }

    @Override
    public float getPreu() {
        return calcularPreu();
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // Setters
    public void setDataCaducitat(String dataCaducitat) {
        this.dataCaducitat = dataCaducitat;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Alimentacio{" + "\n" +
                super.toString() + "\n" +
                "       dataCaducitat = '" + dataCaducitat + '\'' + "\n" +
                "       Preu final = " + getPreu() + "\n" +
                "}";
    }
    //------------------------------------------------------------------------
}

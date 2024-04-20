import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Alimentacio extends Producte {
    private String dataCaducitat; // dd-mm-yyyy
    Scanner scan = new Scanner(System.in);
    //------------------------------------------------------------------------
    // Constructor
    public Alimentacio(String nom, float preu, String CODI_DE_BARRES, String dataCaducitat) {
        super(nom, preu, CODI_DE_BARRES);
        while (!comprovarDataCaducitat(dataCaducitat)) {
            System.out.println();
            System.out.println("Data incorrecta, torna-ho a provar.");
            System.out.println("Introdueix la data en format (dd-mm-yyyy): ");
            dataCaducitat = scan.nextLine();
        }
        this.dataCaducitat = dataCaducitat;
    }
    //------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // Sobreescrivim el mètode obstracte de Producte
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

    //------------------------------------------------------------------------
    // Expressió regular per validar la data
    private boolean comprovarDataCaducitat(String data) {
        Pattern patron = Pattern.compile("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(202[5-9]|20[3-9][0-9])$");
        Matcher mat = patron.matcher(data);
        return mat.matches();
    }
    //------------------------------------------------------------------------
}

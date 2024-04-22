import java.util.ArrayList;
import java.time.LocalTime;
import java.util.Scanner;

public class CarroCompra {
    // Dades globals
    private ArrayList<Producte> llistaProductes;
    private ArrayList<Alimentacio> llistaAlimentacio;
    private ArrayList<Textil> llistaTextil;
    private ArrayList<Electronica> llistaElectronica;
    Scanner input = new Scanner(System.in);

    //-----------------------------------------------------------------------------
    // Constructor
    public CarroCompra() {
        llistaProductes = new ArrayList<Producte>();
        llistaAlimentacio = new ArrayList<Alimentacio>();
        llistaTextil = new ArrayList<Textil>();
        llistaElectronica = new ArrayList<Electronica>();
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Metode per mostrar el menu principal
    public void menu1() {
       // Guardem la mida de la String saludar per que quedi nivellat amb els guions
        String saludar = "---" + saludar() + " benvingut al mercat online---";
        String guions = "";
        for (String guion:saludar.split("")) {
            guions += "-";
        }
        System.out.println(guions);
        System.out.println(saludar);
        System.out.println(guions);
        System.out.println("(1) Introduir producte");
        System.out.println("(2) Passar per caixa");
        System.out.println("(3) Mostar carret de compra");
        System.out.println("(0) Acabar");
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    public String saludar() {
        // Obtenir l'hora actual
        LocalTime horaActual = LocalTime.now();

        // Saludar en funció de l'hora
        String saludar;
        if (horaActual.isBefore(LocalTime.of(14, 0)) && horaActual.isAfter(LocalTime.of(06, 0))) {
            saludar = "Bon dia";
        } else if (horaActual.isBefore(LocalTime.of(20, 0))&& horaActual.isAfter(LocalTime.of(14, 0))) {
            saludar = "Bona tarda";
        } else {
            saludar = "Bona nit";
        }
        return saludar;
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    public void menu2() {
        System.out.println("(1) Afegir aliment");
        System.out.println("(2) Afegir tèxtil");
        System.out.println("(3) Afegir electrònica");
        System.out.println("(0) Acabar");
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte a la llista de productes global
    public void introduirProducte(Producte producte) {
        llistaProductes.add(producte);
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Metode per mostrar el carret de la compra
    public void mostrarProductesCarret() {
        for(Producte producte:llistaProductes) {
            System.out.println("Producte: " + producte);
        }
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte de alimentació a la llista de productes de alimentació
    public void introduirProducteAlimentacio(Alimentacio aliment) {
        llistaAlimentacio.add(aliment);
        System.out.println("Producte "+aliment.getNom()+ " afegit correctament");
    }

    public void crearProducteAlimentacio() {
        String nom;
        float preu;
        String codiDeBarres;
        String dataCaducitat;
        System.out.println("Introdueix el nom del producte: ");
        nom = input.nextLine();
        System.out.println("Introdueix el preu del producte: ");
        preu = Float.parseFloat(input.nextLine());
        System.out.println("Introdueix el codi de barres del producte: ");
        codiDeBarres = input.nextLine();
        System.out.println("Introdueix la data de caducitat del producte: ");
        dataCaducitat = input.nextLine();
        llistaAlimentacio.add(new Alimentacio(nom, preu, codiDeBarres, dataCaducitat));
        System.out.println("Producte "+nom+ " afegit correctament");
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Màtode per afegir un producte de tèxtil a la llista de productes de tèxtil
    public void introduirProducteTextil(Textil textil) {
        llistaTextil.add(textil);
        System.out.println("Producte "+textil.getNom()+ " afegit correctament");
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Màtode per afegir un producte de electrònica a la llista de productes de electrònica
    public void introduirProducteElectronica(Electronica electronica) {
        llistaElectronica.add(electronica);
        System.out.println("Producte "+electronica.getNom()+ " afegit correctament");
    }
    //-----------------------------------------------------------------------------

}


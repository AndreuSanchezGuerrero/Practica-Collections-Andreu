import java.util.ArrayList;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class CarroCompra {
    // Dades globals
    // Llista de productes per calcular el preu
    private ArrayList<Producte> llistaProductes;

    // Diccionari per veure els productes del carro
    private HashMap<String, Integer> mapProductes;

    // Llistes de productes de cada categoria
    private ArrayList<Alimentacio> llistaAlimentacio;
    private ArrayList<Textil> llistaTextil;
    private ArrayList<Electronica> llistaElectronica;
    Scanner input = new Scanner(System.in);

    //Diccionari que farem servir per crear un codi de barres aleatori
    private static HashMap<String, String> nomYCodigsProductes;
    private static Random random;

    //-----------------------------------------------------------------------------
    // Constructor
    public CarroCompra() {
        nomYCodigsProductes= new HashMap<>();
        random = new Random();
        mapProductes = new HashMap<>();
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
    public void afegirProducte() {

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
    public void crearProducteAlimentacio() {
        //
        String nom;
        float preu;
        String codiDeBarres;
        String dataCaducitat;
        //
        System.out.println("Introdueix el nom del producte: ");
        nom = input.nextLine();
        System.out.println("Introdueix el preu del producte: ");
        preu = input.nextFloat();
        if (nomYCodigsProductes.containsKey(nom)) {
            codiDeBarres = nomYCodigsProductes.get(nom);
        } else {
            codiDeBarres= generarCodiDeBarres(nom);
            nomYCodigsProductes.put(nom, codiDeBarres);
        }
        System.out.println("Introdueix la data de caducitat del producte: ");
        dataCaducitat = input.nextLine();
        //
        Alimentacio producte = new Alimentacio(nom, preu, codiDeBarres, dataCaducitat);
        llistaAlimentacio.add(producte);

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

    //-------------------------------------------------------------------------------
    // Generem el codi de barres de forma aleatoria
    public String generarCodiDeBarres(String nom) {
        int numeroAleatori = random.nextInt(1000);
        String codiDeBarresFinal = nom + "-" + String.format("%03d", numeroAleatori);
        return codiDeBarresFinal;
    }
    //-------------------------------------------------------------------------------

}


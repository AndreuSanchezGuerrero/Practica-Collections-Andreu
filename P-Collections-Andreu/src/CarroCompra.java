import java.util.ArrayList;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarroCompra {
    Scanner input = new Scanner(System.in);
    // Dades globals
    // Llista de productes per calcular el preu
    private ArrayList<Producte> llistaProductes;

    // Diccionari per veure els productes del carro
    private HashMap<String, Integer> mapProductes;

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
    // Metode per mostrar el carret de la compra
    public void mostrarProductesCarret() {
        for(Producte producte:llistaProductes) {
            System.out.println("Producte: " + producte);
        }
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte a la llista de productes generals
    public void afegirProducteGeneral(Alimentacio producte) {
        // Afegim el producte a la llista de productes generals per calcular el preu
        llistaProductes.add(producte);


        // Afegim el producte al diccionari de productes per veure els productes del carro i quants tenim
        if (mapProductes.containsKey(producte.getCODI_DE_BARRES())) {
            // Si el producte ja existeix, augmentar la quantitat +1.
            int quantitat = mapProductes.get(producte.getCODI_DE_BARRES());
            mapProductes.put(producte.getCODI_DE_BARRES(), quantitat + 1);
        } else {
            // Si el producte no existeix, afegir-lo amb una quantitat de 1
            mapProductes.put(producte.getCODI_DE_BARRES(), 1);
        }
        System.out.println("S'ha afegit " + producte.getNom() + " amb codi de barres: " + producte.getCODI_DE_BARRES() + '\n');
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
        while (!comprovarDataCaducitat(dataCaducitat)) {
            System.out.println();
            System.out.println("Data incorrecta, torna-ho a provar.");
            System.out.println("Introdueix la data en format (dd-mm-yyyy): ");
            dataCaducitat = input.nextLine();
        }
        //
        Alimentacio producte = new Alimentacio(nom, preu, codiDeBarres, dataCaducitat);
        afegirProducteGeneral(producte);
        System.out.println("Producte "+nom+ " afegit correctament");
    }
    //-----------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // Generem el codi de barres de forma aleatoria
    public String generarCodiDeBarres(String nom) {
        int numeroAleatori = random.nextInt(100,999);
        String codiDeBarresFinal = nom + "-" + String.valueOf(numeroAleatori);
        return codiDeBarresFinal;
    }
    //-------------------------------------------------------------------------------

    //------------------------------------------------------------------------
    // Expressió regular per validar la data
    private boolean comprovarDataCaducitat(String data) {
        Pattern patron = Pattern.compile("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(202[5-9]|20[3-9][0-9])$");
        Matcher mat = patron.matcher(data);
        return mat.matches();
    }
    //------------------------------------------------------------------------

}


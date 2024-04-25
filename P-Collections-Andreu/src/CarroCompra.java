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
    protected ArrayList<Producte> llistaProductes;

    // Diccionari per veure els productes del carro
    private HashMap<String, Integer> mapProductes;

    //Diccionari que farem servir per crear un codi de barres aleatori
    private static HashMap<String, String> nomYCodigsProductes;
    private static Random random;

    // Variable constant que limita el nombre de productes
    static int LIMIT_PRODUCTES = 100;

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
    // Metode per mostrar el carret de la compra
    public void mostrarProductesCarret() {
        for(Producte producte:llistaProductes) {
            System.out.println("Producte: " + producte);
        }
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte a la llista de productes generals
    public void afegirProducte(Producte producte) {
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
    public void escollirProducte() throws ExcepcionsPropies.DataCaducitatException, ExcepcionsPropies.negatiuException {
        System.out.println();
        System.out.println("---Afegir producte---");
        //
        int categoria;
        String nom;
        float preu;
        String codiDeBarres;


        // Demanem dades del producte
        System.out.println("Introdueix la categoria del producte:");
        System.out.println("(1) Alimentacio");
        System.out.println("(2) Electronica");
        System.out.println("(3) Textil");
        categoria = input.nextInt();

        // Comprovem que l'entrada sigui entre 1 i 3, si no tornem a demanar
        while (categoria < 1 || categoria > 3) {
            System.out.println("Entrada no valida. S'esperava un enter entre 1 i 3, torna a provar.");
            categoria = input.nextInt();
        }

        System.out.println("Introdueix el nom del producte: ");
        nom = input.next();

        System.out.println("Introdueix el preu del producte: ");
        preu = input.nextFloat();

        // Comprovem si el nom ja ha sortit previament.
        // Si no ha sortit generem un codi de barres.
        // Si ja ha sortit, agafim el codi de barres ja generat previament per aquest nom.
        if (nomYCodigsProductes.containsKey(nom)) {
            codiDeBarres = nomYCodigsProductes.get(nom);
        } else {
            codiDeBarres= generarCodiDeBarres(nom);
            nomYCodigsProductes.put(nom, codiDeBarres);
        }

        Producte producte;
        if (categoria == 1) { // Categoria 1 = Alimentacio
            String dataCaducitat;
            // Demanem la data de caducitat
            System.out.println("Introdueix la data de caducitat del producte: ");
            dataCaducitat = input.nextLine();

            // Comprovem la data de caducitat i si no es correcte llançem una excepció
            if (!comprovarDataCaducitat(dataCaducitat)) {
                throw new ExcepcionsPropies.DataCaducitatException("Data incorrecta, torna-ho a provar.");
            }

            // Creem el producte
            producte = new Alimentacio(nom, preu, codiDeBarres, dataCaducitat);
        }

        else if (categoria == 2) { // Categoria 2 = Electronica
            int diesGarantia;
            // Demanem els dies de garantia
            System.out.println("Introdueix els dies de garantia del producte: ");
            diesGarantia = input.nextInt();

            // Comprovem que dies de garantia sigui un enter positiu
            if (diesGarantia < 0) {
                throw new ExcepcionsPropies.negatiuException("Entrada no valida. La garantia ha de ser un enter positiu.");
            }

            // Creem el producte
            producte = new Electronica(nom, preu, codiDeBarres, diesGarantia);
        }

        else { // Categoria 3 = Textil, ja haviem comprovat que sigui entre 1 i 3, per descart queda textil
            String composicioTextil;
            // Demanem la composicio del textil
            System.out.println("Introdueix la composició del textil: ");
            composicioTextil = input.nextLine();
            // Creem el producte
            producte = new Textil(nom, preu, codiDeBarres, Textil.enumCompositioTextil.valueOf(composicioTextil));
        }

        afegirProducte(producte);
        System.out.println("Producte "+nom+" amb codi de barres "+'\''+codiDeBarres+'\''+" afegit correctament");
    }
    //-----------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    // Generem el codi de barres de forma aleatoria
    public String generarCodiDeBarres(String nom) {
        // Guardem en aquesta variable un numero aleatori entre 100 i 999 perquè aixi tenim un codi de barres de 3 digits
        int numeroAleatori = random.nextInt(100,999);

        // Concatenem el nom del producte amb el numero aleatori
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


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalTime;
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

    // Diccionari que ferem servir per veure si tenim més de dos productes textils amb el mateix codi de barres
    private HashMap<String, Integer> mapTextilsDuplicats = new HashMap<>();

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
        // Mostrar el carret de la compra
        estilMostrarProducte();
        mapProductes.forEach((k,v) -> System.out.println(String.format("%-20s%20d", buscaProducte(k), v)));
    }

    private static void estilMostrarProducte() {
        System.out.println();
        String guions = "-".repeat(40);
        System.out.println(guions);
        System.out.println("Carret de la compra");
        System.out.println(guions);
        System.out.println(String.format("%-20s%20s", "Producte", "Quantitat"));
        System.out.println(guions);
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte a la llista de productes generals
    public void afegirProducte(Producte producte) {
        try {
            if (mapTextilsDuplicats.containsKey(producte.getCODI_DE_BARRES())) {
                // Si el producte ja existeix, augmentar la quantitat +1.
                throw new ExcepcionsPropies.LimitProductesException("No es pot afegir 2 textils amb el mateix codi de barres.");
            } else {
                // Si el producte no existeix, afegir-lo amb una quantitat de 1
                mapTextilsDuplicats.put(producte.getCODI_DE_BARRES(), 1);
            }
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

        } catch (ExcepcionsPropies.LimitProductesException e) {
            System.out.println(e.getMessage());
        }
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte de alimentació a la llista de productes de alimentació
    public void escollirProducte() {
        try{
            System.out.println();

            // Guardem la mida de la String text per que quedi nivellat amb els guions
            String text = ("---Afegir producte---");
            String guions = "";
            for (String guion:text.split("")) {
                guions += "-";
            }
            System.out.println(guions);
            System.out.println(text);
            System.out.println(guions);
            //
            int categoria;
            String nom;
            float preu;
            String codiDeBarres;
            // Demanem dades del producte
            System.out.println("Introdueix la categoria del producte:");
            System.out.println("    (1) Alimentacio");
            System.out.println("    (2) Electronica");
            System.out.println("    (3) Textil");
            // Comprovem que l'entrada sigui un enter, si no es així que llenci una excepció
            if (!input.hasNextInt()) {
                throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
            }
            categoria = input.nextInt();

            // Comprovem que l'entrada sigui entre 1 i 3, si no tornem a demanar
            while (categoria < 1 || categoria > 3) {
                System.out.println("Entrada no valida. S'esperava un enter entre 1 i 3, torna a provar.");
                // Comprovem que l'entrada sigui un enter, si no es així que llenci una excepció
                if (!input.hasNextInt()) {
                    throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
                }
                categoria = input.nextInt();
            }

            System.out.println();
            System.out.println("Introdueix el nom del producte: ");
            nom = input.next();

            // Demanem el preu i comprovem que l'entrada sigui un float, si no llencem una excepció
            System.out.println("Introdueix el preu del producte: ");
            if (!input.hasNextFloat()) {
                throw new InputMismatchException("Entrada no valida. S'esperava un float.");
            }
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

            // Categoria 1 = Alimentacio
            Producte producte;
            if (categoria == 1) {
                String dataCaducitat;
                // Demanem la data de caducitat
                System.out.println("Introdueix la data de caducitat del producte: (dd-MM-aaaa)");
                dataCaducitat = input.next();

                // Comprovem la data de caducitat i si no es correcte llançem una excepció
                if (!comprovarDataCaducitat(dataCaducitat)) {
                    throw new ExcepcionsPropies.DataCaducitatException("Data incorrecta. S'esperava una data de caducitat amb el format dd-MM-aaaa.");
                }

                // Comprovem que la data de caducitat sigui més tard que la data actual
                if (dataCaducitatEsMenorQueDataActual(dataCaducitat)) {
                    throw new ExcepcionsPropies.DataCaducitatException("Data incorrecta. La data de caducitat ha de ser posterior a la data actual.");
                }

                // Creem el producte
                producte = new Alimentacio(nom, preu, codiDeBarres, dataCaducitat);
            }

            // Categoria 2 = Electronica
            else if (categoria == 2) {
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

            // Categoria 3 = Textil, ja haviem comprovat que sigui entre 1 i 3, per descart queda textil
            else {
                String composicioTextil;
                // Demanem la composicio del textil
                System.out.println("Introdueix la composició del textil: ");
                composicioTextil = input.next().toUpperCase();
                // Creem el producte
                producte = new Textil(nom, preu, codiDeBarres, Textil.enumCompositioTextil.valueOf(composicioTextil));
            }

            afegirProducte(producte);
            System.out.println("Producte "+nom+" amb codi de barres "+'\''+codiDeBarres+'\''+" afegit correctament");

        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            System.out.println("El producte no s'ha afegit. Torna a provar");
        } catch (ExcepcionsPropies.DataCaducitatException e) {
            System.out.println(e.getMessage());
            System.out.println("El producte no s'ha afegit. Torna a provar");
        } catch (ExcepcionsPropies.negatiuException e) {
            System.out.println(e.getMessage());
            System.out.println("El producte no s'ha afegit. Torna a provar");
        }
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
        Pattern patron = Pattern.compile("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(202[4-9]|20[3-9][0-9])$");
        Matcher mat = patron.matcher(data);
        return mat.matches();
    }
    //------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    private boolean dataCaducitatEsMenorQueDataActual(String dataCaducitat) {
        // Convertim la data de caducitat en LocalDate
        LocalDate dataParsejada = LocalDate.parse(dataCaducitat, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // Data actual
        LocalDate dataActual = LocalDate.now();
        return dataParsejada.isBefore(dataActual);
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    private String buscaProducte(String codiProducte) {
        Optional <Producte> producte = llistaProductes.stream().filter(p -> p.getCODI_DE_BARRES().equals(codiProducte)).findFirst();
        return producte.get().getNom();
    }
    //-----------------------------------------------------------------------------
}


import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarroCompra {
    Scanner input = new Scanner(System.in);

    // Llista de productes
    protected static ArrayList<Producte> llistaProductes;

    // Llistes de productes en la que no farem cas a la quantitat i afegirem sempre el producte com a independent,
    // així podem veure la mesura de la llista i si ja té 100 productes afegits. De no fer aquesta copia, no calcularà bé si
    // tenim 100 productes
    protected static ArrayList<Producte> llistaProductesCopia;

    //Diccionari que farem servir per crear un codi de barres aleatori
    protected static HashMap<String, String> nomYCodigsProductes;
    private static Random random;

    // Diccionari que ferem servir per veure si tenim més de dos productes textils amb el mateix codi de barres
    private HashMap<String, Integer> mapTextilsDuplicats;

    // Diccionari que ferem servir per veure si tenim més de dos productes de la mateixa marca amb el mateix codi de barres
    protected static HashMap<String, Integer> mapProductesJaIntroduits;

    // Variable constant que limita el nombre de productes
    static int LIMIT_PRODUCTES = 100;

    //-----------------------------------------------------------------------------
    // Constructor
    public CarroCompra() {
        nomYCodigsProductes= new HashMap<>();
        random = new Random();
        mapTextilsDuplicats = new HashMap<>();
        mapProductesJaIntroduits = new HashMap<>();
        llistaProductes = new ArrayList<Producte>();
        llistaProductesCopia = new ArrayList<Producte>();
    }
    //-----------------------------------------------------------------------------

//Bloc 1: Metodes menu1 i saludar
//-----------------------------------------------------------------------------------------------------------------------------------------------
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
        System.out.println("(4) Omplir carret de productes random");
        System.out.println("(5) Mostar preu total actualment");
        System.out.println("(0) Acabar");
        System.out.println();
        System.out.print("Escull una opció: ");
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
//-------------------------------------------------------------------------------------------------------------------------------------------------


//Bloc 2: Metode generarCodiDeBarres
//-------------------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------
    // Generem el codi de barres de forma aleatoria
    public static String generarCodiDeBarres(String nom) {
        // Guardem en aquesta variable un numero aleatori entre 100 i 999 perquè aixi tenim un codi de barres de 3 digits
        int numeroAleatori = random.nextInt(100,999);

        // Concatenem el nom del producte amb el numero aleatori
        String codiDeBarresFinal = nom + "-" + String.valueOf(numeroAleatori);
        return codiDeBarresFinal;
    }
    //-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------

//Bloc 3: Metodes de comprovacions
//---------------------------------------------------------------------------------------------------------------------------------------------------------------
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
    public static void comprovarPreuTextil(Producte producte) {
        try {
            String seguentLinia;
            String codiBarres;

            // Localització del fitxer updates. Aquest fixer no es pot eliminar ja que s'utilitzarà per actualitzar els preus
            String path = "./updates/UpdatesTextilPrices.dat";
            Scanner arxiuPreusTextilActualitzats = new Scanner(new File(path));

            // Mentres l'arxiu tingui linias entrarem al bucle.
            while (arxiuPreusTextilActualitzats.hasNextLine()) {

                // Obtenim la linia i la dividim entre el codi de barres i el preu
                seguentLinia = arxiuPreusTextilActualitzats.nextLine();
                codiBarres = seguentLinia.split(" ")[0];
                String preuCorrecte = seguentLinia.split(" ")[1];

                // Si el codi de barres coincideix amb el del producte, actualitzem el preu
                if (codiBarres.equals(producte.getCODI_DE_BARRES())) {
                    producte.setPreu(Float.parseFloat(preuCorrecte));
                    arxiuPreusTextilActualitzats.close();
                    System.out.println(); System.out.println("Atenció. S'ha actualitzat el preu del producte: " + producte.getNom());
                    break;
                }
            }



        } catch (FileNotFoundException e) {
            System.out.println(); System.out.println("L'arxiu de preus de textil no s'ha trobat");
        }
    }
    //-----------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------

//Bloc 4: Metode escollirProducte i afegirProducte
//-------------------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte de alimentació a la llista de productes de alimentació
    public void escollirProducte() {
        try{
            // Comprovem que la llista de productes no estigui plena
            if (llistaProductesCopia.size() >= CarroCompra.LIMIT_PRODUCTES) {
                throw new ExcepcionsPropies.LimitProductesException("La llista ja te 100 productes");
            }
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

            // Declaracio de variables
            int categoria;
            String nom;
            float preu;
            String codiDeBarres;

            // Fem un menú petit per escollir la categoria
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

            // Convertim la primera lletra en majúscula i la resta en minúscula, per si l'usuari no ha posat
            // la primera en majuscula i/o la resta en minúscula.
            nom = nom.substring(0, 1).toUpperCase() + nom.substring(1).toLowerCase();

            // Comprovem que el nom no supera els 15 caràcters
            if (nom.length() > 15) {
                throw new ExcepcionsPropies.LimitCaracteresException("El nom del producte no pot superar els 15 caràcters");
            }

            // Demanem el preu i comprovem que l'entrada sigui un float, si no llencem una excepció
            System.out.println("Introdueix el preu del producte: ");
            if (!input.hasNextFloat()) {
                throw new InputMismatchException("Entrada no valida. S'esperava un float.");
            }
            preu = input.nextFloat();
            // Comprovem que el preu no sigui negatiu
            if (preu < 0) {
                throw new ExcepcionsPropies.negatiuException("El preu no pot ser negatiu");
            }

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

            // Categoria 1 = Alimentacio
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
                if (!input.hasNextInt()) {
                    throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
                }
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
                // Fem una arraylist momentanea per introduir textils
                ArrayList<String> llistaEnumCompositioTextil;
                llistaEnumCompositioTextil = new ArrayList<>();
                llistaEnumCompositioTextil.addAll(Arrays.asList("COTO", "POLIESTER", "LLI", "SEDA", "LLANA", "NILO"));
                String composicioTextil;

                // Demanem la composicio del textil
                System.out.println("Introdueix la composició del textil: ");

                // Convertim a majúscula per que coincideixi amb el enum
                composicioTextil = input.next().toUpperCase();

                // Comprovem que la composicio sigui correcta
                if (!llistaEnumCompositioTextil.contains((composicioTextil))) {
                    throw new ExcepcionsPropies.enumFailException("Composicio no valida. S'esperava una de les seguents composicions: COTO, POLIESTER, LLI, SEDA, LLANA o NILO.");
                }

                // Creem el producte
                producte = new Textil(nom, preu, codiDeBarres, Textil.enumCompositioTextil.valueOf(composicioTextil));
            }

            afegirProducte(producte);
            System.out.println();
            System.out.println("Producte "+nom+" de tipus "+producte.getClass().getSimpleName()+" amb codi de barres "+'\''+codiDeBarres+'\''+" afegit correctament");
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            escriureLog(e.getMessage());
        } catch (ExcepcionsPropies.DataCaducitatException e) {
            System.out.println(e.getMessage());
            escriureLog(e.getMessage());
        } catch (ExcepcionsPropies.negatiuException e) {
            System.out.println(e.getMessage());
            escriureLog(e.getMessage());
        } catch (ExcepcionsPropies.LimitProductesException e) {
            System.out.println(e.getMessage());
            escriureLog(e.getMessage());
        } catch (ExcepcionsPropies.LimitCaracteresException e) {
            System.out.println(e.getMessage());
            escriureLog(e.getMessage());
        } catch (ExcepcionsPropies.enumFailException e) {
            System.out.println(e.getMessage());
            escriureLog(e.getMessage());
        }
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte a la llista de productes generals
    public void afegirProducte(Producte producte) {
        try {
            // Controlem que no hi hagin textils amb el mateix codi de barres
            if (producte instanceof Textil) {
                if (mapTextilsDuplicats.containsKey(producte.getCODI_DE_BARRES())) {
                    // Si el producte ja existeix, augmentar la quantitat +1.
                    throw new ExcepcionsPropies.LimitProductesException("No es pot afegir 2 textils amb el mateix codi de barres.");
                } else {
                    // Si el producte no existeix, afegir-lo amb una quantitat de 1
                    mapTextilsDuplicats.put(producte.getCODI_DE_BARRES(), 1);
                }
            }

            // Si ja hi ha un producte amb el mateix codi de barres, augmentar la quantitat +1
            if (mapProductesJaIntroduits.containsKey(producte.getCODI_DE_BARRES())) {
                for (Producte producte2 : llistaProductes) {
                    if (producte2.getCODI_DE_BARRES().equals(producte.getCODI_DE_BARRES())) {
                        producte2.setQuantitat(producte2.getQuantitat() + 1);
                        break;
                    }
                }
            }
            else {
                llistaProductes.add(producte);
                mapProductesJaIntroduits.put(producte.getCODI_DE_BARRES(), 1);
            }

            // Afegim el producte a la llista de productes copia.
            llistaProductesCopia.add(producte);

        } catch (ExcepcionsPropies.LimitProductesException e) {
            escriureLog(e.getMessage());
        }
    }
    //-----------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------------------------------------

//Bloc 5: Metodes mostrarProductesCarret i estilMostrarProducte
//-------------------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    // Metode per mostrar el carret de la compra
    public void mostrarProductesCarret() {
        // Mostrar el carret de la compra
        estilMostrarProducte();

        // Mostrar el carret de la compra amb lambda expresion.
        // String format igual que a estilMostrarProducte().
        // String.format("%-20s%20s", "Producte", "Quantitat"). Lo que fem es afegir 20 espais a la esquerra i 20 espais a la dreta
        // D'aquesta forma fem que quedi perfecte amb els guions. Igual que a un ticket de compra.

        // Ordenar llistaProductes per nom
        Collections.sort(llistaProductes, compareNom);

        // Variables per centinela
        AtomicBoolean centinela1 = new AtomicBoolean(true);
        AtomicBoolean centinela2 = new AtomicBoolean(true);
        AtomicBoolean centinela3 = new AtomicBoolean(true);

        // Mostrar el carret de la compra amb lambda expresion.
        // Mostrar productes de alimentacio
        llistaProductes.forEach((producteA) -> {
            // Comprovem si es de alimentacio.
            if (producteA instanceof Alimentacio) {
                        // Sempre entrarà el primer cop, aixi mostrem la classe primer, després no volem tornar a mostrarla
                        if (centinela1.get()) {
                            System.out.println(String.format("%-20s", producteA.getClass().getSimpleName()));
                            // Mostrar el nom del producte i la quantitat amb espais i una estetica maca.
                            System.out.println(String.format("%-20s%-20s%9d", "", producteA.getNom(), producteA.getQuantitat()));
                            centinela1.set(false);
                        } else {
                            System.out.println(String.format("%-20s%-20s%9d", "", producteA.getNom(), producteA.getQuantitat()));
                        }
                    }
        });

        // Mostrar productes de electronica
        llistaProductes.forEach((producteE) -> {
            if (producteE instanceof Electronica) {
                if (centinela2.get()) {
                    // Sempre entrarà el primer cop, aixi mostrem la classe primer, després no volem tornar a mostrarla
                    System.out.println(String.format("%-20s", producteE.getClass().getSimpleName()));
                    // Mostrar el nom del producte i la quantitat amb espais i una estetica maca.
                    System.out.println(String.format("%-20s%-20s%9d", "", producteE.getNom(), producteE.getQuantitat()));
                    centinela2.set(false);
                } else {
                    System.out.println(String.format("%-20s%-20s%9d", "", producteE.getNom(), producteE.getQuantitat()));
                }
            }
        });

        // Mostrar productes de Textil
        llistaProductes.forEach((producteT) -> {
            if (producteT instanceof Textil) {
                // Sempre entrarà el primer cop, aixi mostrem la classe primer, després no volem tornar a mostrarla
                if (centinela3.get()) {
                    System.out.println(String.format("%-20s", producteT.getClass().getSimpleName()));
                    // Mostrar el nom del producte i la quantitat amb espais i una estetica maca.
                    System.out.println(String.format("%-20s%-20s%9d", "", producteT.getNom(), producteT.getQuantitat()));
                    centinela3.set(false);
                } else {
                    System.out.println(String.format("%-20s%-20s%9d", "", producteT.getNom(), producteT.getQuantitat()));
                }
            }
        });
    }

    private static void estilMostrarProducte() {
        System.out.println();
        String guions = "-".repeat(49);
        System.out.println(guions);
        System.out.println("Carret de la compra");
        System.out.println(guions);
        System.out.println(String.format("%-20s%-20s%-9s", "Categoria", "Producte", "Quantitat"));
        System.out.println(guions);
    }
    //-----------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------




// Bloc 6: Metode generarTicketDeCompra, el estil i calcul del preu total.
//-------------------------------------------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------
    public void generarTicketDeCompra() throws IOException, FileNotFoundException {
        boolean copiaTicket;
        copiaTicket = false;

        // Preguntar si vol copia del ticket
        System.out.println("Voldrà copia? S/N");
        Scanner input = new Scanner(System.in);
        String copia = input.nextLine();
        String arxiuNom;

        // En cas de voler copia, creem un arxiu amb la ruta del path.
        if (copia.toUpperCase().equals("S")) {
            File file = new File("./copies/copiaTicket.txt");
            // Si el arxiu existeix, l'esborrem perque si no es reescriu
            if (file.exists()) {
                file.delete();
            }
            copiaTicket=true;
            String guions = "-".repeat(68);
            // Imprimim l'estetica del ticket
            imprimirCopia(guions);
            imprimirCopia("Ticket de compra");
            imprimirCopia(guions);
            imprimirCopia(String.format("%-20s%-25s%-10s%-5s%-8s", "Categoria", "Producte", "P. Unitari", "", "P. Total"));
            imprimirCopia(guions);

        } else {
            System.out.println("No s'ha copiat");
            copiaTicket = false;
        }

        estilTicket();
        // Mostrar el carret de la compra amb lambda expresion.
        // String format igual que a estilMostrarProducte().
        // String.format("%-20s%20s", "Producte", "Quantitat"). Lo que fem es afegir 20 espais a la esquerra i 20 espais a la dreta
        // D'aquesta forma fem que quedi perfecte amb els guions. Igual que a un ticket de compra.

        // Fem un format de 2 decimals
        DecimalFormat df = new DecimalFormat("0.00");

        // Ordenem per nom
        Collections.sort(llistaProductes, compareNom);

        // Fels centineles atomics per treballar amb lamda igual que he fet abans
        AtomicBoolean centinela1 = new AtomicBoolean(true);
        AtomicBoolean centinela2 = new AtomicBoolean(true);
        AtomicBoolean centinela3 = new AtomicBoolean(true);

        // Fem un integer atomic i un float atomic per treballar amb lamda.
        // El enter sera el numero de producte que volem mostrar
        AtomicInteger i = new AtomicInteger(1);
        // El float sera el preu total
        AtomicReference<Float> preuTotal = new AtomicReference<>((float) 0);

        // No entenc perquè però m'obligaba a fer aquest boolean, si es vol copia serà true i si no serà false
        boolean finalCopiaTicket = copiaTicket;

        // Mostrar el carret de la compra amb lambda expresion.
        // Mostrem els productes de Alimentacio
        llistaProductes.forEach((producteA) -> {
            if (producteA instanceof Alimentacio) {
                // Si tenim true a copiaTicket farem lo seguent
                if (finalCopiaTicket) {
                    // Sempre entrarà el primer cop perque està true, mostrem la classe, després false per les altres.
                    if (centinela1.get()) {
                        // Mostrarem la classe i la copiem a l'arxiu
                        String text = String.format("%-20s", producteA.getClass().getSimpleName());
                        System.out.println(text);
                        imprimirCopia(text);

                        // Mostrem el número de producte, el seu nom, la seva quantitat i el preu.
                        text = String.format("%-20s%-5s%-20s%10.02f%-5s%8.2f","",
                                i.getAndIncrement(),
                                producteA.getNom()+" x "+producteA.getQuantitat(),
                                producteA.calcularPreu(),
                                "",
                                calcularPreuTotal(producteA, producteA.getQuantitat()));

                        // Mostrem i imprimim el text a l'arxiu
                        System.out.println(text);
                        imprimirCopia(text);
                        centinela1.set(false);
                    }

                    // Després farem sempre el mateix, pero sense mostrar la classe
                    else {
                        String text = String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                                "",
                                i.getAndIncrement(),
                                producteA.getNom()+" x "+producteA.getQuantitat(),
                                producteA.calcularPreu(),
                                "",
                                calcularPreuTotal(producteA, producteA.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                    }
                }

                // En el cas de no tenir true a copiaticket, fem el mateix, pero sense imprimir copia.
                // Primer mostrnant la classe, numero de producte, el seu nom, la seva quantitat i el preu.
                else if (centinela1.get()) {
                    System.out.println(String.format("%-20s", producteA.getClass().getSimpleName()));
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteA.getNom()+" x "+producteA.getQuantitat(),
                            producteA.calcularPreu(),
                            "",
                            calcularPreuTotal(producteA, producteA.getQuantitat())));
                    centinela1.set(false);
                }
                // Després mostrem el mateix, pero sense la clase
                else {
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteA.getNom()+" x "+producteA.getQuantitat(),
                            producteA.calcularPreu(),
                            "",
                            calcularPreuTotal(producteA, producteA.getQuantitat())));
                }

                // Actualitzem la variable preuTotal, sumant-li el preu del producte
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producteA, producteA.getQuantitat()))));
            }
        });

        // Tornem a inicilitzar la variable
        i.set(1);
        // Mostrem els productes de Electronics amb la mateixa metodologia que a l'anterior
        llistaProductes.forEach((producteE) -> {
            if (producteE instanceof Electronica) {
                if (finalCopiaTicket) {
                    if (centinela2.get()) {
                        String text = String.format("%-20s", producteE.getClass().getSimpleName());
                        System.out.println(text);
                        imprimirCopia(text);

                        text = String.format("%-20s%-5s%-20s%10s%-5s%8s","",
                                i.getAndIncrement(),
                                producteE.getNom()+" x "+producteE.getQuantitat(),
                                producteE.calcularPreu(),
                                "",
                                calcularPreuTotal(producteE, producteE.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                        centinela2.set(false);
                    } else {
                        String text = String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                                "",
                                i.getAndIncrement(),
                                producteE.getNom()+" x "+producteE.getQuantitat(),
                                producteE.calcularPreu(),
                                "",
                                calcularPreuTotal(producteE, producteE.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                    }
                } else if (centinela2.get()) {
                    System.out.println(String.format("%-20s", producteE.getClass().getSimpleName()));
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteE.getNom()+" x "+producteE.getQuantitat(),
                            producteE.calcularPreu(),
                            "",
                            calcularPreuTotal(producteE, producteE.getQuantitat())));
                    centinela2.set(false);
                } else {
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteE.getNom()+" x "+producteE.getQuantitat(),
                            producteE.calcularPreu(),
                            "",
                            calcularPreuTotal(producteE, producteE.getQuantitat())));
                }
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producteE, producteE.getQuantitat()))));
            }
        });

        // Tornem a inicilitzar la variable
        i.set(1);
        // Mostrem els productes de Textil amb la mateixa metodologia que a l'anterior
        llistaProductes.forEach((producteT) -> {
            if (producteT instanceof Textil) {
                if (finalCopiaTicket) {
                    if (centinela3.get()) {
                        String text = String.format("%-20s", producteT.getClass().getSimpleName());
                        System.out.println(text);
                        imprimirCopia(text);

                        text = String.format("%-20s%-5s%-20s%10s%-5s%8s","",
                                i.getAndIncrement(),
                                producteT.getNom()+" x "+producteT.getQuantitat(),
                                producteT.calcularPreu(),
                                "",
                                calcularPreuTotal(producteT, producteT.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                        centinela3.set(false);
                    } else {
                        String text = String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                                "",
                                i.getAndIncrement(),
                                producteT.getNom()+" x "+producteT.getQuantitat(),
                                producteT.calcularPreu(),
                                "",
                                calcularPreuTotal(producteT, producteT.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                    }
                }
                else if (centinela3.get()) {
                    System.out.println(String.format("%-20s", producteT.getClass().getSimpleName()));
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteT.getNom()+" x "+producteT.getQuantitat(),
                            producteT.calcularPreu(),
                            "",
                            calcularPreuTotal(producteT, producteT.getQuantitat())));
                    centinela3.set(false);
                } else {
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteT.getNom()+" x "+producteT.getQuantitat(),
                            producteT.calcularPreu(),
                            "",
                            calcularPreuTotal(producteT, producteT.getQuantitat())));
                }
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producteT, producteT.getQuantitat()))));
            }
        });

        // Fem 68 guions que son els espais totals del String format, així queda tot nivellat.
        String guions = "-".repeat(68);

        // En cas de voler copia imprimim els guions i el preu total. També el mostem per consola.
        if (finalCopiaTicket) {
            imprimirCopia(guions);
            imprimirCopia(String.format("%68s","Total: "+df.format(preuTotal.get())));
            System.out.println(guions);
            System.out.println(String.format("%68s","Total: "+df.format(preuTotal.get())));
            System.out.println();
        }
        // En cas de no voler copia només mostem els guions i el preu total.
        else {
            System.out.println(guions);
            System.out.println(String.format("%68s","Total: "+df.format(preuTotal.get())));
            System.out.println();
        }
    }

    // ----------------------------------------------------------------------------------
    public void estilTicket() {
        System.out.println();
        String guions = "-".repeat(68);
        System.out.println(guions);
        System.out.println("Ticket de compra");
        System.out.println(guions);
        System.out.println(String.format("%-20s%-25s%-10s%-5s%-8s", "Categoria", "Producte", "P. Unitari", "", "P. Total"));
        System.out.println(guions);
    }
    // ----------------------------------------------------------------------------------

    //-------------------------------------------------------------------------------
    public float calcularPreuTotal(Producte producte, int quantitat) {
        float preuTotal = producte.calcularPreu() * quantitat;
        return preuTotal;
    }
    //-------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------


// Bloc 7: Metode per mostrar el preu total, imprimir ticket i escriure els logs.
//-------------------------------------------------------------------------------------------------------------------------------------------------
    // Mètode per mostrar el preu total
    // -------------------------------------------------------------------------------------------------
    // Fem recorregut dels productes per mostrar el preu total
    public void mostrarPreuTotal() {
        DecimalFormat df = new DecimalFormat("0.00");
        AtomicReference<Float> preuTotal = new AtomicReference<>((float) 0);
        llistaProductes.forEach((producte) -> {
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producte, producte.getQuantitat()))));
        });
        System.out.println("Actualment tens en el carret: " + df.format(preuTotal.get())+"€");
    }
    //------------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    public static void escriureLog(String missatge) {
        String directoriNom = "logs";
        String arxiu = "mercatOnline.log";

        File directori = new File(directoriNom);
        // Comprovem si el directori no existeix, si no existeix, el creem
        if (!directori.exists()) {
            directori.mkdirs();
        }

        // Obtenir la data i l'hora actual per fer mostrar al log
        LocalDateTime dataIhoraActual = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Formatejar el missatge del log: data i hora i missatge que li hem passat
        String logMessage = "[" + dataIhoraActual.format(format) + "] " + missatge;

        // Escriure en el arxiu de log
        try (PrintWriter writer = new PrintWriter(new FileWriter(directori + File.separator + arxiu, true))) {
            writer.println(logMessage);
            writer.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    // -----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    private void imprimirCopia(String text) {

        String directoriNom = "copies";
        String arxiu = "copiaTicket.txt";

        File directori = new File(directoriNom);
        if (!directori.exists()) {
            directori.mkdirs();
        }

        // Escribim el text al arxiu
        try (PrintWriter writer = new PrintWriter(new FileWriter(directori + File.separator + arxiu, true))) {
            writer.println(text);
            writer.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
    //-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per comparar els productes i ordenar-los segons el nom
    Comparator<Producte> compareNom = new Comparator<Producte>() {
        @Override
        public int compare(Producte producte1, Producte producte2) {
            return producte1.getNom().compareTo(producte2.getNom());
        }
    };
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Buidar carret
    public void buidarCarret() {
        llistaProductes.clear();
    }
    //-----------------------------------------------------------------------------


}


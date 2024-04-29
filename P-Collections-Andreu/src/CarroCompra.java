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



//Bloc 2: Metodes mostrarProductesCarret i estilMostrarProducte
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


        Collections.sort(llistaProductes, compareNom);
        AtomicBoolean centinela1 = new AtomicBoolean(true);
        AtomicBoolean centinela2 = new AtomicBoolean(true);
        AtomicBoolean centinela3 = new AtomicBoolean(true);

        llistaProductes.forEach((producteA) -> {
                    if (producteA instanceof Alimentacio) {
                        if (centinela1.get()) {
                            System.out.println(String.format("%-20s", producteA.getClass().getSimpleName()));
                            System.out.println(String.format("%-20s%-20s%9d", "", producteA.getNom(), producteA.getQuantitat()));
                            centinela1.set(false);
                        } else {
                            System.out.println(String.format("%-20s%-20s%9d", "", producteA.getNom(), producteA.getQuantitat()));
                        }
                    }
        });
        llistaProductes.forEach((producteE) -> {
            if (producteE instanceof Electronica) {
                if (centinela2.get()) {
                    System.out.println(String.format("%-20s", producteE.getClass().getSimpleName()));
                    System.out.println(String.format("%-20s%-20s%9d", "", producteE.getNom(), producteE.getQuantitat()));
                    centinela2.set(false);
                } else {
                    System.out.println(String.format("%-20s%-20s%9d", "", producteE.getNom(), producteE.getQuantitat()));
                }
            }
        });
        llistaProductes.forEach((producteT) -> {
            if (producteT instanceof Textil) {
                if (centinela3.get()) {
                    System.out.println(String.format("%-20s", producteT.getClass().getSimpleName()));
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


//-------------------------------------------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte a la llista de productes generals
    public void afegirProducte(Producte producte) {
        try {
            if (llistaProductesCopia.size() >= CarroCompra.LIMIT_PRODUCTES) {
                throw new ExcepcionsPropies.LimitProductesException("La llista ja te 100 productes");
            }
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



            System.out.println("S'ha afegit " + producte.getNom() + " amb codi de barres: " + producte.getCODI_DE_BARRES() + '\n');
            System.out.println();
            llistaProductesCopia.add(producte);

        } catch (ExcepcionsPropies.LimitProductesException e) {
            escriureLog(e.getMessage());
        }
    }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // Mètode per afegir un producte de alimentació a la llista de productes de alimentació
    public void escollirProducte() {
        try{
            if (llistaProductes.size() >= CarroCompra.LIMIT_PRODUCTES) {
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
            System.out.println("Producte "+nom+" de tipus "+producte.getClass().getSimpleName()+" amb codi de barres "+'\''+codiDeBarres+'\''+" afegit correctament");

        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
            System.out.println("El producte no s'ha afegit. Torna a provar");
        } catch (ExcepcionsPropies.DataCaducitatException e) {
            System.out.println(e.getMessage());
            System.out.println("El producte no s'ha afegit. Torna a provar");
        } catch (ExcepcionsPropies.negatiuException e) {
            System.out.println(e.getMessage());
            System.out.println("El producte no s'ha afegit. Torna a provar");
        } catch (ExcepcionsPropies.LimitProductesException e) {
            System.out.println(e.getMessage());
        }
    }
    //-----------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------
    public void generarTicketDeCompra() throws IOException, FileNotFoundException {
        boolean copiaTicket;
        copiaTicket = false;
        System.out.println("Voldrà copia? S/N");
        Scanner input = new Scanner(System.in);
        String copia = input.nextLine();
        String arxiuNom;

        if (copia.toUpperCase().equals("S")) {
            File file = new File("./copies/copiaTicket.txt");
            if (file.exists()) {
                file.delete();
            }
            copiaTicket=true;
            String guions = "-".repeat(68);
            imprimirCopia(guions);
            imprimirCopia("Ticket de compra");
            imprimirCopia(guions);
            imprimirCopia(String.format("%-20s%-25s%-10s%-5s%-8s", "Categoria", "Producte", "P. Unitari", "", "P. Total"));
            imprimirCopia(guions);

        } else {
            copiaTicket = false;
        }

        estilTicket();
        // Mostrar el carret de la compra amb lambda expresion.
        // String format igual que a estilMostrarProducte().
        // String.format("%-20s%20s", "Producte", "Quantitat"). Lo que fem es afegir 20 espais a la esquerra i 20 espais a la dreta
        // D'aquesta forma fem que quedi perfecte amb els guions. Igual que a un ticket de compra.
        DecimalFormat df = new DecimalFormat("0.00");
        Collections.sort(llistaProductes, compareNom);
        AtomicBoolean centinela1 = new AtomicBoolean(true);
        AtomicBoolean centinela2 = new AtomicBoolean(true);
        AtomicBoolean centinela3 = new AtomicBoolean(true);
        AtomicInteger i = new AtomicInteger(1);
        AtomicReference<Float> preuTotal = new AtomicReference<>((float) 0);
        boolean finalCopiaTicket = copiaTicket;
        llistaProductes.forEach((producteA) -> {
            if (producteA instanceof Alimentacio) {
                if (finalCopiaTicket) {
                    if (centinela1.get()) {
                        String text = String.format("%-20s", producteA.getClass().getSimpleName());
                        System.out.println(text);
                        imprimirCopia(text);

                        text = String.format("%-20s%-5s%-20s%10.02f%-5s%8.2f","",
                                i.getAndIncrement(),
                                producteA.getNom()+"x"+producteA.getQuantitat(),
                                producteA.calcularPreu(),
                                "",
                                calcularPreuTotal(producteA, producteA.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                        centinela1.set(false);
                    } else {
                        String text = String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                                "",
                                i.getAndIncrement(),
                                producteA.getNom()+"x"+producteA.getQuantitat(),
                                producteA.calcularPreu(),
                                "",
                                calcularPreuTotal(producteA, producteA.getQuantitat()));
                        System.out.println(text);
                        imprimirCopia(text);
                    }
                }

                else if (centinela1.get()) {
                    System.out.println(String.format("%-20s", producteA.getClass().getSimpleName()));
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteA.getNom()+"x"+producteA.getQuantitat(),
                            producteA.calcularPreu(),
                            "",
                            calcularPreuTotal(producteA, producteA.getQuantitat())));
                    centinela1.set(false);
                } else {
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteA.getNom()+"x"+producteA.getQuantitat(),
                            producteA.calcularPreu(),
                            "",
                            calcularPreuTotal(producteA, producteA.getQuantitat())));
                }
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producteA, producteA.getQuantitat()))));
            }
        });
        i.set(1);
        llistaProductes.forEach((producteE) -> {
            if (producteE instanceof Electronica) {
                if (finalCopiaTicket) {
                    if (centinela2.get()) {
                        String text = String.format("%-20s", producteE.getClass().getSimpleName());
                        System.out.println(text);
                        imprimirCopia(text);

                        text = String.format("%-20s%-5s%-20s%10s%-5s%8s","",
                                i.getAndIncrement(),
                                producteE.getNom()+"x"+producteE.getQuantitat(),
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
                                producteE.getNom()+"x"+producteE.getQuantitat(),
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
                            producteE.getNom()+"x"+producteE.getQuantitat(),
                            producteE.calcularPreu(),
                            "",
                            calcularPreuTotal(producteE, producteE.getQuantitat())));
                    centinela2.set(false);
                } else {
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteE.getNom()+"x"+producteE.getQuantitat(),
                            producteE.calcularPreu(),
                            "",
                            calcularPreuTotal(producteE, producteE.getQuantitat())));
                }
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producteE, producteE.getQuantitat()))));
            }
        });
        i.set(1);
        llistaProductes.forEach((producteT) -> {
            if (producteT instanceof Textil) {
                if (finalCopiaTicket) {
                    if (centinela3.get()) {
                        String text = String.format("%-20s", producteT.getClass().getSimpleName());
                        System.out.println(text);
                        imprimirCopia(text);

                        text = String.format("%-20s%-5s%-20s%10s%-5s%8s","",
                                i.getAndIncrement(),
                                producteT.getNom()+"x"+producteT.getQuantitat(),
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
                                producteT.getNom()+"x"+producteT.getQuantitat(),
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
                            producteT.getNom()+"x"+producteT.getQuantitat(),
                            producteT.calcularPreu(),
                            "",
                            calcularPreuTotal(producteT, producteT.getQuantitat())));
                    centinela3.set(false);
                } else {
                    System.out.println(String.format("%-20s%-5d%-20s%10.02f%-5s%8.2f",
                            "",
                            i.getAndIncrement(),
                            producteT.getNom()+"x"+producteT.getQuantitat(),
                            producteT.calcularPreu(),
                            "",
                            calcularPreuTotal(producteT, producteT.getQuantitat())));
                }
                preuTotal.updateAndGet(v -> new Float((float) (v + calcularPreuTotal(producteT, producteT.getQuantitat()))));
            }
        });
        String guions = "-".repeat(68);
        if (finalCopiaTicket) {
            imprimirCopia(guions);
            imprimirCopia(String.format("%68s","Total: "+df.format(preuTotal.get())));
            System.out.println(guions);
            System.out.println(String.format("%68s","Total: "+df.format(preuTotal.get())));
            System.out.println();
        }
        else {
            System.out.println(guions);
            System.out.println(String.format("%68s","Total: "+df.format(preuTotal.get())));
            System.out.println();
        }
    }

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
    // Mètode per comparar els productes i ordenar-los segons el nom
    Comparator<Producte> compareNom = new Comparator<Producte>() {
        @Override
        public int compare(Producte producte1, Producte producte2) {
            return producte1.getNom().compareTo(producte2.getNom());
        }
    };
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    public static void comprovarPreuTextil(Producte producte) {
        try {
            String path = "./updates/UpdatesTextilPrices.dat";
            Scanner arxiuPreusTextilActualitzats = new Scanner(new File(path));


            String seguentLinia;
            String codiBarres;
            while (arxiuPreusTextilActualitzats.hasNextLine()) {
                boolean trobat = false;
                seguentLinia = arxiuPreusTextilActualitzats.nextLine();
                codiBarres = seguentLinia.split(" ")[0];
                String preuCorrecte = seguentLinia.split(" ")[1];

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

    //-----------------------------------------------------------------------------
    public static void escriureLog(String missatge) {
        String directoriNom = "logs";
        String arxiu = "mercatOnline.log";

        File directori = new File(directoriNom);
        if (!directori.exists()) {
            directori.mkdirs(); // Crea los directorios necesarios
        }

        // Obtener la fecha y hora actual formateada para el log
        LocalDateTime dataIhoraActual = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Formatear el mensaje de log
        String logMessage = "[" + dataIhoraActual.format(format) + "] " + missatge;

        // Escribir en el archivo de log
        try (PrintWriter writer = new PrintWriter(new FileWriter(directori + File.separator + arxiu, true))) {
            writer.println(logMessage);
            writer.close();
        } catch (IOException e) {
            e.getMessage(); // Manejar el error de escritura
        }

        // Imprimir el mensaje de log en la consola
        System.out.println(logMessage);
    }
    // -----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    private void imprimirCopia(String text) {

        String directoriNom = "copies";
        String arxiu = "copiaTicket.txt";

        File directori = new File(directoriNom);
        if (!directori.exists()) {
            directori.mkdirs(); // Crea los directorios necesarios
        }

        // Formatear el mensaje de log
        String message = text;

        // Escribir en el archivo de log
        try (PrintWriter writer = new PrintWriter(new FileWriter(directori + File.separator + arxiu, true))) {
            writer.println(message);
            writer.close();
        } catch (IOException e) {
            e.getMessage(); // Manejar el error de escritura
        }
    }
    //-----------------------------------------------------------------------------
}


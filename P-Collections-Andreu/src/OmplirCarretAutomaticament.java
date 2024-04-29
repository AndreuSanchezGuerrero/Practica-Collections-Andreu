import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class OmplirCarretAutomaticament {
    private ArrayList<String> productesAlimentacio;
    private ArrayList<String> productesTextils;
    private ArrayList<String> productesElectronics;
    private ArrayList<String> llistaComposicioTextils;
    private Random random;
    private String codiDeBarres;


    public OmplirCarretAutomaticament() {
        productesAlimentacio = new ArrayList<>(Arrays.asList(
                "Pa", "Formatge", "Olives", "Tomàquet", "Patates", "Pasta", "Carn", "Peix",
                "Pollastre", "Verdures", "Fruita", "Xocolata", "Gelat", "Paella", "Croissant",
                "Embotits", "Salsitxes", "Aigua", "Vi", "Cervesa", "Suc", "Llet", "Cafè", "Tè",
                "Xarop", "Licor"
        ));

        productesTextils = new ArrayList<>(Arrays.asList(
                "Camisa", "Pantalons", "Samarreta", "Jersei", "Sabates", "Bufanda", "Gorral",
                "Vestit", "Sostenidor", "Mitjons", "Pijama", "Abric", "Guants", "Banyador", "Roba interior",
                "Sarong", "Bata"
        ));

        productesElectronics = new ArrayList<>(Arrays.asList(
                "Mobil", "Portatil", "Tablet", "O.Escriptori",
                "Televisor", "Càmera", "Auriculars", "Ratolí", "Teclat", "Impressora", "Router",
                "Monitor", "Switch", "Altaveu", "Regleta", "Hub", "Cable",
                "Disc dur", "Videocàmera", "Smartwatch", "Drone"
        ));

        llistaComposicioTextils = new ArrayList<>(Arrays.asList(
                "COTO", "POLIESTER", "LLI", "SEDA", "LLANA", "NILO"
        ));
        random = new Random();
    }

    public void omplirCarretDeAliments(ArrayList<Producte> llistaProductes) {
        String nom;
        float preu;
        String codiDeBarres;
        String dataCaducitat;
        try {
            // Fem 25 aleatoris
            for (int i = 0; i < 25; i++) {
                if (CarroCompra.llistaProductesCopia.size() >= CarroCompra.LIMIT_PRODUCTES) {
                    throw new ExcepcionsPropies.LimitProductesException("La llista ja te 100 productes");
                }

                // Creem el producte
                nom = getNomAlimentacio();
                preu = generarPreuAleatori(1, 100);
                codiDeBarres = getCodiDeBarres(nom);
                dataCaducitat = getDataCaducitatSTR();
                Producte producte = new Alimentacio(nom, preu, codiDeBarres, dataCaducitat);
                if (CarroCompra.mapProductesJaIntroduits.containsKey(producte.getCODI_DE_BARRES())) {
                    for (Producte producte2 : llistaProductes) {
                        if (producte2.getCODI_DE_BARRES().equals(producte.getCODI_DE_BARRES())) {
                            producte2.setQuantitat(producte2.getQuantitat() + 1);
                            break;
                        }
                    }
                }
                else {
                    llistaProductes.add(producte);
                    CarroCompra.mapProductesJaIntroduits.put(producte.getCODI_DE_BARRES(), 1);
                }
                CarroCompra.llistaProductesCopia.add(producte);
            }
        } catch (ExcepcionsPropies.LimitProductesException e) {
            CarroCompra.escriureLog(e.getMessage());
        }

    }

    public void omplirCarretDeTextils(ArrayList<Producte> llistaProductes) {
        String nom;
        float preu;
        String codiDeBarres;
        String composicioTextil;
        try {
            for (int i = 0; i < 25; i++) {
                if (CarroCompra.llistaProductesCopia.size() >= CarroCompra.LIMIT_PRODUCTES) {
                    throw new ExcepcionsPropies.LimitProductesException("La llista ja te 100 productes");
                }

                nom = getNomTextils();
                preu = generarPreuAleatori(1, 100);
                codiDeBarres = getCodiDeBarres(nom);
                composicioTextil = getComposicioTextil();
                Producte producte = new Textil(nom, preu, codiDeBarres, Textil.enumCompositioTextil.valueOf(composicioTextil));
                CarroCompra.comprovarPreuTextil(producte);
                if (!CarroCompra.mapProductesJaIntroduits.containsKey(producte.getCODI_DE_BARRES())) {
                    CarroCompra.mapProductesJaIntroduits.put(producte.getCODI_DE_BARRES(), 1);
                    llistaProductes.add(producte);
                }
                CarroCompra.llistaProductesCopia.add(producte);


            }
        } catch (ExcepcionsPropies.LimitProductesException e) {
            CarroCompra.escriureLog(e.getMessage());
        }
    }

    public void omplirCarretDeElectronics(ArrayList<Producte> llistaProductes) {
        String nom;
        float preu;
        String codiDeBarres;
        try {
            for (int i = 0; i < 25; i++) {
                if (CarroCompra.llistaProductesCopia.size() >= CarroCompra.LIMIT_PRODUCTES) {
                    throw new ExcepcionsPropies.LimitProductesException("La llista ja te 100 productes");
                }

                nom = getNomElectronics();
                preu = generarPreuAleatori(30, 300);
                codiDeBarres = getCodiDeBarres(nom);
                Producte producte = new Electronica(nom, preu, codiDeBarres, generarNumAleatori(30,100));
                if (CarroCompra.mapProductesJaIntroduits.containsKey(producte.getCODI_DE_BARRES())) {
                    for (Producte producte2 : llistaProductes) {
                        if (producte2.getCODI_DE_BARRES().equals(producte.getCODI_DE_BARRES())) {
                            producte2.setQuantitat(producte2.getQuantitat() + 1);
                            break;
                        }
                    }
                }
                else {
                    llistaProductes.add(producte);
                    CarroCompra.mapProductesJaIntroduits.put(producte.getCODI_DE_BARRES(), 1);
                }



            }
        } catch (ExcepcionsPropies.LimitProductesException e) {
            CarroCompra.escriureLog(e.getMessage());
        }

    }


    // Mètodes per agafar aleatoriament noms dels productes i composicions dels textils
    // Convertim la seva mida en un int i agafem un numero random entre 0 i la mida.
    // D'aquesta manera tenim un producte aleatori de la llista.
    private String getNomAlimentacio() {
        int producteAlimentacio = random.nextInt(productesAlimentacio.size());
        String producteAlimentacioFinal = productesAlimentacio.get(producteAlimentacio);
        return producteAlimentacioFinal;
    }

    private String getNomElectronics() {
        int producteElectronics = random.nextInt(productesElectronics.size());
        String producteElectronicsFinal = productesElectronics.get(producteElectronics);
        return producteElectronicsFinal;
    }

    private String getNomTextils() {
        int producteTextils = random.nextInt(productesTextils.size());
        String producteTextilsFinal = productesTextils.get(producteTextils);
        return producteTextilsFinal;
    }

    private String getComposicioTextil() {
        int composicioTextil = random.nextInt(llistaComposicioTextils.size());
        String composicioTextilFinal = llistaComposicioTextils.get(composicioTextil);
        return composicioTextilFinal;
    }

    public String getCodiDeBarres(String producte) {
        if (CarroCompra.nomYCodigsProductes.containsKey(producte)) {
            codiDeBarres = CarroCompra.nomYCodigsProductes.get(producte);
            return codiDeBarres;
        } else {
            codiDeBarres=  CarroCompra.generarCodiDeBarres(producte);
            CarroCompra.nomYCodigsProductes.put(producte, codiDeBarres);
            return codiDeBarres;
        }
    }

    // Generem una data aleatoria de caducitat, si o si serà més gran que avui, però maxim 365 dies.
    public String getDataCaducitatSTR() {
        LocalDate dataCaducitat = LocalDate.now().plusDays(random.nextInt(1, 365));
        String dataCaducitatSTR = dataCaducitat.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return dataCaducitatSTR;
    }

    // pasem dos numeros i generem un preu float aleatori entre min i max.
    public static float generarPreuAleatori(int min, int max) {
        Random random2 = new Random();
        float numeroAleatori = random2.nextInt(min,max);
        return numeroAleatori;
    }

    // Pasem dos numeros i generem un numero enter aleatori entre min i max.
    public static int generarNumAleatori(int min, int max) {
        Random random3 = new Random();
        int numeroAleatori = random3.nextInt(min,max);
        return numeroAleatori;
    }
}

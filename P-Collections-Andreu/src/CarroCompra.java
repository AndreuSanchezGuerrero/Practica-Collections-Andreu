import java.util.ArrayList;
import java.time.LocalTime;

public class CarroCompra {
    // Dades globals
    private ArrayList<Producte> LlistaProductes;
    private ArrayList<Alimentacio> LlistaAlimentacio;
    private ArrayList<Textil> LlistaTextil;
    private ArrayList<Electronica> LlistaElectronica;

    //-----------------------------------------------------------------------------
    // Constructor
    public CarroCompra() {
        LlistaProductes = new ArrayList<Producte>();
        LlistaAlimentacio = new ArrayList<Alimentacio>();
        LlistaTextil = new ArrayList<Textil>();
        LlistaElectronica = new ArrayList<Electronica>();
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
}


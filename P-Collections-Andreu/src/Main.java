import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            int opcioMenuPrincipal;
            int opcioMenuOpcio1;

            // Creem el carro
            CarroCompra carro = new CarroCompra();

            do {
                // Mostrem el menu principal on es mostren les opcions
                // (1) Introduir producte);
                // (2) Passar per caixa;
                // (3) Mostar carret de compra;
                // (0) Acabar;
                carro.menu1();

                // Demanem a l'usuari que entri l'opció que vol escollir i fem un doble control d'errors.
                // Comprovem que l'entrada sigui un enter, si no es així que llenci una excepció.
                // Si es un enter comprovem que sigui entre 0 i 3, si no tornem a demanar.
                if (!input.hasNextInt()) {
                    throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
                }
                opcioMenuPrincipal = input.nextInt();
                while (opcioMenuPrincipal < 0 || opcioMenuPrincipal > 5) {
                    System.out.println("L'entrada ha de ser un enter entre 0 i 5, torna a provar.");
                    opcioMenuPrincipal = input.nextInt();

                    if (!input.hasNextInt()) {
                        throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
                    }
                }
                    switch (opcioMenuPrincipal) {
                        case 1: // Introduir producte

                            // Comprovem que no hi hagin més de 100 productes al carret
                            carro.escollirProducte();

                            break;
                        case 2:
                            carro.generarTicketDeCompra();
                            break;
                        case 3:
                            System.out.println();
                            carro.mostrarProductesCarret();
                            System.out.println();
                            break;
                        case 4:
                            OmplirCarretAutomaticament omplir = new OmplirCarretAutomaticament();
                            omplir.omplirCarretDeAliments(CarroCompra.llistaProductes);
                            omplir.omplirCarretDeTextils(CarroCompra.llistaProductes);
                            omplir.omplirCarretDeElectronics(CarroCompra.llistaProductes);
                            break;
                        case 5:
                            System.out.println();
                            carro.mostrarPreuTotal();
                            break;
                        case 0:
                            System.out.println("Gràcies per la seva visita");
                            break;
                        default:
                            break;
                    }
                }
                while (opcioMenuPrincipal != 0) ;
    } catch (InputMismatchException e) {
            CarroCompra.escriureLog(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

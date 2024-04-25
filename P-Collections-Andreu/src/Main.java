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

                // Possem opcioMenuPrincipal a 4 per entrar si o si al while.
                // Demanem a l'usuari que entri l'opció que vol escollir i fem un doble control d'errors.
                // Comprovem que l'entrada sigui un enter, si no es així que llenci una excepció.
                // Si es un enter comprovem que sigui entre 0 i 3, si no tornem a demanar.
                opcioMenuPrincipal = 4;
                while (opcioMenuPrincipal < 0 || opcioMenuPrincipal > 3) {
                    if (!input.hasNextInt()) {
                        throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
                    }
                    opcioMenuPrincipal = input.nextInt();
                    if (opcioMenuPrincipal < 0 || opcioMenuPrincipal > 3) {
                        System.out.println("L'entrada ha de ser un enter entre 0 i 3, torna a provar.");
                    }
                }
                    switch (opcioMenuPrincipal) {
                        case 1: // Introduir producte

                            // Comprovem que no hi hagin més de 100 productes al carret
                            if (carro.llistaProductes.size() >= CarroCompra.LIMIT_PRODUCTES) {
                                throw new ExcepcionsPropies.LimitProductesException("No es pot afegir més de 100 productes.");
                            }
                            carro.escollirProducte();

                            break;
                        case 2: //compra.passarCaixa(); break;
                        case 3:
                            System.out.println("Carret");
                            //compra.printCarret();
                            break;
                        case 0:
                            System.out.println("Gràcies per la seva visita");
                            break;
                        default:
                            break;
                    }
                }
                while (opcioMenuPrincipal != 0) ;
    }
        catch (ExcepcionsPropies.LimitProductesException e) {
            System.out.println(e.getMessage());
        }
        catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
        catch (ExcepcionsPropies.DataCaducitatException e) {
            System.out.println(e.getMessage());
        }
        catch (ExcepcionsPropies.negatiuException e) {
            System.out.println(e.getMessage());
        }

    }
}

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
                carro.menu1();

                // Possem opcioMenuPrincipal a 4 per entrar si o si al while, demanem a l'usuari que entri l'opció i fem control de que sigui un enter
                opcioMenuPrincipal = 4;
                while (opcioMenuPrincipal < 0 || opcioMenuPrincipal > 3) {
                    System.out.println("No es una opència correcte, torna-ho a provar");
                    if (!input.hasNextInt()) {
                        throw new InputMismatchException("Entrada no valida. S'esperava un enter.");
                    }
                    opcioMenuPrincipal = input.nextInt();
                }
                switch(opcioMenuPrincipal) {
                    case 1: // Introduir producte
                        do {
                            carro.menu2();
                            opcioMenuOpcio1 = input.nextInt();
                            switch(opcioMenuOpcio1) {
                                case 1:
                                    System.out.println("Afegir aliment");
                                    //compra.addAliment();
                                    break;
                                case 2:
                                    System.out.println("Afegir tèxtil");
                                    //compra.addTextil();
                                    break;
                                case 3:
                                    System.out.println("Afegir electrònica");
                                    //compra.addElectronica();
                                    break;
                                default:
                                    System.out.println("No es una opció correcte, torna-ho a provar");
                                    break;
                            }
                        }while(opcioMenuOpcio1!=0);
                        break;
                    case 2: //compra.passarCaixa(); break;
                    case 3:
                        System.out.println("Carret");
                        //compra.printCarret();
                        break;
                    case 0:	System.out.println("Gràcies per la seva visita"); break;
                    default:
                        System.out.println(opcioMenuPrincipal + " No es una opció correcte, torna-ho a provar");
                        break;
                }
            }while(opcioMenuPrincipal!=0);

        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }
}

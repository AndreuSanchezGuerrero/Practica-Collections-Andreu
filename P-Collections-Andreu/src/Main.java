import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int opcioMenuPrincipal;
        int opcioMenuOpcio1;

        CarroCompra carro = new CarroCompra();

        do {
            carro.menu1();
            opcioMenuPrincipal = input.nextInt();
            switch(opcioMenuPrincipal) {
                case 1:
                    do {
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
                            default: break;
                        }
                    }while(opcioMenuOpcio1!=0);
                    break;
                case 2: //compra.passarCaixa(); break;
                case 3:
                    System.out.println("Carret");
                    //compra.printCarret();
                    break;
                case 0:	System.out.println("Gràcies per la seva visita"); break;
                default: break;
            }
        }while(opcioMenuPrincipal!=0);
    }
}

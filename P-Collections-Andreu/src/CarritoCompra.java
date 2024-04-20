public class CarritoCompra {
    public static void main(String[] args) {
        Alimentacio aliment1 = new Alimentacio("Poma", 1.6f, "44rnj4", "30-07-2026");
        Textil textil1 = new Textil("Cami", 2.5f, "44rnj4", "Lana");
        System.out.println();
        System.out.println(textil1.toString());
        System.out.println();
        System.out.println(aliment1.toString());
    }
}

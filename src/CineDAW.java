import java.util.InputMismatchException;
import java.util.Scanner;

public class CineDAW {
    static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion;
        // Dibujamos menú
        do {
            System.out.println("----- CineDAW -----");
            System.out.println("1· Ver sala");
            System.out.println("2· Comprar entrada");
            System.out.println("3· Estadísticas");
            System.out.println("0· Salir");
            System.out.println("Elige una opción: ");
            //TODO: try catch
            try {
                opcion = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opción no válida");
            }

            switch (opcion) {
                case 1:
                    //TODO: Ver sala
                    break;
                case 2:
                    //TODO: Comprar entrada
                    break;
                case 3:
                    //TODO: Estadísticas
                case 0:
                    System.out.println("Ha salido del menú.");
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        sc.close();
    }
}

import java.util.InputMismatchException;
import java.util.Scanner;

public class CineDAW {
    static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int opcion;
        final int FILAS = 6;
        final int BUTACAS_POR_FILA = 8;
        String sala = "";
        dibujarSala(sala);
        //boolean ocupada = sala.charAt(posicion) == 'X';                       // consultar
        //sala = sala.substring(0, posicion) + 'X' + sala.substring(posicion + 1);  // ocupar


        // Ver sala
        //for (int fila = 0; fila < 6; fila++) {
        //    for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
        //        // Posición de una butaca dentro del String: A1 -> 0, A2 -> 1 ... F8 -> 47
        //        int posicion = (fila - 'A') * BUTACAS_POR_FILA + (butaca - 1);
        //        System.out.print(sala.charAt(posicion) + " ");
        //    }
        //    System.out.println();
        //}

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
                sc.nextLine(); // Limpiar buffer
                opcion = -1; // Asignar un valor inválido para continuar el bucle
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
                    break;
                case 0:
                    System.out.println("Ha salido del menú.");
                    break;
                case -1:
                    // Entrada incorrecta si se produce la excepción InputMismatchException.
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        sc.close();
    }

    static void dibujarSala(String sala) {
        final int FILAS = 6;
        final int BUTACAS_POR_FILA = 8;
        final int BUTACAS_ANTES_PASILLO = 4;
        // Cabecera: bucle simple, NO anidado
        System.out.print("   ");
        for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
            System.out.print(butaca + " ");
        }
        System.out.println();

        // Dibujamos sala de cine
        for (char fila = 'A'; fila <= 'A' + FILAS; fila++) {
            System.out.print(" " + fila + " ");                      // letra de la fila
            for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
                int posicion = (fila - 'A') * BUTACAS_POR_FILA + (butaca - 1);
                System.out.print(sala.charAt(posicion) + " ");
                if (butaca == BUTACAS_ANTES_PASILLO) {
                    System.out.print("  ");
                }
                System.out.println();
            }
        }
    }
}

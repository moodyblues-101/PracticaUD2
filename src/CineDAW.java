import java.util.InputMismatchException;
import java.util.Scanner;

public class CineDAW {

    // Declaramos constantes de la sala
    private static final int FILAS = 6;
    private static final int BUTACAS_POR_FILA = 8;
    private static final int BUTACAS_ANTES_PASILLO = 4;
    private static final char PRIMERA_FILA = 'A';
    private static final char LIBRE = '-';
    private static final char OCUPADA = 'X';

    private static final int OPCION_INVALIDA = -1;

    private static final String MENU = """
            ===== CineDAW =====
            1. Ver Sala
            2. Comprar entrada
            3. Estadísticas
            0. Salir
            Eligre una opción: """;

    // Scanner visible desde todos los métodos.
    private static final Scanner SC = new Scanner(System.in);

    static void main(String[] args) {
        String sala = construirSala();
        int opcion;

        do {
            System.out.print(MENU);
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    dibujarSala(sala);
                    break;
                case 2:
                    //TODO Comprar entrada
                case 3:
                    //TODO Estadisticas
                case 0:
                    System.out.println("Ha salido del cine!");
                    break;
                case OPCION_INVALIDA:
                    break; // Gestiona entrada inválida.
                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
        SC.close();
    }
    // Construye la sala con las butacas libres (-)
    private static String construirSala() {
        String sala = "";
        for (int i = 1; i <= FILAS * BUTACAS_POR_FILA ; i++) {
            sala += LIBRE; // (-)
        }
        return sala;
    }
    // Lee la opción seleccionada en el menú. Si se recibe texto devuelve OPCION_INVALIDA (-1)
    private static int leerOpcion() {
        int opcion;
        try {
            opcion = SC.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Opción no válida");
            opcion = OPCION_INVALIDA;
        }
        SC.nextLine(); // Limpiamos buffer
        return opcion;
    }
    private static int posicionButaca(char fila, int butaca) {
        return (fila - PRIMERA_FILA) * BUTACAS_POR_FILA + (butaca -1);
    }

    private static void dibujarSala(String sala) {
        System.out.println("        P A N T A L L A");
        System.out.print("    "); // Cabecera butacas (4 esp.)
        for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
            System.out.print(butaca + " ");
            // Pasillo entre butacas (4 esp.)
            if (butaca == BUTACAS_ANTES_PASILLO) {
                System.out.print("    ");
            }
        }
        System.out.println();

        for (char fila = PRIMERA_FILA; fila < PRIMERA_FILA + FILAS; fila++) {
            System.out.print(" " + fila + "  "); // Espacio entre fila y butacas (2 esp.)
            for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
                System.out.print(sala.charAt(posicionButaca(fila, butaca)) + " ");
                // Pasillo entre butacas (4 esp.)
                if (butaca == BUTACAS_ANTES_PASILLO) {
                    System.out.print("    ");
                }
            }
            System.out.println();
        }
        System.out.println(" (" + LIBRE + " libre, " + OCUPADA + " ocupada)");
    }


}

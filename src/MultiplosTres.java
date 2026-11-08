import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * MultiplosTres: muestra cuántos múltiplos de 3 hay entre 1 y un número N
 * (ambos incluidos), su suma y su media. Versión corregida del programa
 * con errores del ejercicio 3.
 *
 * @author Joel C.G.
 */
public class MultiplosTres {
    static final int DIVISOR = 3; // CAMBIO -> declaramos el divisor como constante

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Introduce un número N: ");

        // CAMBIO: controlamos la excepción en caso de entrada errónea
        int n;
        try {
            n = entrada.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Debes introducir un número entero.");
            entrada.close();
            return;
        }

        int suma = 0, cantidad = 0;
        // CAMBIO 1: corregimos i < n por i <= n
        // n quedaba fuera para N = 12, no contaba el 12
        for (int i = 1; i <= n; i++) {
            if (i % DIVISOR == 0) { // CAMBIO: usamos la constante DIVISOR en lugar de 3
                suma += i;
                cantidad++;
            }
        }

        System.out.println("Múltiplos de 3 entre 1 y " + n + ": " + cantidad);
        // CAMBIO: si no hay múltiplos se muestra un mensaje en lugar de dividir entre cero
        // (con enteros, 0 / 0 lanzaba ArithmeticException)
        if (cantidad == 0) {
            System.out.println("No hay múltiplos de 3 en ese rango, no se puede calcular la media.");
        } else {
            // CAMBIO 2: casting a double antes de dividir.
            // La operacion se hacia entre enteros y truncaba -> suma / cantidad (int / int)
            // (30 / 4 daba 7 en vez de 7.5).
            double media = (double) suma / cantidad;
            System.out.println("Suma: " + suma + " | Media: " + media);
        }
        entrada.close();
    }
}
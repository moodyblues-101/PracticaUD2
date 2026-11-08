import java.util.Scanner;

/**
 * MultiplosTres: muestra cuántos múltiplos de 3 hay entre 1 y un número N
 * (ambos incluidos), su suma y su media.
 *
 * @author Joel C.G.
 */
public class MultiplosTres {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        System.out.print("Introduce un número N: ");
        int n = entrada.nextInt();
        int suma = 0, cantidad = 0;
        for (int i = 1; i < n; i++) {
            if (i % 3 == 0) {
                suma += i;
                cantidad++;
            }
        }
        double media = suma / cantidad;
        System.out.println("Múltiplos de 3 entre 1 y " + n + ": " + cantidad);
        System.out.println("Suma: " + suma + " | Media: " + media);
        entrada.close();
    }
} 
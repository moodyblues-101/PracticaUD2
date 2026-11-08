import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Programa CineDAW: taquilla de una sala de cine
 * Permite ver el estado de la sala (butacas vacias o llenas), comprar entradas aplicando
 * tarifas distintas por edad y por día de la semana, y consultar estadísticas de venta.
 * @author Joel C.G.
 */
public class CineDAW {

    // Declaramos constantes de la sala
    private static final int FILAS = 6;
    private static final int BUTACAS_POR_FILA = 8;
    private static final int TOTAL_BUTACAS = FILAS * BUTACAS_POR_FILA;
    private static final int BUTACAS_ANTES_PASILLO = 4;
    private static final char PRIMERA_FILA = 'A';
    private static final char ULTIMA_FILA = (char) (PRIMERA_FILA + FILAS - 1);
    private static final char LIBRE = '-';
    private static final char OCUPADA = 'X';

    // Constantes de precios (en céntimos 4,20 € -> 420) ---
    private static final int PRECIO_INFANTIL = 500;
    private static final int PRECIO_SENIOR = 600;
    private static final int PRECIO_GENERAL = 850;
    private static final int SUPLEMENTO_FIN_SEMANA = 150;
    private static final int PORCENTAJE_DESCUENTO_MIERCOLES = 30;   // porcentaje

    // Constantes de restricción de edad
    private static final int EDAD_MINIMA = 0;
    private static final int EDAD_MAXIMA = 120;
    private static final int EDAD_LIMITE_INFANTIL = 12;
    private static final int EDAD_MINIMA_SENIOR = 65;

    // Constantes de dias de la semana
    private static final int LUNES = 1;
    private static final int MARTES = 2;
    private static final int MIERCOLES = 3;
    private static final int JUEVES = 4;
    private static final int VIERNES = 5;
    private static final int SABADO = 6;
    private static final int DOMINGO = 7;

    // Constantes de formato y menú
    private static final int CENTIMOS_POR_EURO = 100;
    private static final int LIMITE_UN_DIGITO = 10;
    private static final double PORCENTAJE_TOTAL = 100.0;
    private static final int OPCION_NO_VALIDA = -1; // valor que devuelve el menú si se escribe un texto

    private static final String MENU = """
            ===== CineDAW =====
            1. Ver Sala
            2. Comprar entrada
            3. Estadísticas
            0. Salir
            Elige una opción: """;

    // Scanner, se comparte entre métodos
    private static Scanner SC;
    private static String sala;  // 48 caracteres: '-' libre, 'X' ocupada

    // Variables auxiliares para comunicar el resultado de las compras.
    private static  int entradasVendidas = 0;
    private static int recaudacion = 0;

    static void main(String[] args) {
        SC = new Scanner(System.in);
        sala = construirSalaVacia();

        int opcion;
        do {
            opcion = leerOpcionMenu();
            switch (opcion) {
                case 1:
                    dibujarSala();
                    break;
                case 2:
                    comprarEntrada();
                    break;
                case 3:
                    mostrarEstadisticas();
                    break;
                case 0:
                    salirMenu();
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
        SC.close(); // Cerramos Scanner
    }

    /**
     * Construcción inicial de la sala con todas las butacas libres (-)
     */
    private static String construirSalaVacia() {
        String sala = "";
        for (int i = 0; i < FILAS * BUTACAS_POR_FILA; i++) {
            sala += LIBRE; // (-)
        }
        return sala;
    }

    /**
     * Imprime el MENU en pantalla y lee la opción seleccionada en el menú.
     * Si se recibe texto devuelve OPCION_NO_VALIDA (-1)
     */
    private static int leerOpcionMenu() {
        int opcion;
        System.out.println();
        System.out.print(MENU);
        try {
            opcion = SC.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Opción no válida");
            opcion = OPCION_NO_VALIDA; // -1
        }
        SC.nextLine(); // Limpiamos buffer
        return opcion;
    }

    /**
     * Solicita un entero comprendido entre dos límites en bucle mientras no cumpla con el rango (minimo - maximo).
     */
    private static int pedirEntero(String mensaje, int minimo, int maximo) {
        int valor;
        boolean valido;
        do {
            System.out.print(mensaje);
            try {
                valor = SC.nextInt();
                valido = valor >= minimo && valor <= maximo;
                if (!valido) {
                    System.out.println("Debe ser un número entre " + minimo + " y " + maximo + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes introducir un número entero.");
                valor = minimo - 1;
                valido = false;
            }
            SC.nextLine(); // limpia el buffer tras nextInt() o tras la recoger la excepción
        } while (!valido);
        return valor;
    }

    /**
     * Validación de fila.
     * Solicitamos la entrada de la fila como texto y se repite en bucle mientras no sea un valor en rango A-F.
     */
    private static char pedirFila() {
        System.out.print("Fila (" + PRIMERA_FILA + "-" + ULTIMA_FILA + "): ");
        String filaTexto = SC.nextLine().trim().toUpperCase();
        while (filaTexto.length() != 1
                || filaTexto.charAt(0) < PRIMERA_FILA
                || filaTexto.charAt(0) > ULTIMA_FILA) {
            System.out.println("Fila no válida. Debe ser una letra entre " + PRIMERA_FILA + " y " + ULTIMA_FILA + ".");
            System.out.print("Fila (" + PRIMERA_FILA + "-" + ULTIMA_FILA + "): ");
            filaTexto = SC.nextLine().trim().toUpperCase();
        }
        return filaTexto.charAt(0);
    }

     /** Posición de la butaca dentro del string (A1 -> 0 ... F8 -> 47) */
    private static int calcularPosicionButaca(char fila, int butaca) {
        return (fila - PRIMERA_FILA) * BUTACAS_POR_FILA + (butaca - 1);
    }
    /** Marca una butaca como ocupada */
    private static void ocuparButaca(int posicion) {
        sala = sala.substring(0, posicion) + OCUPADA + sala.substring(posicion + 1); // Al tratarse de un string, no se modifica el string, si no que se crea uno nuevo cada vez que cambia algo.
    }
    /** Convierte céntimos a string en euros: 150 -> "1,50 €". */
    private static String formatearEuros(int centimos) {
        int euros = centimos / CENTIMOS_POR_EURO;
        int resto = centimos % CENTIMOS_POR_EURO;
        return euros + "," + (resto < LIMITE_UN_DIGITO ? "0" : "") + resto + " €";
    }

    /* ========================= OPCION 1 (Ver sala) ============================ */
    /** Dibuja la sala entera */
    private static void dibujarSala() {
        System.out.println();
        System.out.println("        P A N T A L L A");
        System.out.print("    "); // Espacio cabecera butacas (4 esp.)
        for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
            System.out.print(butaca + " ");
            // Pasillo central (4 esp.)
            if (butaca == BUTACAS_ANTES_PASILLO) {
                System.out.print("    ");
            }
        }
        System.out.println();

        // Bucle externo: filas. Bucle interno: butacas.
        for (char fila = PRIMERA_FILA; fila < PRIMERA_FILA + FILAS; fila++) {
            System.out.print(" " + fila + "  "); // Espacio entre fila y butacas (2 esp.)
            for (int butaca = 1; butaca <= BUTACAS_POR_FILA; butaca++) {
                System.out.print(sala.charAt(calcularPosicionButaca(fila, butaca)) + " "); //char+String -> Casting de char a String ("X " o "- ")
                // Dibujamos pasillo entre butacas (4 esp.)
                if (butaca == BUTACAS_ANTES_PASILLO) {
                    System.out.print("    ");
                }
            }
            System.out.println();
        }
        System.out.println(" (" + LIBRE + " libre, " + OCUPADA + " ocupada)");
    }
    /* ========================= FIN OPCION 1 (Ver sala) ============================ */

    /* ========================= OPCION 2 (Comprar entrada) ============================ */
    private static void comprarEntrada() {
        // 1. Aviso de sala completa en caso de que no quede ninguna butaca libre
        if (sala.indexOf(LIBRE) == -1) {
            System.out.println("La sala está completa. No quedan butacas libres.");
            return;
        }

        // 2 y 3. Fila y butaca validadas
        char fila = pedirFila();
        int butaca = pedirEntero("Butaca (1-" + BUTACAS_POR_FILA + "): ", 1, BUTACAS_POR_FILA);
        int posicion = calcularPosicionButaca(fila, butaca);

        // 4. Butaca ocupada: salta aviso y se vuelve al menú
        if (sala.charAt(posicion) == OCUPADA) {
            System.out.println("La butaca " + fila + butaca + " ya está ocupada.");
            sugerirButacaLibre(fila);
            return;
        }

        // 5. Precio base según la edad
        int edad = pedirEntero("Edad del espectador: ", EDAD_MINIMA, EDAD_MAXIMA);
        int precio;
        String tarifa;
        if (edad < EDAD_LIMITE_INFANTIL) {
            precio = PRECIO_INFANTIL;
            tarifa = "infantil";
        } else if (edad >= EDAD_MINIMA_SENIOR) {
            precio = PRECIO_SENIOR;
            tarifa = "senior";
        } else {
            precio = PRECIO_GENERAL;
            tarifa = "general";
        }
        System.out.println("Tarifa: " + tarifa);

        // 6. Ajuste de precio según el día de la semana
        int dia = pedirEntero("Día de la semana (1=lunes ... 7=domingo): ", LUNES, DOMINGO);
        precio = aplicarDiaSemana(precio, dia);

        // 7. Muestra la tarifa aplicada y el total a pagar con formato de euros.
        System.out.println("Total a pagar: " + formatearEuros(precio));

        // 8. Ocupar la butaca y actualizar contadores
        ocuparButaca(posicion);
        entradasVendidas++;
        recaudacion += precio;
        System.out.println("Entrada vendida: fila " + fila + ", butaca " + butaca + ".");
    }

    /**
     * Aplica el ajuste de precio según el día de la semana.
     * Los casos que comparten código se agrupan sin break entre ellos.
     */
    private static int aplicarDiaSemana(int precio, int dia) {
        int total = precio;
        switch (dia) {
            case LUNES:
            case MARTES:
            case JUEVES:
            case VIERNES:
                // De lunes a viernes (exceoto miércoles) se mantiene el precio
                break;
            case MIERCOLES:
                // Día del espectador: 30 % de descuento, calculado con Math.round y casting a int
                int descuento = (int) Math.round(precio * PORCENTAJE_DESCUENTO_MIERCOLES / PORCENTAJE_TOTAL);
                total = precio - descuento;
                System.out.println("Miércoles, día del espectador: -" + PORCENTAJE_DESCUENTO_MIERCOLES + " %");
                break;
            case SABADO:
            case DOMINGO:
                // Fin de semana: se suma el suplemento fijo
                total = precio + SUPLEMENTO_FIN_SEMANA;
                System.out.println("Fin de semana: suplemento de " + formatearEuros(SUPLEMENTO_FIN_SEMANA));
                break;
            default:
                // No debería darse el caso, ya validamos el día entre 1 y 7
                System.out.println("Día no válido: se mantiene el precio.");
        }
        return total;
    }

    /** Ampliación opcional | Sugiere la primera butaca libre de la fila indicada. */
    private static void sugerirButacaLibre(char fila) {
        int inicioFila = (fila - PRIMERA_FILA) * BUTACAS_POR_FILA;
        int finFila = inicioFila + BUTACAS_POR_FILA; // primera posición de la fila siguiente
        int libre = sala.indexOf(LIBRE, inicioFila);
        if (libre != -1 && libre < finFila) { // comprobamos que no se sale de la fila
            System.out.println("Sugerencia: la primera butaca libre de la fila " + fila
                    + " es la " + (libre - inicioFila + 1) + ".");
        } else {
            System.out.println("No quedan butacas libres en la fila " + fila + ".");
        }
    }
    /* ========================= FIN OPCION 2 (Comprar entrada) ============================ */

    /* ========================= OPCION 3 (Estadísticas) ============================ */
    private static void mostrarEstadisticas() {
        System.out.println("Entradas vendidas: " + ternariaEntradas());
        System.out.println("Recaudación: " + formatearEuros(recaudacion));

        // Contamos las butacas ocupadas, con continue saltamos las libres
        int ocupadas = 0;
        for (int i = 0; i < TOTAL_BUTACAS; i++) {
            if (sala.charAt(i) == LIBRE) {
                continue;
            }
            ocupadas++;
        }
        int libres = TOTAL_BUTACAS - ocupadas;
        System.out.println("Butacas ocupadas: " + ocupadas + " | libres: " + libres);

        // Devuelve estadistica de ocupación con dos decimales, calculado con casting a double y Math.round
        double porcentaje = Math.round((double) ocupadas / TOTAL_BUTACAS * PORCENTAJE_TOTAL * PORCENTAJE_TOTAL) / PORCENTAJE_TOTAL;
        System.out.println("Ocupación: " + porcentaje + " %");
    }

    /** Uso de ternario, devuelve 1 entrada o N entradas como String según el numero de entradas vendidas */
    private static String ternariaEntradas() {
        return entradasVendidas == 1 ? "1 entrada" : entradasVendidas + " entradas";
    }
    /* ========================= FIN OPCION 3 (Estadísticas) ============================ */

    /* ========================= OPCION 0 (Salir) ============================ */
    private static void salirMenu() {
        System.out.println("¡Hasta pronto! Hoy se han vendido " + ternariaEntradas() + ".");
    }
}
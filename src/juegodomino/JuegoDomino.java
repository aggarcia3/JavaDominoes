package juegodomino;

import java.util.Scanner;

/**
 * Punto de entrada del programa. Controla su ejecución.
 * @author Rubén Vilas Martinez
 * @author Irene Fernández Mariño
 * @author Alejandro Gonzalez García
 * @author Daniel Gómez Rodríguez
 */
public final class JuegoDomino {
    private static final Scanner TECLADO = new Scanner(System.in);

    public static void main(String[] args) {
        int op = 0;

        System.out.println("- * - BIENVENIDO A JavaDominoes - * -");
        
        // Pedir acción a realizar
        do {
            try {
                System.out.println("\nSelecciona qué quieres hacer:\n\t[1] Jugar\n\t[2] Salir");
                op = Integer.parseUnsignedInt(TECLADO.nextLine());
                if (op != 1 && op != 2) {
                    System.out.println("La opción elegida debe de estar entre 1 y 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("No se ha introducido un número válido.");
            }
        } while (op < 1 || op > 2);

        if (op == 1) {
            Juego.iniciarJuego();
        }
        
        System.out.println("\n¡Hasta luego!");
    }
    
    /**
     * Obtiene un objeto {@link Scanner} asociado al flujo de entrada de datos del usuario del programa (normalmente, el teclado).
     * @return El objeto de la clase {@link Scanner} asociado a dicho flujo.
     */
    public static Scanner getTeclado() {
        return TECLADO;
    }
}
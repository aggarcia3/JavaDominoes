package juegodomino;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Montón de fichas para robar y repartir al inicio del juego. Pertenece a una mesa de juego.
 * @author Rubén Vilas Martinez
 * @author Irene Fernández Mariño
 * @author Alejandro Gonzalez García
 * @author Daniel Gómez Rodríguez
 */
public final class Monton {
    /**
     * Número de fichas que tendrá un jugador al comienzo de la partida.
     */
    private final static int FICHAS_JUGADOR = 7;
    private final Stack<Ficha> fichas;
    
    /**
     * Representa un error de montón vacío, que puede ocurrir en tiempo de ejecución al sacar fichas de él.
     */
    public static final class MontonVacioException extends RuntimeException {
        public MontonVacioException() {
            super();
        }
        
        public MontonVacioException(String msg) {
            super(msg);
        }
    }

    /**
     * Crea un montón con todas las fichas de dominó posibles.
     */
    public Monton() {
        this.fichas = new Stack<>();

        // Generar todas las combinaciones de fichas posibles
        // (Hay 7 fichas que contienen cada número)
        for (int i = 0; i < 7; ++i) {
            for (int j = i; j < 7; ++j) {
                fichas.add(new Ficha(i, j));
            }
        }

        // Hacer aleatorio el orden de las fichas en el montón
        Collections.shuffle(fichas);
    }

    /**
     * Da {@link FICHAS_JUGADOR} fichas iniciales del montón al jugador referenciado.
     * Si el jugador tiene más fichas que {@link FICHAS_JUGADOR}, no recibe ninguna.
     * @param j Jugador a repartir fichas.
     * @throws MontonVacioException Si la pila de fichas del montón está vacía.
     */
    public void repartirFichas(Jugador j) throws MontonVacioException {
        while (j.getNumFichasMano() < FICHAS_JUGADOR) {
            try {
                j.añadirFichaMano(fichas.pop());
            } catch (EmptyStackException exc) {
                throw new MontonVacioException();
            }
        }
    }

    /**
     * Saca la ficha que está en la cima del montón.
     * @return La ficha que se ha sacado.
     * @throws MontonVacioException Si no hay fichas en el montón.
     */
    public Ficha sacarFicha() throws MontonVacioException {
        try {
            return fichas.pop();
        } catch (EmptyStackException exc) {
            throw new MontonVacioException();
        }
    }

    /**
     * Obtiene el número de fichas que hay en el montón.
     * @return El número de fichas en el montón.
     */
    public int getNumFichas() {
        return fichas.size();
    }
}
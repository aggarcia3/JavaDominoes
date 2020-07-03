package juegodomino;

import java.util.LinkedList;

/**
 * Mesa de juego. Contiene las fichas jugadas, jugadores y el montón.
 * @author Rubén Vilas Martinez
 * @author Irene Fernández Mariño
 * @author Alejandro Gonzalez García
 * @author Daniel Gómez Rodríguez
 */
public final class Mesa {
    private final Jugador[] jugadores;
    private final LinkedList<Ficha> fichasTablero;
    private final Monton monton;

    /**
     * Crea una mesa con una serie de jugadores.
     * @param numJugadores Los jugadores de la mesa.
     */
    public Mesa(int numJugadores) {
        this.jugadores = new Jugador[numJugadores];
        this.fichasTablero = new LinkedList<>();
        this.monton = new Monton();
        
        // Añadir los jugadores a la mesa
        añadirJugadores();
    }

    /**
     * Obtiene los jugadores de la mesa.
     * @return Los jugadores de la mesa.
     */
    public Jugador[] getJugadores() {
        return jugadores;
    }

    /**
     * Obtiene el montón de fichas de la mesa.
     * @return El montón de fichas de la mesa.
     */
    public Monton getMonton() {
        return monton;
    }
    
    /**
     * Añade una ficha al principio (lado izquierdo) del tablero. Se cuenta el extremo como jugado.
     * @param f La ficha a añadir.
     */
    public void añadirFichaPrincipio(Ficha f) {
        fichasTablero.addFirst(f);
    }
    
    /**
     * Añade una ficha al final (lado derecho) del tablero. Se cuenta el extremo como jugado.
     * @param f La ficha a añadir.
     */
    public void añadirFichaFinal(Ficha f) {
        fichasTablero.addLast(f);
    }

    /**
     * Obtiene la primera ficha colocada en el tablero.
     * @return La primera ficha colocada en el tablero. Puede ser {@code null} si no hay fichas en el tablero.
     */
    public Ficha getPrimeraFicha() {
        return fichasTablero.peekFirst();
    }

    /**
     * Obtiene la última ficha colocada en el tablero.
     * @return La última ficha colocada en el tablero. Puede ser {@code null} si no hay fichas en el tablero.
     */
    public Ficha getUltimaFicha() {
        return fichasTablero.peekLast();
    }
    
    
    /**
     * Obtiene una ficha del tablero.
     * @param pos La posición que ocupa la ficha deseada.
     * @return La ficha en esa posición.
     * @throws IndexOutOfBoundsException Si {@code i} es un índice inválido ({@code i < 0 || i > fichasTablero.size() - 1}).
     */
    public Ficha getFicha(int pos) throws IndexOutOfBoundsException {
        return fichasTablero.get(pos);
    }
    
    /**
     * Devuelve el número de fichas que hay en el tablero.
     * @return El número de fichas que hay en el tablero.
     */
    public int getNumFichasTablero() {
        return fichasTablero.size();
    }

    /**
     * Añade jugadores hasta completar el array que los contiene en la mesa, pidiendo datos por teclado.
     */
    public void añadirJugadores() {
        String nombre;
        int manoElegida = Integer.MIN_VALUE;
        Jugador aux;

        // Pedir nombres de jugadores, crearlos y repartirles cartas
        for (int i = 0; i < jugadores.length; ++i) {
            boolean nombreLibre;
            do {
                System.out.println("\nJugador " + (i + 1));
                System.out.println("Introduce tu nombre: ");
                nombre = JuegoDomino.getTeclado().nextLine().trim();
                nombreLibre = true;
                
                // Ver si ese nombre ya fue elegido por otro jugador
                // (Necesario para garantizar que el ganador en caso de empate se calcule bien)
                int k = i;
                while (nombreLibre && k > 0) {
                    Jugador j = jugadores[--k];
                    if (j.getNombre().equals(nombre)) {
                        nombreLibre = false;
                    }
                }
                
                if (!nombreLibre) {
                    System.out.println("Ese nombre ya fue elegido por otro jugador. Escribe otro.");
                }
            } while (nombre.isEmpty() || !nombreLibre);
            
            jugadores[i] = new Jugador(nombre, this);
            monton.repartirFichas(jugadores[i]);
        }

        // Preguntar cuál de los jugadores será mano
        do {
            try {
                int i = 0;
                
                System.out.println("\nIntroduce qué jugador será mano [1 - " + jugadores.length + "]: ");
                while (i < jugadores.length) {
                    System.out.println("[" + (i + 1) + "] " + jugadores[i].getNombre());
                    ++i;
                }

                manoElegida = Integer.parseUnsignedInt(JuegoDomino.getTeclado().nextLine());
            } catch (NumberFormatException exc) {
                System.out.println("El número introducido no es correcto (1 - " + jugadores.length + ").");

            }
        } while (manoElegida < 1 || manoElegida > jugadores.length);

        // Poner el jugador que lleva la mano en la primera posición del array
        aux = jugadores[0];
        jugadores[0] = jugadores[--manoElegida];
        jugadores[manoElegida] = aux;
    }
    
    /**
     * Devuelve una representación textual de la mesa.
     * @return Devuelve una representación en texto de las fichas que hay en el tablero.
     */
    @Override
    public String toString() {
        return fichasTablero.toString();
    }
}
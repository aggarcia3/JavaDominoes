package juegodomino;

import java.util.ArrayList;

/**
 * Modela un jugador de dominó.
 * @author Rubén Vilas Martinez
 * @author Irene Fernández Mariño
 * @author Alejandro Gonzalez García
 * @author Daniel Gómez Rodríguez
 */
public final class Jugador {
    private final String nombre;
    private final ArrayList<Ficha> fichasMano;
    private final Mesa mesa;
    
    /**
     * Inicializa un jugador, con un nombre y mesa determinados, sin fichas en la mano.
     * @param nombre El nombre del jugador.
     * @param mesa La mesa en la que juega.
     */
    public Jugador(String nombre, Mesa mesa) {
        this.nombre = nombre;
        this.mesa = mesa;
        this.fichasMano = new ArrayList<>(21); // No puede tener más de 21 fichas (28-7)
    }

    /**
     * Obtiene el nombre del jugador.
     * @return El nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Añade una ficha a la mano del jugador.
     * @param f La ficha a añadir.
     */
    public void añadirFichaMano(Ficha f) {
        fichasMano.add(f);
    }
    
    /**
     * Obtiene una ficha de la mano del jugador.
     * @param i La posición que ocupa la ficha deseada.
     * @return La ficha en esa posición. Lanza {@link IndexOutOfBoundsException} si {@code i} es un índice inválido ({@code i < 0 || i > fichasMano.size() - 1}).
     */
    public Ficha getFichaMano(int i) throws IndexOutOfBoundsException {
        return fichasMano.get(i);
    }

    /**
     * Devuelve el número de fichas que el jugador tiene en la mano.
     * @return El número de fichas en posesión del jugador.
     */
    public int getNumFichasMano() {
        return fichasMano.size();
    }
    
    /**
     * Borra una ficha de la mano del jugador.
     * @param f La ficha a borrar.
     * @return {@code true} si fue borrada de la mano, {@code false} si no estaba en la mano.
     */
    public boolean borrarFichaMano(Ficha f) {
        return fichasMano.remove(f);
    }
    
    /**
     * Roba una ficha del montón, si todavía quedan fichas.
     * @return La ficha que se robó si fue posible hacerlo, {@code null} si no.
     */
    public Ficha robarFicha() {
        Ficha toret;
        
        try {
            toret = mesa.getMonton().sacarFicha();
            añadirFichaMano(toret);
        } catch (Monton.MontonVacioException exc) {
            toret = null;
        }
        
        return toret;
    }
    
    /**
     * Devuelve un {@link ArrayList} de fichas en la mano del jugador que se pueden colocar.
     * @return Un {@link ArrayList} de fichas en la mano del jugador que se pueden colocar.
     */
    public ArrayList<Ficha> obtenerFichasJugables() {
        ArrayList<Ficha> toret = new ArrayList<>();

        // Si hay fichas en el tablero, hacer comprobaciones. Si no, es obvio que puede jugar con todas
        if (mesa.getPrimeraFicha() != null) {
            for (Ficha f : fichasMano) {
                if (!f.encajaEn(mesa, false).equals(Juego.Lados.NINGUNO)) {
                    toret.add(f);
                }
            }
        } else {
            toret = fichasMano;
        }

        return toret;
    }

    /**
     * Juega una ficha en la mesa asociada al jugador.
     * @param f La ficha a jugar.
     * @param lado El lado o lados donde la ficha se puede jugar. Si no se puede jugar en ningún lado, la función deja el tablero y la mano como están.
     */
    public void jugarFicha(Ficha f, Juego.Lados lado) {
        switch (lado) {
            case IZQUIERDA:
                mesa.añadirFichaPrincipio(f);
                //System.err.println("Se juega extremo " + f.getNumero1() + ". Veces jugado: " + mesa.obtenerExtremosJugados()[f.getNumero1()]); // Mensaje depuración
                borrarFichaMano(f);
                break;
            case DERECHA:
                mesa.añadirFichaFinal(f);
                //System.err.println("Se juega extremo " + f.getNumero2() + ". Veces jugado: " + mesa.obtenerExtremosJugados()[f.getNumero2()]); // Mensaje depuración
                borrarFichaMano(f);
                break;
            default:
                break;
        }
    }

    /**
     * Devuelve una cadena de texto con una representación de las fichas que tiene el jugador en mano.
     * @return Una cadena de texto con una representación de las fichas que tiene el jugador en mano.
     */
    @Override
    public String toString() {
        StringBuilder toret = new StringBuilder();
        
        for (Ficha f : fichasMano) {
            toret.append(f);
        }
        
        return toret.toString();
    }
}
package juegodomino;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase de control de la ejecución del juego de dominó.
 * @author Rubén Vilas Martinez
 * @author Irene Fernández Mariño
 * @author Alejandro Gonzalez García
 * @author Daniel Gómez Rodríguez
 */
public final class Juego {
    /**
     * Lados en los que se pueden insertar o ver fichas en el juego.
     */
    public static enum Lados { NINGUNO, IZQUIERDA, DERECHA };
    
    /**
     * Jugadas que se pueden realizar.
     */
    private static enum Jugadas { PONER, ROBAR, PASAR };
    
    private static final Scanner TECLADO = JuegoDomino.getTeclado();

    private static Mesa mesa;
    private static int[] puntuaciones;

    /**
     * Ejecuta una partida de dominó.
     */
    public static void iniciarJuego() {
        // Pedir cuántos jugadores habrá
        int numJug = 0;
        do {
            try {
                System.out.print("¿Cuántos jugadores van a jugar? [2-4]: ");
                numJug = Integer.parseUnsignedInt(TECLADO.nextLine());
                if (numJug < 2 || numJug > 4) {
                    System.out.println("El número de jugadores debe de estar entre 2 y 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("No se ha introducido un número válido.");
            }
        } while (numJug < 2 || numJug > 4);

        // Inicializaciones
        mesa = new Mesa(numJug);
        puntuaciones = new int[numJug];
        
        // Control de turnos
        int i = 0;
        ArrayList<Ficha> fichasJugables;
        Jugador j;
        do {
            // Obtener jugador actual e información suya
            j = mesa.getJugadores()[i];
            fichasJugables = j.obtenerFichasJugables();
            
            // Mostrar información de la mesa y del jugador
            System.out.println("\t* * * MESA * * *");
            System.out.println(mesa);
            System.out.println("\nFichas en montón: " + mesa.getMonton().getNumFichas());
            System.out.println("\n\tEs el turno de " + j.getNombre());
            System.out.println(j);
            System.out.println("\nPuedes jugar con:\n" + fichasJugables + "\n");

            switch (decidirJugada(fichasJugables.size() > 0)) {
                case PONER:
                    Ficha fichaElegida = elegirFicha(fichasJugables);
                    System.out.println("Has puesto la ficha " + fichaElegida + ".");
                    j.jugarFicha(fichaElegida, fichaElegida.encajaEn(mesa, true));
                    break;
                case ROBAR:
                    Ficha fichaRobada = j.robarFicha();
                    Lados encajeFicha = fichaRobada.encajaEn(mesa, false);
                    
                    System.out.println("Has robado la ficha " + fichaRobada + ".");
                    
                    if (!encajeFicha.equals(Lados.NINGUNO)) {
                        System.out.println("Has puesto la ficha " + fichaRobada + ".");
                        // Girarla si hace falta
                        // No se hizo antes para mostrar mensajes consistentes al usuario
                        // (No es lo mismo poner la ficha [ 1 | 6 ] que [ 6 | 1 ], p. ej.)
                        fichaRobada.encajaEn(mesa, true);
                        j.jugarFicha(fichaRobada, encajeFicha);
                    } else {
                        System.out.println("La ficha robada no se puede jugar.");
                    }
                    
                    break;
                case PASAR:
                    break;
            }
            System.out.print("\nPresiona Entrar para pasar al siguiente turno.");
            TECLADO.nextLine();    // Esperar a que el usuario pulse Entrar
            System.out.println();
            
            // Pasar al siguiente jugador
            if ((++i) > numJug - 1) {
                i = 0;
            }
        } while (!esCierre() && !esDomino(j));

        // Mostrar cómo acabó la partida
        System.out.println("\n\n\t\t* * * FIN DE LA PARTIDA * * *\nMESA:\n" + mesa);
        for (i = 0; i < numJug; ++i) {
            int n = mesa.getJugadores()[i].getNumFichasMano();
            if (n > 0) {
                System.out.println("\n" + mesa.getJugadores()[i].getNombre()
                        + " se ha quedado con " + n
                        + " ficha(s): \n" + mesa.getJugadores()[i]);
            }
            
            if (!esDomino(j) && esCierre()) {
                puntuaciones[i] = calcularPuntuacion(mesa.getJugadores()[i]);
                System.out.println("PUNTUACIÓN: " + puntuaciones[i]);
            }
        }
        
        // Determinar y mostrar el ganador
        if (esDomino(j)) {
            // Victoria por dominó
            System.out.println("\n" + j.getNombre() + " dominó la partida.\n\n¡¡ENHORABUENA, HAS GANADO!!");
        } else {
            // Victoria por cierre
            System.out.println("\n");
            List<Jugador> ganadoresCierre = obtenerGanadoresCierre();
            
            // Ver si hubo empate o no
            if (ganadoresCierre.size() == 1) {
                System.out.print(ganadoresCierre.get(0).getNombre() + " es el jugador con menor puntuación.\n\n¡¡ENHORABUENA, HAS GANADO!!");
            } else {
                // Mostrar quiénes han empatado
                for (int k = 0; k < ganadoresCierre.size(); ++k) {
                    System.out.print(ganadoresCierre.get(k).getNombre());
                    if (k < ganadoresCierre.size() - 1) {
                        if (k == ganadoresCierre.size() - 2) {
                            System.out.print(" y");
                        } else {
                            System.out.print(",");
                        }
                        System.out.print(" ");
                    }
                }
                System.out.print(" han empatado.");
                
                // Ver si entre los que empataron está el jugador que lleva la mano
                int k = 0;
                String nombreMano = mesa.getJugadores()[0].getNombre();
                Jugador jugadorActual = ganadoresCierre.get(k);
                
                while (!jugadorActual.getNombre().equals(nombreMano) && k < ganadoresCierre.size()) {
                    jugadorActual = ganadoresCierre.get(k++);
                }
                
                // Finalmente, determinar el ganador
                if (jugadorActual.getNombre().equals(nombreMano)) {
                    System.out.println("Gana el jugador que lleva la mano, " + nombreMano + ".\n\n¡¡ENHORABUENA!!");
                } else {
                    System.out.println("Ganan todos los jugadores que han empatado.\n\n¡¡ENHORABUENA A TODOS!!");
                }
            }
        }
        
        System.out.println("\n\n¡Gracias por jugar!\n");
    }
    
    /**
     * Le muestra al jugador la jugada que puede realizar.
     * @param puedePoner Representa si el jugador puede poner fichas o no. {@code true} si y solo si puede.
     * @return Un enumerado {@link Jugadas} representando la jugada a realizar.
     */
    private static Jugadas decidirJugada(boolean puedePoner) {
        Jugadas toret;

        System.out.print("Puedes ");
        
        // Mostrar decisiones posibles
        if (puedePoner) {
            System.out.print("poner una ficha.");
            toret = Jugadas.PONER;
        } else {
            if (mesa.getMonton().getNumFichas() == 0) {
                System.out.print("pasar turno.");
                toret = Jugadas.PASAR;
            } else {
                System.out.print("robar una ficha del montón.");
                toret = Jugadas.ROBAR;
            }
        }
        System.out.println();
        
        return toret;
    }
    
    /**
     * Le pide al jugador que elija una ficha de su mano para jugar.
     * @param fichasJugables Fichas que puede jugar.
     * @return La ficha que el jugador quiere jugar.
     */
    private static Ficha elegirFicha(ArrayList<Ficha> fichasJugables) {
        int i = 0;
        
        if (fichasJugables.size() > 1) {
            do {
                try {
                    System.out.print("\nDe las fichas con las que puedes jugar, ¿cuál quieres poner? [1 - " + fichasJugables.size() + "]: ");
                    i = Integer.parseUnsignedInt(TECLADO.nextLine()) - 1;
                } catch (NumberFormatException exc) {
                    i = Integer.MIN_VALUE;
                }
            } while (i < 0 || i >= fichasJugables.size());
        }

        return fichasJugables.get(i);
    }
    
    /**
     * Comprueba si la partida finaliza por cierre (es decir, si los números que están en los extremos han sido jugados 7 veces).
     * @return {@code true} en caso afirmativo, {@code false} en caso contrario.
     */
    private static boolean esCierre() {
        boolean toret = mesa.getPrimeraFicha() != null;

        if (toret) {
            int extremoIzq = mesa.getPrimeraFicha().getNumero1();
            int extremoDer = mesa.getUltimaFicha().getNumero2();
            int vecesJugadoIzq = 0;
            int vecesJugadoDer = 0;
            
            for (int i = 0; i < mesa.getNumFichasTablero(); ++i) {
                Ficha f = mesa.getFicha(i);
                
                // Ver cuántas veces se jugó el extremo izquierdo
                if (f.getNumero1() == extremoIzq || f.getNumero2() == extremoIzq) {
                    ++vecesJugadoIzq;
                }
                
                // Ver cuántas veces se jugó el derecho
                if (f.getNumero1() == extremoDer || f.getNumero2() == extremoDer) {
                    ++vecesJugadoDer;
                }
            }
            
            toret = vecesJugadoIzq == 7 && vecesJugadoDer == 7;
        }

        return toret;
    }
    
    /**
     * Comprueba si el jugador ha hecho dominó (se ha quedado sin fichas en mano).
     * @param j Jugador a comprobar.
     * @return {@code true} si hizo dominó, {@code false} en caso contrario.
     */
    private static boolean esDomino(Jugador j) {
        return j.getNumFichasMano() == 0;
    }
    
    /**
     * Devuelve la puntuación del jugador (igual a la suma de todos los números de todas las fichas en su mano).
     * @param j El jugador a calcular su puntuación.
     * @return La puntuación del jugador.
     */
    private static int calcularPuntuacion(Jugador j) {
        int toret = 0;

        for (int i = 0; i < j.getNumFichasMano(); ++i) {
            Ficha f = j.getFichaMano(i);
            toret += f.getNumero1();
            toret += f.getNumero2();
        }
        
        return toret;
    }
    
    /**
     * Devuelve el jugador ganador en caso de cierre (aquel con menor puntuación).
     * @return Un array con los jugadores que han ganado. Normalmente solo gana uno, pero en caso de empate puede haber más.
     */
    private static List<Jugador> obtenerGanadoresCierre() {
        Jugador pivote = null;
        ArrayList<Jugador> toret = new ArrayList<>(mesa.getJugadores().length);
        
        // Ver uno de los jugadores con menos puntuación
        for (Jugador j : mesa.getJugadores()) {
            if (pivote == null || calcularPuntuacion(j) < calcularPuntuacion(pivote)) {
                pivote = j;
            }
        }
        
        // Añadir posibles empates a la lista de ganadores
        toret.add(pivote);
        for (Jugador j : mesa.getJugadores()) {
            if (j != pivote && calcularPuntuacion(j) == calcularPuntuacion(pivote)) {
                toret.add(j);
            }
        }
        
        return toret;
    }
}
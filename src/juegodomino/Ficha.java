package juegodomino;

/**
 * Fichas de dominó.
 * @author Rubén Vilas Martinez
 * @author Irene Fernández Mariño
 * @author Alejandro Gonzalez García
 * @author Daniel Gómez Rodríguez
 */
public final class Ficha {
    private int numero1;
    private int numero2;

    /**
     * Crea una ficha con dos números.
     * @param numero1 El primer número de la ficha, como {@code int}.
     * @param numero2 El segundo número de la ficha, como {@code int}.
     */
    public Ficha(int numero1, int numero2) {
        this.numero1 = numero1;
        this.numero2 = numero2;
    }

    /**
     * Devuelve el primer número de la ficha.
     * @return El primer número de la ficha, como {@code int}.
     */
    public int getNumero1() {
        return numero1;
    }

    /**
     * Devuelve el segundo número de la ficha.
     * @return El segundo número de la ficha, como {@code int}.
     */
    public int getNumero2() {
        return numero2;
    }

    /**
     * Comprueba que la ficha actual puede colocarse al lado de alguna de la fichas de los extremos.
     * @param m Mesa que contiene el tablero donde se harán las comprobaciones.
     * @param girar {@code true} si la ficha se debe de girar automáticamente de ser necesario (se va a colocar), {@code false} si no.
     * @return Un enumerado {@link Juego.Lados} que representa en qué extremos encaja y no encaja la ficha.
     * Puede darse el caso de que la ficha encaje, pero que sea necesario rotarla para que eso se pueda comprobar de modo evidente y no se generen errores en tiempo de ejecución.
     * Al ser insertada por la izquierda, eso ocurre cuando {@code getNumero2()} no es igual al extremo izquierdo del tablero. Al ser insertada por la derecha, cuando {@code getNumero1()} no es igual al extremo derecho del tablero. 
     */
    public Juego.Lados encajaEn(Mesa m, boolean girar) {
        // A: Encaja en el lado izquierdo (esto pasa si el extremo izquierdo es igual a alguno de los dos números de la ficha)
        // B: Encaja en el derecho (esto pasa si el extremo derecho es igual a alguno de los dos números de la ficha)
        // Encaja en ambos: A && B
        // Encaja en ninguno: !A && !B <=> !(A || B) (De Morgan)
        Juego.Lados toret;
        
        boolean encajaEnIzquierdo = m.getPrimeraFicha() == null;
        boolean encajaEnDerecho;
        int extremoIzquierdo = encajaEnIzquierdo ? Integer.MIN_VALUE : m.getPrimeraFicha().getNumero1();
        int extremoDerecho = encajaEnIzquierdo ? Integer.MIN_VALUE : m.getUltimaFicha().getNumero2();
        
        // Si no hay fichas en tablero, considerar que encaja por el lado izquierdo
        // Si las hay, analizar si se cumplen A y/o B
        if (encajaEnIzquierdo) {
            toret = Juego.Lados.IZQUIERDA;
        } else {
            // Ver si se cumple A
            encajaEnIzquierdo = extremoIzquierdo == getNumero1() || extremoIzquierdo == getNumero2();
            
            // Ver si se cumple B
            encajaEnDerecho = extremoDerecho == getNumero1() || extremoDerecho == getNumero2();
            
            // Darle el valor correspondiente al enumerado
            if (encajaEnIzquierdo) {    // (Si encaja en ambos, se pondrá por el lado izquierdo)
                toret = Juego.Lados.IZQUIERDA;
                if (girar) {
                    girarFicha(m, toret);  // ¡Asegurarse de que se gira si hace falta!
                }
            } else if (encajaEnDerecho) {
                toret = Juego.Lados.DERECHA;
                if (girar) {
                    girarFicha(m, toret);  // ¡Asegurarse de que se gira si hace falta!
                }
            } else {
                toret = Juego.Lados.NINGUNO;
            }
        }
        
        return toret;
    }

    /**
     * Representa la ficha en formato textual.
     * @return La ficha como {@code String}, en formato {@code "[ numero1 | numero2 ]"}.
     */
    @Override
    public String toString() {
        StringBuilder toret = new StringBuilder(" [");
        toret.append(getNumero1());
        toret.append("|");
        toret.append(getNumero2());
        toret.append("] ");
        return toret.toString();
    }
    
    /**
     * Gira la ficha si es necesario. Ver nota en la documentación del método {@code encajaEn} para una definición de necesario.
     * @param m La mesa donde se colocará la ficha a girar.
     * @param lado El lado donde se colocará la ficha a girar.
     * @throws NullPointerException Si no hay fichas en la mesa.
     */
    private void girarFicha(Mesa m, Juego.Lados lado) throws NullPointerException {
        int extremoIzquierdo = m.getPrimeraFicha().getNumero1();
        int extremoDerecho = m.getUltimaFicha().getNumero2();
        
        if ((lado.equals(Juego.Lados.IZQUIERDA) && (extremoIzquierdo != getNumero2())) ||
            (lado.equals(Juego.Lados.DERECHA) && extremoDerecho != getNumero1())) {
            girarFicha();
        }
    }
    
    /**
     * Gira la ficha, de modo que el primer número pase a ser el segundo y viceversa.
     */
    private void girarFicha() {
        int aux = getNumero1();
        numero1 = numero2;
        numero2 = aux;
    }
}
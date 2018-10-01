package ar.edu.ucc;

public class MiClase {

    public int multiplicar(int x, int y) {
        if (x > 999) {
            throw new IllegalArgumentException("X debe ser menor a 1000");
        }
        return x * y;
    }

}

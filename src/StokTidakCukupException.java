/**
 * [OOP: Inheritance] Exception buatan sendiri, turunan dari class Exception.
 * Dilempar ketika jumlah barang yang diminta melebihi stok.
 */
public class StokTidakCukupException extends Exception {

    public StokTidakCukupException(String pesan) {
        super(pesan); // memanggil constructor class induk (Exception)
    }
}

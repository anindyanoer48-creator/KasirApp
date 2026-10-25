/**
 * [OOP: Inheritance] ProdukMakanan mewarisi Produk (extends).
 * Atribut tambahan khusus makanan: tanggal kedaluwarsa.
 */
public class ProdukMakanan extends Produk {

    private String kedaluwarsa; // atribut milik subclass

    public ProdukMakanan(String kode, String nama, int harga, int stok, String kedaluwarsa) {
        super(kode, nama, harga, stok); // memanggil constructor Produk
        this.kedaluwarsa = kedaluwarsa;
    }

    public String getKedaluwarsa() { return kedaluwarsa; }

    // [OOP: Overriding / Polymorphism] implementasi method abstract
    @Override
    public String getKategori() { return "Makanan"; }

    @Override
    public double getDiskonPersen() { return 5.0; } // diskon makanan 5%

    @Override
    public String getInfoTambahan() { return "Exp: " + kedaluwarsa; }
}

/**
 * [OOP: Inheritance] ProdukMinuman mewarisi Produk.
 * Atribut tambahan khusus minuman: ukuran dalam ml.
 */
public class ProdukMinuman extends Produk {

    private int ukuranMl;

    public ProdukMinuman(String kode, String nama, int harga, int stok, int ukuranMl) {
        super(kode, nama, harga, stok);
        this.ukuranMl = ukuranMl;
    }

    public int getUkuranMl() { return ukuranMl; }

    @Override
    public String getKategori() { return "Minuman"; }

    @Override
    public double getDiskonPersen() { return 10.0; } // diskon minuman 10%

    @Override
    public String getInfoTambahan() { return ukuranMl + " ml"; }
}

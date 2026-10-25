/**
 * [OOP: Inheritance] ProdukSembako mewarisi Produk.
 * Atribut tambahan khusus sembako: satuan (kg, liter, dll).
 */
public class ProdukSembako extends Produk {

    private String satuan;

    public ProdukSembako(String kode, String nama, int harga, int stok, String satuan) {
        super(kode, nama, harga, stok);
        this.satuan = satuan;
    }

    public String getSatuan() { return satuan; }

    @Override
    public String getKategori() { return "Sembako"; }

    @Override
    public double getDiskonPersen() { return 0.0; } // sembako tanpa diskon

    @Override
    public String getInfoTambahan() { return "per " + satuan; }
}

/**
 * [OOP: Abstraction] Abstract class: kerangka umum sebuah produk.
 * Class ini TIDAK bisa di-new langsung, hanya boleh diturunkan.
 *
 * [OOP: Encapsulation] Semua atribut bersifat private,
 * diakses lewat getter/setter yang sudah dilengkapi validasi.
 */
public abstract class Produk {

    // ===== ATRIBUT (private = enkapsulasi) =====
    private String kode;
    private String nama;
    private int harga;
    private int stok;

    // ===== CONSTRUCTOR =====
    public Produk(String kode, String nama, int harga, int stok) {
        this.kode = kode;
        setNama(nama);   // memakai setter agar validasi ikut berjalan
        setHarga(harga);
        setStok(stok);
    }

    // ===== GETTER =====
    public String getKode()  { return kode; }
    public String getNama()  { return nama; }
    public int getHarga()    { return harga; }
    public int getStok()     { return stok; }

    // ===== SETTER (dengan validasi) =====
    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public void setHarga(int harga) {
        if (harga < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif.");
        }
        this.harga = harga;
    }

    public void setStok(int stok) {
        if (stok < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif.");
        }
        this.stok = stok;
    }

    // ===== METHOD ABSTRACT =====
    // Tidak punya isi. Wajib di-override oleh setiap subclass.
    public abstract String getKategori();

    public abstract double getDiskonPersen();

    // ===== METHOD BIASA (diwariskan ke subclass) =====
    // Subclass boleh meng-override jika perlu (di sini dibiarkan kosong).
    public String getInfoTambahan() {
        return "-";
    }

    // Method ini memanggil method abstract: yang dijalankan adalah
    // getDiskonPersen() milik subclass (polimorfisme runtime).
    public int getHargaSetelahDiskon() {
        return harga - (int) Math.round(harga * getDiskonPersen() / 100.0);
    }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > stok) {
            throw new StokTidakCukupException(
                    "Stok " + nama + " tidak cukup. Tersisa: " + stok);
        }
        stok -= jumlah;
    }

    // [OOP: Overriding] meng-override toString() milik class Object
    @Override
    public String toString() {
        return kode + " - " + nama + " (" + getKategori() + ")";
    }
}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Satu transaksi pembayaran.
 *
 * [OOP: Interface] mengimplementasikan Cetakable, sama seperti ItemKeranjang,
 *   tetapi isi cetak() berbeda (struk lengkap).
 * [OOP: Polymorphism] di dalam cetak(), setiap item dipanggil lewat
 *   tipe interface Cetakable.
 */
public class Transaksi implements Cetakable {

    private final LocalDateTime waktu = LocalDateTime.now();
    private final List<ItemKeranjang> items;
    private final long total;
    private final long bayar;

    public Transaksi(Keranjang keranjang, long bayar) {
        if (keranjang.isEmpty()) {
            throw new IllegalArgumentException("Keranjang masih kosong.");
        }
        if (bayar < keranjang.getTotal()) {
            throw new IllegalArgumentException("Uang yang dibayarkan kurang!");
        }
        this.items = new ArrayList<>(keranjang.getItems()); // salinan
        this.total = keranjang.getTotal();
        this.bayar = bayar;
    }

    public long getKembali() { return bayar - total; }

    /** Mengurangi stok semua produk yang dibeli. */
    public void proses() throws StokTidakCukupException {
        for (ItemKeranjang item : items) {
            item.getProduk().kurangiStok(item.getQty());
        }
    }

    @Override
    public String cetak() {
        String garis = "--------------------------------------\n";
        StringBuilder sb = new StringBuilder();
        sb.append("           TOKO SERBA ADA\n");
        sb.append("      Jl. Contoh No. 123, Indonesia\n");
        sb.append(garis);
        sb.append("Tanggal : ").append(waktu.format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n");
        sb.append(garis);

        for (Cetakable item : items) {   // tipe interface
            sb.append(item.cetak());     // dipanggil -> versi ItemKeranjang
        }

        sb.append(garis);
        sb.append(String.format("%-10s %26s%n", "TOTAL", Rupiah.format(total)));
        sb.append(String.format("%-10s %26s%n", "BAYAR", Rupiah.format(bayar)));
        sb.append(String.format("%-10s %26s%n", "KEMBALI", Rupiah.format(getKembali())));
        sb.append(garis);
        sb.append("     Terima kasih atas kunjungan Anda!\n");
        return sb.toString();
    }
}

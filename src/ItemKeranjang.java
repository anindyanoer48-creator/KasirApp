/**
 * Satu baris belanjaan: sebuah produk + jumlahnya.
 *
 * [OOP: Interface] mengimplementasikan Cetakable.
 * [OOP: Polymorphism] atribut 'produk' bertipe Produk (class induk),
 *   isinya bisa ProdukMakanan / ProdukMinuman / ProdukSembako.
 */
public class ItemKeranjang implements Cetakable {

    private final Produk produk;
    private int qty;

    public ItemKeranjang(Produk produk, int qty) {
        this.produk = produk;
        this.qty = qty;
    }

    public Produk getProduk() { return produk; }
    public int getQty()       { return qty; }

    public void tambahQty(int jumlah) { this.qty += jumlah; }

    public long getSubtotal() {
        // hasilnya berbeda-beda tergantung jenis produk (diskon tiap subclass)
        return (long) produk.getHargaSetelahDiskon() * qty;
    }

    @Override
    public String cetak() {
        String s = produk.getNama() + "\n  " + qty + " x "
                + Rupiah.format(produk.getHargaSetelahDiskon());
        if (produk.getDiskonPersen() > 0) {
            s += " (diskon " + (int) produk.getDiskonPersen() + "%)";
        }
        return s + " = " + Rupiah.format(getSubtotal()) + "\n";
    }
}

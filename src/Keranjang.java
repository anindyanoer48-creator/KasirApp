import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Keranjang belanja: kumpulan ItemKeranjang.
 *
 * [OOP: Encapsulation] daftar item bersifat private; dunia luar hanya
 *   bisa membaca (read-only) lewat getItems().
 * [OOP: Overloading] method tambah() ditulis dua kali dengan parameter berbeda.
 */
public class Keranjang {

    private final List<ItemKeranjang> items = new ArrayList<>();

    // ----- Overloading #1: tambah 1 buah -----
    public void tambah(Produk produk) throws StokTidakCukupException {
        tambah(produk, 1);
    }

    // ----- Overloading #2: tambah sebanyak qty -----
    public void tambah(Produk produk, int qty) throws StokTidakCukupException {
        if (qty <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0.");
        }
        ItemKeranjang ada = cari(produk);
        int sudahDiKeranjang = (ada == null) ? 0 : ada.getQty();

        if (sudahDiKeranjang + qty > produk.getStok()) {
            throw new StokTidakCukupException(
                    "Stok " + produk.getNama() + " tidak cukup.\nStok tersisa: "
                    + produk.getStok() + ", di keranjang: " + sudahDiKeranjang);
        }
        if (ada == null) {
            items.add(new ItemKeranjang(produk, qty));
        } else {
            ada.tambahQty(qty);
        }
    }

    private ItemKeranjang cari(Produk produk) {
        for (ItemKeranjang item : items) {
            if (item.getProduk() == produk) {
                return item;
            }
        }
        return null;
    }

    public void hapus(int index) {
        items.remove(index);
    }

    public void kosongkan() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public long getTotal() {
        long total = 0;
        for (ItemKeranjang item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public List<ItemKeranjang> getItems() {
        return Collections.unmodifiableList(items);
    }
}

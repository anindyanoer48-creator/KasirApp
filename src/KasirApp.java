import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Aplikasi Point of Sales (Kasir) Sederhana.
 *
 * [OOP: Inheritance] KasirApp extends JFrame -> mewarisi semua kemampuan jendela.
 * [OOP: Encapsulation] semua atribut private.
 * [OOP: Polymorphism] daftarProduk bertipe Map<String, Produk>, isinya campuran
 *   ProdukMakanan, ProdukMinuman, ProdukSembako.
 */
public class KasirApp extends JFrame {

    // ===== ATRIBUT =====
    private final Map<String, Produk> daftarProduk = new LinkedHashMap<>();
    private final Keranjang keranjang = new Keranjang();

    // [OOP: Inheritance + Overriding] class anonim turunan DefaultTableModel
    private final DefaultTableModel modelProduk = new DefaultTableModel(
            new String[]{"Kode", "Nama", "Kategori", "Info", "Harga", "Diskon", "Harga Akhir", "Stok"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };
    private final DefaultTableModel modelKeranjang = new DefaultTableModel(
            new String[]{"Nama", "Harga Akhir", "Qty", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) { return false; }
    };

    private final JTable tabelProduk = new JTable(modelProduk);
    private final JTable tabelKeranjang = new JTable(modelKeranjang);
    private final JSpinner spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
    private final JLabel lblTotal = new JLabel("Rp 0");
    private final JLabel lblKembali = new JLabel("Rp 0");
    private final JTextField txtBayar = new JTextField();

    // ===== CONSTRUCTOR =====
    public KasirApp() {
        super("Kasir Sederhana - Point of Sales"); // constructor class induk (JFrame)
        isiDataAwal();
        buatUI();
        refreshTabelProduk();
        refreshKeranjang();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 640);
        setLocationRelativeTo(null);
    }

    // [OOP: Polymorphism - upcasting] objek subclass disimpan sebagai tipe Produk
    private void tambahProduk(Produk p) {
        daftarProduk.put(p.getKode(), p);
    }

    private void isiDataAwal() {
        tambahProduk(new ProdukMakanan("P001", "Indomie Goreng", 3500, 100, "12-2027"));
        tambahProduk(new ProdukMinuman("P002", "Aqua 600ml", 4000, 80, 600));
        tambahProduk(new ProdukMinuman("P003", "Teh Botol Sosro", 5000, 60, 450));
        tambahProduk(new ProdukMakanan("P004", "Roti Tawar", 15000, 25, "30-10-2026"));
        tambahProduk(new ProdukSembako("P005", "Telur", 28000, 40, "kg"));
        tambahProduk(new ProdukSembako("P006", "Gula Pasir", 17000, 30, "kg"));
    }

    // ===== TAMPILAN =====
    private void buatUI() {
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabelProduk.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelKeranjang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelProduk.setRowHeight(24);
        tabelKeranjang.setRowHeight(24);

        // Klik dua kali pada produk = tambah 1 buah (memakai overloading tambah(Produk))
        tabelProduk.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) tambahSatu();
            }
        });

        // --- Panel kiri: produk ---
        JPanel kiri = new JPanel(new BorderLayout(5, 5));
        kiri.setBorder(BorderFactory.createTitledBorder("Daftar Produk (klik 2x = tambah 1)"));
        kiri.add(new JScrollPane(tabelProduk), BorderLayout.CENTER);

        JPanel aksiProduk = new JPanel(new FlowLayout(FlowLayout.LEFT));
        aksiProduk.add(new JLabel("Qty:"));
        spinQty.setPreferredSize(new Dimension(70, 28));
        aksiProduk.add(spinQty);
        JButton btnTambah = new JButton("Tambah ke Keranjang →");
        btnTambah.addActionListener(e -> tambahBanyak());
        aksiProduk.add(btnTambah);
        JButton btnBaru = new JButton("+ Produk Baru");
        btnBaru.addActionListener(e -> dialogProdukBaru());
        aksiProduk.add(btnBaru);
        kiri.add(aksiProduk, BorderLayout.SOUTH);

        // --- Panel kanan: keranjang & pembayaran ---
        JPanel kanan = new JPanel(new BorderLayout(5, 5));
        kanan.setBorder(BorderFactory.createTitledBorder("Keranjang Belanja"));
        kanan.add(new JScrollPane(tabelKeranjang), BorderLayout.CENTER);

        JPanel bawah = new JPanel();
        bawah.setLayout(new BoxLayout(bawah, BoxLayout.Y_AXIS));

        JPanel aksiKeranjang = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnHapus = new JButton("Hapus Item");
        btnHapus.addActionListener(e -> hapusItem());
        JButton btnKosong = new JButton("Kosongkan");
        btnKosong.addActionListener(e -> {
            keranjang.kosongkan();
            refreshKeranjang();
        });
        aksiKeranjang.add(btnHapus);
        aksiKeranjang.add(btnKosong);
        bawah.add(aksiKeranjang);

        JPanel bayar = new JPanel(new GridLayout(3, 2, 5, 5));
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 20f));
        lblKembali.setFont(lblKembali.getFont().deriveFont(Font.BOLD, 16f));
        bayar.add(new JLabel("TOTAL:"));
        bayar.add(lblTotal);
        bayar.add(new JLabel("Uang Dibayar (Rp):"));
        bayar.add(txtBayar);
        bayar.add(new JLabel("Kembalian:"));
        bayar.add(lblKembali);
        bawah.add(bayar);

        // [OOP: Interface] DocumentListener diimplementasikan lewat class anonim
        txtBayar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { updateKembalian(); }
            @Override public void removeUpdate(DocumentEvent e)  { updateKembalian(); }
            @Override public void changedUpdate(DocumentEvent e) { updateKembalian(); }
        });

        JButton btnProses = new JButton("PROSES PEMBAYARAN");
        btnProses.setFont(btnProses.getFont().deriveFont(Font.BOLD, 14f));
        btnProses.setBackground(new Color(34, 139, 34));
        btnProses.setForeground(Color.WHITE);
        btnProses.setOpaque(true);
        btnProses.setBorderPainted(false);
        btnProses.setPreferredSize(new Dimension(100, 40));
        btnProses.addActionListener(e -> prosesPembayaran());
        JPanel pBtn = new JPanel(new BorderLayout());
        pBtn.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        pBtn.add(btnProses, BorderLayout.CENTER);
        bawah.add(pBtn);

        kanan.add(bawah, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, kiri, kanan);
        split.setResizeWeight(0.6);
        add(split, BorderLayout.CENTER);
    }

    // ===== METHOD LOGIKA =====
    private void refreshTabelProduk() {
        modelProduk.setRowCount(0);
        for (Produk p : daftarProduk.values()) {
            // [OOP: Polymorphism] p bertipe Produk, tetapi getKategori(),
            // getInfoTambahan() & getDiskonPersen() menjalankan versi subclass-nya
            modelProduk.addRow(new Object[]{
                    p.getKode(), p.getNama(), p.getKategori(), p.getInfoTambahan(),
                    Rupiah.format(p.getHarga()), (int) p.getDiskonPersen() + "%",
                    Rupiah.format(p.getHargaSetelahDiskon()), p.getStok()});
        }
    }

    private void refreshKeranjang() {
        modelKeranjang.setRowCount(0);
        for (ItemKeranjang item : keranjang.getItems()) {
            modelKeranjang.addRow(new Object[]{
                    item.getProduk().getNama(),
                    Rupiah.format(item.getProduk().getHargaSetelahDiskon()),
                    item.getQty(),
                    Rupiah.format(item.getSubtotal())});
        }
        lblTotal.setText(Rupiah.format(keranjang.getTotal()));
        updateKembalian();
    }

    private Produk produkTerpilih() {
        int row = tabelProduk.getSelectedRow();
        if (row < 0) {
            info("Pilih produk terlebih dahulu.");
            return null;
        }
        return daftarProduk.get((String) modelProduk.getValueAt(row, 0));
    }

    // Memanggil overloading tambah(Produk)
    private void tambahSatu() {
        Produk p = produkTerpilih();
        if (p == null) return;
        try {
            keranjang.tambah(p);
            refreshKeranjang();
        } catch (StokTidakCukupException ex) {
            info(ex.getMessage());
        }
    }

    // Memanggil overloading tambah(Produk, int)
    private void tambahBanyak() {
        Produk p = produkTerpilih();
        if (p == null) return;
        try {
            keranjang.tambah(p, (Integer) spinQty.getValue());
            spinQty.setValue(1);
            refreshKeranjang();
        } catch (StokTidakCukupException ex) {
            info(ex.getMessage());
        }
    }

    private void hapusItem() {
        int row = tabelKeranjang.getSelectedRow();
        if (row < 0) {
            info("Pilih item di keranjang yang akan dihapus.");
            return;
        }
        keranjang.hapus(row);
        refreshKeranjang();
    }

    private long ambilBayar() {
        String s = txtBayar.getText().replaceAll("[^0-9]", "");
        if (s.isEmpty() || s.length() > 12) return 0;
        return Long.parseLong(s);
    }

    private void updateKembalian() {
        long kembali = ambilBayar() - keranjang.getTotal();
        lblKembali.setText(kembali >= 0 ? Rupiah.format(kembali)
                                        : "Kurang " + Rupiah.format(-kembali));
    }

    private void prosesPembayaran() {
        try {
            Transaksi trx = new Transaksi(keranjang, ambilBayar());
            trx.proses(); // kurangi stok

            JTextArea area = new JTextArea(trx.cetak());
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area),
                    "Struk Pembayaran", JOptionPane.PLAIN_MESSAGE);

            keranjang.kosongkan();
            txtBayar.setText("");
            refreshKeranjang();
            refreshTabelProduk();
        } catch (IllegalArgumentException | StokTidakCukupException ex) {
            info(ex.getMessage());
        }
    }

    private void dialogProdukBaru() {
        JTextField fKode = new JTextField();
        JTextField fNama = new JTextField();
        JTextField fHarga = new JTextField();
        JTextField fStok = new JTextField();
        JComboBox<String> cbKategori = new JComboBox<>(new String[]{"Makanan", "Minuman", "Sembako"});
        JTextField fInfo = new JTextField();
        JLabel lblInfo = new JLabel("Tanggal kedaluwarsa (mis. 12-2027):");
        cbKategori.addActionListener(e -> {
            switch ((String) cbKategori.getSelectedItem()) {
                case "Makanan": lblInfo.setText("Tanggal kedaluwarsa (mis. 12-2027):"); break;
                case "Minuman": lblInfo.setText("Ukuran dalam ml (angka):"); break;
                default:        lblInfo.setText("Satuan (mis. kg, liter):");
            }
        });

        Object[] form = {"Kode:", fKode, "Nama:", fNama, "Harga:", fHarga, "Stok:", fStok,
                "Kategori:", cbKategori, lblInfo, fInfo};
        int ok = JOptionPane.showConfirmDialog(this, form, "Tambah Produk Baru",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (ok != JOptionPane.OK_OPTION) return;

        try {
            String kode = fKode.getText().trim().toUpperCase();
            String nama = fNama.getText().trim();
            int harga = Integer.parseInt(fHarga.getText().trim());
            int stok = Integer.parseInt(fStok.getText().trim());
            String infoTambahan = fInfo.getText().trim();

            if (kode.isEmpty()) { info("Kode tidak boleh kosong."); return; }
            if (daftarProduk.containsKey(kode)) { info("Kode produk sudah digunakan."); return; }

            // [OOP: Polymorphism] variabel bertipe Produk, objeknya subclass berbeda
            Produk baru;
            switch ((String) cbKategori.getSelectedItem()) {
                case "Makanan":
                    baru = new ProdukMakanan(kode, nama, harga, stok, infoTambahan);
                    break;
                case "Minuman":
                    baru = new ProdukMinuman(kode, nama, harga, stok, Integer.parseInt(infoTambahan));
                    break;
                default:
                    baru = new ProdukSembako(kode, nama, harga, stok, infoTambahan);
            }
            tambahProduk(baru);
            refreshTabelProduk();
        } catch (NumberFormatException ex) {
            info("Harga, stok, dan ukuran (untuk minuman) harus berupa angka.");
        } catch (IllegalArgumentException ex) {
            info(ex.getMessage());
        }
    }

    private void info(String pesan) {
        JOptionPane.showMessageDialog(this, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    // ===== TITIK MASUK PROGRAM =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KasirApp().setVisible(true));
    }
}

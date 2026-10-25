import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class utilitas (helper) untuk memformat angka menjadi Rupiah.
 * Constructor dibuat private agar class ini tidak bisa di-instansiasi,
 * semua method-nya static.
 */
public final class Rupiah {

    private static final NumberFormat FORMAT =
            NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

    private Rupiah() { }

    public static String format(long angka) {
        return "Rp " + FORMAT.format(angka);
    }
}

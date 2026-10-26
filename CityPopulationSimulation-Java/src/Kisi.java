/**
 *
 * @author Zeynep Uludağ - zeynep.uludag2@ogr.sakarya.edu.tr
 * @since 17.04.2026
 * <p>
 * Kisi sinifi - Bir mahallede yasayan kisinin bilgilerini tutar. Her kisinin benzersiz bir ID'si, adi, soyadi ve yasi vardir.
 * </p>
 */

public class Kisi {

    private int id;
    private String ad;
    private String soyad;
    private int yas;

    
    public Kisi(int id, String ad, String soyad, int yas) { // constructor
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.yas = yas;
    }

    
    public int getId() { // id, ad, soyad, yas getter setter fonksiyonlari
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public int getYas() {
        return yas;
    }

    public void setYas(int yas) {
        this.yas = yas;
    }

    
    public void yaslan() { /** kisiyi yaslandirir yasi 1 artirir */
        this.yas++;
    }

    @Override
    public String toString() {
        return id + " - " + ad + " " + soyad + " - " + yas;
    }
}

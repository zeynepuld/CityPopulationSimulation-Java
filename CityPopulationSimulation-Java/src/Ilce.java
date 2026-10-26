/**
 *
 * @author Zeynep Uludağ - zeynep.uludag2@ogr.sakarya.edu.tr
 * @since 17.04.2026
 * <p>
 * Ilce sinifi - Bir sehire bagli ilceyi temsil eder. Ilceye bagli mahalleler listesi tutulur.
 * </p>
 */

import java.util.ArrayList;
import java.util.List;


public class Ilce {

    private String ad;
    private List<Mahalle> mahalleler;

    
    public Ilce(String ad) { // kurucu fonksiyon constructor
        this.ad = ad;
        this.mahalleler = new ArrayList<>();
    }

    
    public String getAd() { // ad, mahalle, nufus getter setter fonksiyonlari
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public List<Mahalle> getMahalleler() {
        return mahalleler;
    }

    public void setMahalleler(List<Mahalle> mahalleler) {
        this.mahalleler = mahalleler;
    }

    
    public void mahalleEkle(Mahalle mahalle) { /** ilceye yeni bir mahalle ekler */
        this.mahalleler.add(mahalle);
    }

   
    public int getNufus() {  /** ilcenin toplam nufusunu hesaplar */
        int toplam = 0;
        for (Mahalle m : mahalleler) {
            toplam += m.getNufus();
        }
        return toplam;
    }

    @Override
    public String toString() {
        return "İlçe: " + ad + " - Nüfus: " + getNufus();
    }
}

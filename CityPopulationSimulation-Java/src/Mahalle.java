/**
 *
 * @author Zeynep Uludağ - zeynep.uludag2@ogr.sakarya.edu.tr
 * @since 17.04.2026
 * <p>
 * Mahalle sinifi - Bir ilceye bagli mahalleyi temsil eder. Mahallede yasayan kisiler listesi tutulur.
 * </p>
 */

import java.util.ArrayList;
import java.util.List;


public class Mahalle {

    private String ad;
    private List<Kisi> kisiler;

    
    public Mahalle(String ad) { // constructor
        this.ad = ad;
        this.kisiler = new ArrayList<>();
    }

    
    public String getAd() { // ad, kisiler, nufus getter setter
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public List<Kisi> getKisiler() {
        return kisiler;
    }

    public void setKisiler(List<Kisi> kisiler) {
        this.kisiler = kisiler;
    }

    
    public void kisiEkle(Kisi kisi) { /** mahalleye yeni bir kisi ekler */
        this.kisiler.add(kisi);
    }

    
    public int getNufus() { /** mahallenin anlik nufusunu dondurur */
        return kisiler.size();
    }

    @Override
    public String toString() {
        return "Mahalle: " + ad + " - Nüfus: " + getNufus();
    }
}

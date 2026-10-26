/**
 *
 * @author Zeynep Uludağ - zeynep.uludag2@ogr.sakarya.edu.tr
 * @since 17.04.2026
 * <p>
 * Sehir sinifi - Oyundaki bir sehri temsil eder. Sehre bagli ilceler, ilcelere bagli mahalleler tutulur. Baz nüfus artis katsayisi hesaplamak icin saklanir.
 * </p>
 */

import java.util.ArrayList;
import java.util.List;


public class Sehir {

    private String ad;
    private List<Ilce> ilceler;
    /**hesaplanmis optimize edilmis baslangic nufusu ("baz nufus") */
    private int bazNufus;

    public Sehir(String ad) { // kurucu fonks
        this.ad = ad;
        this.ilceler = new ArrayList<>();
        this.bazNufus = 0;
    }

    public String getAd() { // ad, ilceler, baznufus getter setter metotlari
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public List<Ilce> getIlceler() {
        return ilceler;
    }

    public void setIlceler(List<Ilce> ilceler) {
        this.ilceler = ilceler;
    }

    public int getBazNufus() {
        return bazNufus;
    }

    public void setBazNufus(int bazNufus) {
        this.bazNufus = bazNufus;
    }

    public void ilceEkle(Ilce ilce) { // sehre yeni bi ilce ekler
        this.ilceler.add(ilce);
    }

    public int getNufus() { // sehrin anlik nufusnu hesaplar
        int toplam = 0;
        for (Ilce i : ilceler) {
            toplam += i.getNufus();
        }
        return toplam;
    }

    /**
     * nufus artiş katsayisini hesaplar. katsayi = bazNufus'un onlar + birler basamagi toplamidir ve toplam 0 ise katsayi 1 kabul edilir (her mahalleye 1 kisi eklenir) */
     
    public int getArtisKatsayisi() {
        int onlar = (bazNufus / 10) % 10;
        int birler = bazNufus % 10;
        int toplam = onlar + birler;
        return (toplam == 0) ? 1 : toplam;
    }

    
    public List<Mahalle> getTumMahalleler() { /** sehrin tum mahallelerini liste seklinnde dondurur */
        List<Mahalle> liste = new ArrayList<>();
        for (Ilce i : ilceler) {
            liste.addAll(i.getMahalleler());
        }
        return liste;
    }

    @Override
    public String toString() {
        return "Şehir: " + ad + " - Nüfus: " + getNufus();
    }
}

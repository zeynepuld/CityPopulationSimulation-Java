/**
 *
 * @author Zeynep Uludağ - zeynep.uludag2@ogr.sakarya.edu.tr
 * @since 17.04.2026
 * <p>
 * Oyun sinifi - Tum oyun mantığını (initialization, döngü, bolunme, raporlama) yonetir. Single Responsibility: oyun akisinin koordinasyonu.
 * </p>
 */

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;


public class Oyun {

    private List<Sehir> sehirler;
    private int turSayisi;
    private Faker faker;
    private Random random;
    
    private Set<Integer> kullanilanIdler; /** oyun genelinde benzersiz id uretmek icin kullanilan set */

    
    public Oyun(int turSayisi) { // constructor fonksiyno
        this.turSayisi = turSayisi;
        this.sehirler = new ArrayList<>();
        this.faker = new Faker(new Locale("tr"));
        this.random = new Random();
        this.kullanilanIdler = new HashSet<>();
    }

    // Sehirlerin olusturulmasi:

    /**
     * verilen iki basamakli sayidan bir Sehir nesnesi olusturur, Onlar basamagi = ilce sayisi , Birler basamagi = ham mahalle sayisi
     * mahalle optimizasyon kurali soyle:
     *   hamMahalle == 0          mahalle = ilce (1 mahalle per ilce)
     *   hamMahalle % ilce == 0   mahalle = hamMahalle (tam boler, degisiklik yok)
     *   Diger:
     *       roundedUp = ((hamMahalle / ilce) + 1) * ilce
     *       Eger roundedUp <= 9   mahalle = roundedUp   (birler siniri icinde, yukari)
     *       Eger roundedUp > 9    mahalle = (hamMahalle / ilce) * ilce  (asagi, sinir koruma)
     *
     * bazNufus kurali soyle:
     *   newSayi = ilce * 10 + mahalle 
     *   newSayi % mahalle == 0 bazNufus = newSayi
     *   Diger                  bazNufus = (newSayi / mahalle + 1) * mahalle
     */
    
    ///////
    public void sehirOlustur(int sayi) {
        int ilceSayisi       = (sayi / 10) % 10;  // onlar basamagi
        int hamMahalleSayisi = sayi % 10;          // birler basamagi

        
        int mahalleSayisi; //  mahalle sayisini optimize eder
        if (ilceSayisi == 0) {
            mahalleSayisi = hamMahalleSayisi;
        } else if (hamMahalleSayisi == 0) {
            mahalleSayisi = ilceSayisi; // ozel durum birler 0: her ilceye 1 mahalle
        } else if (hamMahalleSayisi % ilceSayisi == 0) {
            mahalleSayisi = hamMahalleSayisi; // tam bolunur degisiklik yok
        } else {
            int roundedUp = ((hamMahalleSayisi / ilceSayisi) + 1) * ilceSayisi;
            if (roundedUp <= 9) {
                mahalleSayisi = roundedUp; // yukari yuvarlar hala tek basamak icinde
            } else {
                mahalleSayisi = (hamMahalleSayisi / ilceSayisi) * ilceSayisi; // yukari asacak: asagiya yuvarlaama  birler basamagi siniri korunacak
            }
        }

        // nufusu optimize edecek ---
        // newSayi: optimize edilmis referans (ilce*10 + mahalle)
        int newSayi = ilceSayisi * 10 + mahalleSayisi;
        int bazNufus;
        if (mahalleSayisi == 0) {
            bazNufus = sayi;
        } else if (newSayi % mahalleSayisi == 0) {
            bazNufus = newSayi;
        } else {
            bazNufus = (newSayi / mahalleSayisi + 1) * mahalleSayisi; // bi sonraki adimi as
        }

        int mahalleBasiNufus = (mahalleSayisi == 0) ? 0 : (bazNufus / mahalleSayisi);
        int mahalleBasiIlce  = (ilceSayisi  == 0) ? 0 : (mahalleSayisi / ilceSayisi);

        Sehir sehir = new Sehir(turkceIlAdi()); // faker ile nesne olusturma
        sehir.setBazNufus(bazNufus);

        for (int i = 0; i < ilceSayisi; i++) {
            Ilce ilce = new Ilce(turkceIlceAdi());

            for (int j = 0; j < mahalleBasiIlce; j++) {
                Mahalle mahalle = new Mahalle(turkceMahalleAdi());

                for (int k = 0; k < mahalleBasiNufus; k++) {
                    mahalle.kisiEkle(yeniKisiOlustur());
                }
                ilce.mahalleEkle(mahalle);
            }
            sehir.ilceEkle(ilce);
        }

        sehirler.add(sehir);
    }

    // Oyun dongusu

    /** tum turlari calistirir (tam oyun akisi: yaslanma + artis + bolunme + ekran) */
    public void oyna() {
        for (int tur = 1; tur <= turSayisi; tur++) {
            yaslandir();
            nufusArtir();
            sehirBolunmeKontrol();
            konsoluTemizle();
            matrisYazdir();
        }
    }

    
    public void runTurAdimi() {
        yaslandir();
        nufusArtir();
        sehirBolunmeKontrol();
    }

    /** tum sehirlerdeki tumkisilerin yasini 1 artirir */
    private void yaslandir() {
        for (Sehir sehir : sehirler) {
            for (Ilce ilce : sehir.getIlceler()) {
                for (Mahalle mahalle : ilce.getMahalleler()) {
                    for (Kisi kisi : mahalle.getKisiler()) {
                        kisi.yaslan();
                    }
                }
            }
        }
    }

    /**
     * her sehir icin artis katsayisi hesaplanir ve her mahallenin nufusu o katsayi ile carpilir.
     * formul: yeni_toplam = mevcutNufus * katsayi
     * (mevcutNufus * (katsayi-1)) yeni kisi ekle
     */
    private void nufusArtir() {
        for (Sehir sehir : sehirler) {
            int katsayi = sehir.getArtisKatsayisi();

            for (Ilce ilce : sehir.getIlceler()) {
                for (Mahalle mahalle : ilce.getMahalleler()) {
                    int mevcutNufus = mahalle.getNufus();
                    int eklenecekKisiSayisi;
                    if (mevcutNufus == 0) {
                        eklenecekKisiSayisi = katsayi;
                    } else {
                        eklenecekKisiSayisi = mevcutNufus * (katsayi - 1);
                    }

                    for (int i = 0; i < eklenecekKisiSayisi; i++) {
                        mahalle.kisiEkle(yeniKisiOlustur());
                    }
                }
            }
        }
    }

    // Sehir bolunme

    /**
     * tur sonunda nüfusu >= 1000 olan sehirleri ikiye boler, yeni sehirler ana listeye eklenir
     * tek ilce: eski sehirde 1 fazla yeni sehirde 1 eksik
     */
    private void sehirBolunmeKontrol() {
        List<Sehir> bolunecekler = new ArrayList<>();
        for (Sehir s : sehirler) {
            if (s.getNufus() >= 1000) {
                bolunecekler.add(s);
            }
        }

        for (Sehir eskiSehir : bolunecekler) {
            Sehir yeniSehir = new Sehir(turkceIlAdi());
            yeniSehir.setBazNufus(eskiSehir.getBazNufus());

            List<Ilce> eskiIlceler = eskiSehir.getIlceler();
            int toplamIlce = eskiIlceler.size();

            /////
            int yeniSehireGidecekSayi = toplamIlce / 2; // java tam bolme 7/2=3  yeni=3, eski=4  8/2=4  yeni=4, eski=4

       
            List<Ilce> tasinanIlceler = new ArrayList<>( // son [yeniSehireGidecekSayi] ilceyi alir
                    eskiIlceler.subList(toplamIlce - yeniSehireGidecekSayi, toplamIlce));
            eskiIlceler.subList(toplamIlce - yeniSehireGidecekSayi, toplamIlce).clear();

            for (Ilce ilce : tasinanIlceler) {
                yeniSehir.ilceEkle(ilce);
            }

            sehirler.add(yeniSehir);
        }
    }

    // ekran ciktiisi

    
    public void konsoluTemizle() { // konsolu once temizlemeye calisir hatayla karsilasirsa kullanicisya sorun gostermesin diye bos satirlarla temizler
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    

    /**
     * sehirlerin nüfusunu [Nufus]-[Nufus]-..... placak sekilde cikti
     * her satirda max 5 sehir olacak sekilde yazdirir
     */
    public void matrisYazdir() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sehirler.size(); i++) {
            if (i > 0 && i % 5 == 0) {
                System.out.println(sb.toString());
                sb = new StringBuilder();
            }
            if (sb.length() > 0) {
                sb.append("-");
            }
            sb.append("[").append(sehirler.get(i).getNufus()).append("]");
        }
        if (sb.length() > 0) {
            System.out.println(sb.toString());
        }
    }

    // Hiyerarsik raporlama

    /**
     * satir ve sutun indeksine gore sehri bulur ve detayli raporunu yazdirir
     * matris satir basina 5 sehir , indeks = satir*5 + sutun
     */
    public void sehirRaporu(int satir, int sutun) {
        int indeks = satir * 5 + sutun;

        if (indeks < 0 || indeks >= sehirler.size()) {
            System.out.println("Geçersiz satır/sütun indeksi!");
            return;
        }

        Sehir sehir = sehirler.get(indeks);
        System.out.println("Şehir: " + sehir.getAd() + " - Nüfus: " + sehir.getNufus());

        for (Ilce ilce : sehir.getIlceler()) {
            System.out.println("  İlçe: " + ilce.getAd() + " - Nüfus: " + ilce.getNufus());

            for (Mahalle mahalle : ilce.getMahalleler()) {
                System.out.println("    Mahalle: " + mahalle.getAd() + " - Nüfus: " + mahalle.getNufus());
                System.out.println("    Kişiler:");

                for (Kisi kisi : mahalle.getKisiler()) {
                    System.out.println("      " + kisi.getId() + " - "
                            + kisi.getAd() + " " + kisi.getSoyad()
                            + " - " + kisi.getYas());
                }
            }
        }
    }

    // asagida yardimci fonksiyonlar
    private Kisi yeniKisiOlustur() { /** faker kullanarak benzersiz idli yeni bir Kisi olusturur 0-50 yas */
        int id = benzersizIdUret();
        String ad = faker.name().firstName();
        String soyad = faker.name().lastName();
        int yas = random.nextInt(51); // 0-50 arasi
        return new Kisi(id, ad, soyad, yas);
    }

    private int benzersizIdUret() { /** oyun genelinde daha once kullanilmamis benzersiz bir int id uretir */
        int id;
        do {
            id = random.nextInt(Integer.MAX_VALUE) + 1;
        } while (kullanilanIdler.contains(id));
        kullanilanIdler.add(id);
        return id;
    }

    private String turkceIlAdi() { /** faker ile turkce il adi uretir */
        return faker.address().city();
    }

    private String turkceIlceAdi() { /** faker ile turkce ilce adi uretir */
        return faker.address().cityName();
    }

    private String turkceMahalleAdi() { /** faker ile turkce mahalle adi uretiir */
        return faker.address().streetName();
    }

    // getter metodları

    public List<Sehir> getSehirler() {
        return sehirler;
    }

    public int getTurSayisi() {
        return turSayisi;
    }
}

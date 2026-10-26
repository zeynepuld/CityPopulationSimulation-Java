/**
 *
 * @author Zeynep Uludağ - zeynep.uludag2@ogr.sakarya.edu.tr
 * @since 19.04.2026
 * <p>
 * Main sinifi - Programin giris noktasi. Kullanicidan girdi alir, Oyun nesnesini olusturur ve oyunu baslatir.
 * </p>
 */

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        System.out.print("Tur sayısını girin: "); // 1. adim tur sayisini alir kullanicidan
        int turSayisi = Integer.parseInt(scanner.nextLine().trim());

        
        System.out.print("İki basamaklı sayıları boşlukla ayırarak girin (örn: 18 25 79): "); // iki basamakli sayilari alir
        String satirGirdisi = scanner.nextLine().trim();
        String[] parcalar = satirGirdisi.split("\\s+");

        
        Oyun oyun = new Oyun(turSayisi); // oyun nesnesini olusturur

        
        for (String parca : parcalar) { // 2. adim sehirleri olusturur
            int sayi = Integer.parseInt(parca.trim());
            oyun.sehirOlustur(sayi);
        }

        
        oyun.oyna(); // oyun dongusunu baslatir

        
        System.out.println("\nGörmek istediğiniz şehrin satır ve sütununu girin."); // kullanicidan satir sutun indekslerini alir
        System.out.print("Satır indeksi (0'dan başlar): ");
        int satir = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Sütun indeksi (0'dan başlar): ");
        int sutun = Integer.parseInt(scanner.nextLine().trim());

        System.out.println();

        oyun.sehirRaporu(satir, sutun); // sehir raporu yazdirir

        System.out.println("\nDevam etmek için ENTER'a basın...");
        scanner.nextLine();

        scanner.close();
    }
}

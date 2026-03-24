# Güvercin Takip - Android (Kotlin)

Bu proje tamamen çevrimdışı çalışan bir Android güvercin takip uygulamasıdır.

## Özellikler
- Güvercin kartı: fotoğraf, isim, halka numarası, renk, cins, doğum yılı, notlar
- Performans kaydı: uçuş süresi, takla sayısı, günlük not, en iyi performans işareti
- Hatırlatıcı: vitamin, yem değişimi, eşleme, temizlik
- İstatistik: toplam güvercin, en çok uçan, en iyi takla atan, son uçuş tarihi
- Yedekleme: JSON dosyasına dışa aktar ve geri yükle
- İnternet gerekmez

## Kullanılan Teknolojiler
- Kotlin
- Jetpack Compose
- Room
- WorkManager
- DataStore
- Coil

## Android Studio'da açma
1. Android Studio Hedgehog veya daha güncel bir sürüm açın.
2. `GuvercinTakip` klasörünü proje olarak açın.
3. Gradle senkronizasyonunu çalıştırın.
4. Gerekirse Android SDK 34 ve JDK 17 kurulu olsun.
5. Uygulamayı emülatörde veya telefonda çalıştırın.

## Notlar
- Halka numarası zorunludur ve tekrar edemez.
- Fotoğraf seçildiğinde uygulama içine kopyalanır; böylece yedekte taşınabilir.
- Bildirim izni Android 13+ cihazlarda ilk açılışta istenir.
- WorkManager günlük kontrol yapar; tamamen dakikası dakikasına alarm mantığı yerine güvenilir çevrimdışı günlük hatırlatma mantığı kullanır.

## Geliştirme Önerileri
- Grafik ekranı için çizgi grafik eklenebilir.
- Güvercin detay sayfası ve silme özelliği eklenebilir.
- APK üretmek için Android Studio içinden signed APK alınabilir.

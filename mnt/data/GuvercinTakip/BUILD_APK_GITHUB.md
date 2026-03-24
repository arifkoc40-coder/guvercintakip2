# GitHub ile APK Alma

Bu projede Gradle wrapper olmadığı için, APK almak için en kolay yol GitHub Actions kullanmaktır.

## Adımlar
1. Bu proje klasörünü GitHub'da yeni bir repoya yükle.
2. GitHub'da **Actions** sekmesine gir.
3. **Build Android APK** iş akışını aç.
4. **Run workflow** butonuna bas.
5. İşlem bitince çıkan **Artifacts** kısmından `GuvercinTakip-debug-apk` dosyasını indir.
6. İçindeki `app-debug.apk` dosyasını telefona kur.

## Not
- Bu yöntem debug APK üretir.
- Play Store için release/signed APK veya AAB gerekir.
- Release sürüm istersen keystore eklenerek ayrı workflow hazırlanabilir.

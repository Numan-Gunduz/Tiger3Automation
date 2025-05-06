
Feature: Ekstre aktarım işlemlerinin doğrulanması

Scenario Outline: Kullanıcı eksik bilgi bulunan fişi düzenler ve kaydeder

  Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
  When Sol menüdeki "<Menü>" seçeneğine tıklar
  And Banka dropdown'undan "<Banka>" seçer
  And Hesap dropdown'undan "<IBAN>" hesabını seçer
  And Kullanıcı başlangıç tarihi olarak bugünden <TarihGeriGun> gün önceki tarihi girer
  And "<ListeleButonu>" butonuna tıklar ve sonuçların yüklenmesi beklenir
  And Yüklenen ekstre kayıtlarından "<Durum1>" veya "<Durum2>" durumundaki bir kaydın solundaki seçim kutusunu işaretler
  And Seçilen kayda sağ tık yapar ve "<MenuText>" > "<FisTuru>" seçeneğini seçer
  Then Fiş türünün "<FisTuru>" olarak güncellendiği doğrulanır
  And Kullanıcı "<AlanTipi>" boş olan satırda, Durum alanı "<DurumKontrol>" olmalıdır
  When Kullanıcı "<AlanTipi>" alanındaki üç noktaya tıklar
  And Kullanıcı "<AlanTipi>" için açılan pencerede seç butonuna tıklar
  Then "<AlanTipi>" dolduğunda, Durum sütunu "<DurumSon>" olarak güncellenir
  And Fiş oluştur butonuna tıklar
  And Açılan onay popup'ında Evet'e tıklar
  Then Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir
  Then Durum alanı Eşlendi olarak güncellenmelidir
  Then ERP Fiş No alanı dolu olmalıdır
  When Kullanıcı sağ tıklayıp "<PopupSecenek>" seçeneğini tıklar
  Then Açılan ekrandaki Fiş No alanı ile ERP Fiş No değeri aynı olmalıdır
  And Açılan popup pencereleri kapatır


  Examples:
  | Menü            | Banka          | IBAN                              | TarihGeriGun   | ListeleButonu | Durum1               | Durum2         | MenuText           | FisTuru                   | AlanTipi                   | DurumKontrol           | DurumSon       | PopupSecenek   |


  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT        | 14            | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Banka İşlem Fişi          |                            | Kaydedilebilir         | Kaydedilebilir | Fiş İncele     |
  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT        | 14            | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Hizmet Faturası Fişi      | ERP Hizmet Kodu            | Eksik Bilgi Bulunuyor  | Kaydedilebilir | Fiş İncele     |
  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT        | 14            | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Virman Fişi               | ERP Banka Hesap Kodu       | Eksik Bilgi Bulunuyor  | Kaydedilebilir | Fiş İncele     |
  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT        | 14            | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Havale/EFT Fişi           | ERP Cari Hesap Kodu        | Eksik Bilgi Bulunuyor  | Kaydedilebilir | Fiş İncele     |

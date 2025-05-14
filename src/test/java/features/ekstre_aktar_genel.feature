
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
  | Menü            | Banka          | IBAN                    | TarihGeriGun   | ListeleButonu | Durum1                | Durum2         | MenuText           | FisTuru                   | AlanTipi                   | DurumKontrol           | DurumSon       | PopupSecenek   |


  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT   | 22             | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Banka İşlem Fişi          |                            | Kaydedilebilir         | Kaydedilebilir | Fiş İncele     |
  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT   | 22             | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Hizmet Faturası Fişi      | ERP Hizmet Kodu            | Eksik Bilgi Bulunuyor  | Kaydedilebilir | Fiş İncele     |
  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT   | 22             | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Virman Fişi               | ERP Banka Hesap Kodu       | Eksik Bilgi Bulunuyor  | Kaydedilebilir | Fiş İncele     |
  | Ekstre Aktarımı | Ziraat Bankası | TR0300**5013 - ZİRAAT   | 22             | Listele       | Eksik Bilgi Bulunuyor | Kaydedilebilir | Fiş Türü Değiştir  | Havale/EFT Fişi           | ERP Cari Hesap Kodu        | Eksik Bilgi Bulunuyor  | Kaydedilebilir | Fiş İncele     |


  @krediKartiNegatifKayit
  Scenario: Tutarı negatif olan Kredi Kartı Ödeme Fişi kaydının seçilmesi ve fiş oluşturulması

    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
    And Banka dropdown'undan "Finansbank" seçer
    And Hesap dropdown'undan "TR800011100000000013598759" hesabını seçer
    And Kullanıcı başlangıç tarihi olarak bugünden 22 gün önceki tarihi girer
    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
    And Tutarı negatif ve Durumu "Eksik Bilgi Bulunuyor" olan kaydın checkbox'ını işaretler
    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Kredi Kartı Ödeme Fişi" seçeneğini seçer
    Then Fiş türünün "Kredi Kartı Ödeme Fişi" olarak güncellendiği doğrulanır
    And Kullanıcı "" boş olan satırda, Durum alanı "Kaydedilebilir" olmalıdır
    And Fiş oluştur butonuna tıklar
    And Açılan onay popup'ında Evet'e tıklar
    Then Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir
    Then Durum alanı Eşlendi olarak güncellenmelidir
    Then ERP Fiş No alanı dolu olmalıdır
    When Kullanıcı sağ tıklayıp "Fiş İncele" seçeneğini tıklar
    Then Açılan form ekranındaki fiş Banka işlem fişi olmalıdır
    Then Açılan ekrandaki Fiş No alanı ile ERP Fiş No değeri aynı olmalıdır
    And Açılan popup pencereleri kapatır


  @krediKartiGüncellenmeyenFisTürü
  Scenario: Tutarı pozitif olan Kredi Kartı Ödeme Fişi kaydının türünün değişmediği görülür

    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
    And Banka dropdown'undan "Finansbank" seçer
    And Hesap dropdown'undan "TR800011100000000013598759" hesabını seçer
    And Kullanıcı başlangıç tarihi olarak bugünden 22 gün önceki tarihi girer
    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
    And Tutarı pozitif ve Durumu "Eksik Bilgi Bulunuyor" olan kaydın checkbox'ını işaretler
    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Kredi Kartı Ödeme Fişi" seçeneğini seçer
    Then İlgili kaydın fiş türü "Kredi Kartı Ödeme Fişi" olarak değişmeden kaldığı doğrulanır




  @negatifTutarTesti
  Scenario: Tutarı negatif olan Kasa İşlem Fişi kaydının seçilmesi ve fiş oluşturulması

    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
    And Banka dropdown'undan "Finansbank" seçer
    And Hesap dropdown'undan "TR800011100000000013598759" hesabını seçer
    And Kullanıcı başlangıç tarihi olarak bugünden 22 gün önceki tarihi girer
    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
    And Tutarı negatif ve Durumu "Eksik Bilgi Bulunuyor" olan kaydın checkbox'ını işaretler
    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Kasa İşlem Fişi" seçeneğini seçer
    Then Fiş türünün "Kasa İşlem Fişi" olarak güncellendiği doğrulanır
    And Kullanıcı "ERP Kasa Kodu" boş olan satırda, Durum alanı "Eksik Bilgi Bulunuyor" olmalıdır
    When Kullanıcı "ERP Kasa Kodu" alanındaki üç noktaya tıklar
    And Kullanıcı "ERP Kasa Kodu" için açılan pencerede seç butonuna tıklar
    Then "ERP Kasa Kodu" dolduğunda, Durum sütunu "Kaydedilebilir" olarak güncellenir
    And Fiş oluştur butonuna tıklar
    And Açılan onay popup'ında Evet'e tıklar
    Then Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir
    Then Durum alanı Eşlendi olarak güncellenmelidir
    Then ERP Fiş No alanı dolu olmalıdır
    When Kullanıcı ERP Kasa Kodu alanına göre Kasa İşlemleri'ne gider
    When Kasa İşlemleri ekranında ERP Fiş No satırına çift tıklar
    Then Açılan kayıt Kasa İşlem No ile eşleşmeli ve form ekranı açılmalıdır
    Then Açılan form Bankadan Çekilen formu olmalıdır
    And kasa işlemleri için Açılan popup pencereleri kapatır
    And Online Hesap Özeti uygulamasına tıklar

  @pozitifTutarTesti
  Scenario: Tutarı pozitif olan Kasa İşlem Fişi kaydının seçilmesi ve fiş oluşturulması

    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
    And Banka dropdown'undan "Finansbank" seçer
    And Hesap dropdown'undan "TR800011100000000013598759" hesabını seçer
    And Kullanıcı başlangıç tarihi olarak bugünden 22 gün önceki tarihi girer
    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
    And Tutarı pozitif ve Durumu "Eksik Bilgi Bulunuyor" olan kaydın checkbox'ını işaretler
    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Kasa İşlem Fişi" seçeneğini seçer
    Then Fiş türünün "Kasa İşlem Fişi" olarak güncellendiği doğrulanır
    And Kullanıcı "ERP Kasa Kodu" boş olan satırda, Durum alanı "Eksik Bilgi Bulunuyor" olmalıdır
    When Kullanıcı "ERP Kasa Kodu" alanındaki üç noktaya tıklar
    And Kullanıcı "ERP Kasa Kodu" için açılan pencerede seç butonuna tıklar
    Then "ERP Kasa Kodu" dolduğunda, Durum sütunu "Kaydedilebilir" olarak güncellenir
    And Fiş oluştur butonuna tıklar
    And Açılan onay popup'ında Evet'e tıklar
    Then Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir
    Then Durum alanı Eşlendi olarak güncellenmelidir
    Then ERP Fiş No alanı dolu olmalıdır
    When Kullanıcı ERP Kasa Kodu alanına göre Kasa İşlemleri'ne gider
    When Kasa İşlemleri ekranında ERP Fiş No satırına çift tıklar
    Then Açılan kayıt Kasa İşlem No ile eşleşmeli ve form ekranı açılmalıdır
    Then Açılan form Bankaya Yatırılan formu olmalıdır
    And kasa işlemleri için Açılan popup pencereleri kapatır
    And Online Hesap Özeti uygulamasına tıklar


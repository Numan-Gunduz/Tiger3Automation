Feature: Ekstre Aktarımı Senaryosu

  Scenario: Kullanıcı eksik bilgi bulunan fişi düzenler ve kaydedilebilir hale getirir
    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
    And Banka dropdown'undan "Ziraat Bankası" seçer
    And Hesap dropdown'undan "TR030001000001573636455013" hesabını seçer
    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
    And Yüklenen ekstre kayıtlarından "Eksik Bilgi Bulunuyor" durumundaki bir kaydın solundaki seçim kutusunu işaretler
    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Havale/EFT Fişi" seçeneğini seçer
    Then Fiş türünün "Havale/EFT Fişi" olarak güncellendiği doğrulanır
    And ERP Cari Hesap Kodu alanı boş ise, Durum sütununda "Eksik Bilgi Bulunuyor" yazdığı görülür
    When ERP Cari Hesap Kodu alanındaki üç noktaya tıklar
    And Açılan pencerede ilk satırdaki cari hesap değerine çift tıklar
    Then ERP Cari Hesap Kodu alanı dolduğunda, Durum sütunu "Kaydedilebilir" olarak güncellenir

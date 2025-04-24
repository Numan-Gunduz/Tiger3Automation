Feature: Ekstre Aktarımı Senaryosu

  Scenario: Kullanıcı eksik bilgi bulunan fişi düzenler ve kaydedilebilir hale getirir
    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
    And Banka dropdown'undan "Ziraat Bankası" seçer
    And Hesap dropdown'undan "TR030001000001573636455013" hesabını seçer
    And Kullanıcı başlangıç tarihi olarak bugünden 2 gün önceki tarihi girer
    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
    And Yüklenen ekstre kayıtlarından "Eksik Bilgi Bulunuyor" durumundaki bir kaydın solundaki seçim kutusunu işaretler
    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Havale/EFT Fişi" seçeneğini seçer
    Then Fiş türünün "Havale/EFT Fişi" olarak güncellendiği doğrulanır
    And ERP Cari Hesap Kodu boş olan satırda, Durum alanı 'Eksik Bilgi Bulunuyor' olmalıdır
    When ERP Cari Hesap Kodu alanındaki üç noktaya tıklar
    And Açılan pencerede seç butonuna tıklar
    Then ERP Cari Hesap Kodu alanı dolduğunda, Durum sütunu "Kaydedilebilir" olarak güncellenir

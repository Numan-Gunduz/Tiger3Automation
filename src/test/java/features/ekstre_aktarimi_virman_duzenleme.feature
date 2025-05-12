#Feature: Ekstre Aktarımı Virman Fişi Düzenleme Senaryosu
#
#  Scenario: Kullanıcı eksik bilgi bulunan veya kaydedilebilir durumdaki fişi virman fişi yapar ve kaydeder
#    Given Kullanıcı sisteme başarılı şekilde giriş yapmış ve ana sayfa tamamen yüklenmiştir
#    When Sol menüdeki "Ekstre Aktarımı" seçeneğine tıklar
#    And Banka dropdown'undan "Ziraat Bankası" seçer
#    And Hesap dropdown'undan "TR030001000001573636455013" hesabını seçer
#    And Kullanıcı başlangıç tarihi olarak bugünden 7 gün önceki tarihi girer
#    And "Listele" butonuna tıklar ve sonuçların yüklenmesi beklenir
#    And Yüklenen ekstre kayıtlarından "Eksik Bilgi Bulunuyor" veya "Kaydedilebilir" durumundaki bir kaydın solundaki seçim kutusunu işaretler
#    And Seçilen kayda sağ tık yapar ve "Fiş Türü Değiştir" > "Virman Fişi" seçeneğini seçer
#    Then Fiş türünün "Virman Fişi" olarak güncellendiği doğrulanır
#    And ERP Banka Hesap Kodu boş olan satırda, Durum alanı 'Eksik Bilgi Bulunuyor' olmalıdır
#    When ERP Banka Hesap Kodu alanındaki üç noktaya tıklar
#    And Banka kodu için Açılan pencerede seç butonuna tıklar
#    Then ERP Banka Hesap Kodu alanı dolduğunda, Durum sütunu "Kaydedilebilir" olarak güncellenir
#    And Fiş oluştur butonuna tıklar
#    And Açılan onay popup'ında Evet'e tıklar
#    Then Kaydın başarı ile eşleştiği yeşil bilgi kutucuğu görüntülenir
#    Then Durum alanı Eşlendi olarak güncellenmelidir
#    Then ERP Fiş No alanı dolu olmalıdır
#    When Kullanıcı sağ tıklayıp "Fiş İncele" seçeneğini tıklar
#    Then Açılan ekrandaki Fiş No alanı ile ERP Fiş No değeri aynı olmalıdır
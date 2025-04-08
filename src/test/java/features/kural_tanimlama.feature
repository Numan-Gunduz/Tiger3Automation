Feature: Kural Tanımlama Senaryosu

  Scenario: Kullanıcı Kural Tanımları ekranından yeni kural ekler
    Given Kullanıcı ana sayfaya başarıyla giriş yapmış
    When Kullanıcı sol menüden "Kural Tanımları" seçeneğine tıklar
    And Kullanıcı "Kural Ekle" butonuna tıklar
    Then "Yeni Kural Tanımı - Tiger" sayfasını görmeli

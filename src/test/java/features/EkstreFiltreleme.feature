Feature: Ekstre Aktar

  Scenario: Kullanıcı Ekstre Aktar alanına ulaşabilmelidir
    Given kullanıcı web arayüzü üzerinden Ekstre Aktar menüsüne ulaşır
    When kullanıcı menüden Ekstre Aktar alanına tıklar
    Then kullanıcı Ekstre Aktar sayfasında olduğunu doğrular

  Scenario: Kullanıcı tarih filtresi ile ekstre araması yapar
    Given kullanıcı Ekstre Aktar sayfasında başlangıç ve bitiş tarihlerini girer
    And kullanıcı filtrele butonuna tıklar
    Then ekrana filtrelenmiş ekstre sonuçları gelmelidir

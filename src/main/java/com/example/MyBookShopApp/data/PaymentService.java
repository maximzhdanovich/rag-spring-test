package com.example.MyBookShopApp.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

  @Value("${robokassa.merchant.login}")
  private String merchantLogin;

  @Value("${robokassa.pass.first.test}")
  private String firstTestPass;

  public String getPaymentUrl(List<BookEntity> booksFromCookieSlugs)
      throws NoSuchAlgorithmException {
    Double paymentSumTotal = booksFromCookieSlugs.stream().mapToDouble(BookEntity::discountPrice).sum();
    MessageDigest md = MessageDigest.getInstance("MD5");
    String invId = "5"; //just for testing TODO order numerating later
    md.update((merchantLogin + ":" + paymentSumTotal.toString() + ":" + invId + ":" + firstTestPass).getBytes());
    return "https://auth.robokassa.ru/Merchant/Index.aspx" +
        "?MerchantLogin=" + merchantLogin +
        "&InvId=" + invId +
        "&Culture=ru" +
        "&Encoding=utf-8" +
        "&OutSum=" + paymentSumTotal.toString() +
        "&SignatureValue=" + DatatypeConverter.printHexBinary(md.digest()).toUpperCase() +
        "&IsTest=1";  //for test query
  }
}

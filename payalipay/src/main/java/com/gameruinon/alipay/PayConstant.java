package com.gameruinon.alipay;

public class PayConstant {

	//商户PID
//	public static final String PARTNER = "2088111180146470";
	//商户收款账号
//	public static final String SELLER = "gamerunion201310@gmail.com";
	
	//商户PID
//	public static final String PARTNER = "2088021107060405";
	//商户收款账号
//	public static final String SELLER = "register@smartfuns.com";
	
	//商户PID
	public static final String PARTNER = "2088021107060405";
	//商户收款账号
	public static final String SELLER = "register@smartfuns.com";
	
	//商户私钥，pkcs8格式
//	public static final String RSA_PRIVATE = 
//										   "MIICXQIBAAKBgQCxmigAA6mlvARPBHiwc6Nn/1HOL1CauDv6LdxecDpHdWg7WeXu"
//										   + "hnNZnwVFG3P14/dALEICWB8wjW/V2ag3N0ylIstnGag90m+VnAotEAeBY5l1503B"
//										   + "HQsXq6k0Qy8wwcTVlsEsym2Suk1H7YYHfjlYfM3C/JwdeYVtzABXc1l38wIDAQAB"
//										   + "AoGAF7hgGUhj8xe+d5NAT0jSjURDd8dH+7IZiJtxcEeK8RpUNoEWPZpFc7XSbV1y"
//										   + "SmgNEMwYcNm2KLTrN+OfHOjM2pRmAYmMAxfr7bxT6fSUHf5diZCDLjmXOfwrddHu"
//										   + "Dqpt/m8HeVLLlGfN78HCqroXOYBnHGW4QDQiQ5DGfTGLFQkCQQDa3SL2EpgGoNF4"
//										   + "1aPgcOPxWxTB+Dm2uZ0w6SS1rFbMPsjOok1dNihJnc4mfluMFKxo01syYBp8VC/e"
//										   + "eIZ0mV7PAkEAz7y5MvPVeIpd/8SZUN6TUAnLkxshTgAvEZ+CTpqrng4zAcxxrv7+"
//										   + "AofjT1af5zTMnJpOJSyKzSeoW2GP4909nQJBAMo7nct84Ob3ALfDyPphte5V9/dH"
//										   + "s6SJHJuOQrJwPX/a39g3ln7zxQRJAa0GAveMPsdZzSHeNXr+m4DZI3xoH3cCQFWP"
//										   + "xLPgzE7epsK+xesSEZjIwAlyBjCrULewfCpF+GOcM5QNEdHjAtomABDge/HtxEzI"
//										   + "HWGgS/cd23Dv6SUQLd0CQQDVABEdwaydqdRgyhP2WUdvFK/eF+0j5A1THJAlgWUm"
//										   + "L63S5mlFaGcQFfvB/JdowQVu+rebAiouzqaRtvHYdUtd"
//										   ;
//	//支付宝公钥
//	public static final String RSA_PUBLIC = 
//										  "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGlFtOKnyAEIMpLoRY7SUWIvQ+HWV4evGx2AUX"
//										  + "6hhSZ+4gwjmkRU0tUm0xczEn3sfMwDgkdmBcQu6VZtmDa/vUuvZOh5fO7p5jGyI2jJFdQNxkEsIP"
//										  + "gEYjIh0bthAWhan0AWPJ1GaW4g93sK3E+A1Sgt0KHqFCX1S4sm1zcx0dAQIDAQAB"
//										  ;
	//商户私钥，pkcs8格式
//	public static final String RSA_PRIVATE = 
//										   "MIICXQIBAAKBgQCetoRTHzRoRJBw9lvSlyYTFz224sTwW78pB5t3gd58lpwgY5Pi"
//										   + "kDaDFfWHp4kzBOsO2vQg5rLZm3hq/GfcnRcJPdu/6ZFdlVEAfYZH+VbCR2piWYJ/"
//										   + "ZqIZwp8TRyA45LYIe7QHy29YFp+NRexVbpzTg7D1XJ34gK+jFIbpF9HtgQIDAQAB"
//										   + "AoGAAZks8/lYecRXAAw7GH/VLlQJlxHpkdIatrsQ4b85EmAh6mGWV63EHsXV1yvk"
//										   + "wdLG2ztxQfOQ0YgzSkCIezxJwxYAmSsgpUvgj2s09/Kb5XKaXthWNyof51o2+orF"
//										   + "qKRifUUeA8hTFdzvZaX02roa0Kend1bxO4alhEG5BprYApECQQDPlDkh1RW4QGlM"
//										   + "C0M5w3TbiotwRxfn/ZQR1Xk/uFu3m6l0yWNKIYFpIu9prS5+9mVhEtWnIqJpjMJa"
//										   + "CLqLhULTAkEAw7w2NTB63Fg7voSn0kEM1v/cESj6wbgeeAdeZGsxt4ZV3trHSFZM"
//										   + "lbms2bl06mW/aGOOe9qSpXii9oEoZH9R2wJBAMSob6we3XHvDBMwRGNQSsDHtC0O"
//										   + "HZb7RbHTpgh5V+NOhS+QsBGGRr1djKZ4nSlJtGhQWLv8cxQ+d86moF0YRmMCQQCb"
//										   + "Fw58pqUy4la0ZrfyyMg7sw/UKPxHcnPx9yS7vJPwLujrY75zeq3EFRL4kRIqh+RZ"
//										   + "+khzqoR3yFaXq08uEhPxAkA0pvF77+PEqigPwNbLcir5NEeaMfP2HilVtiF71XMb"
//										   + "RoCD7rhhvF1Lu43NBlyerIH76SGk3hG0H9mCFAq/4JYo"
//										   ;
	
	//商户私钥，pkcs8格式
//	public static final String RSA_PRIVATE = 
//										   "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ62hFMfNGhEkHD2"
//										   + "W9KXJhMXPbbixPBbvykHm3eB3nyWnCBjk+KQNoMV9YeniTME6w7a9CDmstmbeGr8"
//										   + "Z9ydFwk927/pkV2VUQB9hkf5VsJHamJZgn9mohnCnxNHIDjktgh7tAfLb1gWn41F"
//										   + "7FVunNODsPVcnfiAr6MUhukX0e2BAgMBAAECgYABmSzz+Vh5xFcADDsYf9UuVAmX"
//										   + "EemR0hq2uxDhvzkSYCHqYZZXrcQexdXXK+TB0sbbO3FB85DRiDNKQIh7PEnDFgCZ"
//										   + "KyClS+CPazT38pvlcppe2FY3Kh/nWjb6isWopGJ9RR4DyFMV3O9lpfTauhrQp6d3"
//										   + "VvE7hqWEQbkGmtgCkQJBAM+UOSHVFbhAaUwLQznDdNuKi3BHF+f9lBHVeT+4W7eb"
//										   + "qXTJY0ohgWki72mtLn72ZWES1aciommMwloIuouFQtMCQQDDvDY1MHrcWDu+hKfS"
//										   + "QQzW/9wRKPrBuB54B15kazG3hlXe2sdIVkyVuazZuXTqZb9oY4572pKleKL2gShk"
//										   + "f1HbAkEAxKhvrB7dce8MEzBEY1BKwMe0LQ4dlvtFsdOmCHlX406FL5CwEYZGvV2M"
//										   + "pnidKUm0aFBYu/xzFD53zqagXRhGYwJBAJsXDnympTLiVrRmt/LIyDuzD9Qo/Edy"
//										   + "c/H3JLu8k/Au6OtjvnN6rcQVEviREiqH5Fn6SHOqhHfIVperTy4SE/ECQDSm8Xvv"
//										   + "48SqKA/A1styKvk0R5ox8/YeKVW2IXvVcxtGgIPuuGG8XUu7jc0GXJ6sgfvpIaTe"
//										   + "EbQf2YIUCr/glig="
//										   ;
	
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE =
			"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ62hFMfNGhEkHD2"
					+ "W9KXJhMXPbbixPBbvykHm3eB3nyWnCBjk+KQNoMV9YeniTME6w7a9CDmstmbeGr8"
					+ "Z9ydFwk927/pkV2VUQB9hkf5VsJHamJZgn9mohnCnxNHIDjktgh7tAfLb1gWn41F"
					+ "7FVunNODsPVcnfiAr6MUhukX0e2BAgMBAAECgYABmSzz+Vh5xFcADDsYf9UuVAmX"
					+ "EemR0hq2uxDhvzkSYCHqYZZXrcQexdXXK+TB0sbbO3FB85DRiDNKQIh7PEnDFgCZ"
					+ "KyClS+CPazT38pvlcppe2FY3Kh/nWjb6isWopGJ9RR4DyFMV3O9lpfTauhrQp6d3"
					+ "VvE7hqWEQbkGmtgCkQJBAM+UOSHVFbhAaUwLQznDdNuKi3BHF+f9lBHVeT+4W7eb"
					+ "qXTJY0ohgWki72mtLn72ZWES1aciommMwloIuouFQtMCQQDDvDY1MHrcWDu+hKfS"
					+ "QQzW/9wRKPrBuB54B15kazG3hlXe2sdIVkyVuazZuXTqZb9oY4572pKleKL2gShk"
					+ "f1HbAkEAxKhvrB7dce8MEzBEY1BKwMe0LQ4dlvtFsdOmCHlX406FL5CwEYZGvV2M"
					+ "pnidKUm0aFBYu/xzFD53zqagXRhGYwJBAJsXDnympTLiVrRmt/LIyDuzD9Qo/Edy"
					+ "c/H3JLu8k/Au6OtjvnN6rcQVEviREiqH5Fn6SHOqhHfIVperTy4SE/ECQDSm8Xvv"
					+ "48SqKA/A1styKvk0R5ox8/YeKVW2IXvVcxtGgIPuuGG8XUu7jc0GXJ6sgfvpIaTe"
					+ "EbQf2YIUCr/glig="
			;

	//支付宝公钥
	public static final String RSA_PUBLIC =
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCetoRTHzRoRJBw9lvSlyYTFz22"
					+ "4sTwW78pB5t3gd58lpwgY5PikDaDFfWHp4kzBOsO2vQg5rLZm3hq/GfcnRcJPdu/"
					+ "6ZFdlVEAfYZH+VbCR2piWYJ/ZqIZwp8TRyA45LYIe7QHy29YFp+NRexVbpzTg7D1"
					+ "XJ34gK+jFIbpF9HtgQIDAQAB"
			;
}

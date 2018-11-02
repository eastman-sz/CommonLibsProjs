package com.gameruinon.alipay;
/**
 * 支付时要用的公共参数。
 * @author E
 */
public class AlipayParmas {

	private String partner = null;
	private String seller = null;
	private String rsa_private = null;
	private String rsa_public = null;
	private String callbackUrl = null;
	
	public AlipayParmas() {
		super();
	}
	
	public AlipayParmas(String partner, String seller, String rsa_private,
			String rsa_public, String callbackUrl) {
		super();
		this.partner = partner;
		this.seller = seller;
		this.rsa_private = rsa_private;
		this.rsa_public = rsa_public;
		this.callbackUrl = callbackUrl;
	}

	public String getPartner() {
		return partner;
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public String getRsa_private() {
		return rsa_private;
	}
	public void setRsa_private(String rsa_private) {
		this.rsa_private = rsa_private;
	}
	public String getRsa_public() {
		return rsa_public;
	}
	public void setRsa_public(String rsa_public) {
		this.rsa_public = rsa_public;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
}

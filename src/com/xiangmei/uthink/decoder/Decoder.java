package com.xiangmei.uthink.decoder;

public interface Decoder {

	public void setData(String data);
	
	public String getSALE_TIME();
	public String getITEM_NUMBER();
	public String getSALES_VOLUME();
	public String getRETAIL_PRICE();
	public String getMENBERSHIP_ID();
	public boolean isValidity();
	
}

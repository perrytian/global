package com.xiangmei.uthink.decoder.impl;

import com.xiangmei.uthink.decoder.Decoder;

public class SalesDecoder implements Decoder {
	
	private final int SALE_TIME = 4;//销售时间
	private final int ITEM_NUMBER = 6;//货号
	private final int SALES_VOLUME = 10;//销售数量
	private final int RETAIL_PRICE = 15;//销售价格
	private final int MENBERSHIP_ID = 25;//会员卡号
	
	private String[] columns;

	@Override
	public void setData(String data) {
		try {
			columns = data.split(",");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getSALE_TIME() {
		return columns[SALE_TIME];
	}

	@Override
	public String getITEM_NUMBER() {
		return columns[ITEM_NUMBER];
	}

	@Override
	public String getSALES_VOLUME() {
		return columns[SALES_VOLUME];
	}

	@Override
	public String getRETAIL_PRICE() {
		return columns[RETAIL_PRICE];
	}

	@Override
	public String getMENBERSHIP_ID() {
		return columns[MENBERSHIP_ID];
	}

	@Override
	public boolean isValidity() {
		boolean res = true;
		if (columns.length<28) {
			res = false;
		}
		return res;
	}

}

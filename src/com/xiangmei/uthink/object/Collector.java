package com.xiangmei.uthink.object;

import java.util.Hashtable;

public class Collector {

	
	private double totalConsumption = 0;
	private Hashtable<String, Double> salesVolume = new Hashtable<>();
	
	public double getTotalConsumption() {
		return totalConsumption;
	}

	public void setTotalConsumption(double totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

	public Hashtable<String, Double> getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Hashtable<String, Double> salesVolume) {
		this.salesVolume = salesVolume;
	}

}

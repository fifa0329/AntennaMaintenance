package com.station.activity;

public  class  GsmCell {
	 String mnc;
	int lac;
	 int cid;
	 int signal;
	
	public String getmnc()
	{return mnc;};
	public int getlac()
	{return lac;};
	public int getcid()
	{return cid;};
	public int getsignal()
	{return signal;}
	
//	gb.mnc=mccMnc.substring(3, 5);
//	gb.lac=ni.getLac();
//	gb.cid=ni.getCid();
//	gb.signal=-133+2*ni.getRssi();
//	gb.time=System.currentTimeMillis();
//	gsmCells.add(gb)
}

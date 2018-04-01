package com.example.admin.hn.model;

public class EightEntity {
	private boolean checked = true;
	private String noticecontent;
	private String messagestate;
	private String ordernumber;
	private String messageid;
	private String shipname;
	private String noticetime;
	private String orderdetails;
	private String linename;

	public EightEntity(boolean checked, String linename, String orderdetails, String noticetime, String shipname, String messageid, String ordernumber, String messagestate, String noticecontent) {
		this.checked = checked;
		this.linename = linename;
		this.orderdetails = orderdetails;
		this.noticetime = noticetime;
		this.shipname = shipname;
		this.messageid = messageid;
		this.ordernumber = ordernumber;
		this.messagestate = messagestate;
		this.noticecontent = noticecontent;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getMessagestate() {
		return messagestate;
	}

	public void setMessagestate(String messagestate) {
		this.messagestate = messagestate;
	}

	public String getNoticecontent() {
		return noticecontent;
	}

	public void setNoticecontent(String noticecontent) {
		this.noticecontent = noticecontent;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getShipname() {
		return shipname;
	}

	public void setShipname(String shipname) {
		this.shipname = shipname;
	}

	public String getNoticetime() {
		return noticetime;
	}

	public void setNoticetime(String noticetime) {
		this.noticetime = noticetime;
	}

	public String getOrderdetails() {
		return orderdetails;
	}

	public void setOrderdetails(String orderdetails) {
		this.orderdetails = orderdetails;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}
}

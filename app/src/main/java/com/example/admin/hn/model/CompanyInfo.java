package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/12/14 9:55
 */
public class CompanyInfo {
	private List<company> Documents;

	private String message;
	private String status;

	public static class company {
		private String Companynumber;
		private String Companyname;

		public String getCompanynumber() {
			return Companynumber;
		}

		public void setCompanynumber(String companynumber) {
			Companynumber = companynumber;
		}

		public String getCompanyname() {
			return Companyname;
		}

		public void setCompanyname(String companyname) {
			Companyname = companyname;
		}
	}

	public CompanyInfo(List<company> documents, String message, String status) {
		Documents = documents;
		this.message = message;
		this.status = status;
	}

	public List<company> getDocuments() {
		return Documents;
	}

	public void setDocuments(List<company> documents) {
		Documents = documents;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

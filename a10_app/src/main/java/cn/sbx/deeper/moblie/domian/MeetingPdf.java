package cn.sbx.deeper.moblie.domian;

import java.io.Serializable;

public class MeetingPdf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pdfKey;
	private String value;
	private String docID;
	private String pdf;
	private String secret = "0";

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPdfKey() {
		return pdfKey;
	}

	public void setPdfKey(String pdfKey) {
		this.pdfKey = pdfKey;
	}

	@Override
	public String toString() {
		return "MeetingPdf [pdfKey=" + pdfKey + ", value=" + value + "]";
	}

}

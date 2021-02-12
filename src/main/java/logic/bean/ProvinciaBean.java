package logic.bean;

import java.io.Serializable;

public class ProvinciaBean implements Serializable {
	private static final long serialVersionUID = -2351990966375389966L;
	private String sigla;
	private RegioneBean regione;

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setRegione(RegioneBean regione) {
		this.regione = regione;
	}

	public String getSigla() {
		return sigla;
	}

	public RegioneBean getRegione() {
		return regione;
	}
}

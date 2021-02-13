package logic.bean;

import java.io.Serializable;

public final class ComuneBean implements Serializable {
	private static final long serialVersionUID = 772654344964136033L;
	
	private String nome;
	private String cap;
	private ProvinciaBean provincia;

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public void setProvincia(ProvinciaBean provincia) {
		this.provincia = provincia;
	}

	public String getNome() {
		return nome;
	}

	public String getCap() {
		return cap;
	}

	public ProvinciaBean getProvincia() {
		return provincia;
	}
}

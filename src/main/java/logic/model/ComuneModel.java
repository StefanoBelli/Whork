package logic.model;

public final class ComuneModel {
	private String cap;
	private String nome;
	private ProvinciaModel provincia;

	public String getCap() {
		return cap;
	}

	public String getNome() {
		return nome;
	}

	public ProvinciaModel getProvincia() {
		return provincia;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setProvincia(ProvinciaModel provincia) {
		this.provincia = provincia;
	}
}

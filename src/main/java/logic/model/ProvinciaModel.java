package logic.model;

public final class ProvinciaModel implements Comparable<ProvinciaModel>{
	private String sigla;
	private RegioneModel regione;

	public String getSigla() {
		return sigla;
	}

	public RegioneModel getRegione() {
		return regione;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setRegione(RegioneModel regione) {
		this.regione = regione;
	}

	@Override
	public int compareTo(ProvinciaModel provincia) {
		return this.sigla.compareTo(provincia.getSigla());
	}
	
}

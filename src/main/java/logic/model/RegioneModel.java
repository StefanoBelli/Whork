package logic.model;

public final class RegioneModel implements Comparable<RegioneModel> {
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int compareTo(RegioneModel regione) {
		return this.nome.compareTo(regione.getNome());
	}
}

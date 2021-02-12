package logic.bean;

import java.io.Serializable;

public class RegioneBean implements Serializable {
	private static final long serialVersionUID = -6833515035628239642L;
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}

package br.com.votacao.Socket_TCP;

public class Voto {

	Candidato candidado;
	Eleitor eleitor;

	public Candidato getCandidado() {

		return candidado;
	}

	public void setCandidado(Candidato candidado) {

		this.candidado = candidado;
	}

	public Eleitor getEleitor() {

		return eleitor;
	}

	public void setEleitor(Eleitor eleitor) {

		this.eleitor = eleitor;
	}

}


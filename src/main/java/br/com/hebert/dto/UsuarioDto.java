package br.com.hebert.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class UsuarioDto {

	private String identificador;
	private Integer idade;
	private String cep;
	private String nomeCompleto;
	private String apelido;
}

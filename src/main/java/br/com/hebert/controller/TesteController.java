package br.com.hebert.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hebert.dto.UsuarioDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController()
@RequestMapping(path = "/teste")
@Api(value = "Teste", description = "Api de gerenciamento de anotações")
public class TesteController {

	@ApiOperation(value = "Busca informações do usuário")
	@GetMapping(value = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public UsuarioDto getTestData() {
		UsuarioDto usuario = new UsuarioDto();
		usuario.setApelido("Helth");
		usuario.setCep("32223-080");
		usuario.setIdade(31);
		usuario.setIdentificador("c927553");
		usuario.setNomeCompleto("Hebert Alves Ferreira");
		return usuario;
	}
}

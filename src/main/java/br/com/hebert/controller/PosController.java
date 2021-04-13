package br.com.hebert.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.Bucket;

import br.com.hebert.dto.ImagemDto;
import br.com.hebert.services.DocumentosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController()
@RequestMapping(path = "/aws")
@Api(value = "pos", description = "Api de gerenciamento de arquivos")
public class PosController {

	@Autowired
	private DocumentosService documentosService;

	@ApiOperation(value = "Lista Buckets")
	@GetMapping(value = "/listarArmazenamentos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Bucket> listarBuckets() {
		return this.documentosService.listarBuckets();
	}

	@ApiOperation(value = "Criar armazenamento")
	@PostMapping(value = "/criarArmazenamento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity criarBucket(String nomeArmazenamento) {
		Bucket bucket = this.documentosService.criarBucket(nomeArmazenamento);
		if (bucket != null) {
			return new ResponseEntity(bucket, HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/isLogged",  produces = {MediaType.APPLICATION_JSON_VALUE}, method = RequestMethod.GET)
	public ResponseEntity<Object> isLogged() {
		return ResponseEntity.ok(true);
	}

	@ApiOperation(value = "Exclui armazenamento")
	@ApiParam(value = "O id do bucket", required = true)
	@DeleteMapping(value = "/excluirBucket/{id}")
	public ResponseEntity excluirBucket(@PathVariable String id) {
		if (this.documentosService.excluirBucket(id)) {
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Realiza o upload dos arquivos")
	@ApiParam(value = "Arquivos", required = true)
	@PostMapping(value = "/uploadFiles")
	public ResponseEntity uploadFiles(@RequestParam(value = "files", required = true) MultipartFile[] files) {
		try {
			this.documentosService.adicionarImagens(files);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Busca os arquivos de imagem")
	@GetMapping(value = "/getImages")
	public ResponseEntity getImages() {
		List<ImagemDto> imagens = this.documentosService.getListaImagens();
		return new ResponseEntity(imagens, HttpStatus.OK);
	}
	
}

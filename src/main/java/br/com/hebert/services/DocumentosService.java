package br.com.hebert.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import br.com.hebert.dto.ImagemDto;

@Service
public class DocumentosService {

	private static final String DIR_NAME = "images/";
	private static final String BUCKET_NAME = "gallerypos";
	
	
	private static final String KEY = "asdasdasdasd"; 
	private static final String SECRET_KEY = "asdasdasdasdasd";
	 

	/**
	 * Cria uma instância do client S3 da AWS
	 * 
	 * @return instância do tipo AmazonS3
	 */
	private AmazonS3 logginAWS() {
		AWSCredentials credentials = new BasicAWSCredentials(this.KEY, this.SECRET_KEY);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.SA_EAST_1).build();
		return s3client;
	}

	public Bucket criarBucket(String nomeBucket) {
		AmazonS3 s3client = logginAWS();

		if (s3client.doesBucketExist(nomeBucket)) {
			return null;
		}

		return s3client.createBucket(nomeBucket);
	}

	public boolean excluirBucket(String nomeBucket) {
		AmazonS3 s3client = logginAWS();
		if (s3client.doesBucketExist(nomeBucket)) {
			s3client.deleteBucket(nomeBucket);
			return true;
		}
		return false;
	}

	public List<Bucket> listarBuckets() {
		AmazonS3 s3Client = logginAWS();
		return s3Client.listBuckets();
	}

	public void adicionarImagens(MultipartFile[] files) throws IOException {

		for (MultipartFile file : files) {
			File convFile = new File(file.getOriginalFilename());
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			AmazonS3 s3Client = logginAWS();
			ObjectMetadata obj = new ObjectMetadata();
			s3Client.putObject(new PutObjectRequest(this.BUCKET_NAME, DIR_NAME + "" + file.getOriginalFilename(),
					file.getInputStream(), obj).withCannedAcl(CannedAccessControlList.PublicRead));
		}
		System.out.println(files);
	}

	public void removerImagem(ImagemDto imagemDto) {
		AmazonS3 s3Client = logginAWS();
		s3Client.deleteObject(this.BUCKET_NAME, imagemDto.getId());
	}

	public List<ImagemDto> getListaImagens() {
		AmazonS3 s3Client = logginAWS();
		List<ImagemDto> imagens = new ArrayList<>();
		ObjectListing listing = s3Client.listObjects(this.BUCKET_NAME);
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();

		while (listing.isTruncated()) {
			listing = s3Client.listNextBatchOfObjects(listing);
			summaries.addAll(listing.getObjectSummaries());
		}

		summaries.forEach(item -> {
			ImagemDto imagem = new ImagemDto();
			imagem.setId(item.getKey());
			imagem.setSrc(String.format("https://gallerypos.s3-sa-east-1.amazonaws.com/%s", imagem.getId()));
			imagens.add(imagem);
		});

		return imagens;
	}

}

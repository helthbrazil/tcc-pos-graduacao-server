package br.com.hebert.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

@Service
public class DocumentosService {

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

}

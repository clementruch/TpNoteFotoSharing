package local.epul4a.tpnotefotosharing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpNoteFotoSharingApplication {

	private static final Logger logger = LoggerFactory.getLogger(TpNoteFotoSharingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TpNoteFotoSharingApplication.class, args);
		logger.info("Application TpNoteFotoSharing started successfully!");
	}
}

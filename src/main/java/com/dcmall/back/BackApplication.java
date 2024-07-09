package com.dcmall.back;

import com.dcmall.back.config.MasterDataBaseConfig;
import com.dcmall.back.config.SlaveDataBaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MasterDataBaseConfig.class, SlaveDataBaseConfig.class})
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

}

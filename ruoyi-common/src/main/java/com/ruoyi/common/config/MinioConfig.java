package com.ruoyi.common.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class MinioConfig {

    /**
     * 访问地址
     */
    @Value("http://10.1.3.202:9000")
    private String endpoint;

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    @Value("minio_root")
    private String accessKey;

    /**
     * secretKey是你账户的密码
     */
    @Value("minio_123456")
    private String secretKey;

    /**
     * 默认存储桶
     */
    @Value("car-access")
    private String carAccessBucketName;

    @Value("people-access")
    private String peopleAccessBucketName;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }
}
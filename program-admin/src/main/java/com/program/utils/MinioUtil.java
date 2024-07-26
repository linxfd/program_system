package com.program.utils;

import cn.hutool.core.date.DateUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import com.program.config.MinioProperties;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

/**
 * @author linxf
 * @date 2024/7/26
 */
@Component
public class MinioUtil {

    @Autowired
    private MinioProperties minioProperties ;

    public String fileUpload(MultipartFile file) {
        try {
            // 创建一个Minio的客户端对象，配置好Minio的连接信息连接服务器地址、用户名、密码和桶信息等等
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl())
                    .credentials(minioProperties.getAccessKey(), minioProperties.getSecreKey())
                    .build();

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {  // 如果存在打印信息
                System.out.println("Bucket 'spzx-bucket' already exists.");
            }

            // 设置存储对象名称：年月日/随机UUID+原文件名
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //20230801/443e1e772bef482c95be28704bec58a901.jpg
            String fileName = dateDir+"/"+uuid+file.getOriginalFilename();
            System.out.println(fileName);

            // 构建上传参数，参数：桶名称，文件流（文件流、文件大小、元数据）、文件名称
            // 上传方式：使用流上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    //-1 这是可选的，代表分块上传的大小。如果设置了分块大小（且大于 0），则 SDK 会使用分块上传的方式来上传文件。
                    // 这通常用于大文件上传，以提高上传的可靠性和效率。
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build();
            // 开始上传
            minioClient.putObject(putObjectArgs) ;

            // 返回文件访问路径：拼接服务器地址和桶名称和文件名称
            // http://127.0.0.1:9000/spzx-bucket/20240507/2daa1bdbde3c43b4b900e1891fd6fe512104060Z024-5-1200.jpg
            return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + fileName ;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

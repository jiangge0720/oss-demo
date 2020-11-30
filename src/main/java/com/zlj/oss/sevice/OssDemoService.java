package com.zlj.oss.sevice;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AllPermission;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Jiang-gege
 * 2019/12/1517:42
 */
@Service
//@EnableConfigurationProperties(OssProperties.class)
public class OssDemoService {

   // @Autowired
   // private OssProperties prop;

    //访问oss的域名，根据自己账号上的值
    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com/";

    // accessKeyId和accessKeySecret是OSS的访问密钥，可以在控制台上创建和查看，
    private static String accessKeyId = "z";
    private static String accessKeySecret = "z";

    // Bucket用来管理所存储Object的存储空间
    private static String bucketName = "zlj-bucket";

//    //访问oss的域名，根据自己账号上的值
//    String endpoint = prop.getEndpoint();
//
//    // accessKeyId和accessKeySecret是OSS的访问密钥，可以在控制台上创建和查看，
//    String accessKeyId = prop.getAccessKeyId();
//    String accessKeySecret = prop.getAccessKeySecret();
//
//    // Bucket用来管理所存储Object的存储空间
//    String bucketName = prop.getBucketName();
//    //允许上传的文件类型
    private static final List<String> ALLOW_TYPES = Arrays.asList("image/jpeg", "image/png");


    public void uploadImage() {


        // 日志配置，OSS Java SDK使用log4j记录错误信息。示例程序会在工程目录下生成“oss-demo.log”日志文件，默认日志级别是INFO。
        // 日志的配置文件是“conf/log4j.properties”，如果您不需要日志，可以没有日志配置文件和下面的日志配置。


        // 生成OSSClient，可以指定一些参数
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {

            // 判断Bucket是否存在
            if (ossClient.doesBucketExist(bucketName)) {
                System.out.println("您已经创建Bucket：" + bucketName + "。");
            } else {
                System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
                ossClient.createBucket(bucketName);
            }

            // 查看Bucket信息
            BucketInfo info = ossClient.getBucketInfo(bucketName);
            System.out.println("Bucket " + bucketName + "的信息如下：");
            System.out.println("\t数据中心：" + info.getBucket().getLocation());
            System.out.println("\t创建时间：" + info.getBucket().getCreationDate());
            System.out.println("\t用户标志：" + info.getBucket().getOwner());


            //上传本地的新闻图片流文件
            String newFileKey = "1.jpeg";
            InputStream inputStream = new FileInputStream("F:/upload/1.jpeg");
            ossClient.putObject(bucketName, "image/" + newFileKey, inputStream);

            // 设置URL过期时间为1小时。
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, "image/" + newFileKey, expiration);
            System.out.println(url);


            // 查看Bucket中的Object
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
            // 指定前缀。
            final String keyPrefix = "image";
            // 列举包含指定前缀的文件。默认列举100个文件。
            ObjectListing objectListing = ossClient.listObjects(new ListObjectsRequest(bucketName).withPrefix(keyPrefix));
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();
            System.out.println("您有以下Object：");
            for (OSSObjectSummary s : sums) {
                System.out.println("\t" + s.getKey());
            }


        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }

    }
}
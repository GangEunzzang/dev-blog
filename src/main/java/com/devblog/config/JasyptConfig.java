//package com.devblog.config;
//
//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
//import org.jasypt.encryption.StringEncryptor;
//import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Configuration
//@EnableEncryptableProperties
//public class JasyptConfig {
//    @Bean("jasyptEncryptor")
//    public StringEncryptor stringEncryptor() {
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        encryptor.setProvider(new BouncyCastleProvider());
//        encryptor.setPoolSize(2);
//        encryptor.setPassword(getEncryptionKey()); // 암호화 키
//        encryptor.setAlgorithm("PBEWithSHA256And128BitAES-CBC-BC"); // 알고리즘
//        return encryptor;
//    }
//
//    private String getEncryptionKey() {
//        try {
//            ClassPathResource resource = new ClassPathResource("jasypt-encryptor-key.txt");
//            return String.join("", Files.readAllLines(Paths.get(resource.getURI())));
//        } catch (IOException e) {
//            throw new RuntimeException("Not found Jasypt password file.");
//        }
//    }
//}
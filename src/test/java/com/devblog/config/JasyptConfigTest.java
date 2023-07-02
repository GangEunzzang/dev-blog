package com.devblog.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JasyptConfig.class)
class JasyptConfigTest {

    final BeanFactory beanFactory = new AnnotationConfigApplicationContext(JasyptConfig.class);
    final StringEncryptor stringEncryptor = beanFactory.getBean("jasyptEncryptor", StringEncryptor.class);

    @Test
    @DisplayName("암복호화 테스트")
    void encryptorTest() {
        String keyword = "plain text";
        String enc = stringEncryptor.encrypt(keyword);
        String des = stringEncryptor.decrypt(enc);
        assertThat(keyword).isEqualTo(des);

    }

}
package com.devblog.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JasyptConfigTest {

    final BeanFactory beanFactory = new AnnotationConfigApplicationContext(JasyptConfig.class);
    final StringEncryptor stringEncryptor = beanFactory.getBean("jasyptEncryptor", StringEncryptor.class);

    @Test
    @DisplayName("암복호화 테스트")
    void encryptorTest() {
        String keyword = "암호화하고 싶은 값 입력";
        String enc = stringEncryptor.encrypt(keyword);
        String des = stringEncryptor.decrypt(enc);
        assertThat(keyword).isEqualTo(des);
    }

}
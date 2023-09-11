package com.devblog.config;

import com.devblog.domain.entity.Board;
import com.devblog.domain.repository.BoardRepository;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JasyptConfigTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertDate() throws Exception {
        //given

//        List<Board> list = IntStream.range(0, 10000).mapToObj(i -> Board.builder()
//                .writer(UUID.randomUUID().toString().substring(0, 6))
//                .title(UUID.randomUUID().toString().substring(0, 10))
//                .content(UUID.randomUUID().toString().substring(0, 30))
//                .build()).collect(Collectors.toList());
//
//
//        boardRepository.saveAll(list);
        //when

        //then

    }

    @Test
    public void  page_test() throws Exception {
        //given
        //when

        //then

     }

}
package com.att.training.ct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;


@SpringBootTest
@MockBean(DataSource.class)
class ComponentTestingApplicationTests {

    @Test
    void contextLoads() {
    }
}

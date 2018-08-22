package com.github.tyang513.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author tao.yang
 * @date 2018-08-21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexControllerTest {

    @Test
    public void testIndex(){
        System.out.println("x");
    }

    @Test
    @Sql
    public void testAddUser(){

    }

}

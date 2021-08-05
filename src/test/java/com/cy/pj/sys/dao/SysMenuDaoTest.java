package com.cy.pj.sys.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class SysMenuDaoTest {
    @Autowired
    private SysMenuDao sysMenuDao;

    @Test
    public void testFindObjects() {
        List<Map<String, Object>> list = sysMenuDao.findObjects();
        System.out.println(list.size());
//        for (Map<String, Object> map : list) {
//            System.out.println(map);
//        }
        list.forEach((map) -> System.out.println(map));
    }
}

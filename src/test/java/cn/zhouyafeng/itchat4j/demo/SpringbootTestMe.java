package cn.zhouyafeng.itchat4j.demo;

import cn.zhouyafeng.itchat4j.entity.Person;
import cn.zhouyafeng.itchat4j.reposotory.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 71768
 * @Title: SpringbootTestMe
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2617:53
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringbootTestMe {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void test() throws Exception{
        Person person = new Person();
        Person person1 = new Person();
        Person person2= new Person();
        List list = new ArrayList<Person>();
        person.setRemarkName("你好");
        person1.setRemarkName("你好");
        person2.setRemarkName("你好");
        list.add(person);
        list.add(person1);
        list.add(person2);
        personRepository.saveAll(list);
    }


}

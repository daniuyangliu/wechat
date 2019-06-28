package cn.zhouyafeng.itchat4j.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author 杨柳
 * @Title: Person
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2617:16
 */
@Entity(name="person")
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="user_name",length=20)
    private String UserName;
    @Column(name="province",length=20)
    private String Province;
    @Column(name="remark_name",length=20)
    private String RemarkName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }



    public String getRemarkName() {
        return RemarkName;
    }

    public void setRemarkName(String remarkName) {
        RemarkName = remarkName;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", UserName='" + UserName + '\'' +
                ", Province='" + Province + '\'' +
                ", RemarkName='" + RemarkName + '\'' +
                '}';
    }
}

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
@Entity(name="sys_code")
public class SysCode {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="size",length=20)
    private String Size;
    @Column(name="un_recive_user",length=20)
    private String UnReciveUser;
    @Column(name="user_id",length=20)
    private String UserId;

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getUnReciveUser() {
        return UnReciveUser;
    }

    public void setUnReciveUser(String unReciveUser) {
        UnReciveUser = unReciveUser;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

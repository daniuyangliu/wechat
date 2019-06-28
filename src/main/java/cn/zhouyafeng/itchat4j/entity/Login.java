package cn.zhouyafeng.itchat4j.entity;

/**
 * @author 71768
 * @Title: 登陆信息表
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2817:40
 */
public class Login {
    private Long id;
    private String user;
    private int flag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}

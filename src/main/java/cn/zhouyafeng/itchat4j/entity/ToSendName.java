package cn.zhouyafeng.itchat4j.entity;

/**
 * @author 杨柳
 * @Title: 指定发送好友实体类
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2814:30
 */
public class ToSendName {

    private Long id;
    private String name;
    private String oner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOner() {
        return oner;
    }

    public void setOner(String oner) {
        this.oner = oner;
    }

    @Override
    public String toString() {
        return "ToSendName{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

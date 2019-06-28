package cn.zhouyafeng.itchat4j.entity;

/**
 * 群消息尸体
 */
public class UserInfoDTO {
    private Integer ChatRoomId;
    private Integer Sex;
    private String Username;
    private String NickName;

    public Integer getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(Integer chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public Integer getSex() {
        return Sex;
    }

    public void setSex(Integer sex) {
        Sex = sex;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "ChatRoomId=" + ChatRoomId +
                ", Sex=" + Sex +
                ", Username='" + Username + '\'' +
                ", NickName='" + NickName + '\'' +
                '}';
    }
}

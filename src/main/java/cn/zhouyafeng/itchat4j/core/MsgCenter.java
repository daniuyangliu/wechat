package cn.zhouyafeng.itchat4j.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import cn.zhouyafeng.itchat4j.entity.Person;
import cn.zhouyafeng.itchat4j.entity.SysCode;
import cn.zhouyafeng.itchat4j.reposotory.PersonRepository;
import cn.zhouyafeng.itchat4j.reposotory.SysCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgCodeEnum;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * 消息处理中心
 *
 * @author https://github.com/yaphone
 * @version 1.0
 * @date 创建时间：2017年5月14日 下午12:47:50
 */
public class MsgCenter {
    private static Logger LOG = LoggerFactory.getLogger(MsgCenter.class);

    private static Core core = Core.getInstance();

    /**
     * 接收消息，放入队列
     *
     * @param msgList
     * @return
     * @author https://github.com/yaphone
     * @date 2017年4月23日 下午2:30:48
     */
    public static JSONArray produceMsg(JSONArray msgList) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < msgList.size(); i++) {
            JSONObject msg = new JSONObject();
            JSONObject m = msgList.getJSONObject(i);
            m.put("groupMsg", false);// 是否是群消息
            if (m.getString("FromUserName").contains("@@") || m.getString("ToUserName").contains("@@")) { // 群聊消息
                if (m.getString("FromUserName").contains("@@")
                        && !core.getGroupIdList().contains(m.getString("FromUserName"))) {
                    core.getGroupIdList().add((m.getString("FromUserName")));
                } else if (m.getString("ToUserName").contains("@@")
                        && !core.getGroupIdList().contains(m.getString("ToUserName"))) {
                    core.getGroupIdList().add((m.getString("ToUserName")));
                }
                // 群消息与普通消息不同的是在其消息体（Content）中会包含发送者id及":<br/>"消息，这里需要处理一下，去掉多余信息，只保留消息内容
                if (m.getString("Content").contains("<br/>")) {
                    String content = m.getString("Content").substring(m.getString("Content").indexOf("<br/>") + 5);
                    m.put("Content", content);
                    m.put("groupMsg", true);
                }
            } else {
                CommonTools.msgFormatter(m, "Content");
            }
            if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_TEXT.getCode())) { // words
                // 文本消息
                if (m.getString("Url").length() != 0) {
                    String regEx = "(.+?\\(.+?\\))";
                    Matcher matcher = CommonTools.getMatcher(regEx, m.getString("Content"));
                    String data = "Map";
                    if (matcher.find()) {
                        data = matcher.group(1);
                    }
                    msg.put("Type", "Map");
                    msg.put("Text", data);
                } else {
                    msg.put("Type", MsgTypeEnum.TEXT.getType());
                    msg.put("Text", m.getString("Content"));
                }
                m.put("Type", msg.getString("Type"));
                m.put("Text", msg.getString("Text"));
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_IMAGE.getCode())
                    || m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_EMOTICON.getCode())) { // 图片消息
                m.put("Type", MsgTypeEnum.PIC.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VOICE.getCode())) { // 语音消息
                m.put("Type", MsgTypeEnum.VOICE.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VERIFYMSG.getCode())) {// friends
                // 好友确认消息
                // MessageTools.addFriend(core, userName, 3, ticket); // 确认添加好友
                m.put("Type", MsgTypeEnum.VERIFYMSG.getType());

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SHARECARD.getCode())) { // 共享名片
                m.put("Type", MsgTypeEnum.NAMECARD.getType());

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VIDEO.getCode())
                    || m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MICROVIDEO.getCode())) {// viedo
                m.put("Type", MsgTypeEnum.VIEDO.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MEDIA.getCode())) { // 多媒体消息
                m.put("Type", MsgTypeEnum.MEDIA.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_STATUSNOTIFY.getCode())) {// phone
                // init
                // 微信初始化消息

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SYS.getCode())) {// 系统消息
                m.put("Type", MsgTypeEnum.SYS.getType());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_RECALLED.getCode())) { // 撤回消息

            } else {
                LOG.info("Useless msg");
            }
            LOG.info("收到消息一条，来自: " + m.getString("FromUserName"));
            result.add(m);
        }
        return result;
    }

    /**
     * 消息处理
     *
     * @param msgHandler
     * @author https://github.com/yaphone
     * @date 2017年5月14日 上午10:52:34
     */
    public static void handleMsg(IMsgHandlerFace msgHandler, JdbcTemplate jdbcTemplate) {
        while (true) {
            List<Person> list = new ArrayList();
            if (core.getMsgList().size() > 0 && core.getMsgList().get(0).getContent() != null) {
                if (core.getMsgList().get(0).getContent().length() > 0) {
                    BaseMsg msg = core.getMsgList().get(0);
                    if (msg.getType() != null) {
                        boolean flag = false; //初始化标识为不发送
                        try {
                            List<JSONObject> contactList = core.getContactList();
                            for (JSONObject jsonObject : contactList) {
                                Person person = new Person();
                                String userName = jsonObject.get("UserName").toString().trim();
                                String province = jsonObject.get("Province").toString().trim();
                                String remarkName = jsonObject.get("RemarkName").toString().trim();
                                person.setUserName(userName);
                                person.setProvince(province);
                                person.setRemarkName(remarkName);
                                list.add(person);
                            }
                            String sysSql = "select size from sys_code where id=1";
                            List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sysSql);
                            String size = list1.get(0).get("size").toString();


                            if (!size.equals(String.valueOf(list.size()))) {
                                LOG.info("保存实体类开始。。。。");
//								for (Person person : list) {
//									String insertSql="insert into person (user_name,province,remark_name) " +
//											"values('"+person.getUserName()+"','"+person.getProvince()+"'," +
//											"'"+person.getRemarkName()+"')";
                                String batchInsert = "insert into person(user_name,province,remark_name)" +
                                        "values(?,?,?)";
                                jdbcTemplate.batchUpdate(batchInsert, new BatchPreparedStatementSetter() {
                                    @Override
                                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                                        Person person = list.get(i);
                                        int k = 1;
                                        ps.setString(k++, person.getUserName());
                                        ps.setString(k++, person.getProvince());
                                        ps.setString(k++, person.getRemarkName());
                                    }

                                    @Override
                                    public int getBatchSize() {
                                        int size1 = list.size();
                                        LOG.info("jdbc批量插入数据量:" + size1);
                                        return size1;
                                    }
                                });
//								}
                                String updatesize = "update sys_code set size='" + String.valueOf(list.size()) + "'" +
                                        "where id=1 ";
                                jdbcTemplate.execute(updatesize);
                                list.clear();
                            }
                            if (msg.getType().equals(MsgTypeEnum.TEXT.getType())) {
                                String duxiaoping = "杜小平";
                                String baoshuchao = "包书超";
                                String perSql = "select user_name from person where remark_name='" + duxiaoping + "'";
                                List<String> stringList = Arrays.asList(duxiaoping, baoshuchao);
                                //jdbc多参数查询
                                String sql = "SELECT user_name FROM person WHERE remark_name in (:ids)";
                                Map<String, Object> paramMap = new HashMap<String, Object>();
                                paramMap.put("ids", stringList);

                                NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
                                List<String> user_name1 = jdbc.query(sql, paramMap, (rs, i) -> {
                                    //编辑rs
                                    String user_name = rs.getString("user_name");
                                    return user_name;
                                });
                                LOG.info("指定的发送人备注--->" + stringList.toString());
                                LOG.info("指定的发送人ID--->" + user_name1.toString());


                                String user_name = jdbcTemplate.queryForList(perSql).get(0).get("user_name").toString();
                                String result = msgHandler.textMsgHandle(msg);
                                LOG.info("core.getMsgList().get(0).getFromUserName()" + core.getMsgList().get(0).getFromUserName());
                                LOG.info("user_name" + user_name);


                                for (String s : user_name1) {
                                    if (core.getMsgList().get(0).getFromUserName().equals(s)) {
                                        MessageTools.sendMsgById(result, s);
                                    } else {
                                        MessageTools.sendMsgById(null, s);
                                    }
                                }
                            } else if (msg.getType().equals(MsgTypeEnum.PIC.getType())) {

                                String result = msgHandler.picMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.VOICE.getType())) {
                                String result = msgHandler.voiceMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.VIEDO.getType())) {
                                String result = msgHandler.viedoMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.NAMECARD.getType())) {
                                String result = msgHandler.nameCardMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.SYS.getType())) { // 系统消息
                                msgHandler.sysMsgHandle(msg);
                            } else if (msg.getType().equals(MsgTypeEnum.VERIFYMSG.getType())) { // 确认添加好友消息
                                String result = msgHandler.verifyAddFriendMsgHandle(msg);
                                MessageTools.sendMsgById(result,
                                        core.getMsgList().get(0).getRecommendInfo().getUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.MEDIA.getType())) { // 多媒体消息
                                String result = msgHandler.mediaMsgHandle(msg);
                                MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                core.getMsgList().remove(0);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

package cn.zhouyafeng.itchat4j.controller;

import cn.zhouyafeng.itchat4j.Wechat;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.reposotory.PersonRepository;
import cn.zhouyafeng.itchat4j.reposotory.SysCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

/**
 * @author 71768
 * @Title: StartController
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2616:06
 */
@RequestMapping("/login")
@Controller
public class StartController {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SysCodeRepository sysCodeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(StartController.class);

    @RequestMapping("/start")
    @ResponseBody
    public String start(){
        System.out.println("准备启动微信");
        String qrPath = "D://itchat4j//login"; // 保存登陆二维码图片的路径，这里需要在本地新建目录
        IMsgHandlerFace msgHandler = new SimpleDemo(); // 实现IMsgHandlerFace接口的类
        //每次启动把表清空
        String clear="delete from person";
        jdbcTemplate.execute(clear);
        LOG.info("好友数据清空完成...");
        String setOne="update sys_code set size='1' where id=1";
        LOG.info("好友数量清空完成...");
        jdbcTemplate.execute(setOne);
        Wechat wechat = new Wechat(msgHandler, qrPath,jdbcTemplate); // 【注入】
        wechat.start(); // 启动服务，会在qrPath下生成一张二维码图片，扫描即可登陆，注意，二维码图片如果超过一定时间未扫描会过期，过期时会自动更新，所以你可能
        return "启动成功";
    }
}

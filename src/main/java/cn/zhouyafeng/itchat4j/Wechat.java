package cn.zhouyafeng.itchat4j;

import cn.zhouyafeng.itchat4j.entity.Person;
import cn.zhouyafeng.itchat4j.reposotory.PersonRepository;
import cn.zhouyafeng.itchat4j.reposotory.SysCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zhouyafeng.itchat4j.controller.LoginController;
import cn.zhouyafeng.itchat4j.core.MsgCenter;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

public class Wechat {
	private static final Logger LOG = LoggerFactory.getLogger(Wechat.class);
	private IMsgHandlerFace msgHandler;
	JdbcTemplate jdbcTemplate;
	public Wechat(IMsgHandlerFace msgHandler, String qrPath,JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;

		// 登陆
		LoginController login = new LoginController();
		login.setJdbcTemplate(jdbcTemplate);
		login.login(qrPath);
	}
	public Wechat(IMsgHandlerFace msgHandler, String qrPath) {
		System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
		this.msgHandler = msgHandler;

		// 登陆
		LoginController login = new LoginController();
		login.login(qrPath);
	}

	public void start() {
		LOG.info("+++++++++++++++++++开始消息处理+++++++++++++++++++++");
		new Thread(new Runnable() {
			@Override
			public void run() {
				 MsgCenter.handleMsg(msgHandler,jdbcTemplate);
			}
		}).start();
	}


}

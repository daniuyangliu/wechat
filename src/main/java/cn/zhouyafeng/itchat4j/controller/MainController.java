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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 71768
 * @Title: StartController
 * @ProjectName itchat4j
 * @Description: TODO
 * @date 2019/6/2616:06213
 */
@Controller
public class MainController {
    @GetMapping(value = "/main")
    public String main(){
        return "login";
    }

}

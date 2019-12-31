package com.example.controller;

import com.example.entity.User;
import com.example.exception.BizException;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testBoot")
public class EmailController {

    Logger logger = LoggerFactory.getLogger(HtmlController.class);

    @Resource
    private UserService userService;

    @Resource
    private JavaMailSender mailSender;

    /**
     * 批量发送邮箱
     * @param map
     * @return
     */
    @RequestMapping("/sendEmailAll.do")
    public String sendEmailAll(@RequestBody Map<String,String> map) throws BizException{
        String admin = map.get("admin");
        if (!admin.equals("961324049@qq.com")){
            throw new BizException("非管理员用户禁止使用此功能!如发现将封号处理，详情请咨询树屋小狼(961324049@qq.com)");
        }
        String rel = "999999";
        String content = map.get("content");
        List<User> users = userService.findAllUser();
        for (User user:users
             ) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("树屋小狼<961324049@qq.com>");
            message.setTo(user.getEmail());
            message.setSubject("树屋主题：树洞小音");
            message.setText(content);
            mailSender.send(message);
        }

        return rel;
    }
}

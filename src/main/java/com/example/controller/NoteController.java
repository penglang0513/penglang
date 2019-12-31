package com.example.controller;

import com.example.exception.BizException;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/testBoot")
public class NoteController {
    Logger logger = LoggerFactory.getLogger(HtmlController.class);

    @Resource
    private UserService userService;

    /**
     * 根据nbseq删除笔记本
     * @param map
     * @return
     * @throws BizException
     */
    @RequestMapping("/deleteNoteBook.do")
    public String deleteNoteBook(@RequestBody Map<String,String> map) throws BizException{
        String rel = "999999";
        Map sendMap = new HashMap();
        sendMap.put("nbseq",map.get("nbseq"));
        int a = userService.updateNoteByNbseq(sendMap);
        int b = userService.updateNoteBookByNbseq(map);
        if (b>0){
            rel = "000000";
        }else {
            throw new BizException("笔记本删除失败！请截图联系管理员961324049@qq.com");
        }

        return rel;
    }


    /**
     * 根据id删笔记
     * @param map
     * @return
     * @throws BizException
     */
    @RequestMapping("/deleteNote.do")
    public String deleteNote(@RequestBody Map<String,String> map) throws BizException{
        String rel = "999999";
        Map sendMap = new HashMap();
        sendMap.put("noteid",map.get("noteid"));

        int a =userService.updateNoteByNoteId(sendMap);
        if (a>0){
            rel = "000000";
        }else {
            throw new BizException("分组删除失败！请截图联系管理员961324049@qq.com");
        }
        return rel;
    }
}

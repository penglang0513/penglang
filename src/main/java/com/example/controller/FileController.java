package com.example.controller;

import com.example.exception.BizException;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testBoot")
public class FileController {
    Logger logger = LoggerFactory.getLogger(HtmlController.class);

    @Resource
    private UserService userService;

    /**
     * 删除文件分组
     * @param map
     * @return
     * @throws BizException
     */
    @RequestMapping("/deleteFileType.do")
    public String deleteFileType(@RequestBody Map<String,String> map) throws BizException{
        String rel = "999999";
        logger.info("文件分组seq:"+map.get("fileseq"));
        Map sendMap = new HashMap();
        sendMap.put("fileseq",map.get("fileseq"));
        List<Map> fileList = userService.findFileBySeq(sendMap);
        if (fileList!=null&&!fileList.isEmpty()){
            String filename = fileList.get(0).get("filename").toString();
            String filepath = fileList.get(0).get("path").toString();
            String path = filepath.substring(0,filepath.indexOf(filename));
            logger.info("删除的目录："+path);
            File dir = new File(path);
            boolean deleteFlag = deleteDir(dir);
            if (deleteFlag){
                int fileinfo = userService.updateFileInfoStateByseq(sendMap);
                int filetype = userService.updateFileTypeStateByseq(sendMap);
                if (filetype>0){
                    rel = "000000";
                }else{
                    throw new BizException("分组删除失败！请截图联系管理员961324049@qq.com");
                }
            }else {
                throw new BizException("分组删除失败！请截图联系管理员961324049@qq.com");
            }

        }else {
            String paths = "/usr"+File.separator+"local"+File.separator+"myFile"+File.separator+map.get("userName")+File.separator+map.get("fzName");//生产
            File dir = new File(paths);
            boolean deleteFlag = deleteDir(dir);
            if (deleteFlag){
                int filetype = userService.updateFileTypeStateByseq(sendMap);
                if (filetype>0){
                    rel = "000000";
                }else{
                    throw new BizException("分组删除失败！请截图联系管理员961324049@qq.com");
                }
            }else {
                throw new BizException("分组删除失败！请截图联系管理员961324049@qq.com");
            }
        }

        return rel;
    }


    /**
     * 删除文件
     * @param map
     * @return
     * @throws BizException
     */
    @RequestMapping("/deleteFileInfo.do")
    public String deleteFileInfo(@RequestBody Map<String,String> map) throws BizException{
        String filePath = map.get("filePath");
        String rel ="999999";
        String fileId = map.get("fileId");
        Map sendMap = new HashMap();
        sendMap.put("fileId",fileId);
        int a = userService.updateFileInfoState(sendMap);
        if (a>0){
            File file = new File(filePath);
            if (file.delete()){
                rel = "000000";
            }else {
                throw new BizException("文件删除失败！请截图联系管理员961324049@qq.com");
            }
        }else {
            throw new BizException("文件删除失败！请截图联系管理员961324049@qq.com");
        }

        return rel;
    }


    private static boolean deleteDir(File dir) throws BizException{
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}

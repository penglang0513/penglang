package com.example.utils;

import com.example.controller.HtmlController;
import com.example.service.UserService;
import com.example.service.UserServiceImpl;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadUtil {
    Logger logger = LoggerFactory.getLogger(HtmlController.class);

    @Resource
    private UserService userService;

    private static UploadUtil uploadUtil;

    @PostConstruct
    public void init(){
        uploadUtil = this;
        uploadUtil.userService = this.userService;
    }

    private UploadUtil() {
    }


    private static UploadUtil getInstance(){
        return UploadUtil.uploadUtil;
    }

    public static UploadUtil getUploadUtil() {
        return uploadUtil;
    }

    public static void setUploadUtil(UploadUtil uploadUtil) {
        UploadUtil.uploadUtil = uploadUtil;
    }

    public String upload(MultipartFile[] multipartFiles, String path,String email,String model, HttpServletRequest request) throws Exception{

        File fileDirs = new File(path+File.separator);
        if (!fileDirs.exists()&&!fileDirs.isDirectory()){
            fileDirs.mkdirs();
        }
        try {
            if (multipartFiles!=null&&multipartFiles.length>0){
                for (int i=0;i<multipartFiles.length;i++){
                    try {
                        String storagePath = path +File.separator+ multipartFiles[i].getOriginalFilename();
                        logger.info("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                +"，保存的路径为：" + storagePath);

                        Streams.copy(multipartFiles[i].getInputStream(), new FileOutputStream(storagePath), true);

                        String userid = uploadUtil.userService.findUserId(email);//用户id
                        String fileid = "563"+System.currentTimeMillis();//文件id
                        Map<String,String> sendMap = new HashMap<>();
                        sendMap.put("fileId",fileid);
                        sendMap.put("fileName",multipartFiles[i].getOriginalFilename());
                        sendMap.put("filePath",storagePath);
                        sendMap.put("model",model);
                        uploadUtil.userService.addFile(sendMap);

                    }catch (IOException e){
                        throw new Exception("错误信息:"+e.getMessage());
                    }
                }
            }
        }catch (Exception e){
            throw new Exception("错误信息:"+e.getMessage());
        }
        return "000000";
    }

    public void downloadFile(String filePath, HttpServletResponse response){

        InputStream inputStream = null;
        OutputStream os = null;
        try {
            File file = new File(filePath);
            String fileName = file.getName();
            inputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            inputStream.close();
            byte[] data = outputStream.toByteArray();
            response.reset();
            logger.info("下载文件名****"+fileName);

            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Length", file.length()+"");//设置内容长度
            os = response.getOutputStream();
            os.write(data);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (os!=null){
                try {
                    os.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

package com.example.controller;

import com.example.exception.BizException;
import com.example.utils.MD5;
import com.example.utils.UploadUtil;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/testBoot")
public class UserController {
    Logger logger = LoggerFactory.getLogger(HtmlController.class);

    @Resource
    private UserService userService;

    @Resource
    private JavaMailSender mailSender;

    private final static String  fileDir="files";

    /**
     * 根据id查单个笔记
     * @param map
     * @return
     */
    @RequestMapping("/findNoteById.do")
    public Map findNoteById(@RequestBody Map<String,String> map){
        Map sendMap = new HashMap();
        sendMap.put("noteid",map.get("noteid"));

        Map relMap = userService.findNoteById(sendMap);
        return relMap;
    }

    /**
     * 根据seq查找笔记
     * @param map
     * @return
     */
    @RequestMapping("/findNoteBySeq.do")
    public List<Map> findNoteBySeq(@RequestBody Map<String,String> map){
        Map sendMap = new HashMap();
        sendMap.put("nbseq",map.get("nbseq"));
        List<Map> list = userService.findNoteBySeq(sendMap);
        return list;
    }

    /**
     * 新增笔记
     * @param map
     * @return
     * @throws BizException
     */
    @RequestMapping("/addNote.do")
    public String addNote(@RequestBody Map<String,String> map) throws BizException{
        String rel = "999999";
        logger.info("*********************"+map.get("notecontent").toString());
        logger.info("+++++++++++++++++++++"+map.get("noteid").toString());
        if(map.get("noteid")==null||map.get("noteid")==""){//走新增
            if (map.get("nbseq")==null||map.get("nbseq")==""){
                throw new BizException("请选择笔记本！");
            }
            if (map.get("notename")==null||map.get("notename")==""){
                throw new BizException("请输入标题！");
            }
            if (map.get("notecontent")==null||map.get("notecontent")==""){
                throw new BizException("请输入内容！");
            }
            String noteid = "08"+System.currentTimeMillis();
            Map sendMap = new HashMap();
            sendMap.put("noteid",noteid);
            sendMap.put("nbseq",map.get("nbseq"));
            sendMap.put("notename",map.get("notename"));
            sendMap.put("notecontent",map.get("notecontent"));

            int a = userService.addNote(sendMap);
            if (a>0){
                rel = "000000";
            }else {
                throw new BizException("添加笔记失败！");
            }
        }else {//走修改
            Map sendMap = new HashMap();
            sendMap.put("noteid",map.get("noteid"));
            sendMap.put("notecontent",map.get("updatecontent"));

            int a = userService.updateNote(sendMap);
            if (a>0){
                rel = "000000";
            }else {
                throw new BizException("修改笔记失败！");
            }
        }

        return rel;
    }

    /**
     * 新增笔记本
     * @param map
     * @return
     * @throws BizException
     */
    @RequestMapping("/addNoteBook.do")
    public String addNoteBook(@RequestBody Map<String,String> map) throws BizException{
        String cifseq = userService.findUserId(map.get("userName"));
        if (cifseq==null){
            throw new BizException("客户号未正确传递！");
        }
        Map findMap = new HashMap();
        findMap.put("cifseq",cifseq);
        findMap.put("nbname",map.get("nbname"));
        String nbseq = userService.findNoteBookByName(findMap);
        if (nbseq!=null){
            throw new BizException("该笔记已存在！");
        }
        String relt = "999999";
        Map sendMap = new HashMap();
        nbseq = "09"+System.currentTimeMillis();
        sendMap.put("cifseq",cifseq);
        sendMap.put("nbseq",nbseq);
        sendMap.put("nbname",map.get("nbname"));
        int a = userService.addNoteBook(sendMap);
        if (a>0){
            relt = "000000";
        }
        return relt;
    }

    /**
     * 根据客户号查用户所有笔记本
     * @param map
     * @return
     */
    @RequestMapping("/findNoteBookByCifSeq.do")
    public List<Map> findNoteBookByCifSeq(@RequestBody Map<String,String> map) throws BizException{
        String cifseq = userService.findUserId(map.get("userName"));
        if (cifseq==null){
            throw new BizException("客户号未正确传递！");
        }
        Map sendMap = new HashMap();
        sendMap.put("cifseq",cifseq);
        List<Map> list = userService.findNoteBookByCifSeq(sendMap);
        return list;
    }

    /**
     * 下载文件
     * @param map
     * @param req
     * @param res
     */
    @RequestMapping("/downFile.do")
    public void downFile(@RequestBody Map<String,String> map, HttpServletRequest req, HttpServletResponse res){
        logger.info("*****文件下载路径******"+map.get("filePath"));
        UploadUtil.getUploadUtil().downloadFile(map.get("filePath"),res);

    }


    /**
     * 新增分组
     * @param map
     * @return
     */
    @RequestMapping("/addFz.do")
    public String addFz(@RequestBody Map<String,String> map) throws BizException{
        String userid = userService.findUserId(map.get("userName"));
        if (userid==null){
            throw new BizException("新增分组失败！");
        }
        String ftId = "54"+System.currentTimeMillis();
        String rel = "";
        Map sendMap = new HashMap();
        sendMap.put("userId",userid);
        sendMap.put("ftId",ftId);
        sendMap.put("ftName",map.get("fzName"));

        Map relutMap = userService.findFtByName(sendMap);
        if (relutMap!=null){
            throw new BizException("该分组已存在！");
        }
        String paths = "/usr"+File.separator+"local"+File.separator+"myFile"+File.separator+map.get("userName")+File.separator+map.get("fzName");//生产
        Path path = Paths.get(paths);

        if (!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
        int a = userService.addFz(sendMap);
        if (a<1){
            rel = "999999";
            throw new BizException("新增分组失败！");
        }else {
            rel = "000000";
        }
        return rel;
    }

    /**
     * 查询收藏
     */
    @RequestMapping("/findShouC.do")
    public List<Map> findShouC(@RequestBody Map<String,String> map){
        String userid = userService.findUserId(map.get("userName"));
        Map sendMap = new HashMap();
        sendMap.put("userId",userid);
        sendMap.put("fileId",map.get("fileId"));
        List<Map> list = userService.findShouC(sendMap);
        return list;
    }

    /**
     * 查收藏
     * @param map
     * @return
     */
    @RequestMapping("/findSC.do")
    public List<Map<String,String>> findSC(@RequestBody Map<String,String> map){
        String userid = userService.findUserId(map.get("userName"));

        List<Map<String,String>> list = userService.findSC(userid);
        return list;
    }

    /**
     * 上传
     * @param multipartFiles
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/fileUpload.do")
    public  String filetest(@RequestParam("file") MultipartFile[] multipartFiles,
                            HttpServletRequest request) throws Exception {
        String email= request.getParameter("email");
        String model= request.getParameter("model");
        String uu = userService.findFileTypeName(model);
        String path = "/usr"+File.separator+"local"+File.separator+"myFile"+File.separator+email+File.separator+uu;//生产
//        String path = "E:\\usr"+File.separator+"local"+File.separator+"myFile"+File.separator+email+File.separator+uu;//本地
        String reslut = "";
        try {
            reslut = UploadUtil.getUploadUtil().upload(multipartFiles,path,email,model,request);
//            UploadUtil uploadUtil = new UploadUtil();
//            reslut = uploadUtil.upload(multipartFiles,path,email,request);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }


        return reslut;
    }

    /**
     * 上传测试
     * @param multipartFiles
     * @param request
     * @return
     */
    @RequestMapping("/filetestUpload.do")
    public String fileUpload(@RequestParam("file") MultipartFile[] multipartFiles,
                              HttpServletRequest request){
        String loadPath = "files";
        String email = request.getParameter("email");
        logger.info("*******eamil*****"+email);
//        logger.info("****map****"+map.toString());
        String path = System.getProperty("user.home")+File.separator+fileDir+File.separator+loadPath+File.separator;
        File fileDirs = new File(path);
        if (!fileDirs.exists()&&!fileDirs.isDirectory()){
            fileDirs.mkdirs();
        }
        try {
            if (multipartFiles!=null&&multipartFiles.length>0){
                for (int i=0;i<multipartFiles.length;i++){
                    try {
                        String storagePath = path + multipartFiles[i].getOriginalFilename();
                        logger.info("上传的文件：" + multipartFiles[i].getName() + "," + multipartFiles[i].getContentType() + "," + multipartFiles[i].getOriginalFilename()
                                +"，保存的路径为：" + storagePath);
                        Streams.copy(multipartFiles[i].getInputStream(), new FileOutputStream(storagePath), true);
                    }catch (IOException e){
                        logger.info("errror******"+e);
                    }
                }
            }
        }catch (Exception e){
            logger.info("errror******"+e);
        }
        return "000000";
    }

    /**
     * 查询等级
     * @param map
     * @param session
     * @return
     * @throws BizException
     */
    @RequestMapping("/findLevel.do")
    public String findLevel(@RequestBody Map<String,String> map,HttpSession session) throws BizException{
        if (map.get("userName")==null){
            throw  new BizException("系统繁忙，请联系管理员！");
        }
        String level = userService.findLevel(map.get("userName"));
        if (null==level){
            throw  new BizException("系统繁忙，请联系管理员！");
        }
        logger.info("******用户等级******"+level);
        return level;
    }

    /**
     * 登录查询
     * @param map
     * @param session
     * @return
     * @throws BizException
     */
    @RequestMapping("/commAll.do")
    public User commAll(@RequestBody Map<String,String> map,HttpSession session) throws BizException{
        if (map.get("userName")==null){
            throw  new BizException("用户未登陆！");
        }
        if (session.getAttribute(map.get("userName"))==null){
            throw  new BizException("用户未登陆！");
        }
        List<User> userList = userService.findByEmail(map.get("userName"));
        if (userList==null){
            throw  new BizException("用户不存在！");
        }
        return userList.get(0);
    }

    /**
     * 邮箱发送
     * @param map
     * @param session
     * @return
     */
    @RequestMapping("/sendMail.do")
    public String sendMail(@RequestBody Map<String,String> map,HttpSession session){
        int  maxNum = 36;
        int i;
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 6){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        logger.info("map"+map.toString());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("树屋小狼<961324049@qq.com>");
        message.setTo(map.get("userName"));
        message.setSubject("树屋主题：邮箱验证码");
        message.setText("尊敬的树屋大大，您的邮箱验证码是："+pwd.toString());
        mailSender.send(message);
        session.setAttribute(map.get("userName")+"mail",MD5.getInstance().getMD5ofStr(pwd.toString()));

        return "000000";
    }


    /**
     * 登录
     * @param map
     * @param request
     * @param session
     * @return
     * @throws BizException
     */
    @RequestMapping("/forms.do")
    public User forms (@RequestBody Map<String,String> map, HttpServletRequest request, HttpSession session) throws BizException{

        User user = new User();
        List<User> userList = userService.findByEmail(map.get("userName"));

        if (userList.size()==0||userList==null){
            //throw new Exception("用户不存在");
            throw new BizException("用户不存在");
        }
        user.setEmail(map.get("userName"));
        user.setPassword(MD5.getInstance().getMD5ofStr(map.get("password")+userList.get(0).getId()));
        user = this.userService.findIdAndPassword(user);
        if(null==user){
            throw new BizException("密码不正确");
        }
        logger.info("*******user*******"+user.toString());
        session.setAttribute(user.getEmail(),user);

        return user;
    }

    /**
     * 注册
     * @param map
     * @param session
     * @return
     * @throws BizException
     */
    @RequestMapping("/register.do")
    public User register (@RequestBody Map<String,String> map,HttpSession session) throws BizException{

        String emailCodeOld = (String) session.getAttribute(map.get("userName")+"mail");
        String emailCodeNew = MD5.getInstance().getMD5ofStr(map.get("emailCode"));
        if (!emailCodeOld.equals(emailCodeNew)){
            throw new BizException("邮箱验证码不正确！");
        }
        User user = new User();

        List<User> userList = userService.findByEmail(map.get("userName"));

        if (userList.size()>0){
            throw new BizException("用户已存在！");
        }
        user.setEmail(map.get("userName"));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String dates= simpleDateFormat.format(date);
        String idqian = (Long.parseLong((System.currentTimeMillis()+"").substring(0,7))+1l)+"";
        String id = idqian+dates;
        user.setId(id);
        user.setPassword(MD5.getInstance().getMD5ofStr(map.get("password")+id));
        logger.info("注册密码****"+user.getPassword());
        user.setUserName(map.get("userName"));
        int suId = userService.insertSelective(user);
        if(suId == 0){
            throw new BizException("注册失败！");
        }
        return user;
    }

}
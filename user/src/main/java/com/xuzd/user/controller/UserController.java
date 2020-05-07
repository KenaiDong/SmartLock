package com.xuzd.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xuzd.commons.baseclass.Constant;
import com.xuzd.commons.model.JsonModel;
import com.xuzd.commons.model.PageModel;
import com.xuzd.commons.utils.EncryptUtil;
import com.xuzd.commons.utils.SessionUtils;
import com.xuzd.commons.utils.StringUtils;
import com.xuzd.user.entity.AccountRecord;
import com.xuzd.user.entity.Photo;
import com.xuzd.user.entity.User;
import com.xuzd.user.model.AccountStatus;
import com.xuzd.user.model.UserStatus;
import com.xuzd.user.service.AccountRecordService;
import com.xuzd.user.service.PhotoService;
import com.xuzd.user.service.UserService;
import com.xuzd.user.service.CityService;
import com.zhenzi.sms.ZhenziSmsClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api("用户管理")
@Controller
@Configuration
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    CityService cityService;

    @Autowired
    HttpServletRequest req;

    @Autowired
    PhotoService photoService;

    @Autowired
    AccountRecordService accountRecordService;

    @Value("${baidu.AK}")
    private String BAIDU_AK;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final static String MSG_IS_EXIST = "用户信息已存在";

    private final static String MSG_SUCCESS = "操作成功";

    private final static String MSG_FAILURE = "操作失败";

    private final static String MSG_PASSWORD_FAILURE = "密码不正确";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static int r = (int)(Math.random()*8998)+1000+1;

    /**
     * 图片处理
     */
    private final List<String> IMG_EXTENSION_LIST = Lists
            .newArrayList("bmp,jpg,jpeg,png,tif,gif,pcx,tga,exif,fpx,svg,psd,cdr,pcd,dxf,ufo,eps,ai,raw,wmf,webp"
                    .split(","));

    private final Map<String, String> CONTENT_TYPE_MAP = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("gif", "image/gif");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
        }
    };

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 日期处理
        simpleDateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel login(User user, HttpServletRequest request){
        User u = userService.findByName(user.getLoginName());
        if (ObjectUtils.isEmpty(u)) return new JsonModel(false,"用户名不存在");
        if (u.getPassword().equals(EncryptUtil.shaAndMd5(user.getPassword()))){
            //登录IP地址获取
            try {
                u.setIpAddress(getIp(null));
                u.setLastLoginDate(new Date());
                u.setDateFormat(simpleDateFormat.format(new Date()));
                userService.updateUser(u);
            }catch (Exception e){
                e.printStackTrace();
                logger.info("登录ip地址获取失败");
            }
            return new JsonModel(true,"success",u);
        }else return new JsonModel(false,"密码错误");
    }

    /**
     * 根据ip获取地址信息，若未填ip，则会返回本机地址(接入百度LBS)
     * @return
     */
    public String getIp(String ip){
        String url = "http://api.map.baidu.com/location/ip";
        JSONObject obj = new JSONObject();
        obj.put("coor","bd09ll");
        obj.put("ip",ip);
        JSONObject result = getUrl(url, obj);
        return result.getJSONObject("content").getString("address");
    }

    /**
     * 根据输入的地址查询地理数据(接入百度LBS)
     * @param address
     * @return
     */
    @ApiOperation("根据输入的地址查询地理数据")
    @RequestMapping(value = "/getCoords", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getAddressCode(String address, @RequestParam(required = false) String city){
        String url = "http://api.map.baidu.com/geocoding/v3/";
        JSONObject obj = new JSONObject();
        obj.put("output","json");
        obj.put("address",address);
        obj.put("city",city);
        return getUrl(url,obj);
    }

    /**
     * 根据传入的JSON获取静态地图
     * @param obj
     * @return
     */
    public String getMapUrl(JSONObject obj){
        String url = "";
        try {
            URIBuilder uriBuilder = new URIBuilder("http://api.map.baidu.com/staticimage/v2");
            obj.put("ak", BAIDU_AK);
            obj.put("copyright", 1);
            if (ObjectUtils.isEmpty(obj.get("zoom"))) obj.put("zoom",11);
            if (ObjectUtils.isEmpty(obj.get("width"))) obj.put("width",1024);
            if (ObjectUtils.isEmpty(obj.get("height"))) obj.put("height",1024);
            List<NameValuePair> list = Lists.newArrayList();
            obj.keySet().forEach(key -> {list.add(new BasicNameValuePair(key, obj.getString(key)));});
            uriBuilder.addParameters(list);
            url = uriBuilder.build().toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 接口GET方式调用百度LBS接口
     * @param url
     * @param obj
     * @return
     */
    public JSONObject getUrl(String url, JSONObject obj) {
        // 统一增添百度的AK码
        obj.put("ak", BAIDU_AK);
        try {
        HttpClient client = HttpClients.createDefault();
        // 要调用的接口方法
        URIBuilder uriBuilder = new URIBuilder(url);
        List<NameValuePair> list = Lists.newArrayList();
        obj.keySet().forEach(key -> {list.add(new BasicNameValuePair(key, obj.getString(key)));});
        uriBuilder.addParameters(list);
        HttpGet get = new HttpGet(uriBuilder.build());
        get.addHeader("Content-Type", "text/plain;charset=UTF-8");
        HttpResponse res = client.execute(get);
        String response1 = EntityUtils.toString(res.getEntity());
        JSONObject object = JSON.parseObject(response1);
        return object;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("接口调用失败");
            return new JSONObject();
        }
    }

    public static String getHttp(String url) {
        String responseMsg = "";
        return responseMsg;
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @ApiOperation("用户注册")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel register(User user, String sms){
        try {
            if (!ObjectUtils.isEmpty(userService.findByName(user.getLoginName()))){
                return new JsonModel(false, "用户名已被使用");
            }
            if (!ObjectUtils.isEmpty(userService.findByPhone(user.getPhone()))){
                return new JsonModel(false,"该手机号已被注册");
            }
            //TODO 增添一个万能验证码
            if ("test".equals(sms) || Integer.parseInt(sms) == r){
                user.setStatus(UserStatus.OK);
                user.setRole(0);
                user.setPassword(EncryptUtil.shaAndMd5(user.getPassword()));
                user.setBalance(0L);
                user.setIpAddress(getIp(null));
                user.setLastLoginDate(new Date());
                user.setDateFormat(simpleDateFormat.format(new Date()));
                userService.addUser(user);
            }else {
                return new JsonModel(false,"验证码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, "出现异常错误");
        }
        return new JsonModel(true, user);
    }

    @ApiOperation("发送验证码校验")
    @RequestMapping(value = "/register/sms", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel sendVerificationCode(@RequestParam String phone){
        String apiUrl = "https://sms_developer.zhenzikj.com";
        String appId = "105176";
        String appSecret = "687ab8dd-f9b7-42ab-80ce-44f9eec95349";
        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
        Map<String, String> params = new HashMap<String, String>();
//        params.put("message", "【智能锁】您的注册验证码为：" + r);
//        params.put("number", phone);
        String message = "您的注册验证码为: " + r;
        params.put("message", message);
        params.put("number", phone);
        try {
            String result = client.send(params);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false,"短信发送失败");
        }
        return new JsonModel(true,"短信发送成功");
    }

    /**
     * 根据id获取用户基础信息
     *
     * @param userId
     * @return
     */
    @ApiOperation("根据id获取用户基础信息")
    @RequestMapping(value = "/{userId}/info", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel info(@PathVariable Long userId) {
        JsonModel json = new JsonModel();
        json.setSuccess(true);
        json.setMsg(MSG_SUCCESS);
        json.setObj(userService.findById(userId));
        return json;
    }

    /**
     * 根据loginName获取用户基础信息
     *
     * @param loginName
     * @return
     */
    @ApiOperation("根据用户名获取用户基础信息")
    @RequestMapping(value = "/loginName/info", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel infoByName(@RequestParam("loginName") String loginName) {
        JsonModel json = new JsonModel();
        json.setSuccess(true);
        json.setMsg(MSG_SUCCESS);
        json.setObj(userService.findByName(loginName));
        return json;
    }

    /**
     * 根据id获取用户基础信息
     *
     * @return
     */
    @ApiOperation("根据id获取用户基础信息")
    @RequestMapping(value = "getLoginUserName", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel info(HttpSession session) {
        Long userId = SessionUtils.getCurrentUserIdFromSession(session);
        if (null != userId) {
            User user = userService.findById(userId);
            if (ObjectUtils.isEmpty(user)) {
                return new JsonModel(false, "用户信息为空");
            }
            JSONObject obj = JSONObject.parseObject(JSON.toJSONString(user));
            return new JsonModel(true, obj);
        } else {
            return new JsonModel(false, MSG_FAILURE);
        }
    }

//    /**
//     * 新增用户
//     *
//     * @param user
//     * @return
//     */
//    @ApiOperation("新增用户")
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ResponseBody
//    @SuppressWarnings("unchecked")
//    public JsonModel add(@Valid User user, Long[] roleIds, String limitDate) {
//        JsonModel json = new JsonModel();
//        user.setStatus(UserStatus.ToDeleted);
//        List<User> users = (List<User>) userService.findNotByPage(new PageModel(), user).getObject();
//        if (null != users && users.size() > 0) {
//            json.setMsg(MSG_IS_EXIST);
//            json.setSuccess(false);
//        } else {
//            try {
//                user.setCreateDate(new Date());
//                user.setStatus(UserStatus.OK);
//                user.setBalance(0L);
//                User addUser = userService.addUser(user, roleIds);
//                /*// 发送MQ消息至workFlow
//                List<User> userList = new ArrayList<User>(1);
//                userList.add(addUser);
//                JSONObject usersJson = sendMsgHandler.buildUserData(userList, false, null, null);
//                // 临时处理为，每次都重新加载用户角色关系
//                JSONObject userRolesJson = sendMsgHandler.buildUserData(userRoleService.findAll(), true, null, null);
//                sendMsgHandler.sendMsgToWorkFlow(null, userRolesJson, usersJson);*/
//                json.setMsg(MSG_SUCCESS);
//                json.setSuccess(true);
//            } catch (Exception e) {
//                logger.error("用户创建失败:{}", e);
//                json.setMsg(MSG_FAILURE);
//                json.setSuccess(false);
//            }
//        }
//        return json;
//    }

    /**
     * 用户基础信息更新
     *
     * @param user 需要更新的对象
     * @return
     */
    @ApiOperation("更新用户")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel update(@PathVariable Long id, @Valid User user, String limitDate, HttpSession session) {
        try {
            Long currentUserId = SessionUtils.getCurrentUserIdFromSession(session);
            // 判断账号是否已存在[loginName\phone\email\员工号]
            @SuppressWarnings("unchecked")
            List<User> users = (List<User>) userService.findNotByPage(new PageModel(), user).getObject();
            if (null != users && users.size() > 0) {
                return new JsonModel(false, MSG_IS_EXIST, 0);
            }

            User updateUser = userService.updateUser(user);

            if (currentUserId.equals(id)) {
                // 更新session中的用户信息
                session.setAttribute(Constant.SESSION_USER, JSON.parseObject(JSON.toJSONString(updateUser)));
            }

            /*// 发送MQ消息至workFlow
            List<User> userList = new ArrayList<User>(1);
            userList.add(updateUser);
            JSONObject usersJson = sendMsgHandler.buildUserData(null, null, userList, null);
            sendMsgHandler.sendMsgToWorkFlow(null, null, usersJson);*/
            return new JsonModel().success(updateUser);
        } catch (Exception e) {
            logger.error("编辑用户异常：{}", e);
            return new JsonModel().serverError(e.getMessage());
        }
    }

    /**
     * 更新用户邮箱或电话
     *
     * @param email
     * @param phone
     * @return
     */
    @ApiOperation("更新用户邮箱或电话")
    @RequestMapping(value = "/{id}/updateEmailOrPhone", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel updateEmailOrPhone(@PathVariable Long id, String email, String phone, HttpSession session) {
        JsonModel json = new JsonModel(true, "", null, 0);
        try {
            Long userId = SessionUtils.getCurrentUserIdFromSession(session);
            User user = userService.findById(userId);
            if (userId.equals(id)) {
                user.setEmail(email);
                user.setPhone(phone);
                userService.updateUser(user);
            }
        } catch (Exception e) {
            logger.error("更新用户邮箱或电话失败:{}", e);
            return new JsonModel().serverError(e.getMessage());
        }
        return json;
    }

    @ApiOperation("账户消费")
    @RequestMapping(value = "/consumption", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel consumption(Long id, Long amount){
        String info = "消费" + amount + "元";
        try {
            User user = userService.findById(id);
            if (ObjectUtils.isEmpty(user)){
                return new JsonModel(false, "用户不存在");
            }
            Long balance = user.getBalance();
            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setUserId(id);
            accountRecord.setTime(new Date());
            accountRecord.setBalance(balance - amount);
            accountRecord.setAmount(amount);
            accountRecord.setStatus(AccountStatus.Consumption);
            accountRecord.setInfo(info); //String.format("%.2f",amount)
            accountRecordService.addAccountRecord(accountRecord);
            user.setBalance(balance - amount);
            userService.updateUser(user);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, "账户数据错误");
        }
        return new JsonModel(true, info,"");
    }

    //TODO 暂时使用简易的充值方式，创建基本数据，之后需加入支付宝沙箱充值环境
    @ApiOperation("账户充值")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody
    public JsonModel recharge(Long id, Long amount){
        String info = "充值" + amount + "元";
        try {
            User user = userService.findById(id);
            if (ObjectUtils.isEmpty(user)){
                return new JsonModel(false, "用户不存在");
            }
            Long balance = user.getBalance();
            AccountRecord accountRecord = new AccountRecord();
            accountRecord.setUserId(id);
            accountRecord.setBalance(balance + amount);
            accountRecord.setTime(new Date());
            accountRecord.setAmount(amount);
            accountRecord.setStatus(AccountStatus.Recharge);
            accountRecord.setInfo(info); //String.format("%.2f",amount)
            accountRecordService.addAccountRecord(accountRecord);
            user.setBalance(balance + amount);
            userService.updateUser(user);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, "账户数据错误");
        }
        return new JsonModel(true, info,"");
    }

    @ApiOperation("用户账单查询")
    @RequestMapping(value = "/getAccount", method = RequestMethod.GET)
    @ResponseBody
    public JsonModel getAccount(Long userId){
        List<AccountRecord> accountRecords = Lists.newArrayList();
        try {
            accountRecords = accountRecordService.findAccountRecordById(userId);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonModel(false, "用户账单查询失败");
        }
        return new JsonModel(true, accountRecords);
    }

    /**
     * 上传文件
     * @param file 上传的文件
     * @param session  会话
     */
    @ApiOperation("上传文件")
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public JsonModel uploadFile(@ApiParam("上传的文件") @RequestParam("uploaded_file") MultipartFile file,
                                HttpSession session) {
        if (file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename())) {
            return new JsonModel(false, "请上传文件");
        }
        try {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            Photo fileInfo = new Photo();
            fileInfo.setName(fileName);
            fileInfo.setExtension(extension);
//            fileInfo.setUserId(SessionUtils.getCurrentUserIdFromSession(session));
            fileInfo.setFileStream(file.getBytes());
            photoService.save(fileInfo);
            JSONObject result = new JSONObject();
            result.put("id", fileInfo.getId());
            return new JsonModel(true, "上传成功", result);
        } catch (IOException e) {
            logger.error("上传失败", e);
            return new JsonModel(false, "上传失败");
        }
    }


    /**
     * 根据文件ID获取图片
     * @param id 文件ID
     * @param resp 响应消息
     */
    @ApiOperation("根据文件ID获取图片")
    @RequestMapping(value = "/getImg/{id}", method = RequestMethod.GET)
    public void getImage(@ApiParam("文件ID") @PathVariable Long id,
                         HttpServletResponse resp) {

        byte[] data;
        String ext;

        Photo f = photoService.findById(id);
        if(f == null){
            logger.error("文件 -> {} not exists ！", id);
            return;
        }
        ext = f.getExtension();
        data = f.getFileStream();


        if (!IMG_EXTENSION_LIST.contains(ext)) {
            logger.error("文件 -> {} 非图片！", id);
            return;
        }
        try {
            resp.setContentType(CONTENT_TYPE_MAP.getOrDefault(ext, "image/png"));
            ServletOutputStream out = resp.getOutputStream();
            out.write(data);
            out.flush();
        } catch (IOException e) {
            logger.error("下载图片失败:" + id, e);
        }
    }



}

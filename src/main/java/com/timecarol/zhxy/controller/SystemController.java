package com.timecarol.zhxy.controller;

import com.timecarol.zhxy.pojo.Admin;
import com.timecarol.zhxy.pojo.LoginForm;
import com.timecarol.zhxy.pojo.Student;
import com.timecarol.zhxy.pojo.Teacher;
import com.timecarol.zhxy.service.AdminService;
import com.timecarol.zhxy.service.StudentService;
import com.timecarol.zhxy.service.TeacherService;
import com.timecarol.zhxy.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "系统控制器")
@Controller
@RequestMapping("/sms/system")
public class SystemController {

    public static final String VERIFY_CODE_KEY = "verifyCode";
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getVerifiCodeImage", method = {RequestMethod.GET})
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取图片
        BufferedImage image = CreateVerifyCodeImage.getVerifyCodeImage();
        //获取图片上的验证码
        char[] c = CreateVerifyCodeImage.getVerifyCode();
        String verifyCode = String.valueOf(c);
        //将验证码文本放入session域, 为下一次做准备
        request.getSession().setAttribute(VERIFY_CODE_KEY, verifyCode);
        //将验证码的图片响应给浏览器
        response.setContentType("image/jpeg");
        ImageIO.write(image, "JPEG", response.getOutputStream());
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    @ResponseBody
    public Result<Object> login(
            @ApiParam("LoginForm对象的JSON格式") @RequestBody LoginForm loginForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        //校验验证码
        String sessionVerifyCode = (String) session.getAttribute(VERIFY_CODE_KEY);
        String loginVerifyCode = loginForm.getVerifiCode();
        //判断验证码是否失效
        if (sessionVerifyCode == null || sessionVerifyCode.length() == 0) {
            return Result.build(null, ResultCodeEnum.LOGIN_CODE);
        }
        if (!sessionVerifyCode.equalsIgnoreCase(loginVerifyCode)) {
            Result<Object> result = Result.build(null, ResultCodeEnum.CODE_ERROR);
            result.setMessage("验证码有误, 请小心输入后重试");
            return result;
        }
        //从session中移除验证码
        session.removeAttribute(VERIFY_CODE_KEY);
        //准备一个map用来存放响应的数据
        Map<String, String> map = new HashMap<>();
        //分用户类型进行校验
        switch (loginForm.getUserType()) {
            case 1: //管理员
                Admin admin = adminService.login(loginForm);
                if (admin != null) {
                    //用户的类型和ID转换成密文, 以token的名称向客户端反馈
                    String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                    map.put("token", token);
                    return Result.ok(map);
                } else {
                    Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_ERROR);
                    result.setMessage("用户名或者密码有误");
                    return result;
                }
            case 2: //学生
                Student student = studentService.login(loginForm);
                if (student != null) {
                    //用户的类型和ID转换成密文, 以token的名称向客户端反馈
                    String token = JwtHelper.createToken(student.getId().longValue(), 2);
                    map.put("token", token);
                    return Result.ok(map);
                } else {
                    Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_ERROR);
                    result.setMessage("用户名或者密码有误");
                    return result;
                }
            case 3: //教师
                Teacher teacher = teacherService.login(loginForm);
                if (teacher != null) {
                    //用户的类型和ID转换成密文, 以token的名称向客户端反馈
                    String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                    map.put("token", token);
                    return Result.ok(map);
                } else {
                    Result<Object> result = Result.build(null, ResultCodeEnum.LOGIN_ERROR);
                    result.setMessage("用户名或者密码有误");
                    return result;
                }
        }
        //不存在的用户类型
        return Result.build(null, ResultCodeEnum.ILLEGAL_REQUEST);
    }

    @ApiOperation("获取登录信息")
    @GetMapping("/getInfo")
    @ResponseBody
    public Result<Object> getInfo(
            @ApiParam("token字符串") @RequestHeader("token") String token) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            Result<Object> result = Result.fail();
            return result.setMessage("Token已过期");
        }
        //从token中解析出用户id和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        if (userType == null || userId == null) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getById(userId);
                if (admin != null) {
                    map.put("userType", userType);
                    map.put("user", admin);
                    return Result.ok(map);
                }
                break;
            case 2:
                Student student = studentService.getById(userId);
                if (student != null) {
                    map.put("userType", userType);
                    map.put("user", student);
                    return Result.ok(map);
                }
                break;
            case 3:
                Teacher teacher = teacherService.getById(userId);
                if (teacher != null) {
                    map.put("userType", userType);
                    map.put("user", teacher);
                    return Result.ok(map);
                }
                break;
        }
        return Result.build(null, ResultCodeEnum.ILLEGAL_REQUEST);
    }

    @ApiOperation("上传图片")
    @PostMapping("/headerImgUpload")
    @ResponseBody
    public Result<Object> headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        String perfixName = DigestUtils.md5DigestAsHex(multipartFile.getBytes());
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null) {
            return Result.fail();
        }
        String surfixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = perfixName + surfixName;
        //保存文件 将文件发送到第三方/独立的图片服务器上
        String path = new ClassPathResource("public/upload/").getURL().getPath() + newFileName;
        multipartFile.transferTo(new File(path));
        return Result.ok("upload/" + newFileName);
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    @ResponseBody
    public Result<Object> updatePwd(@ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
                                    @ApiParam("新密码") @PathVariable("newPwd") String newPwd,
                                    @ApiParam("token字符串") @RequestHeader("token") String token) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.fail().setMessage("token失效, 请重新登录后修改密码");
        }
        Integer userType = JwtHelper.getUserType(token);
        Long userId = JwtHelper.getUserId(token);
        if (userType == null) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        switch (userType) {
            case 1:
                Admin byId = adminService.getById(userId);
                if (!MD5.encrypt(oldPwd).equals(byId.getPassword())) {
                    //原密码不正确
                    return Result.fail().setMessage("原密码有误!");
                }
                boolean isSuccess = adminService.updateById(byId.setPassword(MD5.encrypt(newPwd)));
                if (isSuccess) return Result.ok();
                else return Result.fail();
            case 2:
                Student byId1 = studentService.getById(userId);
                if (!MD5.encrypt(oldPwd).equals(byId1.getPassword())) {
                    //原密码错误
                    return Result.fail().setMessage("原密码有误!");
                }
                boolean isSuccess1 = studentService.updateById(byId1.setPassword(MD5.encrypt(newPwd)));
                if (isSuccess1) return Result.ok();
                else return Result.fail();
            case 3:
                Teacher byId2 = teacherService.getById(userId);
                if (!MD5.encrypt(oldPwd).equals(byId2.getPassword())) {
                    //原密码错误
                    return Result.fail().setMessage("原密码有误!");
                }
                boolean isSuccess2 = teacherService.updateById(byId2.setPassword(MD5.encrypt(newPwd)));
                if (isSuccess2) return Result.ok();
                else return Result.fail();
        }
        return Result.build(null, ResultCodeEnum.ILLEGAL_REQUEST);
    }
}

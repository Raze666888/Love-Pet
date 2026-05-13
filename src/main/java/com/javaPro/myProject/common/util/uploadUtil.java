package com.javaPro.myProject.common.util;

import com.javaPro.myProject.common.co.Const;
import com.javaPro.myProject.common.co.filePath;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

//文件上传工具类
@Component
public class uploadUtil {
    @Resource
    private filePath ff;


    public  String upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String property = System.getProperty("os.name");
        String path ;

        if (property.contains("Windows")){
            path = ff.getPath() + Const. BASE_PATH1;
        }else{
            path = Const.SERVER_PATH2;
        }
        System.out.println(path);
        System.out.println("------路径");
        File image = new File(path, filename);
        if (!image.exists()) {
            // 创建文件夹
            image.mkdirs();
        }
        try {
            file.transferTo(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (property.contains("Windows")){
            return "\\product\\"+ filename;
        }else{
            return Const.PRO_NGINX + filename;
        }


    }
    public  String upload(MultipartFile file,String special) {//让所有的代码都可以去使用  复用
        String filename = file.getOriginalFilename();
        String property = System.getProperty("os.name");
        String path ;

        if (property.contains("Windows")){
            if (special != null && !"".equals(special)){
                path = ff.getPath() + Const. BASE_PATH1 + special + "\\";
            }else {
                path =ff.getPath()  + Const. BASE_PATH1;
            }

        }else{
            if (special != null && !"".equals(special)){
                path = Const.SERVER_PATH2 + special + "/";
            }else {
                path = Const.SERVER_PATH2;
            }

        }
        File imageEx = new File(path);
        if (!imageEx.exists()) {
            // 创建文件夹
            imageEx.mkdirs();
        }
        File image = new File(path, filename);

        try {
            file.transferTo(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (property.contains("Windows")){
            if (special != null && !"".equals(special)){
                return "\\static\\img\\" + special + "\\" + filename;
            }else {
                return "\\static\\img\\" + filename;
            }

        }else{
            if (special != null && !"".equals(special)){
                return Const.PRO_NGINX + special + "/" + filename;
            }else {
                return Const.PRO_NGINX + filename;
            }

        }

    }
    public  void downloadLocal(HttpServletResponse response,String path,String fileName) throws FileNotFoundException {

        // 读到流中
        InputStream inStream = new FileInputStream(ff.getPath() +Const.BASE_PATH1+ path.replace("product\\",""));// 文件的存放路径
        String[] split = path.split("\\.");
        // 设置输出的格式
        response.reset();
        response.setContentType("application/octet-stream");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName+"."+ split[1] +"\"");
        response.setCharacterEncoding("utf-8");
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0){
                response.getOutputStream().write(b, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}

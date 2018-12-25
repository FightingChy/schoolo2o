package com.fighting.schoolo2o.web.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/image", method = { RequestMethod.GET })
public class ImageController {
	
	@RequestMapping(value="/showimg", method = RequestMethod.GET,  produces = {"application/vnd.ms-excel;charset=UTF-8"})  
    public String getpic(String path, HttpServletResponse response) throws IOException { 
        response.setContentType("image/jpeg/jpg/png/gif/bmp/tiff/svg"); // 设置返回内容格式  
        path=new String(path.getBytes("ISO-8859-1"),"UTF-8");
        File file = new File(path);       //括号里参数为文件图片路径  
        if(file.exists()){   //如果文件存在  
            InputStream in = new FileInputStream(path);   //用该文件创建一个输入流  
            OutputStream os = response.getOutputStream();  //创建输出流  
            byte[] b = new byte[1024];    
            while( in.read(b)!= -1){   
            os.write(b);       
            }  
            in.close();   
            os.flush();  
            os.close();  
        }  
        return null;  
    }
}

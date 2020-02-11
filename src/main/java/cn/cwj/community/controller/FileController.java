package cn.cwj.community.controller;

import cn.cwj.community.dto.FileDTO;
import cn.cwj.community.provider.OssUploadImgProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Date 2020/1/17
 * @Version V1.0
 **/
@Controller
public class FileController {
    @Autowired
    private OssUploadImgProvider ossUploadImgProvider;
    @ResponseBody
    @RequestMapping(value = "/file/upload",method = RequestMethod.POST)
    public FileDTO uploadImg(HttpServletRequest request) throws IOException {
        MultipartRequest multipartRequest= (MultipartRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String url=ossUploadImgProvider.UploadFile(file.getInputStream(),file.getContentType(),file.getOriginalFilename(),"img/jie/"+System.currentTimeMillis());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setMessage("上传成功");
        fileDTO.setSuccess(1);
        fileDTO.setUrl(url);
        return fileDTO;
    }
    @ResponseBody
    @RequestMapping(value = "/user/upload",method = RequestMethod.POST)
    public FileDTO uploadUserImg(HttpServletRequest request) throws IOException {
        MultipartRequest multipartRequest= (MultipartRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        String url=ossUploadImgProvider.UploadFile(file.getInputStream(),file.getContentType(),file.getOriginalFilename(),"img/user/avatar/"+System.currentTimeMillis());
        FileDTO fileDTO = new FileDTO();
        fileDTO.setMessage("上传成功");
        fileDTO.setSuccess(1);
        fileDTO.setUrl(url);
        System.out.println(url);
        return fileDTO;
    }

}

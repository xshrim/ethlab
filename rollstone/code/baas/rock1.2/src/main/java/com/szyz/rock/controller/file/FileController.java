package com.szyz.rock.controller.file;

import com.szyz.rock.util.ConfigKeys;
import com.szyz.rock.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("**.action")
public class FileController {

    private static Log LOG = LogFactory.getLog(FileController.class);

    /**
     * 文件上传接口
     * @param file 文件
     * @return code:0成功 -1失败 path:文件地址 code为0时返回 msg：code为-1时返回
     */
    @RequestMapping("file/upload")
    public Map<String,Object> upload(MultipartFile file ){

        Map<String,Object> resultMap = new HashMap<>();

        if(file ==null){
            resultMap.put("code",-1);
            resultMap.put("msg","文件不能为空");
            return resultMap;
        }
        LOG.info("---------------");
        LOG.info("file.getOriginalFilename()");
        LOG.info("---------------");
        // String suffix = file.getOriginalFilename().split("\\.")[1];
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filename = System.currentTimeMillis()/1000 + suffix;
        String targeDir = ConfigKeys.uploadpath + filename;
        File targeFile = new File(targeDir);
        try{
            file.transferTo(targeFile);

            resultMap.put("code",0);
            resultMap.put("path",ConfigKeys.basepath + filename);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        resultMap.put("code",-1);
        resultMap.put("msg","上传失败");
        return resultMap;
    }




}

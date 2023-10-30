package cn.akfang.berry.controller;

import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/file")
@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @GetMapping("/getById")
    public FilePO getFileById(@RequestParam("fileId") Long fileId) {
        return fileService.getById(fileId);
    }
}

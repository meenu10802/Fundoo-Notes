package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public String uploadExcel(@RequestParam("file") MultipartFile file,
                              Authentication authentication) {

        String email = authentication.getName();

        return excelService.importNotesFromExcel(email, file);
    }
}
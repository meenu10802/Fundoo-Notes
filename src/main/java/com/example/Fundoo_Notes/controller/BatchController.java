package com.example.Fundoo_Notes.controller;

import com.example.Fundoo_Notes.service.ExcelNoteImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private ExcelNoteImportService excelNoteImportService;

    @PostMapping("/notes/import")
    public ResponseEntity<String> importNotes(@RequestParam("file") MultipartFile file,
                                              Authentication authentication) throws IOException {
        int count = excelNoteImportService.importNotes(authentication.getName(), file);
        return ResponseEntity.ok("Imported notes count: " + count);
    }
}

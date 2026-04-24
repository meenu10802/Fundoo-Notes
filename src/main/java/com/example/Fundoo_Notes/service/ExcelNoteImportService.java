package com.example.Fundoo_Notes.service;

import com.example.Fundoo_Notes.entity.Note;
import com.example.Fundoo_Notes.entity.User;
import com.example.Fundoo_Notes.repository.NoteRepository;
import com.example.Fundoo_Notes.repository.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ExcelNoteImportService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    public int importNotes(String email, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int importedCount = 0;
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                String title = row.getCell(0) != null ? row.getCell(0).toString() : "";
                String content = row.getCell(1) != null ? row.getCell(1).toString() : "";
                if (title.isBlank()) {
                    continue;
                }

                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.setUserId(user.getId());
                noteRepository.save(note);
                importedCount++;
            }
        }
        return importedCount;
    }
}

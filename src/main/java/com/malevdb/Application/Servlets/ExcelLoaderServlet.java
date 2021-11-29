package com.malevdb.Application.Servlets;

import com.malevdb.Application.Logging.Logger;
import com.malevdb.Utils.Excel.ExcelParser;
import com.malevdb.Utils.FileResourcesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;

@Controller
public class ExcelLoaderServlet {
    @GetMapping("/load")
    public String doGet(Model model, @ModelAttribute("error") String error) {
        if(!error.isEmpty())
            model.addAttribute("errorMessage", error);
        return "/views/excelLoader";
    }

    @PostMapping("/load/excel")
    public String doPost(Model model, @RequestParam MultipartFile file,  RedirectAttributes attributes) {
        if (file.isEmpty()) {
            Logger.log(this, "File is empty", 3);
            attributes.addAttribute("error", "File is empty");
            return "redirect:/load";
        }
        ExcelParser excelParser = new ExcelParser();
        try {
            //File tmpFile = FileResourcesUtils.transferMultipartFile(file, session.getServletContext().getRealPath("/") + "/temp/excelData.tmp");
            File tmpFile = FileResourcesUtils.transferMultipartFile(file, FileResourcesUtils.RESOURCE_PATH + "temp/excelData.tmp");
            //executor.executeUpdate(excelParser.prepareStatement("people", executor.loadSQLResource("insert_people.sql"), excelParser.read(tmpFile), 0));
            model.addAttribute("table", excelParser.getTable(excelParser.read(tmpFile), file.getOriginalFilename()));
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 3);
            attributes.addAttribute("error", e.getLocalizedMessage());
            return "redirect:/load";
        }
        return "/views/previewExcel";
    }
}

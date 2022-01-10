package com.malevdb.Application.Servlets;

import Utils.FileResourcesUtils;
import Utils.Logging.Logger;
import com.malevdb.Application.Security.AuthorizationManager;
import com.malevdb.Utils.Excel.ExcelParser;
import com.malevdb.Utils.ExtendedResourcesUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
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
    public String doPost(Model model,
                         @RequestParam MultipartFile file,
                         RedirectAttributes attributes,
                         HttpServletRequest request) {
        if (file.isEmpty()) {
            Logger.log(this, "File is empty", 3);
            ServletUtils.showPopup(attributes, "File is empty", "error");
            return "redirect:/load";
        }
        ExcelParser excelParser = new ExcelParser();
        try {
            File tmpFile = ExtendedResourcesUtils.transferMultipartFile(file, FileResourcesUtils.RESOURCE_PATH + "temp/excelData.tmp");
            model.addAttribute("table", excelParser.getTable(excelParser.read(tmpFile), file.getOriginalFilename()));
        } catch (Exception e) {
            Logger.log(this, e.getMessage(), 3);
            ServletUtils.showPopup(attributes, e.getLocalizedMessage(), "error");
            return "redirect:/load";
        }
        String tables
                = new RestTemplate().exchange(request.getRequestURL().toString().replaceAll("/load/excel", "/data/tables"),
                HttpMethod.GET, new HttpEntity<String>(AuthorizationManager.getHeaders()), String.class).getBody();
        model.addAttribute("tables", tables);
        return "/views/previewExcel";
    }
}

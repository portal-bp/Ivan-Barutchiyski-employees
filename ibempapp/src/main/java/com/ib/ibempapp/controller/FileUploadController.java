package com.ib.ibempapp.controller;

import com.ib.ibempapp.entity.Employee;
import com.ib.ibempapp.entity.EmployeeProject;
import com.ib.ibempapp.entity.Project;
import com.ib.ibempapp.service.OverlapService;
import com.ib.ibempapp.store.GlobalStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class FileUploadController {

    private final GlobalStore globalStore;
    private final OverlapService overlapService;

    public FileUploadController(GlobalStore globalStore, OverlapService overlapService) {
        this.globalStore = globalStore;
        this.overlapService = overlapService;
    }

    @PostMapping("/upload")
    public List<EmployeeProject> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        this.globalStore.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            final List<EmployeeProject> employeeProjects = reader.lines()
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(parts -> {
                        int empId = Integer.parseInt(parts[0].trim());
                        int projectId = Integer.parseInt(parts[1].trim());
                        LocalDate dateFrom = overlapService.parseDate(parts[2]);
                        LocalDate dateTo = overlapService.parseDate(parts[3]);

                        Employee employee = new Employee(empId);
                        Project project = new Project(projectId, dateFrom, dateTo);

                        return new EmployeeProject(employee, project);
                    })
                    .collect(Collectors.toList());
            this.globalStore.put("empProj", employeeProjects);
            return this.globalStore.get("empProj");
        }
    }
}
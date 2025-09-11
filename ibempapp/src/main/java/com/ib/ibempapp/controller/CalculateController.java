package com.ib.ibempapp.controller;

import com.ib.ibempapp.entity.CalcData;
import com.ib.ibempapp.entity.EmployeeProject;
import com.ib.ibempapp.service.OverlapService;
import com.ib.ibempapp.store.GlobalStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class CalculateController {

    private final GlobalStore globalStore;
    private final OverlapService overlapService;

    public CalculateController(GlobalStore globalStore, OverlapService overlapService) {
        this.globalStore = globalStore;
        this.overlapService = overlapService;
    }

    @GetMapping("/calculate")
    public List<CalcData> calculate() {
        final List<EmployeeProject> employeeProjects = this.globalStore.get("empProj");
        System.out.println(employeeProjects);
        return this.overlapService.findAllCollaborationsSorted(employeeProjects);
//        return List.of(new CalcData(List.of(1, 2, 3), 100L),
//        new CalcData(List.of(4, 5), 200L),
//        new CalcData(List.of(7, 8, 9, 10), 300L),
//        new CalcData(List.of(0), 400L));
    }
}

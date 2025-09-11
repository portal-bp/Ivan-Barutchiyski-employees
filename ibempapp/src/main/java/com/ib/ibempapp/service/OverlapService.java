package com.ib.ibempapp.service;
import com.ib.ibempapp.entity.CalcData;
import com.ib.ibempapp.entity.EmployeeProject;
import com.ib.ibempapp.store.GlobalStore;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OverlapService {

    private final GlobalStore globalStore;

    private static final DateTimeFormatter[] FORMATS = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy")
    };

    public LocalDate parseDate(String raw) {
        String s = raw.trim();
        if (s.equalsIgnoreCase("NULL") || s.isEmpty()) return LocalDate.now();
        for (DateTimeFormatter f : FORMATS) {
            try {
                return LocalDate.parse(s);
            } catch (IllegalArgumentException ignored) {}
        }
        return LocalDate.parse(s);
    }


    public OverlapService(GlobalStore globalStore) {
        this.globalStore = globalStore;
    }


    public List<CalcData> findAllCollaborationsSorted(List<EmployeeProject> records) {
        Map<String, Long> pairDurations = new HashMap<>();

        Map<Integer, List<EmployeeProject>> projects = records.stream()
                .filter(ep -> ep.project() != null && ep.project().projectId() != 0)
                .collect(Collectors.groupingBy(ep -> ep.project().projectId()));

        for (List<EmployeeProject> employees : projects.values()) {
            for (int i = 0; i < employees.size(); i++) {
                for (int j = i + 1; j < employees.size(); j++) {
                    EmployeeProject e1 = employees.get(i);
                    EmployeeProject e2 = employees.get(j);

                    LocalDate start1 = e1.project().dateFrom();
                    LocalDate end1 = e1.project().dateTo();
                    LocalDate start2 = e2.project().dateFrom();
                    LocalDate end2 = e2.project().dateTo();

                    if (start1 == null || end1 == null || start2 == null || end2 == null) continue;

                    // Normalize reversed dates
                    if (start1.isAfter(end1)) {
                        LocalDate temp = start1;
                        start1 = end1;
                        end1 = temp;
                    }
                    if (start2.isAfter(end2)) {
                        LocalDate temp = start2;
                        start2 = end2;
                        end2 = temp;
                    }

                    LocalDate overlapStart = start1.isAfter(start2) ? start1 : start2;
                    LocalDate overlapEnd = end1.isBefore(end2) ? end1 : end2;

                    if (!overlapStart.isAfter(overlapEnd)) {
                        long days = ChronoUnit.DAYS.between(overlapStart, overlapEnd);

                        int id1 = e1.employee().empId();
                        int id2 = e2.employee().empId();
                        String key = id1 < id2 ? id1 + "-" + id2 : id2 + "-" + id1;

                        pairDurations.put(key, pairDurations.getOrDefault(key, 0L) + days);
                    }
                }
            }
        }

        return pairDurations.entrySet().stream()
                .map(entry -> {
                    List<Integer> empIds = Arrays.stream(entry.getKey().split("-"))
                            .map(Integer::parseInt)
                            .sorted()
                            .toList();
                    return new CalcData(empIds, entry.getValue());
                })
                .sorted(Comparator.comparingLong(CalcData::value).reversed())
                .toList();
    }


}
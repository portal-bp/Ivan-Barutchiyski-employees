package com.ib.ibempapp.store;

import com.ib.ibempapp.entity.EmployeeProject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GlobalStore {

    private final ConcurrentHashMap<String, List<EmployeeProject>> store = new ConcurrentHashMap<>();

    public void put(String key, List<EmployeeProject> employeeProjects) {
        store.put(key, employeeProjects);
    }

    public List<EmployeeProject> get(String key) {
        return store.get(key);
    }

    public void clear() {
        store.clear();
    }
}

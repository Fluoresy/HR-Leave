package com.example.hrleave.repository;

import com.example.hrleave.model.Employee;
import com.example.hrleave.model.LeaveRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Penyimpanan in-memory dengan data contoh. */
public class LeaveRepository {

    private final List<Employee> employees = new ArrayList<>();
    private final List<LeaveRequest> requests = new ArrayList<>();

    public LeaveRepository() {
        employees.add(new Employee(1, "Andi Pratama", 12));
        employees.add(new Employee(2, "Budi Santoso", 12));
        employees.add(new Employee(3, "Citra Lestari", 12));
        employees.add(new Employee(4, "Dewi Anggraini", 12));

        requests.add(new LeaveRequest(1, 1, LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 7)));
        requests.add(new LeaveRequest(2, 1, LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 24)));
        requests.add(new LeaveRequest(3, 2, LocalDate.of(2026, 7, 22), LocalDate.of(2026, 7, 23)));
        requests.add(new LeaveRequest(4, 3, LocalDate.of(2025, 12, 29), LocalDate.of(2026, 1, 2)));
        requests.add(new LeaveRequest(5, 3, LocalDate.of(2026, 8, 10), LocalDate.of(2026, 8, 14)));
        requests.add(new LeaveRequest(6, 4, LocalDate.of(2026, 3, 16), LocalDate.of(2026, 3, 16)));
    }

    public List<Employee> findAllEmployees() {
        return List.copyOf(employees);
    }

    public Optional<Employee> findEmployeeByName(String keyword) {
        String key = keyword.trim().toLowerCase();
        return employees.stream().filter(e -> e.name().toLowerCase().contains(key)).findFirst();
    }

    public Optional<Employee> findEmployeeById(int id) {
        return employees.stream().filter(e -> e.id() == id).findFirst();
    }

    public List<LeaveRequest> findAllRequests() {
        return List.copyOf(requests);
    }

    public List<LeaveRequest> findRequestsByEmployee(int employeeId) {
        return requests.stream().filter(r -> r.employeeId() == employeeId).toList();
    }
}

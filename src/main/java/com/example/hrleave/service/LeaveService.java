package com.example.hrleave.service;

import com.example.hrleave.model.Employee;
import com.example.hrleave.model.LeaveRequest;
import com.example.hrleave.repository.LeaveRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LeaveService {

    private final LeaveRepository repository;

    public LeaveService(LeaveRepository repository) {
        this.repository = repository;
    }

    /** Task a: filter berdasarkan tahun, bulan (opsional), dan tanggal (opsional). */
    public List<LeaveRequest> findByPeriod(int year, Integer month, Integer day) {
        LocalDate from;
        LocalDate to;
        if (month == null) {
            from = LocalDate.of(year, 1, 1);
            to = LocalDate.of(year, 12, 31);
        } else if (day == null) {
            YearMonth ym = YearMonth.of(year, month);
            from = ym.atDay(1);
            to = ym.atEndOfMonth();
        } else {
            from = LocalDate.of(year, month, day);
            to = from;
        }
        return repository.findAllRequests().stream()
                .filter(r -> r.overlaps(from, to))
                .sorted(Comparator.comparing(LeaveRequest::startDate))
                .toList();
    }

    /** Task b: ringkasan nama, periode, dan durasi. */
    public String describe(LeaveRequest request) {
        String name = repository.findEmployeeById(request.employeeId())
                .map(Employee::name).orElse("(tidak dikenal)");
        return String.format("%s | %s s/d %s | %d hari",
                name, request.startDate(), request.endDate(), request.durationDays());
    }

    /** Task c: riwayat cuti pegawai. */
    public List<LeaveRequest> history(Employee employee) {
        return repository.findRequestsByEmployee(employee.id()).stream()
                .sorted(Comparator.comparing(LeaveRequest::startDate))
                .toList();
    }

    /** Task c: sisa cuti pada tahun tertentu (hanya hari yang jatuh di tahun tersebut). */
    public long remainingLeave(Employee employee, int year) {
        LocalDate from = LocalDate.of(year, 1, 1);
        LocalDate to = LocalDate.of(year, 12, 31);
        long used = 0;
        for (LeaveRequest r : history(employee)) {
            if (r.overlaps(from, to)) {
                LocalDate s = r.startDate().isBefore(from) ? from : r.startDate();
                LocalDate e = r.endDate().isAfter(to) ? to : r.endDate();
                used += e.toEpochDay() - s.toEpochDay() + 1;
            }
        }
        return Math.max(0, employee.annualQuota() - used);
    }

    public Optional<Employee> findEmployee(String keyword) {
        return repository.findEmployeeByName(keyword);
    }

    public List<Employee> allEmployees() {
        return repository.findAllEmployees();
    }
}

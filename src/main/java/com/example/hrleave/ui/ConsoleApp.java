package com.example.hrleave.ui;

import com.example.hrleave.model.Employee;
import com.example.hrleave.model.LeaveRequest;
import com.example.hrleave.repository.LeaveRepository;
import com.example.hrleave.service.LeaveService;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private final LeaveService service;
    private final Scanner in;

    public ConsoleApp(LeaveService service, Scanner in) {
        this.service = service;
        this.in = in;
    }

    public static void main(String[] args) {
        new ConsoleApp(new LeaveService(new LeaveRepository()), new Scanner(System.in)).run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Jadwal Cuti (HRD Manajer) ===");
            System.out.println("1. Lihat pengajuan cuti per periode");
            System.out.println("2. Lihat riwayat & sisa cuti pegawai");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            switch (in.nextLine().trim()) {
                case "1" -> showByPeriod();
                case "2" -> showEmployee();
                case "0" -> running = false;
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void showByPeriod() {
        try {
            int year = Integer.parseInt(ask("Tahun (mis. 2026)"));
            String m = ask("Bulan 1-12 (kosongkan = semua)");
            Integer month = m.isBlank() ? null : Integer.valueOf(m);
            Integer day = null;
            if (month != null) {
                String d = ask("Tanggal (kosongkan = semua)");
                day = d.isBlank() ? null : Integer.valueOf(d);
            }
            List<LeaveRequest> result = service.findByPeriod(year, month, day);
            if (result.isEmpty()) {
                System.out.println("Tidak ada pengajuan cuti pada periode tersebut.");
            }
            result.forEach(r -> System.out.println(" - " + service.describe(r)));
        } catch (NumberFormatException | DateTimeException e) {
            System.out.println("Input periode tidak valid: " + e.getMessage());
        }
    }

    private void showEmployee() {
        service.allEmployees().forEach(e -> System.out.println(" * " + e.name()));
        String keyword = ask("Nama pegawai");
        service.findEmployee(keyword).ifPresentOrElse(this::printEmployee,
                () -> System.out.println("Pegawai tidak ditemukan."));
    }

    private void printEmployee(Employee e) {
        System.out.println("Riwayat cuti " + e.name() + ":");
        service.history(e).forEach(r -> System.out.println(" - " + service.describe(r)));
        int year = LocalDate.now().getYear();
        System.out.println("Sisa cuti tahun " + year + ": " + service.remainingLeave(e, year) + " hari");
    }

    private String ask(String label) {
        System.out.print(label + ": ");
        return in.nextLine().trim();
    }
}

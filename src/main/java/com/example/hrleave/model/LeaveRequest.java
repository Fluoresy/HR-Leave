package com.example.hrleave.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record LeaveRequest(int id, int employeeId, LocalDate startDate, LocalDate endDate) {

    public LeaveRequest {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Tanggal selesai tidak boleh sebelum tanggal mulai");
        }
    }

    /** Durasi cuti dalam hari (inklusif). */
    public long durationDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    /** True jika cuti ini beririsan dengan rentang [from, to]. */
    public boolean overlaps(LocalDate from, LocalDate to) {
        return !startDate.isAfter(to) && !endDate.isBefore(from);
    }
}

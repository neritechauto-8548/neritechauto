package com.neritech.saas.common.util;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Classe para representar um intervalo de datas
 */
public class DateRange {

    public DateRange() {
    }

    public DateRange(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Converte o intervalo de datas para um intervalo de data e hora
     * A hora inicial Ã© 00:00:00 e a hora final Ã© 23:59:59
     *
     * @return Intervalo de data e hora
     */
    public DateTimeRange toDateTimeRange() {
        LocalDateTime start = startDate != null ? LocalDateTime.of(startDate, LocalTime.MIN) : null;
        LocalDateTime end = endDate != null ? LocalDateTime.of(endDate, LocalTime.MAX) : null;
        return new DateTimeRange(start, end);
    }

    /**
     * Verifica se o intervalo Ã© vÃ¡lido
     * Um intervalo Ã© vÃ¡lido se a data inicial for anterior ou igual Ã  data final
     *
     * @return true se o intervalo for vÃ¡lido, false caso contrÃ¡rio
     */
    public boolean isValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }

    // Getters and Setters
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Classe para representar um intervalo de data e hora
     */
    public static class DateTimeRange {

        public DateTimeRange() {
        }

        public DateTimeRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            this.startDateTime = startDateTime;
            this.endDateTime = endDateTime;
        }
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;

        // Getters and Setters
        public LocalDateTime getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(LocalDateTime startDateTime) {
            this.startDateTime = startDateTime;
        }

        public LocalDateTime getEndDateTime() {
            return endDateTime;
        }

        public void setEndDateTime(LocalDateTime endDateTime) {
            this.endDateTime = endDateTime;
        }

        /**
         * Verifica se o intervalo Ã© vÃ¡lido
         * Um intervalo Ã© vÃ¡lido se a data/hora inicial for anterior ou igual Ã  data/hora final
         *
         * @return true se o intervalo for vÃ¡lido, false caso contrÃ¡rio
         */
        public boolean isValid() {
            if (startDateTime == null || endDateTime == null) {
                return true;
            }
            return !startDateTime.isAfter(endDateTime);
        }
    }
}

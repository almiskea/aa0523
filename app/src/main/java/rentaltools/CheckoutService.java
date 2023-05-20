package rentaltools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class CheckoutService {
    private static final BigDecimal PERCENTAGE_DIVISOR = BigDecimal.valueOf(100);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final int WEEKEND_CHARGE_DAY_THRESHOLD = 5;

    private ToolRepository toolRepository;

    public CheckoutService(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100 (inclusive).");
        }

        Tool tool = toolRepository.findByToolCode(toolCode);
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeableDays(checkoutDate, dueDate, tool);
        BigDecimal preDiscountCharge = calculatePreDiscountCharge(chargeDays, tool.getDailyCharge());
        BigDecimal discountAmount = calculateDiscountAmount(preDiscountCharge, discountPercent);
        BigDecimal finalCharge = calculateFinalCharge(preDiscountCharge, discountAmount);

        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setToolCode(toolCode);
        rentalAgreement.setToolType(tool.getToolType());
        rentalAgreement.setToolBrand(tool.getBrand());
        rentalAgreement.setRentalDays(rentalDays);
        rentalAgreement.setCheckoutDate(checkoutDate);
        rentalAgreement.setDueDate(dueDate);
        rentalAgreement.setDailyRentalCharge(tool.getDailyCharge());
        rentalAgreement.setChargeDays(chargeDays);
        rentalAgreement.setPreDiscountCharge(preDiscountCharge);
        rentalAgreement.setDiscountPercent(discountPercent);
        rentalAgreement.setDiscountAmount(discountAmount);
        rentalAgreement.setFinalCharge(finalCharge);

        return rentalAgreement;
    }

    private int calculateChargeableDays(LocalDate startDate, LocalDate endDate, Tool tool) {
        int chargeableDays = 0;
        LocalDate date = startDate.plusDays(1); // Start from the day after checkout

        while (!date.isAfter(endDate)) {
            if (isChargeableDay(date, tool)) {
                chargeableDays++;
            }
            date = date.plusDays(1);
        }

        return chargeableDays;
    }

    private boolean isChargeableDay(LocalDate date, Tool tool) {
        boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean isHoliday = isIndependenceDay(date) || isLaborDay(date);

        if (isWeekend && tool.isWeekendCharge()) {
            return true;
        } else if (isHoliday && tool.isHolidayCharge()) {
            return true;
        } else if (!isWeekend && !isHoliday && tool.isWeekdayCharge()) {
            return true;
        }

        return false;
    }

    private boolean isIndependenceDay(LocalDate date) {
        int year = date.getYear();
        LocalDate independenceDay = LocalDate.of(year, 7, 4);

        if (independenceDay.getDayOfWeek() == DayOfWeek.SATURDAY) {
            independenceDay = independenceDay.minusDays(1);
        } else if (independenceDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
            independenceDay = independenceDay.plusDays(1);
        }

        return date.equals(independenceDay);
    }

    private boolean isLaborDay(LocalDate date) {
        int year = date.getYear();
        LocalDate laborDay = LocalDate.of(year, 9, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        return date.equals(laborDay);
    }

    private BigDecimal calculatePreDiscountCharge(int chargeDays, double dailyCharge) {
        return BigDecimal.valueOf(chargeDays).multiply(BigDecimal.valueOf(dailyCharge)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, int discountPercent) {
        BigDecimal discountDecimal = BigDecimal.valueOf(discountPercent).divide(PERCENTAGE_DIVISOR);
        return preDiscountCharge.multiply(discountDecimal).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFinalCharge(BigDecimal preDiscountCharge, BigDecimal discountAmount) {
        return preDiscountCharge.subtract(discountAmount);
    }
}
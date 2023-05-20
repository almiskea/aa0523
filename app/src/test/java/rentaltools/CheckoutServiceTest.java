package rentaltools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CheckoutServiceTest {
    private CheckoutService checkoutService;

    @BeforeEach
    public void setUp() {
        ToolRepository toolRepository = new ToolRepository();
        checkoutService = new CheckoutService(toolRepository);
    }

    @Test
    public void testScenario1() {
        String toolCode = "JAKR";
        int rentalDays = 5;
        int discountPercent = 101;
        LocalDate checkoutDate = LocalDate.of(2015, 9, 3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);
        });
    }

    @Test
    public void testScenario2() {
        String toolCode = "LADW";
        int rentalDays = 3;
        int discountPercent = 10;
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);
        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        Assertions.assertEquals("LADW", rentalAgreement.getToolCode());
        Assertions.assertEquals("Ladder", rentalAgreement.getToolType());
        Assertions.assertEquals("Werner", rentalAgreement.getToolBrand());
        Assertions.assertEquals(rentalDays, rentalAgreement.getRentalDays());
        Assertions.assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());
        Assertions.assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate());
        Assertions.assertEquals(1.99, rentalAgreement.getDailyRentalCharge());
        Assertions.assertEquals(2, rentalAgreement.getChargeDays());
        Assertions.assertEquals(BigDecimal.valueOf(rentalAgreement.getDailyRentalCharge()*rentalAgreement.getChargeDays()).setScale(2), rentalAgreement.getPreDiscountCharge());
        Assertions.assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
        Assertions.assertEquals(BigDecimal.valueOf(0.40).setScale(2), rentalAgreement.getDiscountAmount());
        Assertions.assertEquals(BigDecimal.valueOf(3.58), rentalAgreement.getFinalCharge());
    }

    @Test
    public void testScenario3() {
        String toolCode = "CHNS";
        int rentalDays = 5;
        int discountPercent = 25;
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);

        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        Assertions.assertEquals("CHNS", rentalAgreement.getToolCode());
        Assertions.assertEquals("Chainsaw", rentalAgreement.getToolType());
        Assertions.assertEquals("Stihl", rentalAgreement.getToolBrand());
        Assertions.assertEquals(rentalDays, rentalAgreement.getRentalDays());
        Assertions.assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());
        Assertions.assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate());
        Assertions.assertEquals(1.49, rentalAgreement.getDailyRentalCharge());
        Assertions.assertEquals(3, rentalAgreement.getChargeDays());
        Assertions.assertEquals(BigDecimal.valueOf(4.47), rentalAgreement.getPreDiscountCharge());
        Assertions.assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
        Assertions.assertEquals(BigDecimal.valueOf(1.12), rentalAgreement.getDiscountAmount());
        Assertions.assertEquals(BigDecimal.valueOf(3.35), rentalAgreement.getFinalCharge());
    }

    @Test
    public void testScenario4() {
        String toolCode = "JAKD";
        int rentalDays = 6;
        int discountPercent = 0;
        LocalDate checkoutDate = LocalDate.of(2015, 9, 3);

        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        Assertions.assertEquals("JAKD", rentalAgreement.getToolCode());
        Assertions.assertEquals("Jackhammer", rentalAgreement.getToolType());
        Assertions.assertEquals("DeWalt", rentalAgreement.getToolBrand());
        Assertions.assertEquals(rentalDays, rentalAgreement.getRentalDays());
        Assertions.assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());
        Assertions.assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate());
        Assertions.assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        Assertions.assertEquals(3, rentalAgreement.getChargeDays());
        Assertions.assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getPreDiscountCharge());
        Assertions.assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
        Assertions.assertEquals(BigDecimal.valueOf(0).setScale(2), rentalAgreement.getDiscountAmount());
        Assertions.assertEquals(BigDecimal.valueOf(8.97), rentalAgreement.getFinalCharge());
    }

    @Test
    public void testScenario5() {
        String toolCode = "JAKR";
        int rentalDays = 9;
        int discountPercent = 0;
        LocalDate checkoutDate = LocalDate.of(2015, 7, 2);

        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        Assertions.assertEquals("JAKR", rentalAgreement.getToolCode());
        Assertions.assertEquals("Jackhammer", rentalAgreement.getToolType());
        Assertions.assertEquals("Ridgid", rentalAgreement.getToolBrand());
        Assertions.assertEquals(rentalDays, rentalAgreement.getRentalDays());
        Assertions.assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());
        Assertions.assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate());
        Assertions.assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        Assertions.assertEquals(5, rentalAgreement.getChargeDays());
        Assertions.assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.getPreDiscountCharge());
        Assertions.assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
        Assertions.assertEquals(BigDecimal.valueOf(0).setScale(2), rentalAgreement.getDiscountAmount());
        Assertions.assertEquals(BigDecimal.valueOf(14.95), rentalAgreement.getFinalCharge());
    }

    @Test
    public void testScenario6() {
        String toolCode = "JAKR";
        int rentalDays = 4;
        int discountPercent = 50;
        LocalDate checkoutDate = LocalDate.of(2020, 7, 2);

        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, checkoutDate);

        Assertions.assertEquals("JAKR", rentalAgreement.getToolCode());
        Assertions.assertEquals("Jackhammer", rentalAgreement.getToolType());
        Assertions.assertEquals("Ridgid", rentalAgreement.getToolBrand());
        Assertions.assertEquals(rentalDays, rentalAgreement.getRentalDays());
        Assertions.assertEquals(checkoutDate, rentalAgreement.getCheckoutDate());
        Assertions.assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.getDueDate());
        Assertions.assertEquals(2.99, rentalAgreement.getDailyRentalCharge());
        Assertions.assertEquals(1, rentalAgreement.getChargeDays());
        Assertions.assertEquals(BigDecimal.valueOf(2.99), rentalAgreement.getPreDiscountCharge());
        Assertions.assertEquals(discountPercent, rentalAgreement.getDiscountPercent());
        Assertions.assertEquals(BigDecimal.valueOf(1.50).setScale(2), rentalAgreement.getDiscountAmount());
        Assertions.assertEquals(BigDecimal.valueOf(1.49), rentalAgreement.getFinalCharge());
    }
}

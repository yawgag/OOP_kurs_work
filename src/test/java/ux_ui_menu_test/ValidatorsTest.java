package ux_ui_menu_test;

import Exceptions.Exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import gui.Validators;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class ValidatorsTest {

    JPanel Window = new JPanel();

    @Test
    void validate_room_test() {
        Assertions.assertTrue(Validators.validate_room(new String[]{"2", "3", "1", "да"}));
        Assertions.assertFalse(Validators.validate_room(new String[]{"-1", "4", "5", "да"}));
        Assertions.assertTrue(Validators.validate_room(new String[]{"1", "12", "432", "Да"}));
    }

    @Test
    void integer_validator_test(){
        Assertions.assertThrows(InvalidInt.class, () -> {
            Validators.integer_validator("-123");
        });
        assertDoesNotThrow(() -> Validators.integer_validator("0"));
        assertDoesNotThrow(() -> Validators.integer_validator("123"));
    }

    @Test
    void empty_string_validator_test(){
        Assertions.assertThrows(EmptyDataString.class, () -> {
           Validators.empty_string_validator(" ");
        });
        Assertions.assertThrows(EmptyDataString.class, () -> {
            Validators.empty_string_validator("     ");
        });
        Assertions.assertThrows(EmptyDataString.class, () -> {
            Validators.empty_string_validator("");
        });
        assertDoesNotThrow(() -> Validators.empty_string_validator("not empty string"));
    }

    @Test
    void date_earlier_later_validator_test(){
        Assertions.assertThrows(InvalidDatesInput.class, ()-> {
           Validators.date_earlier_later_validator("2023-12-13", "2023-12-12");
        });
        Assertions.assertThrows(InvalidDatesInput.class, ()-> {
            Validators.date_earlier_later_validator("", "");
        });
        Assertions.assertThrows(InvalidDatesInput.class, ()-> {
            Validators.date_earlier_later_validator("2023-12-12", "2023-12-12");
        });
        Assertions.assertThrows(InvalidDatesInput.class, ()-> {
            Validators.date_earlier_later_validator("2023-13-1", "2023-12-12");
        });
        assertDoesNotThrow(() -> Validators.date_earlier_later_validator("2023-12-12", "2023-12-13"));
    }
}

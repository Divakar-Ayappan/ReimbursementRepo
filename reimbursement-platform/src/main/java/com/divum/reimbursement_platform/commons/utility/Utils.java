package com.divum.reimbursement_platform.commons.utility;

import java.util.Objects;

public class Utils {

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

}

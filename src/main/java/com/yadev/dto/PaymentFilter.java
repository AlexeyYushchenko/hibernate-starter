package com.yadev.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class PaymentFilter {
    String firstName;
    String lastName;

}

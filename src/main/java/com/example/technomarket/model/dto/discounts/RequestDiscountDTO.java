package com.example.technomarket.model.dto.discounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDiscountDTO {
    private int discountPercent;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startedAt;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endedAt;
    private String discountDescription;
}

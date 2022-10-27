package com.example.technomarket.model.dto.discounts;

import com.example.technomarket.model.dto.product.ProductForClientDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDiscountDTO {
    private long discountId;
    private int discountPercent;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startedAt;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endedAt;
    private String discountDescription;
}

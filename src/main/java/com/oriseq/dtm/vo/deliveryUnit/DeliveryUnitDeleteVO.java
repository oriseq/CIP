package com.oriseq.dtm.vo.deliveryUnit;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class DeliveryUnitDeleteVO {

    @NotEmpty
    private List<Long> deliveryUnitIds;

}
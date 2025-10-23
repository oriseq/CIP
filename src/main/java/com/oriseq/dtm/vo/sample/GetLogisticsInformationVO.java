package com.oriseq.dtm.vo.sample;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetLogisticsInformationVO {

    @NotNull
    private String logisticsTrackingNumber;

    private String phoneNumLastFour;


}

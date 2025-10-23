package com.oriseq.dtm.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@TableName("sample_hosting")
public class SampleHosting {
    @Id
    private Long id;

    private Long sampleId;

    private Long hostingUserId;

}
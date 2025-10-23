package com.oriseq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriseq.dtm.entity.Instrument;
import com.oriseq.mapper.InstrumentMapper;
import com.oriseq.service.InstrumentService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class InstrumentServiceImpl extends ServiceImpl<InstrumentMapper, Instrument> implements InstrumentService {

}

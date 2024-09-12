package com.program.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.PointsOrderMapper;
import com.program.model.entity.PointsOrder;
import com.program.service.PointsOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author linxf
 * @date 2024/9/12
 */
@Service
@RequiredArgsConstructor
public class PointsOrderServiceImpl extends ServiceImpl<PointsOrderMapper, PointsOrder> implements PointsOrderService {
    // 如果有复杂的业务逻辑，可以在这些方法里实现

}


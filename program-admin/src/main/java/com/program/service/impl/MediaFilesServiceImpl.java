package com.program.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.program.mapper.MediaFilesMapper;
import com.program.model.entity.MediaFiles;
import com.program.service.MediaFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author linxf
 * @date 2024/9/8
 */

@Service
@RequiredArgsConstructor
public class MediaFilesServiceImpl extends ServiceImpl<MediaFilesMapper, MediaFiles> implements MediaFilesService {

}
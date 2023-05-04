package com.kcx.api.admin.sys.service.impl;

import com.kcx.api.admin.sys.dao.SysOperLogDao;
import com.kcx.api.admin.sys.service.SysOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.SysOperLogEntity;
import org.springframework.stereotype.Service;

/**
 * 操作日志
 *
 * @author
 * @email
 * @date 2020-12-03 14:40:14
 */
@Service("sysOperLogService")
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogDao, SysOperLogEntity> implements SysOperLogService {


}

package com.kcx.service.middleware.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.common.entity.database1.SysDictDataEntity;
import com.kcx.common.entityFegin.apiUser.requestVo.ReqSysDictDataListVO;
import com.kcx.common.entityFegin.apiUser.responseVo.ResSysDictDataListVO;
import com.kcx.common.utils.page.PageUtils;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import com.kcx.service.middleware.sys.dao.SysDictDataDao;
import com.kcx.service.middleware.sys.service.SysDictDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service("sysDictDataService")
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictDataEntity> implements SysDictDataService {




    @Override
    public ResPageDataVO<ResSysDictDataListVO> list(ReqSysDictDataListVO req) {

        return PageUtils.autoPageData(()->baseMapper.listTotal(req),()->baseMapper.list(req),req);
    }
}

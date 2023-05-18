package com.kcx.api.user.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kcx.api.user.sys.dao.SysDictDataDao;
import com.kcx.api.user.sys.feign.JobFeign;
import com.kcx.api.user.sys.requestVo.ReqSysDictDataListVO;
import com.kcx.api.user.sys.responseVo.ResSysDictDataListVO;
import com.kcx.api.user.sys.service.SysDictDataService;
import com.kcx.common.entity.database1.SysDictDataEntity;
import com.kcx.common.utils.page.PageUtils;
import com.kcx.common.utils.page.responseVo.ResPageDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service("sysDictDataService")
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictDataEntity> implements SysDictDataService {

    @Resource
    private JobFeign jobFeign;


    @Override
    public ResPageDataVO<ResSysDictDataListVO> list(ReqSysDictDataListVO req) {
/*        ReqClientUserVO reqClientUserVO = new ReqClientUserVO();
        reqClientUserVO.setUserId("1");
        Result<ResClientUserInfoVO> userInfo = jobFeign.getUserInfo(reqClientUserVO);
        log.info("远程调用job结果："+userInfo);*/
        return PageUtils.autoPageData(()->baseMapper.listTotal(req),()->baseMapper.list(req),req);
    }
}

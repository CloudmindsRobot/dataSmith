package com.cloudminds.data.smith.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.dao.entity.SysUser;
import com.cloudminds.data.smith.util.Check;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author Tao.liu
 * @since 2022-08-04
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据账号查询
     *
     * @param account
     * @return
     */
    default SysUser findByAccount(final String account) {
        Check.hasText(account, "账号名不能为空");
        final LambdaQueryWrapper<SysUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SysUser::getAccount, account).ne(SysUser::getStatus, DataStatusEnum.DELETE.getValue());
        wrapper.last("limit 1");
        return this.selectOne(wrapper);
    }
}

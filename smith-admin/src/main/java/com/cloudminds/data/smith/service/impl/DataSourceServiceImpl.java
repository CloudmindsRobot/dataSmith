package com.cloudminds.data.smith.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.cloudminds.data.smith.constant.DataSourceTypeEnum;
import com.cloudminds.data.smith.constant.DataStatusEnum;
import com.cloudminds.data.smith.dao.entity.DataSource;
import com.cloudminds.data.smith.dao.mapper.DataSourceMapper;
import com.cloudminds.data.smith.dto.PageResult;
import com.cloudminds.data.smith.dto.req.DataSourceQueryReqDTO;
import com.cloudminds.data.smith.dto.req.DataSourceSaveReqDTO;
import com.cloudminds.data.smith.dto.req.DataSourceValidateReqDTO;
import com.cloudminds.data.smith.dto.resp.DataSourceItemRespDTO;
import com.cloudminds.data.smith.exception.ParameterException;
import com.cloudminds.data.smith.service.DataSourceService;
import com.cloudminds.data.smith.service.support.DataSourceCache;
import com.cloudminds.data.smith.util.Check;
import com.cloudminds.data.smith.util.Lists;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 数据源操作接口实现
 *
 * @author Tao.Liu
 * @date 2022/6/29 15:22
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    /**
     * 16位AES Key
     */
    @Value("${crypto.key:GkvSgNBpcnzXXX79}")
    private String encryptKey;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Override
    public PageResult<DataSourceItemRespDTO> findPageList(final DataSourceQueryReqDTO queryReqDTO) {
        final PageResult<DataSource> pageResult = dataSourceMapper.findPageList(queryReqDTO);
        if (pageResult == null || Lists.isEmpty(pageResult.getRecords())) {
            return PageResult.empty();
        }
        return pageResult.convert(e -> {
            final DataSourceItemRespDTO itemRespDTO = BeanUtil.copyProperties(e, DataSourceItemRespDTO.class);
            if (Strings.isNotBlank(e.getPassword()) && Strings.isNotBlank(e.getSalt())) {
                // 密码解密返回
                itemRespDTO.setPassword(this.cryptoText(e.getPassword(), e.getSalt(), false));
            }
            return itemRespDTO;
        });
    }

    @Override
    public void validateDataSource(final DataSourceValidateReqDTO validateReqDTO) {
        DataSource dataSourceInfo = BeanUtil.copyProperties(validateReqDTO, DataSource.class);
        DataSourceCache.validateDataSource(dataSourceInfo);
    }

    @Override
    public Integer saveDataSource(final DataSourceSaveReqDTO saveReqDTO) {
        final DataSource saveDataSource = BeanUtil.copyProperties(saveReqDTO, DataSource.class);
        saveDataSource.setUpdateTime(System.currentTimeMillis());
        if (Strings.isNotBlank(saveDataSource.getPassword())) {
            // 密码明文加密
            final String salt = RandomUtil.randomString(16);
            final String encryptPwd = this.cryptoText(saveDataSource.getPassword(), salt, true);
            saveDataSource.setPassword(encryptPwd);
            saveDataSource.setSalt(salt);
        }
        if (saveDataSource.getId() == null) {
            saveDataSource.setStatus(DataStatusEnum.NORMAL.getValue());
            saveDataSource.setCreateTime(System.currentTimeMillis());
            dataSourceMapper.insert(saveDataSource);
        } else {
            // 更新
            dataSourceMapper.updateById(saveDataSource);
        }
        final Integer dataSourceId = saveDataSource.getId();
        DataSourceCache.removeDataSource(dataSourceId);

        return dataSourceId;
    }

    @Override
    public DataSource getById(final Integer sourceId) {
        final DataSource dataSource = dataSourceMapper.selectById(sourceId);
        if (dataSource == null || Objects.equals(dataSource.getStatus(), DataStatusEnum.DELETE.getValue())) {
            return null;
        }
        return dataSource;
    }

    @Override
    public javax.sql.DataSource getSqlDataSource(final Integer sourceId) {
        final javax.sql.DataSource dataSource = DataSourceCache.getDataSource(sourceId);
        if (dataSource != null) {
            return dataSource;
        }
        final DataSource dataSourceInfo = this.getById(sourceId);
        Check.notNull(dataSourceInfo, "数据源不存在");
        if (Objects.equals(DataSourceTypeEnum.HTTP.getValue(), dataSourceInfo.getType())) {
            throw new ParameterException("暂不支持HTTP数据源");
        }
        // 密码解密
        if (Strings.isNotBlank(dataSourceInfo.getPassword())) {
            final String originPassword = this.cryptoText(dataSourceInfo.getPassword(), dataSourceInfo.getSalt(), false);
            dataSourceInfo.setPassword(originPassword);
        }
        return DataSourceCache.createDataSource(dataSourceInfo);
    }

    @Override
    public void deleteById(final Integer id) {
        Check.notNull(id, "ID不能为空");
        final DataSource updateDataSource = new DataSource();
        updateDataSource.setId(id);
        updateDataSource.setStatus(DataStatusEnum.DELETE.getValue());
        final int updateNum = dataSourceMapper.updateById(updateDataSource);
        Check.isTrue(updateNum > 0, "记录不存在");

        DataSourceCache.removeDataSource(id);
    }

    /**
     * 文本加解密
     *
     * @param text
     * @param salt
     * @param isEncrypt
     * @return
     */
    private String cryptoText(final String text, final String salt, final boolean isEncrypt) {
        final AES aes = new AES(Mode.PCBC, Padding.PKCS5Padding, encryptKey.getBytes(), salt.getBytes());
        if (isEncrypt) {
            return aes.encryptHex(text);
        }
        return aes.decryptStr(text);
    }

}

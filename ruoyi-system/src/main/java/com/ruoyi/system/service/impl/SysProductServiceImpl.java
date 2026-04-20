package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysProductMapper;
import com.ruoyi.system.service.ISysProductService;
import com.ruoyi.common.core.domain.entity.SysProduct;

/**
 * 商品Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysProductServiceImpl implements ISysProductService
{
    @Autowired
    private SysProductMapper productMapper;

    /**
     * 查询商品列表
     */
    @Override
    public List<SysProduct> selectProductList(SysProduct product)
    {
        return productMapper.selectProductList(product);
    }

    /**
     * 根据ID查询商品信息
     */
    @Override
    public SysProduct selectProductById(Long productId)
    {
        return productMapper.selectProductById(productId);
    }

    /**
     * 新增商品
     */
    @Override
    @Transactional
    public int insertProduct(SysProduct product)
    {
        product.setCreateBy(SecurityUtils.getUsername());
        return productMapper.insertProduct(product);
    }

    /**
     * 修改商品
     */
    @Override
    @Transactional
    public int updateProduct(SysProduct product)
    {
        product.setUpdateBy(SecurityUtils.getUsername());
        return productMapper.updateProduct(product);
    }

    /**
     * 删除商品（逻辑删除）
     */
    @Override
    @Transactional
    public int deleteProductById(Long productId)
    {
        return productMapper.deleteProductById(productId);
    }

    /**
     * 批量删除商品（逻辑删除）
     */
    @Override
    @Transactional
    public int deleteProductByIds(Long[] productIds)
    {
        for (Long productId : productIds)
        {
            productMapper.deleteProductById(productId);
        }
        return productIds.length;
    }

    /**
     * 校验商品编码是否唯一
     */
    @Override
    public String checkProductCodeUnique(SysProduct product)
    {
        Long productId = StringUtils.isNull(product.getProductId()) ? -1L : product.getProductId();
        SysProduct info = productMapper.checkProductCodeUnique(product.getProductCode());
        if (StringUtils.isNotNull(info) && info.getProductId().longValue() != productId.longValue())
        {
            return "1";
        }
        return "0";
    }

    /**
     * 修改商品状态
     */
    @Override
    @Transactional
    public int updateProductStatus(SysProduct product)
    {
        product.setUpdateBy(SecurityUtils.getUsername());
        return productMapper.updateProductStatus(product.getProductId(), product.getStatus());
    }

    /**
     * 查询各分类商品库存统计
     */
    @Override
    public List<Map<String, Object>> selectInventoryStatsByCategory()
    {
        return productMapper.selectInventoryStatsByCategory();
    }
}

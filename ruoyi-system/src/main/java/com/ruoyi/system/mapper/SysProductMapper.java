package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysProduct;
import org.apache.ibatis.annotations.Param;

/**
 * 商品Mapper接口
 *
 * @author ruoyi
 */
public interface SysProductMapper
{
    /**
     * 查询商品列表
     *
     * @param product 商品信息
     * @return 商品集合
     */
    public List<SysProduct> selectProductList(SysProduct product);

    /**
     * 根据ID查询商品信息
     *
     * @param productId 商品ID
     * @return 商品信息
     */
    public SysProduct selectProductById(Long productId);

    /**
     * 校验商品编码是否唯一
     *
     * @param productCode 商品编码
     * @return 结果
     */
    public SysProduct checkProductCodeUnique(@Param("productCode") String productCode);

    /**
     * 新增商品
     *
     * @param product 商品信息
     * @return 结果
     */
    public int insertProduct(SysProduct product);

    /**
     * 修改商品
     *
     * @param product 商品信息
     * @return 结果
     */
    public int updateProduct(SysProduct product);

    /**
     * 删除商品（逻辑删除）
     *
     * @param productId 商品ID
     * @return 结果
     */
    public int deleteProductById(Long productId);

    /**
     * 批量删除商品（逻辑删除）
     *
     * @param productIds 需要删除的商品ID
     * @return 结果
     */
    public int deleteProductByIds(Long[] productIds);
}

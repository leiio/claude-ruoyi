package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.common.core.domain.entity.SysProductCategory;
import org.apache.ibatis.annotations.Param;

/**
 * 商品分类Mapper接口
 *
 * @author ruoyi
 */
public interface SysProductCategoryMapper
{
    /**
     * 查询商品分类列表
     *
     * @param category 商品分类
     * @return 商品分类集合
     */
    public List<SysProductCategory> selectCategoryList(SysProductCategory category);

    /**
     * 查询所有商品分类
     *
     * @return 商品分类列表
     */
    public List<SysProductCategory> selectCategoryAll();

    /**
     * 查询所有分类（树形）
     *
     * @return 所有分类树
     */
    public List<SysProductCategory> selectCategoryTreeList();

    /**
     * 根据ID查询分类信息
     *
     * @param categoryId 分类ID
     * @return 分类信息
     */
    public SysProductCategory selectCategoryById(Long categoryId);

    /**
     * 校验分类名称是否唯一
     *
     * @param categoryName 分类名称
     * @param parentId 父分类ID
     * @return 结果
     */
    public SysProductCategory checkCategoryNameUnique(@Param("categoryName") String categoryName, @Param("parentId") Long parentId);

    /**
     * 查询分类是否存在子分类
     *
     * @param categoryId 分类ID
     * @return 结果
     */
    public int checkExistChildCategory(Long categoryId);

    /**
     * 查询分类是否存在商品
     *
     * @param categoryId 分类ID
     * @return 结果
     */
    public int checkExistProduct(Long categoryId);

    /**
     * 新增商品分类
     *
     * @param category 商品分类
     * @return 结果
     */
    public int insertCategory(SysProductCategory category);

    /**
     * 修改商品分类
     *
     * @param category 商品分类
     * @return 结果
     */
    public int updateCategory(SysProductCategory category);

    /**
     * 删除商品分类
     *
     * @param categoryId 分类ID
     * @return 结果
     */
    public int deleteCategoryById(Long categoryId);
}

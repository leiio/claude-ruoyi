package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysProductCategory;

/**
 * 商品分类Service接口
 *
 * @author ruoyi
 */
public interface ISysProductCategoryService
{
    /**
     * 查询商品分类列表
     *
     * @param category 商品分类
     * @return 商品分类集合
     */
    public List<SysProductCategory> selectCategoryList(SysProductCategory category);

    /**
     * 查询所有分类（树形）
     *
     * @return 分类树列表
     */
    public List<SysProductCategory> selectCategoryTreeList();

    /**
     * 构建前端所需要下拉树结构
     *
     * @param categories 分类列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildCategoryTreeSelect(List<SysProductCategory> categories);

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
     * @param category 分类信息
     * @return 结果
     */
    public String checkCategoryNameUnique(SysProductCategory category);

    /**
     * 新增商品分类
     *
     * @param category 分类信息
     * @return 结果
     */
    public int insertCategory(SysProductCategory category);

    /**
     * 修改商品分类
     *
     * @param category 分类信息
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

    /**
     * 判断分类下是否有子分类
     *
     * @param categoryId 分类ID
     * @return 结果
     */
    public boolean hasChildByCategoryId(Long categoryId);

    /**
     * 判断分类下是否存在商品
     *
     * @param categoryId 分类ID
     * @return 结果
     */
    public boolean checkExistProduct(Long categoryId);
}

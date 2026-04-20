package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysProductCategoryMapper;
import com.ruoyi.system.service.ISysProductCategoryService;
import com.ruoyi.common.core.domain.entity.SysProductCategory;

/**
 * 商品分类Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysProductCategoryServiceImpl implements ISysProductCategoryService
{
    @Autowired
    private SysProductCategoryMapper categoryMapper;

    /**
     * 查询商品分类列表
     */
    @Override
    public List<SysProductCategory> selectCategoryList(SysProductCategory category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    /**
     * 查询所有分类（树形）
     */
    @Override
    public List<SysProductCategory> selectCategoryTreeList()
    {
        return categoryMapper.selectCategoryTreeList();
    }

    /**
     * 构建前端所需要下拉树结构
     */
    @Override
    public List<TreeSelect> buildCategoryTreeSelect(List<SysProductCategory> categories)
    {
        List<SysProductCategory> categoryTrees = buildCategoryTree(categories);
        return categoryTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     */
    public List<SysProductCategory> buildCategoryTree(List<SysProductCategory> categories)
    {
        List<SysProductCategory> returnList = new ArrayList<>();
        List<Long> tempList = categories.stream().map(SysProductCategory::getCategoryId).collect(Collectors.toList());
        for (SysProductCategory category : categories)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(category.getParentId()))
            {
                recursionFn(categories, category);
                returnList.add(category);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = categories;
        }
        return returnList;
    }

    /**
     * 递归运算
     */
    private void recursionFn(List<SysProductCategory> list, SysProductCategory t)
    {
        // 得到子节点列表
        List<SysProductCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysProductCategory tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysProductCategory> getChildList(List<SysProductCategory> list, SysProductCategory t)
    {
        List<SysProductCategory> tlist = new ArrayList<>();
        Iterator<SysProductCategory> it = list.iterator();
        while (it.hasNext())
        {
            SysProductCategory n = it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getCategoryId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysProductCategory> list, SysProductCategory t)
    {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 根据ID查询分类信息
     */
    @Override
    public SysProductCategory selectCategoryById(Long categoryId)
    {
        return categoryMapper.selectCategoryById(categoryId);
    }

    /**
     * 校验分类名称是否唯一
     */
    @Override
    public String checkCategoryNameUnique(SysProductCategory category)
    {
        Long categoryId = StringUtils.isNull(category.getCategoryId()) ? -1L : category.getCategoryId();
        SysProductCategory info = categoryMapper.checkCategoryNameUnique(category.getCategoryName(), category.getParentId());
        if (StringUtils.isNotNull(info) && info.getCategoryId().longValue() != categoryId.longValue())
        {
            return "1";
        }
        return "0";
    }

    /**
     * 新增商品分类
     */
    @Override
    public int insertCategory(SysProductCategory category)
    {
        SysProductCategory parent = categoryMapper.selectCategoryById(category.getParentId());
        if (StringUtils.isNotNull(parent))
        {
            String ancestors = parent.getAncestors() + "," + parent.getCategoryId();
            category.setAncestors(ancestors);
        }
        category.setCreateBy(SecurityUtils.getUsername());
        return categoryMapper.insertCategory(category);
    }

    /**
     * 修改商品分类
     */
    @Override
    public int updateCategory(SysProductCategory category)
    {
        SysProductCategory parent = categoryMapper.selectCategoryById(category.getParentId());
        if (StringUtils.isNotNull(parent))
        {
            String ancestors = parent.getAncestors() + "," + parent.getCategoryId();
            category.setAncestors(ancestors);
        }
        category.setUpdateBy(SecurityUtils.getUsername());
        return categoryMapper.updateCategory(category);
    }

    /**
     * 删除商品分类
     */
    @Override
    public int deleteCategoryById(Long categoryId)
    {
        if (hasChildByCategoryId(categoryId))
        {
            throw new ServiceException("存在下级分类，不允许删除");
        }
        if (checkExistProduct(categoryId))
        {
            throw new ServiceException("分类下存在商品，不允许删除");
        }
        return categoryMapper.deleteCategoryById(categoryId);
    }

    /**
     * 判断分类下是否有子分类
     */
    @Override
    public boolean hasChildByCategoryId(Long categoryId)
    {
        int result = categoryMapper.checkExistChildCategory(categoryId);
        return result > 0;
    }

    /**
     * 判断分类下是否存在商品
     */
    @Override
    public boolean checkExistProduct(Long categoryId)
    {
        int result = categoryMapper.checkExistProduct(categoryId);
        return result > 0;
    }
}

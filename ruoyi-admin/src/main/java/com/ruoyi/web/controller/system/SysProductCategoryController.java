package com.ruoyi.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysProductCategory;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysProductCategoryService;

/**
 * 商品分类管理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/category")
public class SysProductCategoryController extends BaseController
{
    @Autowired
    private ISysProductCategoryService categoryService;

    /**
     * 获取分类列表
     */
    @PreAuthorize("@ss.hasPermi('product:category:list')")
    @GetMapping("/list")
    public AjaxResult list(SysProductCategory category)
    {
        List<SysProductCategory> list = categoryService.selectCategoryList(category);
        return success(list);
    }

    /**
     * 获取分类树结构
     */
    @PreAuthorize("@ss.hasPermi('product:category:list')")
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysProductCategory category)
    {
        List<SysProductCategory> categories = categoryService.selectCategoryList(category);
        return success(categoryService.buildCategoryTreeSelect(categories));
    }

    /**
     * 导出分类列表
     */
    @PreAuthorize("@ss.hasPermi('product:category:export')")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysProductCategory category)
    {
        List<SysProductCategory> list = categoryService.selectCategoryList(category);
        ExcelUtil<SysProductCategory> util = new ExcelUtil<>(SysProductCategory.class);
        util.exportExcel(response, list, "分类数据");
    }

    /**
     * 根据分类ID获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:category:query')")
    @GetMapping("/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId)
    {
        return success(categoryService.selectCategoryById(categoryId));
    }

    /**
     * 新增分类
     */
    @PreAuthorize("@ss.hasPermi('product:category:add')")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysProductCategory category)
    {
        if ("1".equals(categoryService.checkCategoryNameUnique(category)))
        {
            return error("新增分类'" + category.getCategoryName() + "'失败，分类名称已存在");
        }
        return toAjax(categoryService.insertCategory(category));
    }

    /**
     * 修改分类
     */
    @PreAuthorize("@ss.hasPermi('product:category:edit')")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysProductCategory category)
    {
        if ("1".equals(categoryService.checkCategoryNameUnique(category)))
        {
            return error("修改分类'" + category.getCategoryName() + "'失败，分类名称已存在");
        }
        return toAjax(categoryService.updateCategory(category));
    }

    /**
     * 删除分类
     */
    @PreAuthorize("@ss.hasPermi('product:category:remove')")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryId}")
    public AjaxResult remove(@PathVariable Long categoryId)
    {
        return toAjax(categoryService.deleteCategoryById(categoryId));
    }
}

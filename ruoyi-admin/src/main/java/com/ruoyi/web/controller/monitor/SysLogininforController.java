// 包名，声明该类所属的包
package com.ruoyi.web.controller.monitor;

// 引入Java集合框架中的List接口，用于存储登录日志列表
import java.util.List;

// 引入HTTP响应对象，用于导出Excel时写入响应流
import jakarta.servlet.http.HttpServletResponse;

// 引入Spring的自动装配注解，用于注入依赖
import org.springframework.beans.factory.annotation.Autowired;

// 引入Spring Security的权限预授权注解
import org.springframework.security.access.prepost.PreAuthorize;

// 引入RESTful风格的HTTP方法映射注解
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 引入自定义日志注解，用于记录操作日志
import com.ruoyi.common.annotation.Log;

// 引入基础控制器类，提供通用方法
import com.ruoyi.common.core.controller.BaseController;

// 引入统一响应结果封装类
import com.ruoyi.common.core.domain.AjaxResult;

// 引入分页数据封装类
import com.ruoyi.common.core.page.TableDataInfo;

// 引入业务类型枚举
import com.ruoyi.common.enums.BusinessType;

// 引入Excel工具类，用于导出Excel文件
import com.ruoyi.common.utils.poi.ExcelUtil;

// 引入密码服务类，用于清除登录记录缓存
import com.ruoyi.framework.web.service.SysPasswordService;

// 引入登录日志实体类
import com.ruoyi.system.domain.SysLogininfor;

// 引入登录日志服务接口
import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问记录控制器
 * 提供登录日志的查询、导出、删除、清空和解锁功能
 *
 * @author ruoyi
 */
@RestController
// 请求映射，指定该控制器的基础URL路径
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController
{
    // 自动装配登录日志服务实例
    @Autowired
    private ISysLogininforService logininforService;

    // 自动装配密码服务实例，用于账户解锁
    @Autowired
    private SysPasswordService passwordService;

    // 权限预授权校验，需拥有monitor:logininfor:list权限
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    // HTTP GET请求映射
    @GetMapping("/list")
    // 查询登录日志列表，支持分页
    public TableDataInfo list(SysLogininfor logininfor)
    {
        // 启动分页功能
        startPage();
        // 调用服务查询登录日志列表
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        // 返回分页数据表格
        return getDataTable(list);
    }

    // 记录操作日志，标题为"登录日志"，操作类型为导出
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    // 权限预授权校验，需拥有monitor:logininfor:export权限
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    // HTTP POST请求映射
    @PostMapping("/export")
    // 导出登录日志到Excel文件
    public void export(HttpServletResponse response, SysLogininfor logininfor)
    {
        // 调用服务查询登录日志列表
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        // 创建Excel工具类实例，指定数据对应的实体类
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        // 导出Excel文件，文件名为"登录日志"
        util.exportExcel(response, list, "登录日志");
    }

    // 权限预授权校验，需拥有monitor:logininfor:remove权限
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    // 记录操作日志，标题为"登录日志"，操作类型为删除
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    // HTTP DELETE请求映射，路径参数为日志ID数组
    @DeleteMapping("/{infoIds}")
    // 批量删除登录日志
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        // 调用服务批量删除登录日志并返回结果
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    // 权限预授权校验，需拥有monitor:logininfor:remove权限
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    // 记录操作日志，标题为"登录日志"，操作类型为清空
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    // HTTP DELETE请求映射，清空所有登录日志
    @DeleteMapping("/clean")
    // 清空所有登录日志
    public AjaxResult clean()
    {
        // 调用服务清空登录日志
        logininforService.cleanLogininfor();
        // 返回成功结果
        return success();
    }

    // 权限预授权校验，需拥有monitor:logininfor:unlock权限
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:unlock')")
    // 记录操作日志，标题为"账户解锁"，操作类型为其他
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    // HTTP GET请求映射，路径参数为用户名
    @GetMapping("/unlock/{userName}")
    // 解锁指定用户的账户
    public AjaxResult unlock(@PathVariable("userName") String userName)
    {
        // 清除该用户的登录记录缓存
        passwordService.clearLoginRecordCache(userName);
        // 返回成功结果
        return success();
    }
}

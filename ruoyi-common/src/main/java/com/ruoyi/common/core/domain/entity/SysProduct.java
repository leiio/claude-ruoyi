package com.ruoyi.common.core.domain.entity;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 商品表 sys_product
 *
 * @author ruoyi
 */
public class SysProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 商品ID */
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 商品编码 */
    private String productCode;

    /** 商品分类ID */
    private Long categoryId;

    /** 商品价格 */
    private BigDecimal price;

    /** 成本价 */
    private BigDecimal costPrice;

    /** 库存数量 */
    private Integer stock;

    /** 单位 */
    private String productUnit;

    /** 重量(kg) */
    private BigDecimal productWeight;

    /** 商品描述 */
    private String productDesc;

    /** 商品图片 */
    private String productImg;

    /** 商品状态（0正常 1下架） */
    private String status;

    /** 是否促销（0否 1是） */
    private String promote;

    /** 促销价格 */
    private BigDecimal promotePrice;

    /** 删除标志（0存在 1删除） */
    private String delFlag;

    /** 分类名称 */
    private String categoryName;

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    @NotBlank(message = "商品名称不能为空")
    @Size(min = 0, max = 100, message = "商品名称长度不能超过100个字符")
    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    @Size(min = 0, max = 50, message = "商品编码长度不能超过50个字符")
    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    @NotNull(message = "商品分类不能为空")
    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    @NotNull(message = "商品价格不能为空")
    @DecimalMin(value = "0.00", message = "商品价格不能为负数")
    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getCostPrice()
    {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice)
    {
        this.costPrice = costPrice;
    }

    public Integer getStock()
    {
        return stock;
    }

    public void setStock(Integer stock)
    {
        this.stock = stock;
    }

    @Size(min = 0, max = 20, message = "单位长度不能超过20个字符")
    public String getProductUnit()
    {
        return productUnit;
    }

    public void setProductUnit(String productUnit)
    {
        this.productUnit = productUnit;
    }

    public BigDecimal getProductWeight()
    {
        return productWeight;
    }

    public void setProductWeight(BigDecimal productWeight)
    {
        this.productWeight = productWeight;
    }

    @Size(min = 0, max = 500, message = "商品描述长度不能超过500个字符")
    public String getProductDesc()
    {
        return productDesc;
    }

    public void setProductDesc(String productDesc)
    {
        this.productDesc = productDesc;
    }

    public String getProductImg()
    {
        return productImg;
    }

    public void setProductImg(String productImg)
    {
        this.productImg = productImg;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPromote()
    {
        return promote;
    }

    public void setPromote(String promote)
    {
        this.promote = promote;
    }

    public BigDecimal getPromotePrice()
    {
        return promotePrice;
    }

    public void setPromotePrice(BigDecimal promotePrice)
    {
        this.promotePrice = promotePrice;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("productCode", getProductCode())
            .append("categoryId", getCategoryId())
            .append("price", getPrice())
            .append("costPrice", getCostPrice())
            .append("stock", getStock())
            .append("productUnit", getProductUnit())
            .append("productWeight", getProductWeight())
            .append("productDesc", getProductDesc())
            .append("productImg", getProductImg())
            .append("status", getStatus())
            .append("promote", getPromote())
            .append("promotePrice", getPromotePrice())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

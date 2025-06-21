package com.example.membersystem.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分頁返回結果
 */
@Data
@Accessors(chain = true)
@Schema(description = "分頁返回結果")
public class PageResult<T> {

    @Schema(description = "資料列表")
    private List<T> records;

    @Schema(description = "總記錄數", example = "100")
    private Long total;

    @Schema(description = "每頁大小", example = "10")
    private Long size;

    @Schema(description = "當前頁數", example = "1")
    private Long current;

    @Schema(description = "總頁數", example = "10")
    private Long pages;

    @Schema(description = "是否有上一頁", example = "false")
    private Boolean hasPrevious;

    @Schema(description = "是否有下一頁", example = "true")
    private Boolean hasNext;

    /**
     * 從 MyBatis Plus 的 IPage 轉換
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<T>()
                .setRecords(page.getRecords())
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent())
                .setPages(page.getPages())
                .setHasPrevious(page.getCurrent() > 1)  // 手動計算是否有上一頁
                .setHasNext(page.getCurrent() < page.getPages());  // 手動計算是否有下一頁
    }

    /**
     * 建立空的分頁結果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<T>()
                .setRecords(List.of())
                .setTotal(0L)
                .setSize(10L)
                .setCurrent(1L)
                .setPages(0L)
                .setHasPrevious(false)
                .setHasNext(false);
    }
}
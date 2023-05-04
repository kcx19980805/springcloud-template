package com.kcx.common.utils.file;

import lombok.Data;

/**
 * 文件上传下载基类，子类继承使用
 * @Component
 * @ConfigurationProperties(prefix = "file")注入
 */
@Data
public class FileBaseConfig {
    /**
     * 文件上传路径
     */
    public String uploadPath;

    /**
     * 文件下载路径
     */
    public String downloadPath;

    /**
     * 单个文件上传大小,单位byte
     */
    public long uploadMaxSize;
}

package com.kcx.job.config.file;

import com.kcx.common.utils.file.FileBaseConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取文件上传/下载相关配置
 */
@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileConfig extends FileBaseConfig {

}

package com.scdy.ju_fang_resolving.utils.commonUtil;

import lombok.SneakyThrows;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Properties;

public class CommonUtil {

    @SneakyThrows
    public static File getFile() {
        //获取根目录
        File parent = new File(ResourceUtils.getURL("classpath:")
                .getPath() + File.separator + "temporary");
        if (!parent.exists()) {
            parent.mkdirs();
        }

        File file = new File(parent.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    @SneakyThrows
    public static String getLocalURL() {
        StringBuffer strBuffer = new StringBuffer();
        Properties properties = new Properties();
        properties.load(CommonUtil.class.getClassLoader().getResourceAsStream("./prop/server_prop/server_prop.properties"));
        String port = properties.getProperty("port");
        strBuffer.append("http://localhost:");
        strBuffer.append(port);
        return strBuffer.toString();
    }

    public static boolean isNotNull(Object o) {
        if (null == o) {
            return false;
        }
        if ("".equals(o)) {
            return false;
        }
        return true;
    }
}

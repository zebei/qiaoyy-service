package com.qiaoyy.qcloud;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.log.AppLog;
import org.json.JSONException;

import com.qcloud.weapp.ConfigurationManager;
import com.qcloud.weapp.ConfigurationException;

public class QCloud {

    public static void setupSDK() {
        try {
            String configFilePath = getConfigFilePath();
            AppLog.LOG_COMMON.info("QCloud SDK file path：" + configFilePath);

            ConfigurationManager.setupFromFile(configFilePath);
            AppLog.LOG_COMMON.info("QCloud SDK config success！");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static String getConfigFilePath() {
        String defaultConfigFilePath = AppInit.run.getEnvironment().getProperty("app.sdk.path");
        return defaultConfigFilePath;
    }

}

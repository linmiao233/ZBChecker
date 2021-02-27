package lau.ZBChecker.threads;

import lau.ZBChecker.Main;
import lau.ZBChecker.Windows;
import lau.ZBChecker.check.Get;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Arrays;

public class RefreshApiProxyThread extends Thread {
    public static void loadProxyFromApi() {
        Log log = LogFactory.getLog("RefreshApiProxyThread");
        try {
            Main.proxyList = Arrays.asList(Get.get(Main.config.proxyApi, null, null).trim().replace("\r\n", "\n").replace("\r", "\n").split("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Fail to load proxies from API. Please restart.");
            System.exit(0);
        }
        log.info("Successfully loaded " + Main.proxyList.size() + " proxies.\n");
    }

    @Override
    public void run() {
        if (Main.config.refreshDelay == -1) return;
        while (true) {
            try {
                sleep(Main.config.refreshDelay * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Main.log.fatal("Fatally error : Error when thread sleeps.");
                System.exit(0);
            }
            loadProxyFromApi();
            Windows.refreshTitle();
        }
    }
}

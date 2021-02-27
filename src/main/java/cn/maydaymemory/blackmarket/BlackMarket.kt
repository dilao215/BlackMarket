package cn.maydaymemory.blackmarket

import cn.maydaymemory.blackmarket.listener.BlackMarketAreaLogoutListener
import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.logger.TLoggerManager
import org.bukkit.Bukkit
import java.time.Duration
import java.time.temporal.ChronoUnit

object BlackMarket : Plugin() {

    /** 控制台logger */
    val logger = TLoggerManager.getLogger(this.plugin)

    @TInject(value = ["config.yml"], locale = "LOCALE-PRIORITY")
    lateinit var config : TConfig
        private set

    override fun onEnable() {
        if(Bukkit.getPluginManager().getPlugin("QuickShop") == null) {
            logger.warn("找不到QuickShop插件，请安装QuickShop插件后重启服务器")
            Bukkit.getPluginManager().disablePlugin(this.plugin)
            return
        }
        if(Bukkit.getPluginManager().getPlugin("AreaShop") == null) {
            logger.warn("找不到AreaShop插件，请安装AreaShop插件后重启服务器")
            Bukkit.getPluginManager().disablePlugin(this.plugin)
            return
        }
        Bukkit.getPluginManager().registerEvents(BlackMarketAreaLogoutListener,BlackMarket.plugin)
    }

    override fun onDisable() {
        Duration.of(11, ChronoUnit.DAYS)
    }
}

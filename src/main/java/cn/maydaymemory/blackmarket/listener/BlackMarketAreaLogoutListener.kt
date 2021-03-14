package cn.maydaymemory.blackmarket.listener

import cn.maydaymemory.blackmarket.BlackMarket
import com.sk89q.worldedit.Vector
import com.tripleying.qwq.MailBox.API.MailBoxAPI
import com.tripleying.qwq.MailBox.Mail.PlayerFileMail
import com.tripleying.qwq.MailBox.Original.MailNew
import me.wiefferink.areashop.events.ask.UnrentingRegionEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.maxgamer.quickshop.QuickShop
import org.maxgamer.quickshop.Shop.Shop
import java.util.*
import kotlin.collections.ArrayList

object BlackMarketAreaLogoutListener : Listener{
    @EventHandler
    fun onAreaLogout(event : UnrentingRegionEvent){
        //获取出租区域内的商店，并将需要删除的商店放进del
        val r = event.region.region
        val world = event.region.world
        val shopIterator = QuickShop.instance.shopManager.shopIterator
        val del = ArrayList<Shop>()
        val renter = Bukkit.getOfflinePlayer(event.region.renter).name
        for(shop in shopIterator){
            val loc = shop.location
            if(world.name != loc.world?.name ?:continue) continue
            val vector = Vector(loc.x,loc.y,loc.z)
            if(r.contains(vector)) del.add(shop)
        }

        //把商店中剩余的物品放到items中
        val items = ArrayList<ItemStack>()
        for(shop in del){
            val item = shop.item.clone();
            item.amount = shop.remainingStock
            items.add(item)
            shop.delete()
        }

        //创建邮件，把剩余物品放入附件，发送邮件
        if(Bukkit.getPluginManager().getPlugin("MailBox") == null) {
            BlackMarket.logger.warn("找不到MailBox插件，商店剩余物品无法发还。")
            return
        }
        val mail: PlayerFileMail? = MailBoxAPI.createBaseFileMail("player","管理员",
                        "§e§l 摊位租赁结束，货物返还。",
                        "为了确保安全，请尽量在租赁结束前清空商店，以免物品丢失。",
                        Date().toString()) as PlayerFileMail?
        val list = ArrayList<String>()
        renter?.let { list.add(it) }
        mail?.recipient = list
        mail?.itemList = items
        MailNew.New(Bukkit.getConsoleSender(),mail)

        //发送邮件需要控制台输入0
        object : BukkitRunnable(){
            override fun run() {
                Bukkit.getConsoleSender().acceptConversationInput("0")
            }
        }.runTaskLater(BlackMarket.plugin,20)

    }
}
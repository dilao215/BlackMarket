package cn.maydaymemory.blackmarket.listener

import com.sk89q.worldedit.Vector
import me.wiefferink.areashop.events.notify.UnrentedRegionEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.maxgamer.quickshop.QuickShop
import org.maxgamer.quickshop.Shop.Shop

object BlackMarketAreaLogoutListener : Listener{
    @EventHandler
    fun onAreaLogout(event : UnrentedRegionEvent){
        val r = event.region.region
        val world = event.region.world
        val shopIterator = QuickShop.instance.shopManager.shopIterator
        val del = ArrayList<Shop>()
        for(shop in shopIterator){
            val loc = shop.location
            if(world.name != loc.world?.name ?:continue) continue
            val vector = Vector(loc.x,loc.y,loc.z)
            if(r.contains(vector)) del.add(shop)
        }
        for(shop in del){
            shop.delete()
        }
    }
}
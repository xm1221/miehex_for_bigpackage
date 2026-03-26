package cn.xm1221.miehex.api;


import cn.xm1221.miehex.iota.EnchantIota;

public class KubeJSApi {
    public static EnchantIota createEnchantIota(String id, int level) {
        return new EnchantIota(id, (short) level);
    }
}
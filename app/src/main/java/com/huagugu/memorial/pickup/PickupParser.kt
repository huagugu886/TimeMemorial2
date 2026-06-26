package com.huagugu.memorial.pickup

object PickupParser {

    data class PickupInfo(
        val code: String,
        val station: String = "",
        val courier: String = ""
    )

    private val CODE_PATTERNS = listOf(
        Regex("""取件码[：:\s]*(\d{1,4}[-－]\d{1,4}[-－]\d{1,4})"""),
        Regex("""取件码[：:\s]*(\d{6,8})"""),
        Regex("""取件码[：:\s]*([A-Za-z0-9]{4,12})"""),
        Regex("""提取码[：:\s]*(\S{4,12})"""),
        Regex("""凭码[：:\s]*(\S{4,12})\s*取"""),
    )

    private val STATION_PATTERNS = listOf(
        Regex("""(菜鸟驿站\S{0,10})"""),
        Regex("""(丰巢\S{0,6}柜\S{0,6})"""),
        Regex("""(\S{2,6}(?:驿站|快递点|代收点|自提柜))"""),
    )

    private val COURIERS = mapOf(
        "顺丰" to "顺丰", "京东" to "京东", "中通" to "中通",
        "圆通" to "圆通", "韵达" to "韵达", "申通" to "申通",
        "极兔" to "极兔", "邮政" to "邮政", "EMS" to "EMS",
        "丰巢" to "丰巢", "菜鸟" to "菜鸟",
    )

    fun parse(body: String): PickupInfo? {
        val code = CODE_PATTERNS.firstNotNullOfOrNull {
            it.find(body)?.groupValues?.get(1)
        } ?: return null

        val station = STATION_PATTERNS.firstNotNullOfOrNull {
            it.find(body)?.groupValues?.get(1)
        } ?: ""

        val courier = COURIERS.entries.firstOrNull { (k, _) ->
            body.contains(k)
        }?.value ?: ""

        return PickupInfo(code, station, courier)
    }
}

package com.chargepoint.poctaxengine.models

import com.chargepoint.poctaxengine.taxmodules.enums.Countries
import com.chargepoint.poctaxengine.taxmodules.enums.Currencies
import com.chargepoint.poctaxengine.taxmodules.enums.PowerType

class ChargeDetailRecord(
    val uuid: String,
    val netTotal: Float,
    val currency: Currencies,
    var taxId: Int?,
    val country: Countries,
    val powerType: PowerType,
) {
    var grossTotal: Float = 0f
    var taxTotal: Float = 0f
    var taxes = mutableMapOf<String, Float>()
}

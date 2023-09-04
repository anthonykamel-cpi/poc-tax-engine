package com.chargepoint.poctaxengine.taxmodules.dto

import com.chargepoint.poctaxengine.taxmodules.TaxingConditionSet
import com.chargepoint.poctaxengine.taxmodules.enums.ConfigurationScopes
import com.chargepoint.poctaxengine.taxmodules.enums.TaxCalculationMethod
import com.chargepoint.poctaxengine.taxmodules.enums.TaxType

data class TaxModuleParametersDto(
    val id: Int,
    val lable: String,
    val type: TaxType,
    val calculationMethod: TaxCalculationMethod,
    val amount: Float,
    val scope: ConfigurationScopes,
    val conditions: MutableList<TaxingConditionSet>,
)

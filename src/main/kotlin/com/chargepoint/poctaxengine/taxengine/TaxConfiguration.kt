package com.chargepoint.poctaxengine.taxengine

import com.chargepoint.poctaxengine.taxmodules.TaxModule

class TaxConfiguration {
    val id: Int = (5000..1000000).random()
    val taxModules = mutableListOf<TaxModule>()
    fun appendTaxModules(newTaxModules: List<TaxModule>) {
        taxModules.addAll(newTaxModules)
    }

    fun prependTaxModules(newTaxModules: List<TaxModule>) {
        taxModules.addAll(0, newTaxModules)
    }
}

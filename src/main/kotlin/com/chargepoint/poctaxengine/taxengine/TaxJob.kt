package com.chargepoint.poctaxengine.taxengine

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxmodules.enums.Currencies
import java.util.UUID

data class TaxJob(
    val taxConfiguration: TaxConfiguration,
    val cdr: ChargeDetailRecord,
) {
    val id = UUID.randomUUID().toString()
    lateinit var currency: Currencies
    var grossAmount = 0f
    public var netAmount = 0f
    val calculatedTaxes = mutableMapOf<String, Float>()
    private var taxSum: Float = 0f

    fun calculateTaxSum(): Float {
        calculatedTaxes.forEach { (_, taxAmount) -> taxSum += taxAmount }
        return taxSum
    }

    fun calculateGrossAmount(): Float {
        grossAmount = netAmount + taxSum
        return grossAmount
    }

    fun updateCdr() {
        cdr.taxTotal = taxSum
        cdr.grossTotal = grossAmount
        cdr.taxes = calculatedTaxes
    }
}

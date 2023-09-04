package com.chargepoint.poctaxengine.taxmodules

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxmodules.dto.TaxModuleParametersDto
import com.chargepoint.poctaxengine.taxmodules.enums.ConfigurationScopes
import com.chargepoint.poctaxengine.taxmodules.enums.TaxCalculationMethod
import com.chargepoint.poctaxengine.taxmodules.enums.TaxType
import org.apache.kafka.common.Uuid

class TaxModule(taxMOduleDto: TaxModuleParametersDto) {
    val id: Int = taxMOduleDto.id
    val uuid: String = Uuid.randomUuid().toString()
    val label: String = taxMOduleDto.lable
    val type: TaxType = taxMOduleDto.type
    private val calculationMethod: TaxCalculationMethod = taxMOduleDto.calculationMethod
    private val amount: Float = taxMOduleDto.amount
    val scope: ConfigurationScopes = taxMOduleDto.scope
    private val prerequisites: MutableList<TaxingConditionSet> = taxMOduleDto.conditions

    fun getTax(netAmount: Float): Float {
        println("Calculating $label tax for net amount $netAmount")
        val tax = if (TaxCalculationMethod.PERCENT == calculationMethod) {
            calculateTax(netAmount)
        } else {
            amount
        }
        println("Calculated $label tax with $tax")
        return tax
    }

    private fun calculateTax(netAmount: Float): Float {
        return (netAmount / 100) * amount
    }

    fun taxApplies(cdr: ChargeDetailRecord): Boolean {
        return cdrMeetsPrerequisites() && cdrIsInScope(cdr)
    }

    private fun cdrIsInScope(cdr: ChargeDetailRecord): Boolean {
        return true
    }

    private fun cdrMeetsPrerequisites(): Boolean {
        if (prerequisites.isEmpty()) return true

        prerequisites.forEach { prerequisite ->
            if (!prerequisite.evaluate()) return false
        }
        return true
    }
}

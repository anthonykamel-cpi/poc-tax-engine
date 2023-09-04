package com.chargepoint.poctaxengine.taxmodules

import com.chargepoint.poctaxengine.taxmodules.conditions.TaxingCondition
import com.chargepoint.poctaxengine.taxmodules.enums.LogicOperators

class TaxingConditionSet(
    private val taxingConditions: List<TaxingCondition<*, *>>,
    private val logicalOperator: LogicOperators,
) {
    fun evaluate(): Boolean {
        when (logicalOperator) {
            LogicOperators.AND -> {
                taxingConditions.forEach { taxingCondition: TaxingCondition<*, *> ->
                    if (!taxingCondition.evaluate()) return false
                }
                return true
            }

            LogicOperators.OR -> {
                taxingConditions.forEach { taxingCondition ->
                    if (taxingCondition.evaluate()) return true
                }
                return false
            }

            else -> throw Exception("That's not a supported operator")
        }
    }
}

package com.chargepoint.poctaxengine.taxmodules.conditions

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxmodules.enums.ComparisonOperators
import com.chargepoint.poctaxengine.taxmodules.enums.ConditionalProperties

class TaxingConditionMaxNetAmount(
    source: ChargeDetailRecord,
    expected: Float,
    comparisonOperator: ComparisonOperators,
) : TaxingCondition<Float, ChargeDetailRecord>(
    source,
    ConditionalProperties.MAX_NET_AMOUNT,
    expected,
    comparisonOperator,
) {
    override fun evaluate(): Boolean {
        return when (comparisonOperator) {
            ComparisonOperators.IS -> source.netTotal == expected
            ComparisonOperators.IS_NOT -> source.netTotal != expected
            ComparisonOperators.GREATER -> source.netTotal > expected
            ComparisonOperators.LESS -> source.netTotal < expected
            ComparisonOperators.GREATER_EQUAL -> source.netTotal >= expected
            ComparisonOperators.LESS_EQUAL -> source.netTotal <= expected
        }
    }
}

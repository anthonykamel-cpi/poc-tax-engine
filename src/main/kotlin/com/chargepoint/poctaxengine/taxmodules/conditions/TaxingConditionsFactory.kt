package com.chargepoint.poctaxengine.taxmodules.conditions

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxmodules.enums.ComparisonOperators
import com.chargepoint.poctaxengine.taxmodules.enums.ConditionalProperties

class TaxingConditionsFactory {
    fun <T, V> createTaxingCondition(
        source: V,
        property: ConditionalProperties,
        expected: T,
        comparisonOperator: ComparisonOperators,
    ): TaxingCondition<*, *> {
        return when (property) {
            ConditionalProperties.COUNTRY -> TaxingConditionCountry(
                source as ChargeDetailRecord,
                expected.toString(),
                comparisonOperator,
            )

            ConditionalProperties.POWER_TYPE -> TaxingConditionPowerType(
                source as ChargeDetailRecord,
                expected.toString(),
                comparisonOperator,
            )

            ConditionalProperties.MAX_NET_AMOUNT -> TaxingConditionMaxNetAmount(
                source as ChargeDetailRecord,
                expected as Float,
                comparisonOperator,
            )
        }
    }
}

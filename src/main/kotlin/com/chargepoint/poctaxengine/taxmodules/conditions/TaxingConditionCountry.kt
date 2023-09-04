package com.chargepoint.poctaxengine.taxmodules.conditions

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxmodules.enums.ComparisonOperators
import com.chargepoint.poctaxengine.taxmodules.enums.ConditionalProperties

class TaxingConditionCountry(
    source: ChargeDetailRecord,
    expected: String,
    comparisonOperator: ComparisonOperators,
) : TaxingCondition<String, ChargeDetailRecord>(
    source,
    ConditionalProperties.COUNTRY,
    expected,
    comparisonOperator,
) {
    override fun evaluate(): Boolean {
        return when (comparisonOperator) {
            ComparisonOperators.IS -> source.country.toString() == expected
            ComparisonOperators.IS_NOT -> source.country.toString() != expected
            else -> throw Exception("That's not a supported operator")
        }
    }
}

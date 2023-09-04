package com.chargepoint.poctaxengine.taxmodules.conditions

import com.chargepoint.poctaxengine.taxmodules.enums.ComparisonOperators
import com.chargepoint.poctaxengine.taxmodules.enums.ConditionalProperties

abstract class TaxingCondition<T, V>(
    protected val source: V,
    protected val property: ConditionalProperties,
    protected val expected: T,
    protected val comparisonOperator: ComparisonOperators,
) {
    abstract fun evaluate(): Boolean
}

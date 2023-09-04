package com.chargepoint.poctaxengine.taxmodules.enums

enum class ComparisonOperators(operator: String) {
    IS("=="),
    IS_NOT("!="),
    GREATER(">"),
    LESS("<"),
    GREATER_EQUAL(">="),
    LESS_EQUAL("<=")
}

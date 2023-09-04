package com.chargepoint.poctaxengine

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxengine.TaxConfiguration
import com.chargepoint.poctaxengine.taxengine.TaxEngine
import com.chargepoint.poctaxengine.taxengine.TaxJob
import com.chargepoint.poctaxengine.taxmodules.TaxModule
import com.chargepoint.poctaxengine.taxmodules.TaxingConditionSet
import com.chargepoint.poctaxengine.taxmodules.conditions.TaxingCondition
import com.chargepoint.poctaxengine.taxmodules.conditions.TaxingConditionsFactory
import com.chargepoint.poctaxengine.taxmodules.dto.TaxModuleParametersDto
import com.chargepoint.poctaxengine.taxmodules.enums.ComparisonOperators
import com.chargepoint.poctaxengine.taxmodules.enums.ConditionalProperties
import com.chargepoint.poctaxengine.taxmodules.enums.ConfigurationScopes
import com.chargepoint.poctaxengine.taxmodules.enums.Countries
import com.chargepoint.poctaxengine.taxmodules.enums.Currencies
import com.chargepoint.poctaxengine.taxmodules.enums.LogicOperators
import com.chargepoint.poctaxengine.taxmodules.enums.PowerType
import com.chargepoint.poctaxengine.taxmodules.enums.TaxCalculationMethod
import com.chargepoint.poctaxengine.taxmodules.enums.TaxType
import org.apache.kafka.common.Uuid
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.random.Random

@SpringBootApplication
class PocTaxEngineApplication

val taxEngine = TaxEngine()
fun main(args: Array<String>) {
    runApplication<PocTaxEngineApplication>(*args)
    generateTaxJobs((10..50).random())
    println("Starting in 3 seconds.\n\r")
    taxEngine.processJobs()
}

fun generateTaxJobs(jobCount: Int) {
    (1..jobCount).forEach { _ ->
        val cdr: ChargeDetailRecord = generateCdr()
        val taxConfiguration = generateTaxConfigruation(cdr)
        cdr.taxId = taxConfiguration.id
        val taxJob = TaxJob(taxConfiguration, cdr)
        taxJob.netAmount = cdr.netTotal
        taxEngine.addJob(taxJob)
    }
}

fun generateCdr(): ChargeDetailRecord {
    val netAmount = Random.nextFloat() + (1..50).random()
    return ChargeDetailRecord(
        Uuid.randomUuid().toString(),
        netAmount,
        Currencies.EUR,
        null,
        Countries.values().toList().shuffled().first(),
        PowerType.values().toList().shuffled().first(),
    )
}

fun generateTaxConfigruation(cdr: ChargeDetailRecord): TaxConfiguration {
    val taxConfiguration = TaxConfiguration()
    val taxModules = generateTaxModules(cdr, (1..3).random())
    taxConfiguration.appendTaxModules(taxModules)
    return taxConfiguration
}

fun generateTaxModules(cdr: ChargeDetailRecord, count: Int): List<TaxModule> {
    val moduleList = mutableListOf<TaxModule>()
    (1..count).forEach { index ->
        val vatTaxParams = TaxModuleParametersDto(
            (1..1000).random(),
            "Demo module label $index",
            TaxType.values().toList().shuffled().first(),
            TaxCalculationMethod.values().toList().shuffled().first(),
            Random.nextFloat() + (5..50).random(),
            ConfigurationScopes.BUNDESLAND,
            generateModulePrerequisites(cdr),
        )
        moduleList.add(TaxModule(vatTaxParams))
    }
    return moduleList
}

fun generateModulePrerequisites(cdr: ChargeDetailRecord): MutableList<TaxingConditionSet> {
    val taxingConditionSets = mutableListOf<TaxingConditionSet>()
    if ((0..1).random() == 1) {
        val list = mutableListOf<TaxingCondition<*, *>>()
        val conditionsFactory = TaxingConditionsFactory()
        val conditionsCount = (1..3).random()
        val countryCondition = generateCountryCondition(conditionsFactory, cdr, (0..1).random() == 1)
        list.add(countryCondition)
        if (conditionsCount >= 2) {
            val powerTypeCondition = generatedPowerTypeCondition(conditionsFactory, cdr, (0..1).random() == 1)
            list.add(powerTypeCondition)
        }
        if (conditionsCount == 3) {
            val maxNetAmountCondition = generateMaxNetAmountCondition(conditionsFactory, cdr, (0..1).random() == 1)
            list.add(maxNetAmountCondition)
        }
        taxingConditionSets.add(TaxingConditionSet(list, LogicOperators.values().toList().shuffled().first()))
    }
    return taxingConditionSets
}

private fun generateMaxNetAmountCondition(
    conditionsFactory: TaxingConditionsFactory,
    cdr: ChargeDetailRecord,
    criteriaMatches: Boolean,
): TaxingCondition<*, *> {
    return conditionsFactory.createTaxingCondition(
        cdr,
        ConditionalProperties.MAX_NET_AMOUNT,
        if (criteriaMatches) cdr.netTotal else 0.0f,
        ComparisonOperators.IS,
    )
}

private fun generatedPowerTypeCondition(
    conditionsFactory: TaxingConditionsFactory,
    cdr: ChargeDetailRecord,
    criteriaMatches: Boolean,
): TaxingCondition<*, *> {
    return conditionsFactory.createTaxingCondition(
        cdr,
        ConditionalProperties.POWER_TYPE,
        if (criteriaMatches) cdr.powerType else PowerType.values().toList().shuffled().first(),
        ComparisonOperators.IS,
    )
}

private fun generateCountryCondition(
    conditionsFactory: TaxingConditionsFactory,
    cdr: ChargeDetailRecord,
    criteriaMatches: Boolean,
): TaxingCondition<*, *> {
    return conditionsFactory.createTaxingCondition<Any, ChargeDetailRecord>(
        cdr,
        ConditionalProperties.COUNTRY,
        if (criteriaMatches) cdr.country.toString() else Countries.values().toList().shuffled().first(),
        ComparisonOperators.IS,
    )
}

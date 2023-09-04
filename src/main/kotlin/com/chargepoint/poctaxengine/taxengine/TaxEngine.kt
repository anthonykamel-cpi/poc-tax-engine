package com.chargepoint.poctaxengine.taxengine

import com.chargepoint.poctaxengine.models.ChargeDetailRecord
import com.chargepoint.poctaxengine.taxmodules.TaxModule
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.LinkedList
import java.util.UUID

class TaxEngine {
    private val taxJobs = LinkedList<TaxJob>()

    fun addJob(taxJob: TaxJob) {
        taxJobs.add(taxJob)
    }

    fun processJobs() {
        taxJobs.forEach { taxJob ->
            runBlocking {
                val taxJobThread = launch(CoroutineName(UUID.randomUUID().toString())) {
                    println("Processing tax job ${taxJob.id}")
                    taxJob.taxConfiguration.taxModules.forEach loop@{ taxModule ->
                        if (!taxModule.taxApplies(taxJob.cdr)) {
                            println("Tax module \"${taxModule.label}\" is't applicable to CDR ${taxJob.cdr.uuid}")
                            val taxModuleJson = Gson().toJson(taxModule, TaxModule::class.java)
                            println(taxModuleJson)
                            taxJob.calculatedTaxes[taxModule.uuid] = -1f
                            return@loop
                        }
                        taxJob.calculatedTaxes[taxModule.uuid] = taxModule.getTax(taxJob.netAmount)
                    }
                }

                taxJobThread.invokeOnCompletion { _ ->
                    taxJob.calculateTaxSum()
                    taxJob.calculateGrossAmount()
                    taxJob.updateCdr()
                    val json = Gson().toJson(taxJob.cdr, ChargeDetailRecord::class.java)
                    println("Tax calculation for job ${taxJob.id} is finished\n\r")
                    println("Net Amount: $json \n\r")
                }
            }
        }
    }
}

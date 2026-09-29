package com.kronos.core.domain.model

enum class PlanType(
    val title: String,
    val basePrice: Int,
    val maxCpu: Int,
    val maxRam: Int,
    val maxStorage: Int,
    val description: String,
    val features: List<String>
) {
    EMPRESARIAL(
        title = "Empresarial",
        basePrice = 299,
        maxCpu = 20,
        maxRam = 64,
        maxStorage = 2000,
        description = "Para equipos de hasta 50 profesionales IT",
        features = listOf(
            "20 vCPUs máx",
            "64 GB RAM",
            "2 TB Storage",
            "SLA 99.9%",
            "Soporte 24/7"
        )
    ),
    DEDICADO(
        title = "Dedicado",
        basePrice = 899,
        maxCpu = 128,
        maxRam = 512,
        maxStorage = 20000,
        description = "Infraestructura dedicada para enterprise",
        features = listOf(
            "128 vCPUs máx",
            "512 GB RAM",
            "20 TB Storage",
            "SLA 99.99%",
            "Soporte VIP + CSM"
        )
    )
}

data class LicenseCalculation(
    val planType: PlanType,
    val vCpus: Int,
    val ramGb: Int,
    val storageGb: Int,
    val baseCost: Int,
    val cpuCost: Int,
    val ramCost: Int,
    val storageCost: Int,
    val totalCost: Int
) {
    companion object {
        fun calculate(plan: PlanType, vCpus: Int, ramGb: Int, storageGb: Int): LicenseCalculation {
            val base = plan.basePrice
            val cpu = vCpus * 8
            val ram = ramGb * 2
            val storage = (storageGb * 0.05).toInt()
            val total = base + cpu + ram + storage

            return LicenseCalculation(
                planType = plan,
                vCpus = vCpus,
                ramGb = ramGb,
                storageGb = storageGb,
                baseCost = base,
                cpuCost = cpu,
                ramCost = ram,
                storageCost = storage,
                totalCost = total
            )
        }
    }
}

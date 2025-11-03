package iap.entity

import android.content.Context
import com.android.billingclient.api.AccountIdentifiers
import com.taymay.library.iap.R
import kotlin.collections.forEachIndexed


class DataWrappers {

    data class ProductDetails(
        val title: String?,
        val description: String?,
        val offers: List<Offer>?
    ) {
        fun prettyToString(): String {
            val sb = StringBuilder()
            val product = this
            sb.appendLine("────────────────────────────")
            sb.appendLine("🏷 Title: ${product.title ?: "N/A"}")
            sb.appendLine("📃 Description: ${product.description ?: "N/A"}")

            product.offers?.forEachIndexed { index, offer ->
                sb.appendLine("🔹 Offer #${index + 1}:")
                sb.appendLine("    🔑 ID: ${offer.id ?: "N/A"}")
                sb.appendLine("    🏷 Token: ${offer.token ?: "N/A"}")
                sb.appendLine("    🏷 Tags: ${offer.tags?.joinToString(", ") ?: "None"}")

                offer.pricingPhases.forEachIndexed { i, phase ->
                    sb.appendLine("    💰 Phase #${i + 1}:")
                    sb.appendLine("        💵 Price: ${phase.price ?: "N/A"} (${phase.priceCurrencyCode ?: "N/A"})")
                    sb.appendLine("        🔁 Billing Period: ${phase.billingPeriod ?: "N/A"}")
                    sb.appendLine("        🔄 Cycle Count: ${phase.billingCycleCount ?: "N/A"}")
                    sb.appendLine("        🔁 Recurrence Mode: ${phase.recurrenceMode ?: "N/A"}")
                }
            }

            sb.appendLine("────────────────────────────")
            return sb.toString()
        }
    }

    data class PurchaseInfo(
        val purchaseState: Int,
        val developerPayload: String,
        val isAcknowledged: Boolean,
        val isAutoRenewing: Boolean,
        val orderId: String?,
        val originalJson: String,
        val packageName: String,
        val purchaseTime: Long,
        val purchaseToken: String,
        val signature: String,
        val sku: String,
        val accountIdentifiers: AccountIdentifiers?
    )

    data class Offer(
        val id: String?,
        val token: String?,
        val tags: List<String>?,
        val pricingPhases: List<PricingPhase>
    )

    data class PricingPhase(
        val price: String?,
        val priceAmount: Double?,
        val priceCurrencyCode: String?,
        val billingCycleCount: Int?,
        val billingPeriod: String?,
        val recurrenceMode: Int?
    ) {
        fun formatPrice(context: Context): String {
            val regex = Regex("""P(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)W)?(?:(\d+)D)?""")
            val match = regex.matchEntire(billingPeriod.toString()) ?: return "Invalid format"
            val (years, months, weeks, days) = match.destructured

            val parts = listOfNotNull(
                years.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.price_year, price
                    )
                },
                months.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.price_month, price
                    )
                },
                weeks.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.price_week, price
                    )
                },
                days.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.price_day, price
                    )
                }
            )
            return parts.joinToString(", ")
        }

        fun formatFreeTrialDuration(context: Context): String {
            val regex = Regex("""P(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)W)?(?:(\d+)D)?""")
            val match = regex.matchEntire(billingPeriod.toString()) ?: return "Invalid format"
            val (years, months, weeks, days) = match.destructured

            val parts = listOfNotNull(
                years.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.trial_year, it
                    )
                },
                months.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.trial_month, it
                    )
                },
                weeks.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.trial_week, it
                    )
                },
                days.takeIf { it.isNotEmpty() }?.let {
                    context.getString(
                        R.string.trial_day, it
                    )
                }
            )
            return parts.joinToString(", ")
        }

    }
}
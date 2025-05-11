package model

import play.api.libs.json.{Format, Json}
import java.time.Instant

case class Payment(
                    id: Int,          // Unikalny identyfikator płatności
                    cardNumber: String,
                    amount: Double,
                    timestamp: Instant,  // Data i godzina transakcji
                    status: String,      // Status płatności (np. "pending", "completed")
                  )

object Payment {
  implicit val format: Format[Payment] = Json.format[Payment]
}
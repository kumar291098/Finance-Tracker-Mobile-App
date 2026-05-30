package com.example.viewmodel

import android.app.Application
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.util.Base64
import android.util.Xml
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.Transaction
import com.example.data.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class DateFilter {
    ALL,
    TODAY,
    YESTERDAY,
    THIS_MONTH,
    CUSTOM
}

data class ChartPoint(
    val dateLabel: String,
    val income: Double,
    val expense: Double
)

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransactions: StateFlow<List<Transaction>>

    // Filtering states
    val selectedFilter = MutableStateFlow(DateFilter.ALL)
    val customDateRange = MutableStateFlow(Pair(0L, 0L))

    // Reactive filtered transactions
    val filteredTransactions: StateFlow<List<Transaction>>

    val totalBalance: StateFlow<Double>
    val totalIncome: StateFlow<Double>
    val totalExpense: StateFlow<Double>

    // Category breakdown percentage statistics (e.g. "Food" -> 35.0%)
    val categoryStats: StateFlow<Map<String, Double>>

    // Dynamic Chart Points for trend graphs
    val chartPoints: StateFlow<List<ChartPoint>>

    // New Analytical variables for the analysis section
    val categoryTotals: StateFlow<Map<String, Double>>
    val averageTransactionSize: StateFlow<Double>
    val topSpendingCategory: StateFlow<String?>
    val financialHealthScore: StateFlow<Int>

    private val client = OkHttpClient()

    private val validCategories = listOf("Salary", "Food", "Shopping", "Rent", "Transport", "Utilities", "Entertainment", "Other")

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TransactionRepository(database.transactionDao)

        allTransactions = repository.allTransactions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Combine allTransactions with filter settings to produce filteredTransactions
        filteredTransactions = combine(allTransactions, selectedFilter, customDateRange) { list, filter, range ->
            when (filter) {
                DateFilter.ALL -> list
                DateFilter.TODAY -> {
                    val bounds = getDayBounds(0)
                    list.filter { it.date >= bounds.first && it.date < bounds.second }
                }
                DateFilter.YESTERDAY -> {
                    val bounds = getDayBounds(-1)
                    list.filter { it.date >= bounds.first && it.date < bounds.second }
                }
                DateFilter.THIS_MONTH -> {
                    val bounds = getMonthBounds()
                    list.filter { it.date >= bounds.first && it.date < bounds.second }
                }
                DateFilter.CUSTOM -> {
                    list.filter { it.date >= range.first && it.date <= range.second }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Calculate totals based on FILTERED transactions
        totalIncome = filteredTransactions
            .map { list -> list.filter { it.type == "INCOME" }.sumOf { it.amount } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        totalExpense = filteredTransactions
            .map { list -> list.filter { it.type == "EXPENSE" }.sumOf { it.amount } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        totalBalance = filteredTransactions
            .map { list ->
                val income = list.filter { it.type == "INCOME" }.sumOf { it.amount }
                val expense = list.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                income - expense
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        categoryStats = filteredTransactions
            .map { list ->
                val expensesOnly = list.filter { it.type == "EXPENSE" }
                val totalExp = expensesOnly.sumOf { it.amount }
                if (totalExp == 0.0) {
                    emptyMap()
                } else {
                    expensesOnly.groupBy { it.category }
                        .mapValues { entry -> (entry.value.sumOf { it.amount } / totalExp) * 100.0 }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

        // Derive daily chart points from filtered transactions
        chartPoints = filteredTransactions.map { list ->
            val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
            val sortedList = list.sortedBy { it.date }
            val groupedByDay = sortedList.groupBy { dateFormat.format(Date(it.date)) }

            groupedByDay.map { (day, dayList) ->
                ChartPoint(
                    dateLabel = day,
                    income = dayList.filter { it.type == "INCOME" }.sumOf { it.amount },
                    expense = dayList.filter { it.type == "EXPENSE" }.sumOf { it.amount }
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Initialize New Analytical variables
        categoryTotals = filteredTransactions
            .map { list ->
                val expensesOnly = list.filter { it.type == "EXPENSE" }
                expensesOnly.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

        averageTransactionSize = filteredTransactions
            .map { list ->
                val expensesOnly = list.filter { it.type == "EXPENSE" }
                if (expensesOnly.isEmpty()) 0.0 else expensesOnly.map { it.amount }.average()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

        topSpendingCategory = filteredTransactions
            .map { list ->
                val expensesOnly = list.filter { it.type == "EXPENSE" }
                if (expensesOnly.isEmpty()) {
                    null
                } else {
                    expensesOnly.groupBy { it.category }
                        .maxByOrNull { entry -> entry.value.sumOf { it.amount } }?.key
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        financialHealthScore = combine(totalBalance, totalIncome, totalExpense) { balance, income, expense ->
            if (income == 0.0 && expense == 0.0) {
                100
            } else {
                val savingsRate = if (income > 0) ((income - expense) / income) else 0.0
                var score = 40
                
                score += (savingsRate * 40.0).coerceIn(0.0, 40.0).toInt()
                
                if (balance > 0) {
                    score += 20
                } else if (balance == 0.0) {
                    score += 10
                }
                
                if (balance < 0) {
                    score -= 15
                }

                if (expense < income) {
                    score += 40
                } else if (expense == income) {
                    score += 20
                }
                
                score.coerceIn(0, 100)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 100)
    }

    fun addTransaction(title: String, amount: Double, type: String, category: String, date: Long, description: String? = null) {
        viewModelScope.launch {
            repository.insert(
                Transaction(
                    title = title,
                    amount = amount,
                    type = type,
                    category = category,
                    date = date,
                    description = description
                )
            )
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }

    // Helper functions for date calculations
    private fun getDayBounds(offsetDays: Int): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, offsetDays)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    private fun getMonthBounds(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val end = calendar.timeInMillis
        return Pair(start, end)
    }

    // XML Export
    fun exportToXml(outputStream: java.io.OutputStream, onComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = allTransactions.value
                val sb = StringBuilder()
                sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n")
                sb.append("<transactions>\n")
                for (t in list) {
                    sb.append("    <transaction>\n")
                    sb.append("        <title>${escapeXml(t.title)}</title>\n")
                    sb.append("        <amount>${t.amount}</amount>\n")
                    sb.append("        <type>${t.type}</type>\n")
                    sb.append("        <category>${escapeXml(t.category)}</category>\n")
                    sb.append("        <date>${t.date}</date>\n")
                    sb.append("        <description>${escapeXml(t.description ?: "")}</description>\n")
                    sb.append("    </transaction>\n")
                }
                sb.append("</transactions>\n")
                outputStream.write(sb.toString().toByteArray())
                outputStream.close()
                withContext(Dispatchers.Main) {
                    onComplete()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Failed to export XML: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun escapeXml(str: String): String {
        return str.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    // CSV Export
    fun exportToCsv(outputStream: java.io.OutputStream, onComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = filteredTransactions.value
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val sb = StringBuilder()
                sb.append("Title,Amount (₹),Type,Category,Date,Description\n")
                for (t in list) {
                    val title = escapeCsv(t.title)
                    val amount = t.amount
                    val type = t.type
                    val category = escapeCsv(t.category)
                    val dateStr = dateFormat.format(Date(t.date))
                    val desc = escapeCsv(t.description ?: "")
                    sb.append("\"$title\",$amount,$type,\"$category\",\"$dateStr\",\"$desc\"\n")
                }
                outputStream.write(sb.toString().toByteArray())
                outputStream.close()
                withContext(Dispatchers.Main) {
                    onComplete()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Failed to export CSV: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun escapeCsv(str: String): String {
        return str.replace("\"", "\"\"")
    }

    // XML Import
    fun importFromXml(inputStream: java.io.InputStream, onComplete: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val parser = Xml.newPullParser()
                parser.setInput(inputStream, null)
                var eventType = parser.eventType
                var count = 0

                var currentTitle = ""
                var currentAmount = 0.0
                var currentType = ""
                var currentCategory = ""
                var currentDate = 0L
                var currentDescription: String? = null

                var insideTag = ""

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            insideTag = parser.name
                            if (insideTag == "transaction") {
                                currentTitle = ""
                                currentAmount = 0.0
                                currentType = "EXPENSE"
                                currentCategory = "Other"
                                currentDate = System.currentTimeMillis()
                                currentDescription = null
                            }
                        }
                        XmlPullParser.TEXT -> {
                            val text = parser.text.trim()
                            if (text.isNotEmpty()) {
                                when (insideTag) {
                                    "title" -> currentTitle = text
                                    "amount" -> currentAmount = text.toDoubleOrNull() ?: 0.0
                                    "type" -> currentType = text
                                    "category" -> currentCategory = text
                                    "date" -> currentDate = text.toLongOrNull() ?: System.currentTimeMillis()
                                    "description" -> currentDescription = text.ifEmpty { null }
                                }
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            val tag = parser.name
                            if (tag == "transaction") {
                                if (currentAmount > 0.0 && currentTitle.isNotEmpty()) {
                                    repository.insert(
                                        Transaction(
                                            title = currentTitle,
                                            amount = currentAmount,
                                            type = currentType,
                                            category = currentCategory,
                                            date = currentDate,
                                            description = currentDescription
                                        )
                                    )
                                    count++
                                }
                            }
                            insideTag = ""
                        }
                    }
                    eventType = parser.next()
                }
                inputStream.close()
                withContext(Dispatchers.Main) {
                    onComplete(count)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Failed to parse XML: ${e.localizedMessage}")
                }
            }
        }
    }

    // PDF Statement Export
    fun exportToPdf(outputStream: OutputStream, onComplete: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = filteredTransactions.value
                val pdfDocument = PdfDocument()
                val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas = page.canvas

                val titlePaint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 24f
                    isFakeBoldText = true
                }

                val headerPaint = Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 14f
                    isFakeBoldText = true
                }

                val textPaint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12f
                }

                val incomePaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#10B981")
                    textSize = 12f
                    isFakeBoldText = true
                }

                val expensePaint = Paint().apply {
                    color = android.graphics.Color.parseColor("#EF4444")
                    textSize = 12f
                    isFakeBoldText = true
                }

                val linePaint = Paint().apply {
                    color = android.graphics.Color.LTGRAY
                    strokeWidth = 1f
                }

                canvas.drawText("RupeeFlow - Financial Statement", 40f, 60f, titlePaint)

                val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                canvas.drawText("Generated on: ${dateFormat.format(Date())}", 40f, 85f, textPaint)

                // Summary Background Box
                canvas.drawRect(40f, 110f, 555f, 170f, Paint().apply {
                    color = android.graphics.Color.parseColor("#F1F5F9")
                    style = Paint.Style.FILL
                })

                val summaryTitlePaint = Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 10f
                }
                val summaryValuePaint = Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 14f
                    isFakeBoldText = true
                }

                canvas.drawText("Total Income", 60f, 130f, summaryTitlePaint)
                canvas.drawText(String.format("₹%.2f", totalIncome.value), 60f, 155f, summaryValuePaint)

                canvas.drawText("Total Expenses", 240f, 130f, summaryTitlePaint)
                canvas.drawText(String.format("₹%.2f", totalExpense.value), 240f, 155f, summaryValuePaint)

                canvas.drawText("Net Balance", 420f, 130f, summaryTitlePaint)
                canvas.drawText(String.format("₹%.2f", totalBalance.value), 420f, 155f, summaryValuePaint)

                // Table Header
                var yPosition = 210f
                canvas.drawText("Title", 40f, yPosition, headerPaint)
                canvas.drawText("Category", 220f, yPosition, headerPaint)
                canvas.drawText("Date", 350f, yPosition, headerPaint)
                canvas.drawText("Amount", 480f, yPosition, headerPaint)
                canvas.drawLine(40f, yPosition + 10f, 555f, yPosition + 10f, linePaint)

                yPosition += 30f
                val itemDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

                for (t in list) {
                    if (yPosition > 800f) {
                        break
                    }
                    canvas.drawText(t.title, 40f, yPosition, textPaint)
                    canvas.drawText(t.category, 220f, yPosition, textPaint)
                    canvas.drawText(itemDateFormat.format(Date(t.date)), 350f, yPosition, textPaint)

                    val prefix = if (t.type == "INCOME") "+" else "-"
                    val amountText = String.format("%s₹%.2f", prefix, t.amount)
                    val paint = if (t.type == "INCOME") incomePaint else expensePaint
                    canvas.drawText(amountText, 480f, yPosition, paint)

                    canvas.drawLine(40f, yPosition + 8f, 555f, yPosition + 8f, Paint().apply {
                        color = android.graphics.Color.parseColor("#F1F5F9")
                        strokeWidth = 0.5f
                    })
                    yPosition += 25f
                }

                pdfDocument.finishPage(page)
                pdfDocument.writeTo(outputStream)
                pdfDocument.close()
                outputStream.close()

                withContext(Dispatchers.Main) {
                    onComplete()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Failed to export PDF: ${e.localizedMessage}")
                }
            }
        }
    }

    // Gemini AI Invoice Scanner
    fun scanInvoiceWithAi(fileBytes: ByteArray, mimeType: String, onComplete: (Int) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val base64File = Base64.encodeToString(fileBytes, Base64.NO_WRAP)
                val apiKey = BuildConfig.GEMINI_API_KEY
                if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
                    withContext(Dispatchers.Main) {
                        onError("Gemini API Key is not configured in your .env file.")
                    }
                    return@launch
                }

                val requestJson = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", "Analyze this receipt/invoice/statement file. Extract all transactions. Return ONLY a raw JSON array of objects representing the transactions. Each transaction object MUST contain: 'title' (string), 'amount' (double), 'type' (string: either 'INCOME' or 'EXPENSE'), 'category' (string: one of 'Salary', 'Food', 'Shopping', 'Rent', 'Transport', 'Utilities', 'Entertainment', 'Other'), and 'date' (timestamp in milliseconds, use current timestamp if not specified). Do not include any markdown formatting like ```json ... ``` or anything else. Just return the raw JSON array string.")
                                })
                                put(JSONObject().apply {
                                    put("inlineData", JSONObject().apply {
                                        put("mimeType", mimeType)
                                        put("data", base64File)
                                    })
                                })
                            })
                        })
                    })
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = requestJson.toString().toRequestBody(mediaType)
                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        onError("AI Server error: ${response.code} ${response.message}")
                    }
                    return@launch
                }

                val responseBody = response.body?.string() ?: ""
                val responseJson = JSONObject(responseBody)
                val textCandidate = responseJson.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .trim()

                var cleanJson = textCandidate
                if (cleanJson.startsWith("```json")) {
                    cleanJson = cleanJson.substringAfter("```json").substringBeforeLast("```").trim()
                } else if (cleanJson.startsWith("```")) {
                    cleanJson = cleanJson.substringAfter("```").substringBeforeLast("```").trim()
                }

                val transactionsArray = JSONArray(cleanJson)
                var count = 0
                for (i in 0 until transactionsArray.length()) {
                    val item = transactionsArray.getJSONObject(i)
                    val title = item.optString("title", "Scanned Bill")
                    val amount = item.optDouble("amount", 0.0)
                    val type = item.optString("type", "EXPENSE").uppercase()
                    val category = item.optString("category", "Other")
                    val date = item.optLong("date", System.currentTimeMillis())

                    if (amount > 0.0) {
                        repository.insert(
                            Transaction(
                                title = title,
                                amount = amount,
                                type = if (type == "INCOME") "INCOME" else "EXPENSE",
                                category = if (validCategories.contains(category)) category else "Other",
                                date = date
                            )
                        )
                        count++
                    }
                }

                withContext(Dispatchers.Main) {
                    onComplete(count)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Scanning failed: ${e.localizedMessage}")
                }
            }
        }
    }

    private val sharedPrefs = getApplication<Application>().getSharedPreferences("rupeeflow_prefs", android.content.Context.MODE_PRIVATE)

    fun isBiometricEnabled(): Boolean {
        return sharedPrefs.getBoolean("use_biometric", false)
    }

    fun setBiometricEnabled(enabled: Boolean) {
        sharedPrefs.edit().putBoolean("use_biometric", enabled).apply()
    }
}

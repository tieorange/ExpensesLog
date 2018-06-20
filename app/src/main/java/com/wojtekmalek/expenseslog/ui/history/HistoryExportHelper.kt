package com.wojtekmalek.expenseslog.ui.history

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.mcxiaoke.koi.ext.asString
import com.wojtekmalek.expenseslog.model.ExpenseItem
import org.supercsv.cellprocessor.ift.CellProcessor
import java.util.*


class HistoryExportHelper(
        private val context: Context,
        private val expenses: List<ExpenseItem>
        //        private val rxDrive: RxDrive
) {
    val seperator = "   ";
    val newLine = "\n";

    fun getExpensesCsvFile() {
        val groupedByDate = groupByDate()
        val csvString = getCsvString(groupedByDate)
        uploadCsvToDrive(csvString)
        shareToEmail(csvString)


        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).absolutePath;
//        val csvBeanWriter = CsvBeanWriter(FileWriter(storageDir), CsvPreference.STANDARD_PREFERENCE)
//        val processors = getProcessors()
    }

    private fun shareToEmail(csvString: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abc@gmail.com", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Export expenses history")
        emailIntent.putExtra(Intent.EXTRA_TEXT, csvString)
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    private fun uploadCsvToDrive(csvString: String) {

    }

    private fun groupByDate(): Map<String, List<ExpenseItem>> {

        return expenses
                .sortedBy { it.timeStamp }
                .groupBy { Date(it.timeStamp).asString("dd.MM.yyyy") }
    }

    private fun getCsvString(groupedByDate: Map<String, List<ExpenseItem>>): String {
        val csvString = StringBuilder()

        groupedByDate.entries.forEach { entry: Map.Entry<String, List<ExpenseItem>> ->
            csvString.append("    ${entry.key} \n") // "10.01.2018"
            entry.value.forEach { expenseItem ->
                csvString.append("${expenseItem.price} zł") // 15 zł
                csvString.append(seperator)
                csvString.append("${expenseItem.category?.name}") // Food
                csvString.append(seperator)
                csvString.append(expenseItem.getTimeString()) // 15:00
                csvString.append(newLine)
            }
            csvString.append(newLine)
        }

        csvString.append("")

        return csvString.toString()
    }

    companion object {
        private fun getProcessors(): Array<CellProcessor> {
            return arrayOf(org.supercsv.cellprocessor.constraint.NotNull(), // date/price
                    org.supercsv.cellprocessor.constraint.NotNull() // category
                    //                    FmtDate("dd/MM/yyyy"), // birthDate
            )
        }
    }
}
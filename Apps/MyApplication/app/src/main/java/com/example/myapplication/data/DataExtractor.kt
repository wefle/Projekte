package com.example.myapplication.data

import android.os.Environment
import android.util.Log
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import java.io.File
import java.time.LocalDate

class DataExtractor() {
    // get trash data from pdf file
    suspend fun extractPDFData(vm: TrashViewModel, str: String, nr: Int, version: String) {
        //create file -> data bank
        val extractedText : Array<String> = arrayOf("","")

        try {
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS)
            val file = File(path, "${str}_${nr}_magdeburg_abfuhrtermine_${version}.pdf")

            if(file.exists()) {

                val pdfReader: PdfReader = PdfReader(file)

                val pdfDoc: PdfDocument = PdfDocument(pdfReader)

                //extract data (doc consists of 2 pages - 1.half + 2.half of year)
                extractedText[0] =
                    """
             $extractedText${
                        PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1)).trim { it <= ' ' }
                    }
             """.trimIndent()

                //filtering data
                extractedText[0] = extractedText[0].replace("Mo", " ").replace("Di", " ")
                    .replace("Mi", " ").replace("Do", " ")
                    .replace("Fr", " ").replace("Sa", " ")
                    .replace("So", " ").replace("*", "")
                    .replace("+", "")
                    .replace("Bio", "B")
                    .replace("Gelbe Tonne", "G")
                    .replace("Papier", "P")
                    .replace("Rest", "R")
                val page1: MutableList<String> = extractedText[0].split("\n").toMutableList()
                if (!page1[page1.lastIndex].contains("31")) {page1.removeAt(page1.lastIndex)}
                page1.subList(0, 3).clear()

                //clean data -> page 1
                for (i in 0 until page1.size) {
                    val line: MutableList<String> = page1[i].split("\\s ".toRegex()).toMutableList()
                    line.removeAt(0)
                    if (line.size < 4) {
                        //January
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 1, i + 1),
                            trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //March
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 3, i + 1),
                            trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //May
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 5, i + 1),
                            trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                    } else if (line.size < 6) {
                        //January
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 1, i + 1),
                            trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //March
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 3, i + 1),
                            trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //April
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 4, i + 1),
                            trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //May
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 5, i + 1),
                            trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //June
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 6, i + 1),
                            trashType = line[4].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                    } else {
                        //January
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 1, i + 1),
                            trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //February
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 2, i + 1),
                            trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //March
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 3, i + 1),
                            trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //April
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 4, i + 1),
                            trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //May
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 5, i + 1),
                            trashType = line[4].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //June
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 6, i + 1),
                            trashType = line[5].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                    }
                }

                //extract data (doc consists of 2 pages - 1.half + 2.half of year)
                extractedText[1] =
                    """
             $extractedText${
                        PdfTextExtractor.getTextFromPage(pdfDoc.getPage(2)).trim { it <= ' ' }
                    }
             """.trimIndent()

                //filtering data
                extractedText[1] = extractedText[1].replace("Mo", " ").replace("Di", " ")
                    .replace("Mi", " ").replace("Do", " ")
                    .replace("Fr", " ").replace("Sa", " ")
                    .replace("So", " ").replace("*", "")
                    .replace("+", "")
                    .replace("Bio", "B")
                    .replace("Gelbe Tonne", "G")
                    .replace("Papier", "P")
                    .replace("Rest", "R")
                val page2: MutableList<String> = extractedText[1].split("\n").toMutableList()
                if (!page2[page2.lastIndex].contains("31")) {page2.removeAt(page2.lastIndex)}
                page2.subList(0, 3).clear()

                //clean data -> page 2
                for (i in 0 until page2.size) {
                    val line: MutableList<String> = page2[i].split("\\s ".toRegex()).toMutableList()
                    line.removeAt(0)
                    if (line.size < 5) {
                        //July
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 7, i + 1),
                            trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //August
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 8, i + 1),
                            trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //October
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 10, i + 1),
                            trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //December
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 12, i + 1),
                            trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                    } else {
                        //July
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 7, i + 1),
                            trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //August
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 8, i + 1),
                            trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //September
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 9, i + 1),
                            trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //October
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 10, i + 1),
                            trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //November
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 11, i + 1),
                            trashType = line[4].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                        //December
                        vm.addTrashData(
                            date = LocalDate.of(version.toInt(), 12, i + 1),
                            trashType = line[5].replace("\\d+".toRegex(), "").replace(" ", "")
                        )
                    }
                }

                pdfReader.close()

                file.delete()
            }
        }
        catch (e: Exception) {
            Log.e("PDFExtractor", "Something went wrong", e)
            e.printStackTrace()
        }
    }

    // update trash data from pdf file
    suspend fun updatePDFData(trashDataList: List<TrashEntity>, vm: TrashViewModel, str: String, nr: Int, version: String) {
        val extractedText : Array<String> = arrayOf("","")

        try {
            if(version.toInt() != trashDataList[0].year) {
                extractPDFData(vm = vm, str = str, nr = nr, version = version)
            }
            else {
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS
                )
                val file = File(path, "${str}_${nr}_magdeburg_abfuhrtermine_${version}.pdf")

                if (file.exists()) {
                    val pdfReader: PdfReader = PdfReader(file)

                    val pdfDoc: PdfDocument = PdfDocument(pdfReader)

                    //extract data (doc consists of 2 pages - 1.half + 2.half of year)
                    extractedText[0] =
                        """
                 $extractedText${
                            PdfTextExtractor.getTextFromPage(pdfDoc.getPage(1)).trim { it <= ' ' }
                        }
                 """.trimIndent()

                    //filtering data
                    extractedText[0] = extractedText[0].replace("Mo", " ").replace("Di", " ")
                        .replace("Mi", " ").replace("Do", " ")
                        .replace("Fr", " ").replace("Sa", " ")
                        .replace("So", " ").replace("*", "")
                        .replace("+", "")
                        .replace("Bio", "B")
                        .replace("Gelbe Tonne", "G")
                        .replace("Papier", "P")
                        .replace("Rest", "R")
                    val page1: MutableList<String> = extractedText[0].split("\n").toMutableList()
                    if (!page1[page1.lastIndex].contains("31")) {page1.removeAt(page1.lastIndex)}
                    page1.subList(0, 3).clear()

                    var count = 0

                    //clean data -> page 1
                    for (i in 0 until page1.size) {
                        val line: MutableList<String> =
                            page1[i].split("\\s ".toRegex()).toMutableList()
                        line.removeAt(0)
                        if (line.size < 4) {
                            //January
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 1, i + 1),
                                trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //March
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 3, i + 1),
                                trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //May
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 5, i + 1),
                                trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                        } else if (line.size < 6) {
                            //January
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 1, i + 1),
                                trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //March
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 3, i + 1),
                                trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //April
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 4, i + 1),
                                trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //May
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 5, i + 1),
                                trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //June
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 6, i + 1),
                                trashType = line[4].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                        } else {
                            //January
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 1, i + 1),
                                trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //February
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 2, i + 1),
                                trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //March
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 3, i + 1),
                                trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //April
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 4, i + 1),
                                trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //May
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 5, i + 1),
                                trashType = line[4].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //June
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 6, i + 1),
                                trashType = line[5].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                        }
                    }

                    //extract data (doc consists of 2 pages - 1.half + 2.half of year)
                    extractedText[1] =
                        """
                 $extractedText${
                            PdfTextExtractor.getTextFromPage(pdfDoc.getPage(2)).trim { it <= ' ' }
                        }
                 """.trimIndent()

                    //filtering data
                    extractedText[1] = extractedText[1].replace("Mo", " ").replace("Di", " ")
                        .replace("Mi", " ").replace("Do", " ")
                        .replace("Fr", " ").replace("Sa", " ")
                        .replace("So", " ").replace("*", "")
                        .replace("+", "")
                        .replace("Bio", "B")
                        .replace("Gelbe Tonne", "G")
                        .replace("Papier", "P")
                        .replace("Rest", "R")
                    val page2: MutableList<String> = extractedText[1].split("\n").toMutableList()
                    if (!page2[page2.lastIndex].contains("31")) {page2.removeAt(page2.lastIndex)}
                    page2.subList(0, 3).clear()

                    //clean data -> page 2
                    for (i in 0 until page2.size) {
                        val line: MutableList<String> =
                            page2[i].split("\\s ".toRegex()).toMutableList()
                        line.removeAt(0)
                        if (line.size < 5) {
                            //July
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 7, i + 1),
                                trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //August
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 8, i + 1),
                                trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //October
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 10, i + 1),
                                trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //December
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 12, i + 1),
                                trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                        } else {
                            //July
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 7, i + 1),
                                trashType = line[0].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //August
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 8, i + 1),
                                trashType = line[1].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //September
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 9, i + 1),
                                trashType = line[2].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //October
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 10, i + 1),
                                trashType = line[3].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //November
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 11, i + 1),
                                trashType = line[4].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                            //December
                            vm.updateTrashData(
                                trashDataList[count],
                                date = LocalDate.of(version.toInt(), 12, i + 1),
                                trashType = line[5].replace("\\d+".toRegex(), "").replace(" ", "")
                            )
                            count += 1
                        }
                    }

                    pdfReader.close()

                    file.delete()
                }
            }
        }
        catch (e: Exception) {
            Log.e("PDFExtractor", "Something went wrong", e)
            e.printStackTrace()
        }
    }
}

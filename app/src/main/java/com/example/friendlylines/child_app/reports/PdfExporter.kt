
package com.example.friendlylines.child_app.reports

import android.content.Context
import android.net.Uri
import android.graphics.Paint
import android.graphics.pdf.PdfDocument

object PdfExporter {
    fun save(
        context: Context,
        uri: Uri,
        content: String
    ) {
        val document = PdfDocument()

        val pageWidth = 595
        val pageHeight = 842
        val margin = 40f

        val paint = Paint().apply {
            textSize = 10f
            isAntiAlias = true
        }

        val lineHeight = 15f
        val maxWidth = pageWidth - 2 * margin

        var pageNumber = 1

        var page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())

        var canvas = page.canvas
        var y = margin + lineHeight

        fun newPage() {
            document.finishPage(page)
            pageNumber++
            page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            canvas = page.canvas
            y = margin + lineHeight
        }

        content.lines().forEach { originalLine ->

            if (originalLine.isEmpty()) {
                y += lineHeight
            } else {
                var remaining = originalLine

                while (remaining.isNotEmpty()) {
                    if (y + lineHeight > pageHeight - margin) { newPage() }

                    val count = paint.breakText(remaining, true, maxWidth, null).coerceAtLeast(1)
                    val part = remaining.substring(0, count)
                    canvas.drawText(part, margin, y, paint)
                    remaining = remaining.substring(count)
                    y += lineHeight
                }
            }

            if (y + lineHeight > pageHeight - margin) { newPage() }
        }

        document.finishPage(page)

        try {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                document.writeTo(output)
            } ?: error("Cannot open file")
        } finally {
            document.close()
        }
    }
}

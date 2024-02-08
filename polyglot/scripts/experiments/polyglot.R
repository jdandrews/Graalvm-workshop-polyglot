library("asciichartr")

toPlot <- function(data) {
    y = scan(textConnection(data))
    cat(asciiPlot(y, cfg = list('height' = 14)))
}

export('toPlot', toPlot)

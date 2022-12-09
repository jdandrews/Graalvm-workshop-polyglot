library("asciichartr")

toPlot <- function(data) {
    y = scan(textConnection(data))
    cat(asciiPlot(y, cfg = list('height' = 20)))
}

export('toPlot', toPlot)

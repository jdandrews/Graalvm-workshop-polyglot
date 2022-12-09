from polyglot import import_value, export_value
r_plot = import_value('toPlot')

@export_value
def transform_message(data):
    print (r_plot(data))


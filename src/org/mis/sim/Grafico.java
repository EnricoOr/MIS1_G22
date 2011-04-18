package org.mis.sim; 


import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * A simple demonstration application showing how to create a line chart using data from an
 * {@link XYDataset}.
 *
 */
public class Grafico extends ApplicationFrame {
	final XYSeries serie1;
	//final XYSeries serie2;
	private final JFreeChart chart;
	private final String title;
	private final String xLabel;
	private final String yLabel;
	
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new demo
     * @param title  the frame title.
     */
    public Grafico(final String title, final String xLabel, final String yLabel, final String valName) {
        super(title);
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.serie1 = new XYSeries(valName);
        //serie2 = new XYSeries("Varianza");
        final XYDataset dataset = createDataset();
        chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
        chart.removeLegend();
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {

        final XYSeriesCollection dataset = new XYSeriesCollection();
       
        dataset.addSeries(serie1);
        //dataset.addSeries(serie2);

        return dataset;
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,	// titolo grafico
            xLabel,									// etichetta asse x
            yLabel,									// etichetta asse y
            dataset,								// data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Rectangle(2, 2));
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        final ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        domainAxis.setTickLabelFont(domainAxis.getTickLabelFont().deriveFont(new Float(16.0)));
        rangeAxis.setTickLabelFont(rangeAxis.getTickLabelFont().deriveFont(new Float(16.0)));
        
        return chart;        
    }

	public void addValue(double a, double b)
	{
		this.serie1.add(a, b);
	}
		
	/**
	 * Exports a JFreeChart to a SVG file.
	 * 
	 * @param bounds the dimensions of the viewport
	 * @throws IOException if writing the svgFile fails.
	 */
	public void exportChartAsSVG(int width, int height) 
	{
		try
		{
			File svgFile = new File(title + ".svg");
	        // Get a DOMImplementation and create an XML document
	        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
	        Document document = domImpl.createDocument(null, "svg", null);
	
	        // Create an instance of the SVG Generator
	        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
	
	        // draw the chart in the SVG generator
	        Rectangle r = new Rectangle(width, height);
	        chart.draw(svgGenerator, r);
	
	        // Write svg file
	        OutputStream outputStream = new FileOutputStream(svgFile);
	        Writer out = new OutputStreamWriter(outputStream, "UTF-8");
	        svgGenerator.stream(out, true /* use css */);						
	        outputStream.flush();
	        outputStream.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
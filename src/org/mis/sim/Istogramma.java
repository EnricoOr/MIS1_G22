package org.mis.sim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

public class Istogramma extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	public final DefaultCategoryDataset dataset;
	public final double media;
	public final boolean columnMedia;
	private final JFreeChart chart;
	private final String title;

	/**
     * Creates a new demo instance.
     * @param title  the frame title.
     */
    public Istogramma(final String title, double media, boolean columnMedia)
    {
        super(title);
        this.title = title;
        dataset = new DefaultCategoryDataset();
        this.media = media;
        this.columnMedia = columnMedia;
        final CategoryDataset dataset = createDataset();
        chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1000, 540));
        setContentPane(chartPanel);
    }

    /**
     * Returns a sample dataset.
     * @return The dataset.
     */
    private CategoryDataset createDataset() {
        
        // row keys...
//        final String serie1 = "First";
        //final String series2 = "Second";
        //final String series3 = "Third";

        // column keys...
//        final String categoria1 = "media";

        // create the dataset...
//        dataset.addValue(1.0, serie1, categoria1);
        return dataset;
    }
    public void addvalues(int value,String category)
    {
    	this.dataset.addValue(value, "First", category);
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final CategoryDataset dataset) {
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            "Istogramma",         // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );


        CategoryPlot cp = (CategoryPlot)chart.getPlot();
        if (!columnMedia)
        {
        	ValueMarker vm = new ValueMarker (media);
	        vm.setLabel("Media");
	        vm.setLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	        vm.setLabelAnchor(RectangleAnchor.TOP_LEFT);
	        vm.setLabelOffset(new RectangleInsets(5,20,5,200));
	//        vm.setLabelOffset(RectangleInsets.ZERO_INSETS);
	//        vm.setLabelOffsetType(LengthAdjustmentType.CONTRACT);
	        vm.setLabelTextAnchor(TextAnchor.BASELINE_CENTER);
	        vm.setPaint(Color.yellow);
	        vm.setStroke(new BasicStroke(2.0f));
	        cp.addRangeMarker(vm);
        }
        
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                
        // disable bar outlines...
        if (columnMedia)
        	plot.setRenderer(new CustomRenderer(Color.BLUE, Color.YELLOW, (int)this.media));
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setDrawBarOutline(false);
       
        final Color c = new Color(0, 0, 255);
        renderer.setSeriesPaint(0, c);
        renderer.setShadowVisible(false);
        
        final CategoryAxis domainAxis = plot.getDomainAxis();

        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 2.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
    
	public JFreeChart getChart()
	{
		return chart;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	
	
	
	class CustomRenderer extends BarRenderer {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/** The colors. */
        private Paint color, hColor;
        private int hColumn;

        /**
         * Creates a new renderer.
         *
         * @param colors  the colors.
         */
        public CustomRenderer(final Paint color, final Paint hColor, int hColumn) {
            this.color = color;
            this.hColor = hColor;
            this.hColumn = hColumn;
        }

        /**
         * Returns the paint for an item.  Overrides the default behaviour inherited from
         * AbstractSeriesRenderer.
         *
         * @param row  the series.
         * @param column  the category.
         *
         * @return The item color.
         */
        public Paint getItemPaint(final int row, final int column) {
        	if (column == hColumn)
        		return this.hColor;
        	else
        		return this.color;
        }
    }
}
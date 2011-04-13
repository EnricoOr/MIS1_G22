package org.mis.sim;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryTick;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.entity.CategoryLabelEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.text.TextBlock;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class Istogramma extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	public final DefaultCategoryDataset dataset;
	public final double media;
	public final boolean columnMedia;
	private final JFreeChart chart;
	private final String title;
	private final String xLabel;
	private final String yLabel;
	public final boolean longCategory;

	/**
	 * Creates a new demo instance.
	 * 
	 * @param title
	 *            the frame title.
	 */
	public Istogramma(final String title, final String xLabel,
			final String yLabel, double media, boolean columnMedia, boolean longCategory) {
		super(title);
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.longCategory = longCategory;
		dataset = new DefaultCategoryDataset();
		this.media = media;
		this.columnMedia = columnMedia;
		final CategoryDataset dataset = createDataset();
		chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(1000, 540));
		chart.removeLegend();
		setContentPane(chartPanel);
	}

	/**
	 * Returns a sample dataset.
	 * 
	 * @return The dataset.
	 */
	private CategoryDataset createDataset() 
	{
		return dataset;
	}

	public void addvalues(int value, String group, String category) {
		this.dataset.addValue(value, group, category);
	}

	/**
	 * Creates a sample chart.
	 * 
	 * @param dataset
	 *            the dataset.
	 * 
	 * @return The chart.
	 */
	private JFreeChart createChart(final CategoryDataset dataset) {
		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart(this.title, // chart
																			// title
				this.xLabel, // domain axis label
				this.yLabel, // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		CategoryPlot cp = (CategoryPlot) chart.getPlot();
		if (!columnMedia) {
			ValueMarker vm = new ValueMarker(media);
			vm.setLabel("Media");
			vm.setLabelFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
			vm.setLabelAnchor(RectangleAnchor.TOP_LEFT);
			vm.setLabelOffset(new RectangleInsets(5, 20, 5, 200));
			vm.setLabelTextAnchor(TextAnchor.BASELINE_CENTER);
			vm.setPaint(Color.yellow);
			vm.setStroke(new BasicStroke(2.0f));
			cp.addRangeMarker(vm);
		}

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		if (longCategory) plot.setDomainAxis(new CustomCategoryAxis(this.xLabel));
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setTickLabelFont(rangeAxis.getTickLabelFont().deriveFont(new Float(16.0)));
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// disable bar outlines...
		if (columnMedia)
			plot.setRenderer(new CustomRenderer(Color.BLUE, Color.YELLOW, (int) this.media));
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setDrawBarOutline(false);

		final Color c = new Color(0, 0, 255);
		renderer.setSeriesPaint(0, c);
		renderer.setShadowVisible(false);

		final CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
		domainAxis.setTickLabelFont(domainAxis.getTickLabelFont().deriveFont(new Float(16.0)));
		domainAxis.setLabelFont(domainAxis.getLabelFont().deriveFont(Font.BOLD));

		return chart;
	}

	/**
	 * Exports a JFreeChart to a SVG file.
	 * 
	 * @param bounds
	 *            the dimensions of the viewport
	 * @throws IOException
	 *             if writing the svgFile fails.
	 */
	public void exportChartAsSVG(int width, int height) {
		try {
			File svgFile = new File(title + ".svg");
			// Get a DOMImplementation and create an XML document
			DOMImplementation domImpl = GenericDOMImplementation
					.getDOMImplementation();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	class CustomRenderer extends BarRenderer 
	{
		private static final long serialVersionUID = 1L;
		/** The colors. */
		private Paint color, hColor;
		private int hColumn;

		/**
		 * Creates a new renderer.
		 * 
		 * @param colors
		 *            the colors.
		 */
		public CustomRenderer(final Paint color, final Paint hColor, int hColumn) {
			this.color = color;
			this.hColor = hColor;
			this.hColumn = hColumn;
		}

		/**
		 * Returns the paint for an item. Overrides the default behaviour
		 * inherited from AbstractSeriesRenderer.
		 * 
		 * @param row
		 *            the series.
		 * @param column
		 *            the category.
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

	

	private class CustomCategoryAxis extends CategoryAxis 
	{
		private static final long serialVersionUID = 1L;
		private CategoryLabelPositions categoryLabelPositions;
		private int categoryLabelPositionOffset; 
		
		private CustomCategoryAxis(String label) {
			super(label);
			this.categoryLabelPositions = CategoryLabelPositions.STANDARD;
			this.categoryLabelPositionOffset = 4;
		}

	
		/**
		 * Draws the category labels and returns the updated axis state.
		 * 
		 * @param g2
		 *            the graphics device (<code>null</code> not permitted).
		 * @param plotArea
		 *            the plot area (<code>null</code> not permitted).
		 * @param dataArea
		 *            the area inside the axes (<code>null</code> not
		 *            permitted).
		 * @param edge
		 *            the axis location (<code>null</code> not permitted).
		 * @param state
		 *            the axis state (<code>null</code> not permitted).
		 * @param plotState
		 *            collects information about the plot (<code>null</code>
		 *            permitted).
		 * 
		 * @return The updated axis state (never <code>null</code>).
		 */
		@SuppressWarnings("unchecked")
		protected AxisState drawCategoryLabels(Graphics2D g2,
				Rectangle2D plotArea, Rectangle2D dataArea, RectangleEdge edge,
				AxisState state, PlotRenderingInfo plotState) {

			if (state == null) {
				throw new IllegalArgumentException("Null 'state' argument.");
			}

			if (isTickLabelsVisible()) {
				List<Tick> ticks = refreshTicks(g2, state, plotArea, edge);
				state.setTicks(ticks);

				int categoryIndex = 0;
				Iterator iterator = ticks.iterator();
				while (iterator.hasNext()) {

					CategoryTick tick = (CategoryTick) iterator.next();
					g2.setFont(getTickLabelFont(tick.getCategory()));
					g2.setPaint(getTickLabelPaint(tick.getCategory()));

					CategoryLabelPosition position = this.categoryLabelPositions.getLabelPosition(edge);
					double x0 = 0.0;
					double x1 = 0.0;
					double y0 = 0.0;
					double y1 = 0.0;
					if (edge == RectangleEdge.TOP) {
						x0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
						x1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
						y1 = state.getCursor() - this.categoryLabelPositionOffset;
						y0 = y1 - state.getMax();
					} else if (edge == RectangleEdge.BOTTOM) {
						x0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
						x1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
						y0 = state.getCursor() + this.categoryLabelPositionOffset;
						y1 = y0 + state.getMax();
					} else if (edge == RectangleEdge.LEFT) {
						y0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
						y1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
						x1 = state.getCursor() - this.categoryLabelPositionOffset;
						x0 = x1 - state.getMax();
					} else if (edge == RectangleEdge.RIGHT) {
						y0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge);
						y1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge);
						x0 = state.getCursor() + this.categoryLabelPositionOffset;
						x1 = x0 - state.getMax();
					}
					Rectangle2D area = new Rectangle2D.Double(x0, y0, (x1 - x0), (y1 - y0));
					Point2D anchorPoint = RectangleAnchor.coordinates(area, position.getCategoryAnchor());
					TextBlock block = tick.getLabel();

					if (Integer.parseInt(tick.getLabel().getLastLine().getFirstTextFragment().getText()) % 5 == 0)
						block.draw(g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(), position.getLabelAnchor(), (float) anchorPoint.getX(), (float) anchorPoint.getY(), position.getAngle());
					
					Shape bounds = block.calculateBounds(g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(), position.getLabelAnchor(), (float) anchorPoint.getX(), (float) anchorPoint.getY(), position.getAngle());
					if (plotState != null && plotState.getOwner() != null) {
						EntityCollection entities = plotState.getOwner().getEntityCollection();
						if (entities != null) {
							String tooltip = getCategoryLabelToolTip(tick.getCategory());
							entities.add(new CategoryLabelEntity(tick.getCategory(), bounds, tooltip, null));
						}
					}
					categoryIndex++;
				}

				if (edge.equals(RectangleEdge.TOP)) {
					double h = state.getMax() + this.categoryLabelPositionOffset;
					state.cursorUp(h);
				} else if (edge.equals(RectangleEdge.BOTTOM)) {
					double h = state.getMax() + this.categoryLabelPositionOffset;
					state.cursorDown(h);
				} else if (edge == RectangleEdge.LEFT) {
					double w = state.getMax() + this.categoryLabelPositionOffset;
					state.cursorLeft(w);
				} else if (edge == RectangleEdge.RIGHT) {
					double w = state.getMax() + this.categoryLabelPositionOffset;
					state.cursorRight(w);
				}
			}
			return state;
		}		
	}
}
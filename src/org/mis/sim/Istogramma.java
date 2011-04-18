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

/**
 * Classe usata per disegnare un istogramma.
 * @author Battista Daniele
 * @author Dell'Anna Luca
 * @author Orsini Enrico
 *
 */
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
	 * Costruttore della classe.
	 * @param title titolo dell'istogramma
	 * @param xLabel titolo dell'asse x
	 * @param yLabel titolo dell'asse y
	 * @param media media da disegnare sull'istogramma
	 * @param columnMedia se true la media evidenzierà una barra dell'istogramma, altrimenti sarà una linea orizzontale
	 * @param longCategory se true scriverà solo una etichetta ogni 5 per l'asse x
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
		chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(1000, 540));
		chart.removeLegend();
		setContentPane(chartPanel);
	}

	/**
	 * Metodo che aggiunge un valore al dataset dell'istogramma
	 * @param value valore da aggiungere al dataset
	 * @param group gruppo al quale aggiungere il valore
	 * @param category categoria alla quale aggiungere il valore
	 */
	public void addvalues(int value, String group, String category) {
		this.dataset.addValue(value, group, category);
	}

	/**
	 * Metodo che crea l'istogramma
	 * @param dataset il dataset da rappresentare
	 * @return restituisce l'oggetto che rappresenta l'istogramma
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
	 * Metodo che esporta l'istogramma in formato SVG
	 * @param width la larghezza dell'istogramma
	 * @param height l'altezza dell'istogramma
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

	
	
	
	
	
	/**
	 * Classe che implementa un renderer modificato per le barre dell'istogramma. Usata per poter evidenziare una barra dell'istogramma.
	 * @author Battista Daniele
	 * @author Dell'Anna Luca
	 * @author Orsini Enrico
	 */
	class CustomRenderer extends BarRenderer 
	{
		private static final long serialVersionUID = 1L;
		private Paint color, hColor;
		private int hColumn;

		/**
		 * Costruttore per la classe.
		 * @param color colore per la barra normale
		 * @param hColor colore per la barra evidenziata
		 * @param hColumn indice della barra da evidenziare
		 */
		public CustomRenderer(final Paint color, final Paint hColor, int hColumn) {
			this.color = color;
			this.hColor = hColor;
			this.hColumn = hColumn;
		}

		/**
		 * Metodo che restituisce l'oggetto Paint per ogni barra: se la barra è quella da evidenziare, restituisce il colore opportuno.
		 */
		public Paint getItemPaint(final int row, final int column) {
			if (column == hColumn)
				return this.hColor;
			else
				return this.color;
		}
	}

	

	/**
	 * Classe che implementa un renderer modificato per le etichette dell'asse x. Usata per consentire di disegnare una etichetta su 5 in caso di un asse x molto denso.
	 * @author Battista Daniele
	 * @author Dell'Anna Luca
	 * @author Orsini Enrico
	 */
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
		 * Metodo che disegna le etichette dell'asse x e restituisce lo stato aggiornato dell'asse.
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
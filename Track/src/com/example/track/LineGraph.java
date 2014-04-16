package com.example.track;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

public class LineGraph {

	private GraphicalView view;
	
	private TimeSeries dataset1 = new TimeSeries(""); 
	//private TimeSeries dataset2 = new TimeSeries(""); 
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	
	private XYSeriesRenderer renderer1 = new XYSeriesRenderer(); // This will be used to customize line 1
	//private XYSeriesRenderer renderer2 = new XYSeriesRenderer();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
	
	public LineGraph()
	{
		// Add single dataset to multiple dataset
		mDataset.addSeries(dataset1);
		//mDataset.addSeries(dataset2);
		
		// Customization time for line 1!
		renderer1.setColor(Color.green(102));
		renderer1.setPointStyle(PointStyle.SQUARE);
		renderer1.setFillPoints(true);
		
//		renderer2.setColor(Color.RED);
//		renderer2.setPointStyle(PointStyle.SQUARE);
//		renderer2.setFillPoints(true);

		// Enable Zoom
		//mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Time");
		mRenderer.setYTitle("Angle");
		mRenderer.setYAxisMin(0);
		mRenderer.setYAxisMax(999);
		mRenderer.setPointSize(0);
		
		// Add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer1);
		//mRenderer.addSeriesRenderer(renderer2);
	}
	
	public GraphicalView getView(Context context) 
	{
		view =  ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	
	public void addNewPoints(Point p)
	{
		dataset1.add(p.getX(), p.getY());
		if(p.getX() > 1000){
			mRenderer.setXAxisMin(p.getX() - 1000);
		}
	}
	
	public void addNewPoints2(Point p){
		dataset1.add(p.getX(), p.getY());
	}
	
//	public void addNewPointsR(Point p)
//	{
//		dataset2.add(p.getX(), p.getY());
//		if(p.getX() > 1000){
//			mRenderer.setXAxisMin(p.getX() - 1000);
//		}
//	}
//	
	
	public void setColor(int color){
//		renderer1.setGradientEnabled(true);
//		renderer1.setGradientStart(100,color);
//		renderer1.setGradientStop(800,Color.RED);
		renderer1.setColor(color);
	}
	
	public void titleGraph(String title){
		mRenderer.setChartTitle(title);
}
}


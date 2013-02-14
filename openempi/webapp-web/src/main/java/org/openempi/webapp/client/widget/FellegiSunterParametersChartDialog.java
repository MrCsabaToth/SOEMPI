/**
 * 
 *  Copyright (C) 2013 Vanderbilt University <csaba.toth, b.malin @vanderbilt.edu>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.openempi.webapp.client.widget;

import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.FellegiSunterParametersWeb;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.ToolTip;
import com.extjs.gxt.charts.client.model.ToolTip.MouseStyle;
import com.extjs.gxt.charts.client.model.axis.XAxis;
import com.extjs.gxt.charts.client.model.axis.YAxis;
import com.extjs.gxt.charts.client.model.charts.BarChart;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.charts.client.model.charts.BarChart.Bar;
import com.extjs.gxt.charts.client.model.charts.dots.Dot;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class FellegiSunterParametersChartDialog extends Dialog {
	public FellegiSunterParametersChartDialog(FellegiSunterParametersWeb fellegiSunterParameters) {
		setButtons(Dialog.CLOSE);
		setHideOnButtonClick(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setIcon(IconHelper.create("images/folder_go.png"));
		setHeading("EM Results");
		setModal(true);
		setBlinkModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setSize(1000, 500);
		setResizable(false);
		setMaximizable(true);
		setLayout(new FitLayout());

		ContentPanel panel = new ContentPanel();
		panel.setLayout(new BorderLayout());
		panel.setHeading("Thresholds");
		FormPanel form = new FormPanel();
		Text formText = new Text("Upper threshold: " + fellegiSunterParameters.getUpperBound() + ", " +
				"lower threshold: " + fellegiSunterParameters.getLowerBound());
		form.add(formText);
		form.setHeaderVisible(false);
		BorderLayoutData lay = new BorderLayoutData(LayoutRegion.NORTH, 30);
		panel.add(form, lay);

		LayoutContainer chartLayoutContainer = getChart(fellegiSunterParameters);
		BorderLayoutData chartLayout = new BorderLayoutData(LayoutRegion.CENTER);
		panel.add(chartLayoutContainer, chartLayout);

		add(panel);
	}

	private LayoutContainer getChart(FellegiSunterParametersWeb fellegiSunterParameters) {
		Chart chart = new Chart(Constants.CHART_URL_POSTFIX);
		chart.setChartModel(getLineChartModel(fellegiSunterParameters));
		FieldSet fs = new FieldSet();
		fs.setHeading("M-U Value Chart");
		fs.setLayout(new FitLayout());
		fs.add(chart, new FitData(0, 0, 20, 0));
		fs.setCollapsible(false);
		fs.getElement().getStyle().setProperty("border-color", "#99bbe8"); //.setBorderColor("#99bbe8");
		return fs;
	}

	public void addBoundMarkers(boolean lowerOrUpper, double boundValue, double minScore, LineChart lineChart, BarChart barChart) {
		Dot dot = new Dot(boundValue);
		String boundText = (lowerOrUpper ? "Lower" : "Upper");
		dot.setTooltip(boundText + " bound: " + boundValue);
		dot.setColour("00ff00");
		dot.setSize(5);
		lineChart.addDots(dot);

		Bar lowerBar = new Bar(boundValue, minScore);
		lowerBar.setTooltip(boundText + " Bound");
		barChart.addBars(lowerBar);
	}
	
	public ChartModel getLineChartModel(FellegiSunterParametersWeb fellegiSunterParameters) {
		// Create a ChartModel with the Chart Title and some style attributes
		ChartModel cm = new ChartModel("Weights", "font-size: 14px; font-family: Verdana; text-align: center;");
		cm.setBackgroundColour("ffdef5");

		XAxis xa = new XAxis();
		// set the maximum, minimum and the step value for the X axis
		xa.setColour("c799e8");
		YAxis ya = new YAxis();
		ya.setGridColour("81aeea");
		// Add the labels to the Y axis
		ya.setRange(0.0, 1.0, 0.05);
		cm.setXAxis(xa);
		LineChart mLineChart = new LineChart();
		Integer index = 0;
		for (Double mValue : fellegiSunterParameters.getMValues()) {
			xa.addLabels(index.toString());
			Dot dot = new Dot(mValue);
			dot.setTooltip(index + ".: " + mValue);
			dot.setSize(3);
			mLineChart.addDots(dot);
			index++;
		}
		LineChart uLineChart = new LineChart();
		index = 0;
		for (Double uValue : fellegiSunterParameters.getUValues()) {
			xa.addLabels(index.toString());
			Dot dot = new Dot(uValue);
			dot.setTooltip(index + ".: " + uValue);
			dot.setSize(3);
			uLineChart.addDots(dot);
			index++;
		}
		xa.setOffset(true);
		cm.setYAxis(ya);

		mLineChart.setColour("e73585");
		cm.addChartConfig(mLineChart);
		uLineChart.setColour("11ee11");
		cm.addChartConfig(uLineChart);
		cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));
		return cm;
	}

}

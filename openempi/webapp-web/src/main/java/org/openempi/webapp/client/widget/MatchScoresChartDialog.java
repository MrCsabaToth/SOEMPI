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

import java.util.List;

import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PersonLinkWeb;

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

public class MatchScoresChartDialog extends Dialog {
	public MatchScoresChartDialog(List<PersonLinkWeb> personLinks, Double lowerBound, Double upperBound) {
		setButtons(Dialog.CLOSE);
		setHideOnButtonClick(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setIcon(IconHelper.create("images/folder_go.png"));
		setHeading("Match Scores");
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
		panel.setHeading("Match Scores");
		FormPanel form = new FormPanel();
		Text formText = new Text("Upper threshold: " + lowerBound + ", " +
								"lower threshold: " + upperBound);
		form.add(formText);
		form.setHeaderVisible(false);
		BorderLayoutData lay = new BorderLayoutData(LayoutRegion.NORTH, 30);
		panel.add(form, lay);

		BorderLayoutData chartLayout = new BorderLayoutData(LayoutRegion.CENTER);
		LayoutContainer chartLayoutContainer = getChart(personLinks, lowerBound, upperBound);
		panel.add(chartLayoutContainer, chartLayout);

		add(panel);
	}

	private LayoutContainer getChart(List<PersonLinkWeb> personLinks, Double lowerBound, Double upperBound) {
		Chart chart = new Chart(Constants.CHART_URL_POSTFIX);
		chart.setChartModel(getLineChartModel(personLinks, lowerBound, upperBound));
		FieldSet fs = new FieldSet();
		fs.setHeading("Match Score Chart");
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
	
	public ChartModel getLineChartModel(List<PersonLinkWeb> personLinks, Double lowerBound, Double upperBound) {
		// Create a ChartModel with the Chart Title and some style attributes
		ChartModel cm = new ChartModel("Weights", "font-size: 14px; font-family: Verdana; text-align: center;");
		cm.setBackgroundColour("ffdef5");

		XAxis xa = new XAxis();
		// set the maximum, minimum and the step value for the X axis
		xa.setColour("c799e8");
		YAxis ya = new YAxis();
		ya.setGridColour("81aeea");
		// Add the labels to the Y axis
		double minScore = 0.0;
		double maxScore = 0.0;
		int maximumPointsOnGraph = 30;
		int stride = personLinks.size() / maximumPointsOnGraph + 1;
		if (personLinks != null && personLinks.size() > 0) {
			minScore = personLinks.get(0).getWeight();
			maxScore = personLinks.get(personLinks.size() - 1).getWeight();
			int interval = (int) ((maxScore - minScore) / 10);
			ya.setRange(minScore, maxScore, interval);
		} else {
			ya.setRange(-50, 50, 10);
		}
		cm.setXAxis(xa);
		LineChart lineChart = new LineChart();
		BarChart barChart = new BarChart();
		if (lowerBound < minScore)
			addBoundMarkers(true, lowerBound, minScore, lineChart, barChart);
		if (upperBound < minScore)
			addBoundMarkers(false, upperBound, minScore, lineChart, barChart);
		Integer index = 0;
		for (PersonLinkWeb personLink : personLinks) {
			Double weight = personLink.getWeight();
			if (index % stride == 0) {
				xa.addLabels(index.toString());
				Dot dot = new Dot(weight);
				StringBuilder tooltip = new StringBuilder(index + ".: " + weight + " (" +
						personLink.getLeftPersonId());
				tooltip.append("; " + personLink.getRightPersonId());
				tooltip.append(")");
				dot.setTooltip(tooltip.toString());
				dot.setSize(3);
				lineChart.addDots(dot);

				Bar dummyBar = new Bar(minScore, minScore);
				barChart.addBars(dummyBar);
			}
			if (index < personLinks.size() - 1) {
				double nextWeight = personLinks.get(index + 1).getWeight();
				if (lowerBound >= weight && lowerBound < nextWeight)
					addBoundMarkers(true, lowerBound, minScore, lineChart, barChart);
				if (upperBound >= weight && upperBound < nextWeight)
					addBoundMarkers(false, upperBound, minScore, lineChart, barChart);
			}
			index++;
		}
		if (lowerBound >= maxScore)
			addBoundMarkers(true, lowerBound, minScore, lineChart, barChart);
		if (upperBound >= maxScore)
			addBoundMarkers(false, upperBound, minScore, lineChart, barChart);
		xa.setOffset(true);
		cm.setYAxis(ya);

		lineChart.setColour("e73585");
		cm.addChartConfig(lineChart);
		barChart.setColour("00ff00");
		cm.addChartConfig(barChart);
		cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));
		return cm;
	}

}

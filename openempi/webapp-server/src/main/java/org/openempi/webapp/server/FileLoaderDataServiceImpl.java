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
package org.openempi.webapp.server;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.openempi.webapp.client.FileLoaderDataService;
import org.openempi.webapp.client.model.LoaderConfigWeb;
import org.openempi.webapp.client.model.LoaderDataFieldWeb;
import org.openempi.webapp.client.model.LoaderFieldCompositionWeb;
import org.openempi.webapp.client.model.LoaderSubFieldWeb;
import org.openempi.webapp.client.model.LoaderTargetFieldWeb;
import org.openempi.webapp.server.util.ModelTransformer;
import org.openhie.openempi.configuration.Component;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ConfigurationLoader;
import org.openhie.openempi.configuration.ConfigurationRegistry;
import org.openhie.openempi.configuration.Component.ComponentType;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.loader.configuration.LoaderConfig;
import org.openhie.openempi.loader.configuration.LoaderDataField;
import org.openhie.openempi.loader.configuration.LoaderFieldComposition;
import org.openhie.openempi.loader.configuration.LoaderSubField;
import org.openhie.openempi.loader.configuration.LoaderTargetField;
import org.springframework.context.ApplicationContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FileLoaderDataServiceImpl extends RemoteServiceServlet implements FileLoaderDataService
{
	private static final long serialVersionUID = -2074260236615572867L;

	private Logger log = Logger.getLogger(getClass());
	private ApplicationContext context;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		context = (ApplicationContext) config.getServletContext().getAttribute(WebappConstants.APPLICATION_CONTEXT);
	}
	
	public LoaderConfigWeb loadFileLoaderConfigurationData() {
		log.debug("Received request to load the file loader configuration data.");
		try {
			Configuration configuration = Context.getConfiguration();
			LoaderConfig loaderConfiguration = (LoaderConfig)
				configuration.lookupConfigurationEntry(ConfigurationRegistry.DATA_LOADER_CONFIGURATION);
			return convertToClientModel(loaderConfiguration);
		} catch (Throwable t) {
			log.error("Failed to execute: " + t.getMessage(), t);
			throw new RuntimeException(t);
		}
	}

	private LoaderConfigWeb convertToClientModel(LoaderConfig loaderConfiguration) {
		LoaderConfigWeb loaderConfigWeb = new LoaderConfigWeb();
		loaderConfigWeb.setDelimiterRegex(loaderConfiguration.getDelimiterRegex());
		loaderConfigWeb.setHeaderLinePresent(loaderConfiguration.getHeaderLinePresent());
		List<LoaderDataFieldWeb> loaderDataFieldsWeb =
			new java.util.ArrayList<LoaderDataFieldWeb>(loaderConfiguration.getDataFields().size());
		for (LoaderDataField loaderDataField : loaderConfiguration.getDataFields()) {
			LoaderDataFieldWeb loaderDataFieldWeb = new LoaderDataFieldWeb(
					loaderDataField.getSourceColumnIndex(),
					loaderDataField.getSourceFieldName(),
					loaderDataField.getFieldName());
			convertToClientModel(loaderDataField, loaderDataFieldWeb);
			if (loaderDataField.getFieldComposition() != null) {
				LoaderFieldCompositionWeb loaderFieldCompositionWeb = new LoaderFieldCompositionWeb(
						loaderDataField.getFieldComposition().getIndex(),
						loaderDataField.getFieldComposition().getSeparator());
				convertToClientModel(loaderDataField.getFieldComposition(), loaderFieldCompositionWeb);
				loaderDataFieldWeb.setLoaderFieldComposition(loaderFieldCompositionWeb);
			}
			if (loaderDataField.getSubFields() != null) {
				List<LoaderSubFieldWeb> loaderSubFieldsWeb =
					new java.util.ArrayList<LoaderSubFieldWeb>(loaderDataField.getSubFields().size());
				for (LoaderSubField loaderSubField : loaderDataField.getSubFields()) {
					LoaderSubFieldWeb loaderSubFieldWeb = new LoaderSubFieldWeb(
							loaderSubField.getFieldName(),
							loaderSubField.getBeginIndex(),
							loaderSubField.getEndIndex());
					convertToClientModel(loaderSubField, loaderSubFieldWeb);
					loaderSubFieldWeb.updateRedunantFields();
					loaderSubFieldsWeb.add(loaderSubFieldWeb);
				}
				loaderDataFieldWeb.setLoaderSubFields(loaderSubFieldsWeb);
			}
			loaderDataFieldWeb.updateRedunantFields();
			loaderDataFieldsWeb.add(loaderDataFieldWeb);
		}
		loaderConfigWeb.setLoaderDataFields(loaderDataFieldsWeb);
		return loaderConfigWeb;
	}

	private void convertToClientModel(LoaderTargetField loaderTargetField, LoaderTargetFieldWeb loaderTargetFieldWeb) {
		loaderTargetFieldWeb.setFieldName(loaderTargetField.getFieldName());
		loaderTargetFieldWeb.setFieldTypeModifier(loaderTargetField.getFieldTypeModifier());
		loaderTargetFieldWeb.setFieldType(ModelTransformer.convertToClientModel(loaderTargetField.getFieldType()));
		if (loaderTargetField.getFieldTransformation() != null)
			loaderTargetFieldWeb.setFieldTransformation(ModelTransformer.convertToClientModel(loaderTargetField.getFieldTransformation()));
	}

	public String saveFileLoaderConfigurationData(LoaderConfigWeb loaderConfigWeb) {
		Configuration configuration = Context.getConfiguration();
		String returnMessage = "";
		try {
			Component component = configuration.lookupExtensionComponentByComponentType(ComponentType.DATALOADER);
			String loaderBeanName = configuration.getExtensionBeanNameFromComponent(component);
			ConfigurationLoader loader = (ConfigurationLoader) context.getBean(loaderBeanName);
			LoaderConfig loaderConfiguration = convertFromClientModel(loaderConfigWeb);
			loader.saveAndRegisterComponentConfiguration(configuration, loaderConfiguration);
		} catch (Exception e) {
			log.warn("Failed while saving the file loader configuration: " + e, e);
			returnMessage = e.getMessage();
		}
		return returnMessage;
	}

	private LoaderConfig convertFromClientModel(LoaderConfigWeb loaderConfigWeb) {
		LoaderConfig loaderConfiguration = new LoaderConfig();
		loaderConfiguration.setDelimiterRegex(loaderConfigWeb.getDelimiterRegex());
		loaderConfiguration.setHeaderLinePresent(loaderConfigWeb.getHeaderLinePresent());
		List<LoaderDataField> loaderDataFields =
			new java.util.ArrayList<LoaderDataField>(loaderConfigWeb.getLoaderDataFields().size());
		for (LoaderDataFieldWeb loaderDataFieldWeb : loaderConfigWeb.getLoaderDataFields()) {
			LoaderDataField loaderDataField = new LoaderDataField();
			loaderDataField.setSourceColumnIndex(loaderDataFieldWeb.getSourceColumnIndex());
			loaderDataField.setSourceFieldName(loaderDataFieldWeb.getSourceFieldName());
			convertFromClientModel(loaderDataFieldWeb, loaderDataField);
			if (loaderDataFieldWeb.getLoaderFieldComposition() != null) {
				LoaderFieldComposition loaderFieldComposition = new LoaderFieldComposition();
				LoaderFieldCompositionWeb loaderFieldCompositionWeb = loaderDataFieldWeb.getLoaderFieldComposition();
				loaderFieldComposition.setIndex(loaderFieldCompositionWeb.getIndex());
				loaderFieldComposition.setSeparator(loaderFieldCompositionWeb.getSeparator());
				convertFromClientModel(loaderFieldCompositionWeb, loaderFieldComposition);
				loaderDataField.setFieldComposition(loaderFieldComposition);
			}
			if (loaderDataFieldWeb.getLoaderSubFields() != null) {
				List<LoaderSubField> loaderSubFields =
					new java.util.ArrayList<LoaderSubField>(loaderDataFieldWeb.getLoaderSubFields().size());
				for (LoaderSubFieldWeb loaderSubFieldWeb : loaderDataFieldWeb.getLoaderSubFields()) {
					LoaderSubField loaderSubField = new LoaderSubField();
					convertFromClientModel(loaderSubFieldWeb, loaderSubField);
					loaderSubField.setBeginIndex(loaderSubFieldWeb.getBeginIndex());
					loaderSubField.setEndIndex(loaderSubFieldWeb.getEndIndex());
					loaderSubFields.add(loaderSubField);
				}
				loaderDataField.setSubFields(loaderSubFields);
			}
			loaderDataFields.add(loaderDataField);
		}
		loaderConfiguration.setDataFields(loaderDataFields);
		return loaderConfiguration;
	}

	private void convertFromClientModel(LoaderTargetFieldWeb loaderTargetFieldWeb, LoaderTargetField loaderTargetField) {
		loaderTargetField.setFieldName(loaderTargetFieldWeb.getFieldName());
		loaderTargetField.setFieldType(ModelTransformer.convertFromClientModel(loaderTargetFieldWeb.getFieldType()));
		loaderTargetField.setFieldTypeModifier(loaderTargetFieldWeb.getFieldTypeModifier());
		loaderTargetField.setFieldMeaning(ModelTransformer.convertFromClientModel(loaderTargetFieldWeb.getFieldMeaning()));
		if (loaderTargetFieldWeb.getFieldTransformation() != null)
			loaderTargetField.setFieldTransformation(ModelTransformer.convertFromClientModel(loaderTargetFieldWeb.getFieldTransformation()));
	}

}

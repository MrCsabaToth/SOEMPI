/**
 *
 *  Copyright (C) 2010 SYSNET International, Inc. <support@sysnetint.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.openempi.webapp.client.mvc;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.AdminConfigurationWeb;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;

public class MenuToolbarView extends View
{	
	private LayoutContainer container;
	private MenuBar menuBar;
	private ToolBar toolBar;
	
	public MenuToolbarView(Controller controller) {
		super(controller);
	}

	private void initUI() {
		container = new LayoutContainer();
		RowLayout rowLayout = new RowLayout(Orientation.VERTICAL);
		container.setLayout(rowLayout);

		AdminConfigurationWeb adminConfiguration = (AdminConfigurationWeb) Registry.get(Constants.ADMIN_CONFIGURATION);
		
		MenuBarItem search = null;
		if (adminConfiguration.getExperimentalMode()) {
			Menu searchSubmenu = new Menu();
			searchSubmenu.add(new MenuItem("Basic Search", IconHelper.create("images/search_icon_16x16.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.BasicSearchView);
				}
			}));
			searchSubmenu.add(new MenuItem("Advanced Search", IconHelper.create("images/search_adv_16x16.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.AdvancedSearchView);
				}
			}));
			searchSubmenu.add(new MenuItem("Real Search", IconHelper.create("images/search_real_16x16.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.RealSearchView);
				}
			}));
			search = new MenuBarItem("Search", searchSubmenu);
		}

		MenuBarItem files = null;
		if (adminConfiguration.getComponentMode() != AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			Menu datasetList = new Menu();
			datasetList.add(new MenuItem("Dataset List", IconHelper.create("images/folder_explore.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.DatasetListView);
				}
			}));
			files = new MenuBarItem("Dataset", datasetList);
		}

		MenuBarItem config = null;
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			Menu configurationSubMenu = new Menu();
			configurationSubMenu.add(new MenuItem("File Loader", IconHelper.create("images/table_gear.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.FileLoaderConfigurationView);
				}
			}));
			configurationSubMenu.add(new MenuItem("Component Settings", IconHelper.create("images/wrench.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.ComponentSettingsView);
				}
			}));
			configurationSubMenu.add(new MenuItem("Bloomfilter Settings", IconHelper.create("images/wrench_orange.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.BloomfilterSettingsView);
				}
			}));
			configurationSubMenu.add(new MenuItem("Blocking Configuration", IconHelper.create("images/bricks.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.BlockingConfigurationView);
				}
			}));
			configurationSubMenu.add(new MenuItem("Privacy Preserving Blocking Configuration", IconHelper.create("images/bricks.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.PrivacyPreservingBlockingConfigurationView);
				}
			}));
			configurationSubMenu.add(new MenuItem("Match Field Configuration", IconHelper.create("images/wrench_orange.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.MatchFieldConfigurationView);
				}
			}));
			configurationSubMenu.add(new MenuItem("Configure Match Parameters", IconHelper.create("images/wrench_orange.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.MatchConfigurationParametersView);
				}
			}));
			config = new MenuBarItem("Configuration", configurationSubMenu);
		}
		
		MenuBarItem match = null;
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			Menu matchSubMenu = new Menu();
			matchSubMenu.add(new MenuItem("Perform Match", IconHelper.create("images/link_16x16.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.MatchView);
				}
			}));
			match = new MenuBarItem("Match", matchSubMenu);
		}

		MenuBarItem admin = null;
		if (adminConfiguration.getExperimentalMode()) {
			Menu adminSubmenu = new Menu();

			adminSubmenu.add(new MenuItem("Add Person", IconHelper.create("images/add.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.AddPersonView);
				}
			}));
			admin = new MenuBarItem("Admin", adminSubmenu);
		}
		
		MenuBarItem wizard = null;
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			Menu wizardSubmenu = new Menu();
			if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
				adminConfiguration.getExperimentalMode())
			{
				wizardSubmenu.add(new MenuItem("File Import Wizard", IconHelper.create("images/wand.png"), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						Dispatcher.get().dispatch(AppEvents.FileImportWizardSelected);
					}
				}));
			}
			String wizardButtonTitle = "Record Linkage Wizard";
			if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE)
				wizardButtonTitle = "Privacy Preserving " + wizardButtonTitle;
			wizardSubmenu.add(new MenuItem(wizardButtonTitle, IconHelper.create("images/wand.png"), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					Dispatcher.get().dispatch(AppEvents.RecordLinkageWizardSelected);
				}
			}));
			wizard = new MenuBarItem("Wizard", wizardSubmenu);
		}

		MenuBarItem management = null;
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
		    Menu managementSubmenu = new Menu();
		    managementSubmenu.add(new MenuItem("Key Management", new SelectionListener<MenuEvent>() {
		    	@Override
		    	public void componentSelected(MenuEvent ce) {
		    		Dispatcher.get().dispatch(AppEvents.KeyManagementView);
		    	}
		    }));
		    managementSubmenu.add(new MenuItem("Salt Management", new SelectionListener<MenuEvent>() {
		    	@Override
		    	public void componentSelected(MenuEvent ce) {
		    		Dispatcher.get().dispatch(AppEvents.SaltManagementView);
		    	}
		    }));
		    management = new MenuBarItem("Management", managementSubmenu);
		}

		menuBar = new MenuBar();
		menuBar.setBorders(true);
//		menuBar.setStyleAttribute("borderTop", "none");
		if (files != null)
			menuBar.add(files);
		if (config != null)
			menuBar.add(config);
		if (match != null)
			menuBar.add(match);
		if (adminConfiguration.getExperimentalMode()) {
			if (search != null)
				menuBar.add(search);
			if (admin != null)
				menuBar.add(admin);
		}
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			if (management != null)
				menuBar.add(management);
		}
		if (wizard != null)
			menuBar.add(wizard);
		
		container.add(menuBar); // , new MarginData(5,0,0,0));
		
		toolBar = new ToolBar();
		toolBar.setBorders(true);
		toolBar.add(new SeparatorToolItem());
		Button button = null;
		button = new Button();
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			button.setToolTip("List Datasets");
			button.setIcon(IconHelper.create("images/folder_explore.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched file list view event.", null);
					Dispatcher.get().dispatch(AppEvents.DatasetListView);
				}
			});
			toolBar.add(button);
		}

		if (adminConfiguration.getExperimentalMode()) {
			toolBar.add(new SeparatorToolItem());
			button = new Button();
			button.setToolTip("Basic Search");
			button.setIcon(IconHelper.create("images/search_icon_16x16.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched basic search view event.", null);
					Dispatcher.get().dispatch(AppEvents.BasicSearchView);
				}
			});
			toolBar.add(button);
	
			button = new Button();
			button.setToolTip("Real Search");
			button.setIcon(IconHelper.create("images/search_real_16x16.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched real search view event.", null);
					Dispatcher.get().dispatch(AppEvents.RealSearchView);
				}
			});
			toolBar.add(button);
	
			button = new Button();
			button.setToolTip("Advanced Search");
			button.setIcon(IconHelper.create("images/search_adv_16x16.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					Dispatcher.get().dispatch(AppEvents.AdvancedSearchView);
				}
			});
			toolBar.add(button);
		}
		
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			toolBar.add(new SeparatorToolItem());
	
			button = new Button();
			button.setToolTip("Configure Blocking");
			button.setIcon(IconHelper.create("images/bricks.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched configure blocking view event.", null);
					Dispatcher.get().dispatch(AppEvents.BlockingConfigurationView);
				}
			});
			toolBar.add(button);
			
			button = new Button();
			button.setToolTip("Configure Match Fields");
			button.setIcon(IconHelper.create("images/wrench_orange.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched configure match field view event.", null);
					Dispatcher.get().dispatch(AppEvents.MatchFieldConfigurationView);
				}
			});
			toolBar.add(button);
			
			button = new Button();
			button.setToolTip("Configure Match Parameters");
			button.setIcon(IconHelper.create("images/wrench_orange.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched configure match parameters view event.", null);
					Dispatcher.get().dispatch(AppEvents.MatchConfigurationParametersView);
				}
			});
			toolBar.add(button);
			
			button = new Button();
			button.setToolTip("Perform Match");
			button.setIcon(IconHelper.create("images/link_16x16.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched probabilistic match view event.", null);
					Dispatcher.get().dispatch(AppEvents.MatchView);
				}
			});
			toolBar.add(button);
		}

		toolBar.add(new SeparatorToolItem());

		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			button = new Button();
			button.setToolTip("File Import Wizard");
			button.setIcon(IconHelper.create("images/wand.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched configure blocking view event.", null);
					Dispatcher.get().dispatch(AppEvents.FileImportWizardSelected);
				}
			});
			toolBar.add(button);
		}
		
		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_PROVIDER_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.DATA_INTEGRATOR_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			button = new Button();
			button.setToolTip("Record Linkage Wizard");
			button.setIcon(IconHelper.create("images/wand.png"));
			button.addSelectionListener( new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					GWT.log("Dispatched configure match view event.", null);
					Dispatcher.get().dispatch(AppEvents.RecordLinkageWizardSelected);
				}
			});
			toolBar.add(button);
		}

		if (adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.PARAMETER_MANAGER_MODE ||
			adminConfiguration.getComponentMode() == AdminConfigurationWeb.ComponentTypeWeb.KEYSERVER_MODE ||
			adminConfiguration.getExperimentalMode())
		{
			toolBar.add(new SeparatorToolItem());

		    button = new Button();
		    button.setToolTip("Key Management");
		    button.setIcon(IconHelper.create("images/server_key.png"));
		    button.addSelectionListener( new SelectionListener<ButtonEvent>() {
		          @Override
		          public void componentSelected(ButtonEvent ce) {
		        	  GWT.log("Key management view event.", null);
		        	  Dispatcher.get().dispatch(AppEvents.KeyManagementView);
		          }
		    });
		    toolBar.add(button);

		    button = new Button();
		    button.setToolTip("Salt Management");
		    button.setIcon(IconHelper.create("images/server_database.png"));
		    button.addSelectionListener( new SelectionListener<ButtonEvent>() {
		          @Override
		          public void componentSelected(ButtonEvent ce) {
		        	  GWT.log("Salt Management view event.", null);
		        	  Dispatcher.get().dispatch(AppEvents.SaltManagementView);
		          }
		    });
		    toolBar.add(button);
		}
		
//		button.setIcon(Examples.ICONS.menu_show());
		toolBar.add(new FillToolItem());
		toolBar.add(new Button("Logout"));
		
		container.add(toolBar);
		
		LayoutContainer north = Registry.get(Constants.NORTH_PANEL);
		north.add(container);
		north.layout();
//
//		Viewport viewport = Registry.get(AppView.VIEWPORT);
//		
//		BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 52);
//		data.setMargins(new Margins());
//		
//		viewport.add(container, data);
//		viewport.layout();
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == AppEvents.Init) {
			initUI();
		} else if (event.getType() == AppEvents.FileImportWizardSelected ||
				event.getType() == AppEvents.RecordLinkageWizardSelected)
		{
			menuBar.disable();
			toolBar.disable();
		} else if (event.getType() == AppEvents.ExitWizardSelected ||
				event.getType() == AppEvents.WizardEnded)
		{
			menuBar.enable();
			toolBar.enable();
		}
	}
}

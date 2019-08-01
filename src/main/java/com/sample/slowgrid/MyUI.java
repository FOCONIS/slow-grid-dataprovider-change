package com.sample.slowgrid;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point.
 */
@Theme("mytheme")
public class MyUI extends UI implements PollListener {
	private static final long serialVersionUID = 1L;

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = true)
	public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}

	private static ListDataProvider<GridEntry> emptyGridDataProvider = new ListDataProvider<GridEntry>(Collections.emptyList());
	private ListDataProvider<GridEntry> fullGridDataProvider = new ListDataProvider<GridEntry>(createCollection(200));
	// TODO - slow DataProvider that simulates a slower database backend
	// private ListDataProvider<GridEntry> slowDataProvider = new SlowDataProvider<GridEntry>(createCollection(200));
	
	private static final String vaadinVersion = com.vaadin.shared.Version.getFullVersion();

	private CssLayout gridwrapper;
	private Grid<GridEntry> grid;
	private boolean loadOnPoll;		// use one short poll to force replacing of the DataProvider which might be slow

	@Override
	protected void init(final VaadinRequest vaadinRequest) {

		final VerticalLayout vertLayout = new VerticalLayout();
		vertLayout.setSizeFull();

		addPollListener(this);		// register this UI with the poll listener

		// Adding Vaadin version label
		Label versionLabel = new Label("Grid Test with Vaadin Version: " + vaadinVersion);
		versionLabel.setId("vaadinVersionLabel");
		vertLayout.addComponent(versionLabel);

		Label descriptionLabel = new Label("Default grid loading with deferred DataProvider is the fastest option");
		descriptionLabel.setId("vaadinVersionLabel");
		vertLayout.addComponent(descriptionLabel);

		VerticalLayout vertLayoutInner = new VerticalLayout();

		HorizontalLayout horLayout = new HorizontalLayout();
		vertLayout.addComponent(horLayout);

		horLayout.addComponent(vertLayoutInner);
		horLayout.setComponentAlignment(vertLayoutInner, Alignment.TOP_LEFT);

		// BTN0
		final Button buttonButt = new Button("clear UI");
		buttonButt.setId("buttonButt");
		buttonButt.addClickListener(event -> gridwrapper.removeAllComponents());
		horLayout.addComponent(buttonButt);

		// BTN1
		final Button button1 = new Button("build empty Grid");
		button1.setId("btnGridEmpty");
		button1.addClickListener(event -> buildGrid(emptyGridDataProvider));
		horLayout.addComponent(button1);

		// BTN2
		final Button button2 = new Button("build full Grid");
		button2.setId("btnGridFull");
		button2.addClickListener(event -> buildGrid(fullGridDataProvider));
		horLayout.addComponent(button2);

		// BTN3
		final Button button3 = new Button("build empty Grid deferred to full Grid");
		button3.setId("btnGridEmptyDeferFull");
		button3.addClickListener(event -> {
			buildGrid(emptyGridDataProvider);
			loadOnPoll = true;
			setPollInterval(1); // 1ms UI poll (as fast as possible)
		});
		horLayout.addComponent(button3);

		gridwrapper = new CssLayout();
		gridwrapper.setSizeFull();

		vertLayout.addComponent(gridwrapper);
		vertLayout.setExpandRatio(gridwrapper, 1);
		vertLayout.setMargin(true);
		vertLayout.setSpacing(true);

		setContent(vertLayout);

		getPage().setTitle("Vaadin Grid Loading Performance Comparison");
	}

	/**
	 * If there is a poll event switch to the fullGridDataProvider and disable polling again.
	 */
	@Override
	public void poll(PollEvent event) {
		if (loadOnPoll) {
			grid.setDataProvider(fullGridDataProvider);
			setPollInterval(0);
		}
	}

	/**
	 * Builds a default grid with 25 visible fixed-width columns and 5 hidden columns.
	 */
	private void buildGrid(DataProvider<GridEntry, SerializablePredicate<GridEntry>> gridDP) {

		gridwrapper.removeAllComponents();

		int columns = 25;
		int hiddenColumns = 5;
		
		grid = new Grid<>("My Test Grid", gridDP);

		for (int i = 0; i < columns; i++) {
			Column<GridEntry, String> col = grid.addColumn(GridEntry::getContent).setHidable(true);
			col.setCaption("Col#" + i);
			col.setWidth(100);
		}

		for (int i = 0; i < hiddenColumns; i++) {
			Column<GridEntry, String> col = grid.addColumn(GridEntry::getContent).setCaption("hidden #" + i)
					.setHidable(true).setHidden(true);
			col.setCaption("hCol#" + i);
			col.setWidth(100); // for safety also set fixed width for hidden columns
		}

		grid.setId("testGrid");
		grid.setSizeFull();
		gridwrapper.addComponent(grid);
	}

	/**
	 * Return a collection of GridEntries containing Strings with the defined amount of rows.
	 */
	private LinkedList<GridEntry> createCollection(final int rows) {

		LinkedList<GridEntry> gridEntryLinkedList = new LinkedList<>();
		Random random = new Random();

		for (int i = 1; i < rows + 1; i++) {
			gridEntryLinkedList.add(new GridEntry("Long Content #" + i + ":" + random.nextInt()));
		}

		return gridEntryLinkedList;
	}
	
}

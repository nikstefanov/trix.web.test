package example.client.view;

import org.enunes.gwt.mvp.client.view.Display;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import example.client.presenter.ReportTreePresenter;

public class ReportTreeWidget extends Composite implements ReportTreePresenter.Display{

	final Tree staticTree;
	
	public ReportTreeWidget() {
		// Create a static tree and a container to hold it
		staticTree = createStaticTree();
//		staticTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
//			
//			@Override
//			public void onSelection(SelectionEvent<TreeItem> event) {
//				getSelectedReport(event.getSelectedItem().getText());
//			}
//		});
		
		staticTree.setAnimationEnabled(true);
		staticTree.ensureDebugId("cwTree-staticTree");
		ScrollPanel staticTreeWrapper = new ScrollPanel(staticTree);
		staticTreeWrapper.ensureDebugId("cwTree-staticTree-Wrapper");
		staticTreeWrapper.setSize("300px", "300px");

		// Wrap the static tree in a DecoratorPanel
		VerticalPanel staticDecorator = new VerticalPanel();
		staticDecorator.add(staticTreeWrapper);
		this.initWidget(staticDecorator);
		
	}

	/**
	 * Create a static tree with some data in it.
	 * 
	 * @return the new tree
	 */
	private Tree createStaticTree() {
		// Create the tree
		Tree staticTree = new Tree();

		// Add some of Beethoven's music
		TreeItem beethovenItem = staticTree.addItem("Beethoven");
		addMusicSection(beethovenItem, "Konzerte", new String[] {"bla"});
		addMusicSection(beethovenItem, "Quartetts", new String[] {"bla"});
		addMusicSection(beethovenItem, "Sonaten",  new String[] {"bla"});
		addMusicSection(beethovenItem, "Sinfonien", new String[] {"5. Sinfonie", "9. Sinfonie"});

		// Add some of Brahms's music
		TreeItem brahmsItem = staticTree.addItem("Brahms");
		addMusicSection(brahmsItem, "Konzerte", new String[] {"bla"});
		addMusicSection(brahmsItem, "Quartetts", new String[] {"bla"});
		addMusicSection(brahmsItem, "Sonaten",  new String[] {"bla"});
		addMusicSection(brahmsItem, "Sinfonien", new String[] {"bla"});

		// Add some of Mozart's music
		TreeItem mozartItem = staticTree.addItem("Mozart");
		addMusicSection(mozartItem, "Sinfonien", new String[] {"bla"});
		// Return the tree
		return staticTree;
	}
	
	/**
	   * Add a new section of music created by a specific composer.
	   * 
	   * @param parent the parent {@link TreeItem} where the section will be added
	   * @param label the label of the new section of music
	   * @param composerWorks an array of works created by the composer
	   */
	  private void addMusicSection(TreeItem parent, String label,
	      String[] composerWorks) {
	    TreeItem section = parent.addItem(label);
	    for (String work : composerWorks) {
	      section.addItem(work);
	    }
	  }

	@Override
	public void addContent(Display display) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeContent() {
		// TODO Auto-generated method stub
		
	}

	public Tree asTree() {
		return staticTree;
	}

//	@Override
//	public String getSelectedReport(String report) {
//		
//		return null;
//	}

	@Override
	public HasSelectionHandlers<TreeItem> getReportTree() {
		return staticTree;
	}

	@Override
	public Tree getSelectionHandlers() {
		return null;
	}

	@Override
	public Widget asWidget() {
		return staticTree;
	}

	

}

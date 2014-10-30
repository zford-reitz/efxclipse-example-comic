package ece2014.app.part;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.fx.ui.di.FXMLBuilder.Data;
import org.eclipse.fx.ui.di.FXMLLoader;
import org.eclipse.fx.ui.di.FXMLLoaderFactory;

public class FXMLPart {

	private static final String KEY_FXML_PROPERTY = "fxml";
	
	@PostConstruct
	private void createPart(
			BorderPane borderPane,
			MPart part,
			@FXMLLoader FXMLLoaderFactory fxmlLoaderFactory) throws IOException {
		
		Data<Object, Object> content = 
				fxmlLoaderFactory.loadBundleRelative("ece2014/app/fxml/" + part.getProperties().get(KEY_FXML_PROPERTY) + ".fxml").loadWithController();
		Node contentNode = (Node)content.getNode();
		borderPane.setCenter(contentNode);
		
	}
	
}

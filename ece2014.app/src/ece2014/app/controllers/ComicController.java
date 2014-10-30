package ece2014.app.controllers;

import java.io.File;

import javafx.animation.Transition;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.fx.core.databinding.JFXBeanProperties;
import org.eclipse.fx.ui.databinding.JFXUIProperties;

import ece2014.app.model.Comic;
import ece2014.app.transitions.Transitions;
import ece2014.app.validators.MandatoryValidator;

public class ComicController {

	// It's worth noting that PseudoClass is first available with JavaFX 8
	public final static PseudoClass VALIDATION_ERROR = 
			PseudoClass.getPseudoClass("validation-error");
	
	public static class FXValidationStatusListener implements IValueChangeListener {

		private Node validationTarget;

		public FXValidationStatusListener(Node validationTarget) {
			this.validationTarget = validationTarget;
		}
		
		@Override
		public void handleValueChange(ValueChangeEvent event) {
			boolean isOK = ((IStatus)event.diff.getNewValue()).isOK();
			this.validationTarget.pseudoClassStateChanged(VALIDATION_ERROR, !isOK);
		}

	}

	// Even though the order of declaration is technically irrelevant,
	// I generally try to declare @Inject annotated fields before
	// @FXML annotated fields as that most closely resembles the 
	// actual order of injection (first e4, then FXML).
	@Inject
	private Stage m_stage;
	
    @FXML
    private ImageView m_coverImageView;

    @FXML
    private TextField m_titleTextField;

    @FXML
    private TextField m_issueNumberTextField;
    
    @FXML
    private Label m_errorMessageLabel;

    @FXML
    private Button m_setCoverButton;
    
    @FXML
    private void initialize() {
    	m_coverImageView.setOnMouseClicked(event -> setCoverImage());
    	m_setCoverButton.setOnAction(event -> setCoverImage());
    	
    	Comic comic = new Comic();
    	DataBindingContext dbc = new DataBindingContext();
    	
    	AggregateValidationStatus aggregateValidationStatus = new AggregateValidationStatus(dbc, AggregateValidationStatus.MAX_SEVERITY);
    	aggregateValidationStatus.addValueChangeListener(new IValueChangeListener() {
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				IStatus value = (IStatus) event.getObservableValue().getValue();
				if (value == null || value.isOK()) {
					m_errorMessageLabel.setText("");
				} else {
					m_errorMessageLabel.setText(value.getMessage());
				}
			}
		});

    	Binding titleBinding = dbc.bindValue(
    			JFXUIProperties.text().observe(m_titleTextField), 
    			PojoProperties.value("title").observe(comic),
    			new UpdateValueStrategy().setAfterGetValidator(new MandatoryValidator()),
    			null);
    	titleBinding.getValidationStatus().addValueChangeListener(new FXValidationStatusListener(m_titleTextField));
    	
    	Binding issueNumberBinding = dbc.bindValue(
    			JFXUIProperties.text().observe(m_issueNumberTextField), 
    			PojoProperties.value("issueNumber").observe(comic));
    	issueNumberBinding.getValidationStatus().addValueChangeListener(new FXValidationStatusListener(m_issueNumberTextField));
    	
    	Binding coverBinding = dbc.bindValue(
    			JFXBeanProperties.value(m_coverImageView.imageProperty().getName()).observe(m_coverImageView), 
    			PojoProperties.value("cover").observe(comic));
    	coverBinding.getValidationStatus().addValueChangeListener(new FXValidationStatusListener(m_coverImageView));

    	// Trigger initial validation. Otherwise there will be no error messages
    	// until the user enters input.
    	for (Object b : dbc.getBindings()) {
    		((Binding)b).validateTargetToModel();
    	}
    	
    	m_stage.sizeToScene();
    }

	private void setCoverImage() {
		File imageFile = new FileChooser().showOpenDialog(m_stage);
		
		if (imageFile != null) {
			Image newImage = new Image(imageFile.toURI().toString());
			if (!newImage.isError()) {
				Transition transitionOut = 
						Transitions.createBatmanTransition(m_coverImageView);
				
				Image oldImage = m_coverImageView.getImage();
				if (oldImage != null && !oldImage.isError()) {
					transitionOut.setOnFinished(event -> 
						setCoverImageAndTransitionIn(transitionOut, m_coverImageView, newImage));
					transitionOut.playFromStart();
				} else {
					setCoverImageAndTransitionIn(transitionOut, m_coverImageView, newImage);
				}
			}
			
		}
	}

	private void setCoverImageAndTransitionIn(
			Transition transitionOut,
			ImageView imageView,
			Image image) {
		
		imageView.setImage(image);
		transitionOut.setOnFinished(null);
		transitionOut.jumpTo(transitionOut.getTotalDuration());
		transitionOut.setRate(-1);
		transitionOut.play();
	}
}

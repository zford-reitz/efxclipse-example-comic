package ece2014.app.validators;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

public class MandatoryValidator implements IValidator {
	@Override
	public IStatus validate(Object value) {
		if (value == null) {
			return ValidationStatus.error("Required field is empty.");
		}
		if (value instanceof String && ((String)value).isEmpty()) {
			return ValidationStatus.error("Required field is empty.");
		}

		return ValidationStatus.ok();
	}
	
}
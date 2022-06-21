package team.space.models.pollsforms;

import com.dlsc.formsfx.model.structure.Form;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.structure.Section;
import com.dlsc.formsfx.model.util.ResourceBundleService;
import com.dlsc.formsfx.model.validators.DoubleRangeValidator;
import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.controls.SimpleCheckBoxControl;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import com.dlsc.formsfx.view.util.ColSpan;

import java.util.Arrays;
import java.util.Collections;

public class RuntimeFormsModel {

    private Form formInstance;


    /**
     * Creates or simply returns to form singleton instance.
     *
     * @return Returns the form instance.
     */
    public Form getFormInstance() {
        if (formInstance == null) {
            createForm();
        }

        return formInstance;
    }

    /**
     * Creates a new form instance with the required information.
     */
    private void createForm() {

        formInstance = Form.of(
                Section.of(

                        Field.ofSingleSelectionType(Arrays.asList("Red", "Blue"),0)
                                .label("What color theme do you prefer?")
                               // .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),

                        Field.ofMultiSelectionType(Arrays.asList("Africa", "Asia"), Collections.singletonList(2))
                                .label("Continent")
                                .render(new SimpleCheckBoxControl<>()),

                        Field.ofSingleSelectionType(Arrays.asList("Right", "Left"),0)
                                .label("Driving on the")
                             //   .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),
                        Field.ofSingleSelectionType(Arrays.asList("Right", "Left"),0)
                                .label("Review Side")
                              //  .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),
                        Field.ofSingleSelectionType(Arrays.asList("Zürich (ZH)", "Bern (BE)"), 1)
                                .label("Capital")
                ).title("Poll"),Section.of(

                        Field.ofSingleSelectionType(Arrays.asList("Red", "Blue"),0)
                                .label("What color theme do you prefer?")
                               // .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),

                        Field.ofMultiSelectionType(Arrays.asList("Africa", "Asia"), Collections.singletonList(2))
                                .label("Continent")
                                .render(new SimpleCheckBoxControl<>()),

                        Field.ofSingleSelectionType(Arrays.asList("Right", "Left"),0)
                                .label("Driving on the")
                             //   .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),
                        Field.ofSingleSelectionType(Arrays.asList("Right", "Left"),0)
                                .label("Review Side")
                              //  .span(ColSpan.HALF)
                                .render(new SimpleRadioButtonControl<>()),
                        Field.ofSingleSelectionType(Arrays.asList("Zürich (ZH)", "Bern (BE)"), 1)
                                .label("Capital")
                ).title("Poll2")

                        );
    }

}

package com.base.parsergen.rules;

import com.base.model.AbstractModel;
import com.base.model.PrimitiveField;
import java.util.Collections;
import java.util.Map;

public interface ModelAugmenterI {

    public static final ModelAugmenterI EMPTY_AUGMENTER =
            (final AbstractModel model) -> Collections.EMPTY_MAP;
            
    public Map<PrimitiveField, String> getAugmentedFields(final AbstractModel model);

}

package base.parsergen.xml;

import base.model.AbstractModel;
import java.util.HashMap;
import java.util.Map;

// TODO add support for FK insertion
// TODO detect many to one relations
    public class ModelHierarchy {

    final public AbstractModel model;
    final AbstractModel parent = null;
//    final boolean multipleInstance = false;
    final public String elementName;

    public Map<AbstractModel, ModelHierarchy> subNestings = new HashMap<>();

    public ModelHierarchy(final AbstractModel m, final String elementName) {
        this.model = m;
        this.elementName = elementName;
    }

    @Override
    public int hashCode() {
        return model.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModelHierarchy other = (ModelHierarchy) obj;
        return this.model.getJavaClassName().equals(other.model.getJavaClassName());
    }

    @Override
    public String toString() {
        return "Model: " + model.getJavaClassName() + " (with " + subNestings.size() + " children)";
    }
}

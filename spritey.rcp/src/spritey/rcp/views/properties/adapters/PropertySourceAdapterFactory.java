package spritey.rcp.views.properties.adapters;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import spritey.core.node.Node;
import spritey.rcp.editors.editparts.SheetEditPart;

/**
 * Factory for creating property source adapters. This factory can adapt
 * SheetEditPart.
 */
public class PropertySourceAdapterFactory implements IAdapterFactory {

    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if (adapterType == IPropertySource.class) {
            if (adaptableObject instanceof SheetEditPart) {
                SheetEditPart part = (SheetEditPart) adaptableObject;
                Node node = (Node) part.getModel();
                return new SheetPropertySource(node.getModel());
            } else if (adaptableObject instanceof Node) {
                Node node = (Node) adaptableObject;
                return new SheetPropertySource(node.getModel());
            }
        }

        return null;

    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }

}

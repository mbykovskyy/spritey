package spritey.rcp.views.properties.adapters;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.node.Node;
import spritey.rcp.editors.editparts.SheetEditPart;
import spritey.rcp.editors.editparts.SpriteEditPart;

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
            } else if (adaptableObject instanceof SpriteEditPart) {
                SpriteEditPart part = (SpriteEditPart) adaptableObject;
                Node node = (Node) part.getModel();
                return new SpritePropertySource(node.getModel());
            } else if (adaptableObject instanceof Node) {
                Model model = ((Node) adaptableObject).getModel();

                if (model instanceof Sheet) {
                    return new SheetPropertySource(model);
                } else if (model instanceof Sprite) {
                    return new SpritePropertySource(model);
                }
            }
        }

        return null;
    }

    @Override
    public Class[] getAdapterList() {
        return new Class[] { IPropertySource.class };
    }

}

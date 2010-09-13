/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy and Alan Morey.
 * 
 * Spritey is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Spritey is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Spritey. If not, see <http://www.gnu.org/licenses/>.
 */
package spritey.rcp.wizards;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.RGB;

import spritey.core.Model;
import spritey.core.ModelFactory;
import spritey.core.Sheet;
import spritey.core.node.Node;
import spritey.core.node.NodeFactory;
import spritey.core.validator.NotNullValidator;
import spritey.core.validator.StringLengthValidator;
import spritey.core.validator.TypeValidator;
import spritey.core.validator.Validator;
import spritey.rcp.SpriteyPlugin;
import spritey.rcp.core.SheetConstants;
import spritey.rcp.validators.BackgroundValidator;
import spritey.rcp.validators.SizeValidator;

/**
 * A wizard for creating a new sprite sheet.
 */
public class NewSpriteSheetWizard extends Wizard {

    static final String TITLE = "New Sprite Sheet";

    NewSpriteSheetMainPage mainPage;

    @Override
    public void addPages() {
        mainPage = new NewSpriteSheetMainPage();

        setWindowTitle(TITLE);
        addPage(mainPage);
    }

    /**
     * Creates and populates sheet model.
     * 
     * @param modelFactory
     *        factory to use to create sheet model.
     * 
     * @return instance of sheet model.
     */
    private Model createSheet(ModelFactory modelFactory) {
        Validator notNullValidator = new NotNullValidator();

        Model sheet = modelFactory.createSheet();
        sheet.addValidator(Sheet.BACKGROUND, new BackgroundValidator(RGB.class));

        sheet.addValidator(Sheet.OPAQUE, notNullValidator);
        sheet.addValidator(Sheet.OPAQUE, new TypeValidator(Boolean.class));

        sheet.addValidator(Sheet.DESCRIPTION, notNullValidator);
        sheet.addValidator(Sheet.DESCRIPTION, new TypeValidator(String.class));
        sheet.addValidator(Sheet.DESCRIPTION, new StringLengthValidator(
                SheetConstants.MIN_DESCRIPTION_LENGTH,
                SheetConstants.MAX_DESCRIPTION_LENGTH));

        sheet.addValidator(Sheet.SIZE, notNullValidator);
        sheet.addValidator(Sheet.SIZE, new TypeValidator(Dimension.class));

        Dimension min = new Dimension(SheetConstants.MIN_WIDTH,
                SheetConstants.MIN_HEIGHT);
        Dimension max = new Dimension(SheetConstants.MAX_WIDTH,
                SheetConstants.MAX_HEIGHT);
        sheet.addValidator(Sheet.SIZE, new SizeValidator(min, max));

        mainPage.populateSheet(sheet);

        return sheet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        SpriteyPlugin plugin = SpriteyPlugin.getDefault();
        ModelFactory modelFactory = plugin.getModelFactory();
        NodeFactory nodeFactory = plugin.getNodeFactory();

        Node sheet = nodeFactory.createNode(SheetConstants.DEFAULT_NAME);
        sheet.setModel(createSheet(modelFactory));

        return plugin.getRootNode().addChild(sheet);
    }
}

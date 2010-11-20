/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2010 Maksym Bykovskyy.
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
package spritey.core.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;

import spritey.core.Group;
import spritey.core.Model;
import spritey.core.Sheet;
import spritey.core.Sprite;
import spritey.core.filter.VisibleSpriteFilter;

import com.jamesmurty.utils.XMLBuilder;

/**
 * A writer for serializing sprite sheet in XML format. This writer skips empty
 * groups and invisible sprites.
 */
public class XmlWriter implements Writer {

    private static final String SHEET = "sheet";
    private static final String SPRITE = "sprite";
    private static final String GROUP = "group";

    protected void validate(Model node) {
        if (null == node) {
            throw new IllegalArgumentException("Node is null.");
        }
    }

    @Override
    public void write(Sheet sheet, File file) throws FileNotFoundException,
            IOException {
        validate(sheet);

        try {
            write(build(sheet), file);
        } catch (ParserConfigurationException e) {
            throw new IOException(e);
        } catch (FactoryConfigurationError e) {
            throw new IOException(e);
        } catch (TransformerException e) {
            throw new IOException(e);
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
    }

    /**
     * Builds the specified node.
     * 
     * @param builder
     *        the builder for generating XML.
     * @param model
     *        the model to generate XML for.
     */
    protected void buildNode(XMLBuilder builder, Model model) {
        if (model instanceof Sprite) {
            Rectangle bounds = (Rectangle) model.getProperty(Sprite.BOUNDS);

            // Only add visible sprites.
            if ((bounds.x > -1) && (bounds.y > -1)) {
                String name = (String) model.getProperty(Sprite.NAME);

                builder = builder.e(SPRITE).a("name", name)
                        .a("x", String.valueOf(bounds.x))
                        .a("y", String.valueOf(bounds.y))
                        .a("width", String.valueOf(bounds.width))
                        .a("height", String.valueOf(bounds.height)).up();
            }
        } else if (model instanceof Group) {
            if (new VisibleSpriteFilter().filter(model).length > 0) {
                String name = (String) model.getProperty(Group.NAME);
                builder = builder.e(GROUP).a("name", name);

                for (Model child : model.getChildren()) {
                    buildNode(builder, child);
                }
            }
        }

    }

    /**
     * Builds the XML for the specified tree.
     * 
     * @param model
     *        the sheet node.
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * @throws TransformerException
     */
    protected XMLBuilder build(Sheet model)
            throws ParserConfigurationException, FactoryConfigurationError {
        Dimension size = (Dimension) model.getProperty(Sheet.SIZE);
        Color bg = (Color) model.getProperty(Sheet.BACKGROUND);
        String description = (String) model.getProperty(Sheet.DESCRIPTION);
        boolean opaque = (Boolean) model.getProperty(Sheet.OPAQUE);

        String bgStr = opaque ? bg.getRed() + ", " + bg.getGreen() + ", "
                + bg.getBlue() + ", 255" : "0, 0, 0, 0";

        // @formatter:off
        XMLBuilder builder = XMLBuilder.create(SHEET)
            .a("width", String.valueOf(size.width))
            .a("height", String.valueOf(size.height))
            .a("background", bgStr)
            .a("description", description);
        // @formatter:on

        for (Model child : model.getChildren()) {
            buildNode(builder, child);
        }
        return builder;
    }

    /**
     * Writers the specified builder to the file system.
     * 
     * @param builder
     *        the builder to write to the file system.
     * @param file
     *        the file to write to.
     * @throws TransformerException
     * @throws FileNotFoundException
     *         if the file exists but is a directory rather than a regular file,
     *         does not exist but cannot be created, or cannot be opened for any
     *         other reason.
     */
    protected void write(XMLBuilder builder, File file)
            throws TransformerException, FileNotFoundException {
        Properties p = new Properties();
        p.put(OutputKeys.METHOD, "xml");
        p.put(OutputKeys.INDENT, "yes");
        p.put("{http://xml.apache.org/xslt}indent-amount", "2");

        PrintWriter writer = new PrintWriter(new FileOutputStream(file));
        builder.toWriter(writer, p);
        writer.close();
    }
}

/**
 * This source file is part of Spritey - the sprite sheet creator.
 * 
 * Copyright 2011 Maksym Bykovskyy.
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
import java.awt.Point;
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
import spritey.core.Messages;
import spritey.core.Node;
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

    @Override
    public void write(Sheet sheet, File file) throws FileNotFoundException,
            IOException {
        if (null == sheet) {
            throw new IllegalArgumentException(Messages.NULL);
        }

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
     * @param node
     *        the model to generate XML for.
     */
    protected void buildNode(XMLBuilder builder, Node node) {
        if (node instanceof Sprite) {
            Sprite sprite = (Sprite) node;
            Point location = sprite.getLocation();

            if (sprite.isVisible()) {
                Dimension size = sprite.getSize();
                String name = sprite.getName();

                builder = builder.e(SPRITE).a("name", name)
                        .a("x", String.valueOf(location.x))
                        .a("y", String.valueOf(location.y))
                        .a("width", String.valueOf(size.width))
                        .a("height", String.valueOf(size.height)).up();
            }
        } else if (node instanceof Group) {
            // Skip group when it doesn't contain at least one visible sprite.
            if (new VisibleSpriteFilter().filter(node).length > 0) {
                builder = builder.e(GROUP).a("name", node.getName());

                for (Node child : node.getChildren()) {
                    buildNode(builder, child);
                }
            }
        }
    }

    /**
     * Builds the XML for the specified tree.
     * 
     * @param sheet
     *        the sheet node.
     * @throws ParserConfigurationException
     * @throws FactoryConfigurationError
     * @throws TransformerException
     */
    protected XMLBuilder build(Sheet sheet)
            throws ParserConfigurationException, FactoryConfigurationError {
        Color bg = sheet.getBackground();

        String bgStr = bg.getRed() + ", " + bg.getGreen() + ", " + bg.getBlue()
                + ", " + bg.getAlpha();

        // @formatter:off
        XMLBuilder builder = XMLBuilder.create(SHEET)
            .a("width", String.valueOf(sheet.getWidth()))
            .a("height", String.valueOf(sheet.getHeight()))
            .a("background", bgStr)
            .a("description", sheet.getDescription());
        // @formatter:on

        for (Node child : sheet.getChildren()) {
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

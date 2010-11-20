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
package spritey.rcp.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.dialogs.IOverwriteQuery;

import spritey.core.Sheet;
import spritey.core.io.ImageWriter;
import spritey.core.io.Writer;
import spritey.core.io.XmlWriter;
import spritey.rcp.Messages;
import spritey.rcp.SpriteyPlugin;

/**
 * Operation for exporting sprite sheet.
 */
public class ExportSheetOperation implements IRunnableWithProgress {

    private final static int TOTAL_WORK = 2;

    private IOverwriteQuery overwriteCallback;
    private String overwriteState;

    private File imageFile;
    private File metadataFile;

    private List<IStatus> errorMessages;

    /**
     * Creates an instance of operation.
     * 
     * @param imageFile
     *        file to write image data to.
     * @param metadataFile
     *        file to write meta data to.
     * @param overwriteCallback
     *        the overwrite callback.
     */
    public ExportSheetOperation(File imageFile, File metadataFile,
            IOverwriteQuery overwriteCallback) {
        this.imageFile = imageFile;
        this.metadataFile = metadataFile;
        this.overwriteCallback = overwriteCallback;
        overwriteState = "";
        errorMessages = new ArrayList<IStatus>();
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        monitor.beginTask("", TOTAL_WORK);

        export(imageFile, new ImageWriter(), monitor);
        export(metadataFile, new XmlWriter(), monitor);

        monitor.done();
    }

    /**
     * Exports the sprite sheet to the specified file with a help of the writer.
     * 
     * @param file
     *        the file to export to.
     * @param writer
     *        the writer to use for writing.
     * @param monitor
     *        the progress monitor.
     * 
     * @throws InterruptedException
     *         when this operation is cancelled or interrupted.
     */
    protected void export(File file, Writer writer, IProgressMonitor monitor)
            throws InterruptedException {
        monitor.subTask(file.getPath());

        if (file.exists()) {
            if (overwriteState.equals(IOverwriteQuery.NO_ALL)) {
                monitor.worked(1);
                return;
            } else if (!overwriteState.equals(IOverwriteQuery.ALL)) {
                overwriteState = overwriteCallback.queryOverwrite(file
                        .getPath());

                if (overwriteState.equals(IOverwriteQuery.CANCEL)) {
                    throw new InterruptedException();
                } else if (overwriteState.equals(IOverwriteQuery.NO)
                        || overwriteState.equals(IOverwriteQuery.NO_ALL)) {
                    // Skip to next file
                    monitor.worked(1);
                    return;
                }
            }
        }

        try {
            Sheet sheet = (Sheet) SpriteyPlugin.getDefault().getRootModel()
                    .getChildren()[0];
            writer.write(sheet, file);
        } catch (FileNotFoundException e) {
            errorMessages.add(new Status(IStatus.ERROR,
                    SpriteyPlugin.PLUGIN_ID, NLS.bind(
                            Messages.OPEN_FILE_FAILED, file.getPath())));
        } catch (IOException e) {
            errorMessages.add(new Status(IStatus.ERROR,
                    SpriteyPlugin.PLUGIN_ID, NLS.bind(Messages.WRITING_FAILED,
                            file.getPath())));
        }

        monitor.worked(1);
        if (monitor.isCanceled()) {
            throw new InterruptedException();
        }
    }

    /**
     * Returns the status of this operation. If there were errors, the result is
     * a multi-status object containing status object for each error. If there
     * were no errors, the result is a status object with error code
     * <code>OK</code>.
     * 
     * @return the status the status object containing errors for each error.
     */
    public IStatus getStatus() {
        IStatus[] errors = new IStatus[errorMessages.size()];
        errorMessages.toArray(errors);
        return new MultiStatus(SpriteyPlugin.PLUGIN_ID, IStatus.OK, errors,
                Messages.PROBLEMS_SAVING, null);
    }

}

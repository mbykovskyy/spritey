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
package spritey.ui.operations;

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

import spritey.core.Sheet;
import spritey.core.io.ImageWriter;
import spritey.core.io.Writer;
import spritey.core.io.XmlWriter;
import spritey.core.packer.FirstFitStrategy;
import spritey.core.packer.Packer;
import spritey.ui.Messages;

/**
 * Operation for saving sprite sheet to a disk.
 */
public class SaveSheetOperation implements IRunnableWithProgress {

    private static final int TOTAL_WORK = 3;

    private Packer packer;

    private OverwriteQuery overwriteQuery;
    private int overwrite;

    private Sheet sheet;
    private File imageFile;
    private File metadataFile;

    private List<IStatus> errors;

    /**
     * Creates an instance of SaveSheetOperation.
     * 
     * @param imageFile
     *        file to write image data to.
     * @param metadataFile
     *        file to write meta data to.
     * @param overwriteCallback
     *        the overwrite callback.
     */
    public SaveSheetOperation(Sheet sheet, File imageFile, File metadataFile,
            OverwriteQuery overwriteQuery) {
        this.sheet = sheet;
        this.imageFile = imageFile;
        this.metadataFile = metadataFile;
        this.overwriteQuery = overwriteQuery;

        packer = new Packer(new FirstFitStrategy());
        overwrite = -1;
        errors = new ArrayList<IStatus>();
    }

    /**
     * Saves the sprite sheet to the specified file with a help of the writer.
     * 
     * @param file
     *        the file to save to.
     * @param writer
     *        the writer to use for writing.
     * @param monitor
     *        the progress monitor.
     * 
     * @throws InterruptedException
     *         when this operation is cancelled or interrupted.
     */
    protected void save(File file, Writer writer, IProgressMonitor monitor)
            throws InterruptedException {
        monitor.subTask(NLS.bind(Messages.SAVE_AS_SAVING, file.getPath()));

        if (file.exists()) {
            if (OverwriteQuery.NO_ALL == overwrite) {
                return;
            } else if (OverwriteQuery.ALL != overwrite) {
                overwrite = overwriteQuery.queryOverwrite(file.getPath());

                if (OverwriteQuery.CANCEL == overwrite) {
                    throw new InterruptedException();
                } else if ((OverwriteQuery.NO == overwrite)
                        || (OverwriteQuery.NO_ALL == overwrite)) {
                    return;
                }
            }
        }

        try {
            writer.write(sheet, file);
        } catch (FileNotFoundException e) {
            errors.add(new Status(IStatus.ERROR, null, NLS.bind(
                    Messages.SAVE_AS_OPEN_FILE_FAILED, file.getPath())));
        } catch (IOException e) {
            errors.add(new Status(IStatus.ERROR, null, NLS.bind(
                    Messages.SAVE_AS_WRITING_FAILED, file.getPath())));
        }

        if (monitor.isCanceled()) {
            throw new InterruptedException();
        }
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        monitor.beginTask("", TOTAL_WORK);
        monitor.subTask(Messages.SAVE_AS_PACKING);

        packer.pack(sheet, false);
        monitor.worked(1);

        save(imageFile, new ImageWriter(), monitor);
        monitor.worked(1);

        save(metadataFile, new XmlWriter(), monitor);
        monitor.worked(1);

        monitor.done();
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
        IStatus[] status = new IStatus[errors.size()];
        errors.toArray(status);
        return new MultiStatus(Messages.SPRITE_SHEET_WIZARD_TITLE, IStatus.OK,
                status, Messages.SAVE_AS_PROBLEMS_SAVING, null);
    }

}

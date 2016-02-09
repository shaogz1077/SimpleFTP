package com.paulds.simpleftp.data.repository;

import android.os.Environment;

import com.paulds.simpleftp.data.entities.FileEntity;
import com.paulds.simpleftp.data.entities.FtpServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

/**
 * This class provide an access to remote files.
 *
 * @author Paul-DS
 */
public class FtpRepository {

    /**
     * Lists the files present at the specified path.
     * @param path The specified path.
     * @return The list of files.
     */
    public List<FileEntity> listFiles(FtpServer server, String path) throws FTPException, IOException, FTPIllegalReplyException, FTPAbortedException, FTPDataTransferException, FTPListParseException {
        FTPClient client = new FTPClient();
        client.connect(server.getHost(), server.getPort());
        client.login(server.isAnonymous() ? "anonymous" : server.getLogin(), server.getPassword());

        //client.changeDirectory(path);

        FTPFile[] list = client.list();

        List<FileEntity> results = new ArrayList<FileEntity>();

        if(list != null) {
            for (FTPFile file : list) {
                results.add(this.ftpFileToEntity(file));
            }
        }

        return results;
    }


    /**
     * Create a folder at the specified path.
     * @param path The specified path.
     * @param name The name of the folder to create.
     */
    public void createFolder(String path, String name) {
        String fullPath = Environment.getExternalStorageDirectory().toString() + path + "/" + name;

        File folder = new File(fullPath);

        if(!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     * Convert a FTP file to a file entity.
     * @param file The FTP file to convert.
     * @return The converted file entity.
     */
    private FileEntity ftpFileToEntity(FTPFile file)
    {
        FileEntity entity = new FileEntity();
        entity.setPath(file.getLink());
        entity.setName(file.getName());
        entity.setSize(file.getSize());
        entity.setIsDirectory(file.getType() == FTPFile.TYPE_DIRECTORY);
        return entity;
    }
}
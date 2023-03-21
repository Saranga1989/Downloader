package thogakade.handler;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import thogakade.Shared.Shared;
import thogakade.controllers.DBController;
import thogakade.model.Link;

public class Downloader {

    ReadableByteChannel rbc = null;
    FileOutputStream fos = null;
    String filename = null;
    String filetype = null;

    ArrayList<ReadableByteChannel> chanellist = new ArrayList<>();
    ArrayList<FileOutputStream> fileoutlist = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try {
            String file = "";
            String url = "";
            Downloader dl = new Downloader();
            dl.downloadFile();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Downloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadFile() throws IOException, ClassNotFoundException, SQLException {
        DBController con = new DBController();
        String filepath = null;
        ArrayList<Link> list = con.getLinksForSchedule();
        ArrayList<Link> returnlist = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Link object = list.get(i);
                boolean isdownload = false;
                try {
                    ReadableByteChannel rbc = null;
                    FileOutputStream fos = null;
                    URL url = new URL(object.getLink());
                    rbc = Channels.newChannel(url.openStream());
                    fos = new FileOutputStream(filepath);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    isdownload = true;
                    object.setStatus(Shared.STATUS_COMPLETED);
                    returnlist.add(object);
                    fos.close();
                    rbc.close();
                } catch (Exception e) {
                    object.setStatus(Shared.STATUS_FAILD);
                    isdownload = false;
                    returnlist.add(object);
                } finally {
                    con.updateLinks(list);
                }
            }
        }

    }

    public static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            String filename = null;
            if (file != null) {
                filename = file.getName();
            }
            if (filename.startsWith("file-") && file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }

    public boolean deleteLatestFile(boolean needtocancel) {
        String home = System.getProperty("user.home");
        File file = new File(home + "/Downloads/");
        String filepath = file.getPath();
        System.out.println("filepath" + filepath);
        File latestfile = lastFileModified(filepath);
        // String filename = null;
        String filename = null;
        if (latestfile != null) {
            filename = latestfile.getName();
        }
        System.out.println("latest file name" + filename);
        if (filename != null && !filename.isEmpty()) {
            File fileobj = new File(filepath + "/" + filename);
            if (fileobj != null) {
                fileobj.delete();
                return true;
            }
        }

        return false;
    }

    public void downloadFilesNormal(String sheduledatetime, boolean needtocancel, String dllocation) throws IOException, ClassNotFoundException, SQLException {
        DBController con = new DBController();
        System.out.println("sheduledatetime" + sheduledatetime);
        String filepath = "";
        File file = null;
        System.out.println("dllocation"+dllocation);
        if (dllocation != null && !dllocation.isEmpty()) {
            filepath = dllocation;
            file = new File(filepath);
        } else {
            String home = System.getProperty("user.home");
            file = new File(home + "/Downloads/");
            filepath = file.getPath();
        }
        if (needtocancel) {
            System.out.println("filepath" + filepath);
            File latestfile = lastFileModified(filepath);
            // String filename = null;
            String filename = latestfile.getName();
            System.out.println("latest file name" + latestfile.getName());
            File fileobj = new File(filepath + "/" + filename);
            if (fileobj != null) {
                fileobj.deleteOnExit();
            }
        } else {
            SimpleDateFormat df = new SimpleDateFormat(Shared.DF_INVENTORY_NUMBER_DATE_FORMAT);
            ArrayList<Link> returnlist = new ArrayList<>();
            ArrayList<Link> list = con.getLinksForNormal(sheduledatetime);
            System.out.println("OS " + System.getProperty("os.name"));
            if (!System.getProperty("os.name").toLowerCase().startsWith("windows") && file.exists()) {
                setPermission(file);
                file.canExecute();
                file.canRead();
                file.canWrite();
                System.out.println("canExecute" + file.canExecute());
                System.out.println("canRead" + file.canRead());
                System.out.println("canWrite" + file.canWrite());

            }
            for (int i = 0; i < list.size(); i++) {
                Link link = list.get(i);
                if (link != null) {
                    Link object = link;
                    String filename = "file-" + i + 1 + "-" + df.format(new Date());
                    String fileType = filetype(object.getLink());
                    System.out.println("filetype" + fileType);
                    String finalfilepath = filepath + "/" + filename;
                    System.out.println("Download  LInk" + object.getLink());
                    System.out.println("filepath" + finalfilepath);
                    boolean isdownload = false;
                    try {

                        URL url = new URL(object.getLink());
                        URLConnection connection = url.openConnection();
                        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
                        connection.setRequestProperty("User-Agent", USER_AGENT);
                        rbc = Channels.newChannel(connection.getInputStream());
                        boolean isopen = rbc.isOpen();
                        System.out.println("isopen" + isopen);
                        chanellist.add(rbc);
                        System.out.println("name" + FilenameUtils.getName(url.getPath()));
                        if (fileType.isEmpty()) {
                            fileType = FilenameUtils.getExtension(url.getPath());
                        }
                        System.out.println("connection.getContent()" + connection.getContent());
                        System.out.println("connection.getContentType()" + connection.getContentType());
                        System.out.println("connection.getContentEncoding()" + connection.getContentEncoding());

                        System.out.println(" filetype" + fileType);  //url.getContent();
                        fos = new FileOutputStream(finalfilepath + "." + fileType);
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        java.nio.file.Path filePath = Paths.get(finalfilepath);
                        System.out.println("probeContentType" + Files.probeContentType(filePath));;
                        isdownload = true;
                        object.setStatus(Shared.STATUS_COMPLETED);
                        returnlist.add(object);
                        con.updateLink(object);
                        fos.close();
                        rbc.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        object.setStatus(Shared.STATUS_FAILD);
                        returnlist.add(object);
                        con.updateLink(object);
                        isdownload = false;
                    } finally {
                    }

                }
            }
            con.updateDownloadDetails(sheduledatetime);
        }
    }

    public void downloadFileSchdule(boolean iscancel, String sheduledatetime, String dllocation) throws IOException, ClassNotFoundException, SQLException {
        DBController con = new DBController();
        System.out.println("sheduledatetime" + sheduledatetime);

        String filepath = "";
        File file = null;
        if (dllocation != null && !dllocation.isEmpty()) {
            filepath = dllocation;
            file = new File(filepath);
        } else {
            String home = System.getProperty("user.home");
            file = new File(home + "/Downloads/");
            filepath = file.getPath();
        }
        //    String home = System.getProperty("user.home");
        //   File file = new File(home + "/Downloads/");
        //    String filepath = file.getPath();
        SimpleDateFormat df = new SimpleDateFormat(Shared.DF_INVENTORY_NUMBER_DATE_FORMAT);
        ArrayList<Link> returnlist = new ArrayList<>();
        ArrayList<Link> list = con.getLinksForSchedule();
        System.out.println("OS " + System.getProperty("os.name"));
        if (!System.getProperty("os.name").toLowerCase().startsWith("windows") && file.exists()) {
            setPermission(file);
            file.canExecute();
            file.canRead();
            file.canWrite();
            System.out.println("canExecute" + file.canExecute());
            System.out.println("canRead" + file.canRead());
            System.out.println("canWrite" + file.canWrite());

        }
        for (int i = 0; i < list.size(); i++) {
            Link link = list.get(i);
            if (link != null) {
                Link object = link;
                String filename = "file-" + i + 1 + "-" + df.format(new Date());
                String fileType = filetype(object.getLink());
                String finalfilepath = filepath + "/" + filename;
                System.out.println("Download  LInk" + object.getLink());
                System.out.println("filepath" + finalfilepath);
                boolean isdownload = false;
                try {
                    URL url = new URL(object.getLink());
                    //   rbc = Channels.newChannel(url.openStream());
                    chanellist.add(rbc);
                    // fos = new FileOutputStream(finalfilepath);
                    URLConnection connection = url.openConnection();
                    String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
                    connection.setRequestProperty("User-Agent", USER_AGENT);
                    System.out.println("connection.getContent()" + connection.getContent());
                    System.out.println("connection.getContentType()" + connection.getContentType());
                    System.out.println("connection.getContentEncoding()" + connection.getContentEncoding());
                    rbc = Channels.newChannel(connection.getInputStream());
                    fileType = FilenameUtils.getExtension(url.getPath());
                    fos = new FileOutputStream(finalfilepath + "." + fileType);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    isdownload = true;
                    object.setStatus(Shared.STATUS_COMPLETED);
                    returnlist.add(object);
                    con.updateLink(object);
                    fos.close();
                    rbc.close();
                } catch (Exception e) {
                    object.setStatus(Shared.STATUS_FAILD);
                    returnlist.add(object);
                    con.updateLink(object);
                    isdownload = false;
                } finally {
                }

            }
        }
        con.updateDownloadDetails(sheduledatetime);
    }

    public File getTheNewestFile(String filePath, String ext) {
        File theNewestFile = null;
        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("file_");
        File[] files = dir.listFiles(fileFilter);
        if (files.length > 0) {
            /**
             * The newest file comes first *
             */
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            theNewestFile = files[0];
        }

        return theNewestFile;
    }

    public void setPermission(File file) throws IOException {
        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);

        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);

        Files.setPosixFilePermissions(file.toPath(), perms);
    }

    public void downloadFileNormal(Link link) throws IOException, ClassNotFoundException, SQLException {
        DBController con = new DBController();
        String home = System.getProperty("user.home");
        SimpleDateFormat df = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
        String filename = "file_" + df.format(new Date());
        File file = new File(home + "/Downloads/");
        String filepath = file.getPath();
        ArrayList<Link> returnlist = new ArrayList<>();
        if (link != null) {
            Link object = link;
            String filetype = filetype(object.getLink());
            filepath = filepath + "/" + filename + filetype;
            System.out.println("Download  LInk" + object.getLink());
            System.out.println("filepath" + filepath);
            boolean isdownload = false;
            try {
                ReadableByteChannel rbc = null;
                FileOutputStream fos = null;
                URL url = new URL(object.getLink());
                rbc = Channels.newChannel(url.openStream());
                fos = new FileOutputStream(filepath);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                isdownload = true;
                object.setStatus(Shared.STATUS_COMPLETED);
                fos.close();
                rbc.close();
                Path filePath = Paths.get(filepath);
                String contentType = Files.probeContentType(filePath);
                System.out.println("contentType" + contentType);
            } catch (Exception e) {
                object.setStatus(Shared.STATUS_FAILD);
                isdownload = false;
            } finally {
                con.updateLink(object);
            }

        }

    }

    private String filetype(String url) {
        System.out.println("filetype" + url);
        String filetype = "";
        if (url.contains(".mp3")) {
            filetype = "mp3";
        } else if (url.contains(".mp4")) {
            filetype = "mp4";
        } else if (url.contains(".pdf")) {
            filetype = "pdf";
        } else if (url.contains(".docx")) {
            filetype = "docx";
        } else if (url.contains(".aif")) {
            filetype = "aif";
        } else if (url.contains(".cda")) {
            filetype = "cda";
        } else if (url.contains(".mid")) {
            filetype = "mid";
        } else if (url.contains(".midi")) {
            filetype = "midi";
        } else if (url.contains(".mpa")) {
            filetype = "mpa";
        } else if (url.contains(".ogg")) {
            filetype = "ogg";
        } else if (url.contains(".wav")) {
            filetype = "wav";
        } else if (url.contains(".wma")) {
            filetype = "wma";
        } else if (url.contains(".wpl")) {
            filetype = "wpl";
        } else if (url.contains(".7z")) {
            filetype = "7z";
        } else if (url.contains(".arj")) {
            filetype = "arj";
        } else if (url.contains(".deb")) {
            filetype = "deb";
        } else if (url.contains(".pkg")) {
            filetype = "pkg";
        } else if (url.contains(".deb")) {
            filetype = "deb";
        } else if (url.contains(".rar")) {
            filetype = "rar";
        } else if (url.contains(".rpm")) {
            filetype = "rpm";
        } else if (url.contains(".tar.gz ")) {
            filetype = "tar.gz ";
        } else if (url.contains(".zip")) {
            filetype = "zip";
        } else if (url.contains(".bin")) {
            filetype = "bin";
        } else if (url.contains(".dmg")) {
            filetype = "dmg";
        } else if (url.contains(".iso")) {
            filetype = "iso";
        } else if (url.contains(".toast")) {
            filetype = "toast";
        } else if (url.contains(".vcd")) {
            filetype = "vcd";
        } else if (url.contains(".csv")) {
            filetype = "csv";
        } else if (url.contains(".dat")) {
            filetype = "dat";
        } else if (url.contains(".db")) {
            filetype = "db";
        } else if (url.contains(".dbf")) {
            filetype = "dbf";
        } else if (url.contains(".log")) {
            filetype = "log";
        } else if (url.contains(".mdb")) {
            filetype = "mdb";
        } else if (url.contains(".sav")) {
            filetype = "sav";
        } else if (url.contains(".sql")) {
            filetype = "sql";
        } else if (url.contains(".tar")) {
            filetype = "tar";
        } else if (url.contains(".xml")) {
            filetype = "xml";
        } else if (url.contains(".email")) {
            filetype = "email";
        } else if (url.contains(".apk")) {
            filetype = "apk";
        } else if (url.contains(".bat")) {
            filetype = "bat";
        } else if (url.contains(".exe")) {
            filetype = "exe";
        } else if (url.contains(".jpeg")) {
            filetype = "jpeg";
        } else if (url.contains(".jpg")) {
            filetype = "jpg";
        } else if (url.contains(".png")) {
            filetype = "png";
        }
        return filetype;
    }
}

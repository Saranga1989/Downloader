/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.view.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;
import thogakade.Shared.Shared;
import thogakade.controllers.DBController;
import thogakade.handler.Downloader;
import thogakade.model.DownloadData;
import thogakade.model.Link;
import thogakade.model.SettingsData;
import thogakade.view.Main;

/**
 *
 * @author cmjd
 */
public class DownloadProgressForNormal extends javax.swing.JInternalFrame {

    ReadableByteChannel rbc = null;
    FileOutputStream fos = null;
    String filename = null;
    String filetype = null;
    private static DownloadProgressForNormal myInstance;
    ArrayList<ReadableByteChannel> chanellist = new ArrayList<>();
    ArrayList<FileOutputStream> fileoutlist = new ArrayList<>();
    JProgressBar jb;
    int i = 0, num = 0;
    ArrayList<Link> list = null;
    SimpleDateFormat df = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
    DefaultTableModel model = null;
    ImageIcon loading = new ImageIcon("/home/saranga/Desktop/ajax-loader.gif");
    Multithred mt = null;
    Thread t1 = null;
    int begin = 0;
    int timeInterval = 30000;
    int timeInterval1 = 30000;
    Timer timer = null;
    Timer timer1 = null;

    /**
     * Creates new form CreateOrder
     */
    public DownloadProgressForNormal() {
        initComponents();
        timer = new Timer();
        timer1 = new Timer();
        timer.schedule(
                new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                //call the method
                System.out.println(".run()" + new Date());
            }
        }, begin, timeInterval
        );
        //JLabel  label=new JLabel("loading... ", loading, JLabel.CENTER);
        //this.add(label);
    }

    public DownloadProgressForNormal(final ArrayList<Link> list, final Date scheduletime, final String downloadlocation) {
        initComponents();
        this.list = list;
        final DBController dbcon = new DBController();
        model = (DefaultTableModel) jTable1.getModel();
        timer = new Timer();
        timer1 = new Timer();
        jLabelSuccess.setVisible(false);
        jLabelfail.setVisible(false);
        tfsuccess.setVisible(false);
        tffailed.setVisible(false);
        timer.schedule(new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                try {
                    Downloader dl = new Downloader();
                    try {
                        System.out.println("downloadFilesNormal");
                        Object rowData[] = new Object[3];
                        model.getDataVector().removeAllElements();
                        model.fireTableDataChanged();
                        for (int i = 0; i < list.size(); i++) {
                            Link link = list.get(i);
                            rowData[0] = i + 1;
                            rowData[1] = link.getLink();
                            rowData[2] = Shared.STATUS_DOWNLOADING;
                            model.addRow(rowData);
                        }
                        dl.downloadFilesNormal(df.format(scheduletime), false, downloadlocation);
                    } catch (IOException ex) {
                        Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ArrayList<Link> linklist = dbcon.getLinksForNormalbytime(df.format(scheduletime));
                    System.out.println(new Date() + ":" + linklist.size());
                    Object rowData1[] = new Object[3];
                    model.getDataVector().removeAllElements();
                    model.fireTableDataChanged();
                    ArrayList<String> successlist = new ArrayList<>();
                    ArrayList<String> failedlist = new ArrayList<>();
                    //  jTable1.setModel(new DefaultTableModel(null, new String[]{"Id","Link","Status"}));
                    for (int i = 0; i < linklist.size(); i++) {
                        Link link = linklist.get(i);
                        rowData1[0] = i + 1;
                        rowData1[1] = link.getLink();
                        rowData1[2] = link.getStatus();
                        if (link.getStatus() != null && link.getStatus().equals(Shared.STATUS_COMPLETED)) {
                            successlist.add(link.getStatus());
                        }
                        if (link.getStatus() != null && link.getStatus().equals(Shared.STATUS_FAILD)) {
                            failedlist.add(link.getStatus());
                        }
                        model.addRow(rowData1);
                    }
                    DownloadData data = dbcon.getDownloadDetails(df.format(scheduletime));
                    //  jTable1.setModel(new DefaultTableModel(null, new String[]{"Id","Link","Status"}));
                    // jTable1.updateUI();
                    System.out.println("data.getStatus()" + data.getStatus());
                    // if (data != null && data.getStatus() != null && data.getStatus().equals(Shared.STATUS_COMPLETED)) {
                    jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/complete.png")));
                    jLabel4.setVisible(true);
                    tfsuccess.setText("" + successlist.size());
                    tffailed.setText("" + failedlist.size());
                    jLabelSuccess.setVisible(true);
                    jLabelfail.setVisible(true);
                    tfsuccess.setVisible(true);
                    tffailed.setVisible(true);
                    timer1.cancel();
                    timer1.purge();
                    timer.cancel();
                    timer.purge();
                    //  }
                    // jTable1.updateUI();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    SimpleDateFormat df = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
                }
            }
        }, begin, timeInterval1
        );
        /*    timer1.schedule(new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                try {
                    //call the method
                    DownloadData data = dbcon.getDownloadDetails(df.format(scheduletime));
                    //  jTable1.setModel(new DefaultTableModel(null, new String[]{"Id","Link","Status"}));
                    // jTable1.updateUI();
                    System.out.println("data.getStatus()" + data.getStatus());
                    if (data != null && data.getStatus() != null && data.getStatus().equals(Shared.STATUS_COMPLETED)) {
                        jLabel4.setIcon(new javax.swing.ImageIcon("/home/saranga/Desktop/complete.png"));
                        jLabel4.setVisible(true);
                        timer1.cancel();
                        timer1.purge();
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    SimpleDateFormat df = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
                }
            }
        }, begin, timeInterval
        );*/
    }

    DownloadProgressForNormal(boolean b) {

    }

    private void setDataToTable() {
        Object rowData[] = new Object[3];
        for (int i = 0; i < list.size(); i++) {
            rowData[0] = list.get(i).getId();
            rowData[1] = list.get(i).getLink();
            rowData[2] = list.get(i).getStatus();
            model.addRow(rowData);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        cancelDownloading = new javax.swing.JButton();
        tfsuccess = new javax.swing.JTextField();
        jLabelSuccess = new javax.swing.JLabel();
        jLabelfail = new javax.swing.JLabel();
        tffailed = new javax.swing.JTextField();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/4206344.png"))); // NOI18N

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Real Time Downloader");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Download Process Status", 0, 0, new java.awt.Font("Dialog", 0, 12), new java.awt.Color(0, 24, 255))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Download Links", "Download Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(50);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
            jTable1.getColumnModel().getColumn(2).setMinWidth(200);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setMaxWidth(200);
        }

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/ajax-loader.gif"))); // NOI18N

        cancelDownloading.setBackground(new java.awt.Color(255, 255, 255));
        cancelDownloading.setText("Cancel ");
        cancelDownloading.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDownloadingActionPerformed(evt);
            }
        });

        tfsuccess.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        tfsuccess.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfsuccess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfsuccessActionPerformed(evt);
            }
        });

        jLabelSuccess.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelSuccess.setForeground(new java.awt.Color(0, 0, 0));
        jLabelSuccess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSuccess.setText("Success");

        jLabelfail.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelfail.setForeground(new java.awt.Color(0, 0, 0));
        jLabelfail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelfail.setText("Failed");

        tffailed.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tffailed.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cancelDownloading, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelSuccess)
                .addGap(3, 3, 3)
                .addComponent(tfsuccess, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelfail)
                .addGap(4, 4, 4)
                .addComponent(tffailed, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfsuccess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSuccess)
                    .addComponent(jLabelfail)
                    .addComponent(tffailed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelDownloading)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelDownloadingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDownloadingActionPerformed
        if (timer1 != null) {
            timer1.cancel();
            timer1.purge();
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        Downloader dl = new Downloader();
        System.out.println("downloadFilesNormal");
        dl.deleteLatestFile(true);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/cancel.png")));
        jLabel4.setVisible(true);
        if (timer1 != null) {
            timer1.cancel();
            timer1.purge();
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }//GEN-LAST:event_cancelDownloadingActionPerformed

    private void tfsuccessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfsuccessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfsuccessActionPerformed
    public boolean isValidDate(Date pDateString) throws ParseException {
        System.out.println("download date time" + pDateString);
        return new Date().before(pDateString);
    }

    private void validateObject() {
        try {
            DBController controller = new DBController();
            SettingsData settings = controller.searchSetting(Shared.FIRE_TIME);
            SettingsData settings2 = controller.searchSetting(Shared.DL_LOCATION);
            boolean islocationset = false;
            boolean istimeset = false;
            if (settings != null && settings.getValue() != null && !settings.getValue().isEmpty()) {
                istimeset = true;
            }
            if (settings2 != null && settings2.getValue() != null && !settings2.getValue().isEmpty()) {
                islocationset = true;
            }
            if (istimeset == false && islocationset == false) {
                JOptionPane.showMessageDialog(this, "Please set the  download location and download start time");
            } else if (istimeset == false && islocationset == true) {
                JOptionPane.showMessageDialog(this, "Please set the  download start time");

            } else if (istimeset == true && islocationset == false) {
                JOptionPane.showMessageDialog(this, "Please set the  download location ");

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void iterate(ArrayList<Link> list) {
        Downloader dl = new Downloader();
        for (int i = 0; i <= list.size(); i++) {
            Link link = list.get(i);
            jb.setValue(i);
            i = i + 20;
            try {
                dl.downloadFileNormal(link);
                Thread.sleep(150);
            } catch (Exception e) {
            }
        }
    }

    void startDownload(ArrayList<Link> list) {
        //this.getParent().add(jb).setVisible(true);
        System.out.println("list  size" + list.size());
        // iterate(list);
        Downloader dl = new Downloader();
        for (int i = 0; i < list.size(); i++) {
            try {
                Link link = list.get(i);
                System.out.println("DL link");
                dl.downloadFileNormal(link);
                Thread.sleep(1000);
                System.out.println("link:" + link.getLink());
            } catch (IOException ex) {
                Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadProgressForNormal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void downloadFileSchdule(boolean iscancel) throws IOException, ClassNotFoundException, SQLException {
        DBController con = new DBController();
        String home = System.getProperty("user.home");
        File file = new File(home + "/Downloads/");
        String filepath = file.getPath();
        if (iscancel) {
            System.out.println("iscancel" + iscancel);
            for (int i = 0; i < chanellist.size(); i++) {
                ReadableByteChannel chanel = chanellist.get(i);
                chanel.close();
                System.out.println("closed  1...........");

            }
            for (int i = 0; i < fileoutlist.size(); i++) {
                FileOutputStream fos = fileoutlist.get(i);
                fos.close();
                System.out.println("closed  2..........");
            }
        } else {
            SimpleDateFormat df = new SimpleDateFormat("ddMMyy-hhmmss.SSS");
            ArrayList<Link> returnlist = new ArrayList<>();
            ArrayList<Link> list = con.getLinksForSchedule();
            for (int i = 0; i < list.size(); i++) {
                Link link = list.get(i);
                if (link != null) {
                    Link object = link;
                    String filename = null;
                    filename = "file_" + i + "_" + df.format(new Date());
                    String filetype = null;
                    this.filetype = filetype(object.getLink());
                    filepath = filepath + "/" + filename + filetype;
                    System.out.println("Download  LInk" + object.getLink());
                    System.out.println("filepath" + filepath);
                    boolean isdownload = false;
                    try {
                        object.setStatus(Shared.STATUS_DOWNLOADING);
                        con.updateLink(object);
                        URL url = new URL(object.getLink());
                        rbc = Channels.newChannel(url.openStream());
                        chanellist.add(rbc);
                        fos = new FileOutputStream(filepath);
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
        }

    }

    private String filetype(String url) {
        String filetype = ".mp4";
        if (url.contains(".mp3")) {
            filetype = ".mp3";
        } else if (url.contains(".mp4")) {
            filetype = ".mp4";
        } else if (url.contains(".pdf")) {
            filetype = ".pdf";
        } else if (url.contains(".docx")) {
            filetype = ".docx";
        } else if (url.contains(".aif")) {
            filetype = ".aif";
        } else if (url.contains(".cda")) {
            filetype = ".cda";
        } else if (url.contains(".mid")) {
            filetype = ".mid";
        } else if (url.contains(".midi")) {
            filetype = ".midi";
        } else if (url.contains(".mpa")) {
            filetype = ".mpa";
        } else if (url.contains(".ogg")) {
            filetype = ".ogg";
        } else if (url.contains(".wav")) {
            filetype = ".wav";
        } else if (url.contains(".wma")) {
            filetype = ".wma";
        } else if (url.contains(".wpl")) {
            filetype = ".wpl";
        } else if (url.contains(".7z")) {
            filetype = ".7z";
        } else if (url.contains(".arj")) {
            filetype = ".arj";
        } else if (url.contains(".deb")) {
            filetype = ".deb";
        } else if (url.contains(".pkg")) {
            filetype = ".pkg";
        } else if (url.contains(".deb")) {
            filetype = ".deb";
        } else if (url.contains(".rar")) {
            filetype = ".rar";
        } else if (url.contains(".rpm")) {
            filetype = ".rpm";
        } else if (url.contains(".tar.gz ")) {
            filetype = ".tar.gz ";
        } else if (url.contains(".zip")) {
            filetype = ".zip";
        } else if (url.contains(".bin")) {
            filetype = ".bin";
        } else if (url.contains(".dmg")) {
            filetype = ".dmg";
        } else if (url.contains(".iso")) {
            filetype = ".iso";
        } else if (url.contains(".toast")) {
            filetype = ".toast";
        } else if (url.contains(".vcd")) {
            filetype = ".vcd";
        } else if (url.contains(".csv")) {
            filetype = ".csv";
        } else if (url.contains(".dat")) {
            filetype = ".dat";
        } else if (url.contains(".db")) {
            filetype = ".db";
        } else if (url.contains(".dbf")) {
            filetype = ".dbf";
        } else if (url.contains(".log")) {
            filetype = ".log";
        } else if (url.contains(".mdb")) {
            filetype = ".mdb";
        } else if (url.contains(".sav")) {
            filetype = ".sav";
        } else if (url.contains(".sql")) {
            filetype = ".sql";
        } else if (url.contains(".tar")) {
            filetype = ".tar";
        } else if (url.contains(".xml")) {
            filetype = ".xml";
        } else if (url.contains(".email")) {
            filetype = ".email";
        } else if (url.contains(".apk")) {
            filetype = ".apk";
        } else if (url.contains(".bat")) {
            filetype = ".bat";
        } else if (url.contains(".exe")) {
            filetype = ".exe";
        } else if (url.contains(".jpeg")) {
            filetype = ".jpeg";
        } else if (url.contains(".jpg")) {
            filetype = ".jpg";
        } else if (url.contains(".png")) {
            filetype = ".png";
        }
        return filetype;
    }

    public static DownloadProgressForNormal getInstance() {
        if (myInstance == null) {
            myInstance = new DownloadProgressForNormal();
        }
        return myInstance;
    }

    public class Multithred implements Runnable {

        public void run() {
            System.out.println("thread is running...");

        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelDownloading;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelSuccess;
    private javax.swing.JLabel jLabelfail;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField tffailed;
    private javax.swing.JTextField tfsuccess;
    // End of variables declaration//GEN-END:variables
}

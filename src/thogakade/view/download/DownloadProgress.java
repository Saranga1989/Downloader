/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thogakade.view.download;

import java.awt.Color;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
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
public class DownloadProgress extends javax.swing.JInternalFrame {

    ReadableByteChannel rbc = null;
    FileOutputStream fos = null;
    String filename = null;
    String filetype = null;
    private static DownloadProgress myInstance;
    ArrayList<ReadableByteChannel> chanellist = new ArrayList<>();
    ArrayList<FileOutputStream> fileoutlist = new ArrayList<>();
    JProgressBar jb;
    int i = 0, num = 0;
    ArrayList<Link> list = null;
    SimpleDateFormat df = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);
    DefaultTableModel model = null;
    ImageIcon loading = new ImageIcon(getClass().getResource("/thogakade/icon/ajax-loader.gif"));
    Multithred mt = null;
    Thread t1 = null;
    int begin = 0;
    int timeInterval = 30000;
    Timer timer = null;
    Timer timer1 = null;
    Timer timer2 = null;

    /**
     * Creates new form CreateOrder
     */
    public DownloadProgress() {
        initComponents();
        dlStartTIme.setBorder(BorderFactory.createLineBorder(Color.white));
        tfsuccess.setBorder(BorderFactory.createLineBorder(Color.white));
        tffailed.setBorder(BorderFactory.createLineBorder(Color.white));

        //dlStartTIme.setBorder(new EmptyBorder(0, 0, 0, 0));
        // timer = new Timer();
        //  timer1 = new Timer();
        /* timer.schedule(
                new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                //call the method
                System.out.println(".run()" + new Date());
            }
        }, begin, timeInterval
        );*/
        //JLabel  label=new JLabel("loading... ", loading, JLabel.CENTER);
        //this.add(label);
    }

    public DownloadProgress(ArrayList<Link> list, final Date scheduletime, final String locationdlpath) {
        initComponents();
        this.list = list;
        final Downloader dl = new Downloader();
        jLabelSuccess.setVisible(false);
        jLabelfail.setVisible(false);
        tfsuccess.setVisible(false);
        tffailed.setVisible(false);
        final DBController dbcon = new DBController();
        dlStartTIme.setText(df.format(scheduletime));
        dlStartTIme.setBorder(BorderFactory.createLineBorder(Color.white));
        model = (DefaultTableModel) jTable1.getModel();
        timer = new Timer();
        timer1 = new Timer();
        // timer2.schedule(new DownloadProgress.MyTimeTask(list, df.format(scheduletime)), scheduletime);
        timer.schedule(
                new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                try {
                    //call the method
                    Date currunttime = new Date();
                    System.out.println(".run()" + currunttime);
                    if ((currunttime.equals(scheduletime)) || (currunttime.after(scheduletime))) {
                        System.out.println("currunttime.equals(scheduletime) ");
                        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/ajax-loader.gif"))); // NOI18N
                        jLabel4.setVisible(true);
                        ArrayList<Link> linklist = dbcon.getLinksForSchedule(df.format(scheduletime));
                        Object rowData[] = new Object[3];
                        model.getDataVector().removeAllElements();
                        model.fireTableDataChanged();
                        for (int i = 0; i < linklist.size(); i++) {
                            Link link = linklist.get(i);
                            rowData[0] = i + 1;
                            rowData[1] = link.getLink();
                            rowData[2] = Shared.STATUS_DOWNLOADING;
                            model.addRow(rowData);
                        }
                        dl.downloadFileSchdule(false, df.format(scheduletime), locationdlpath);

                    }
                    ArrayList<String> successlist = new ArrayList<>();
                    ArrayList<String> failedlist = new ArrayList<>();
                    ArrayList<Link> linklist = dbcon.getLinksForSchedule(df.format(scheduletime));
                    System.out.println(new Date() + ":" + linklist.size());
                    Object rowData[] = new Object[3];
                    model.getDataVector().removeAllElements();
                    model.fireTableDataChanged();
                    //  jTable1.setModel(new DefaultTableModel(null, new String[]{"Id","Link","Status"}));
                    for (int i = 0; i < linklist.size(); i++) {
                        Link link = linklist.get(i);
                        rowData[0] = i + 1;
                        rowData[1] = link.getLink();
                        rowData[2] = link.getStatus();
                        if (link.getStatus() != null && link.getStatus().equals(Shared.STATUS_COMPLETED)) {
                            successlist.add(link.getStatus());
                        }
                        if (link.getStatus() != null && link.getStatus().equals(Shared.STATUS_FAILD)) {
                            failedlist.add(link.getStatus());
                        }
                        model.addRow(rowData);
                    }
                    DownloadData data = dbcon.getDownloadDetails(df.format(scheduletime));
                    //  jTable1.setModel(new DefaultTableModel(null, new String[]{"Id","Link","Status"}));
                    // jTable1.updateUI();
                    if (data != null && data.getStatus() != null && data.getStatus().equals(Shared.STATUS_COMPLETED)) {
                        jLabelSuccess.setVisible(true);
                        jLabelfail.setVisible(true);
                        tfsuccess.setText("" + successlist.size());
                        tffailed.setText("" + failedlist.size());
                        tfsuccess.setVisible(true);
                        tffailed.setVisible(true);
                        //jLabel4.setIcon(new javax.swing.ImageIcon("/home/saranga/Desktop/complete.png"));
                        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/complete.png")));
                        jLabel4.setVisible(true);
                        if (timer != null) {
                            timer.cancel();
                            timer.purge();
                        }
                        if (timer1 != null) {
                            timer1.cancel();
                            timer1.purge();
                        }
                    }
                    // jTable1.updateUI();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
                } finally {

                }
            }
        }, begin, timeInterval
        );
        timer1.schedule(
                new TimerTask() {
            int counter = 0;

            @Override
            public void run() {
                try {
                    //call the method
                    DownloadData data = dbcon.getDownloadDetails(df.format(scheduletime));
                    //  jTable1.setModel(new DefaultTableModel(null, new String[]{"Id","Link","Status"}));
                    // jTable1.updateUI();
                    if (data != null && data.getStatus() != null && data.getStatus().equals(Shared.STATUS_COMPLETED)) {

                        //jLabel4.setIcon(new javax.swing.ImageIcon("/home/saranga/Desktop/complete.png"));
                        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/complete.png")));
                        jLabel4.setVisible(true);
                        if (timer != null) {
                            timer.cancel();
                            timer.purge();
                        }
                        if (timer1 != null) {
                            timer1.cancel();
                            timer1.purge();
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
                } finally {

                }
            }
        }, begin, timeInterval
        );
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
        jLabel3 = new javax.swing.JLabel();
        dlStartTIme = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cancelDownloading = new javax.swing.JButton();
        tffailed = new javax.swing.JTextField();
        tfsuccess = new javax.swing.JTextField();
        jLabelfail = new javax.swing.JLabel();
        jLabelSuccess = new javax.swing.JLabel();

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
        jLabel1.setText("Schedule Downloader");

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

        jLabel3.setText("Download Start time:");

        dlStartTIme.setEditable(false);
        dlStartTIme.setBackground(new java.awt.Color(255, 255, 255));
        dlStartTIme.setOpaque(false);
        dlStartTIme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dlStartTImeActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/pending7.jpeg"))); // NOI18N

        cancelDownloading.setBackground(new java.awt.Color(255, 255, 255));
        cancelDownloading.setText("Cancel ");
        cancelDownloading.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDownloadingActionPerformed(evt);
            }
        });

        tffailed.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tffailed.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        tfsuccess.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        tfsuccess.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfsuccess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfsuccessActionPerformed(evt);
            }
        });

        jLabelfail.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelfail.setForeground(new java.awt.Color(0, 0, 0));
        jLabelfail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelfail.setText("Failed");

        jLabelSuccess.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabelSuccess.setForeground(new java.awt.Color(0, 0, 0));
        jLabelSuccess.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSuccess.setText("Success");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cancelDownloading, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelSuccess)
                        .addGap(3, 3, 3)
                        .addComponent(tfsuccess, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelfail)
                        .addGap(4, 4, 4)
                        .addComponent(tffailed, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dlStartTIme, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(338, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dlStartTIme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 184, Short.MAX_VALUE)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfsuccess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelSuccess)
                    .addComponent(jLabelfail)
                    .addComponent(tffailed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelDownloading))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void dlStartTImeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dlStartTImeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dlStartTImeActionPerformed

    private void cancelDownloadingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDownloadingActionPerformed
        if (timer1 != null) {
            timer1.cancel();
            timer1.purge();
        }
        timer1 = null;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = null;
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

        if (DownloadSchduler.timer != null) {
            DownloadSchduler.timer.cancel();
            DownloadSchduler.timer.purge();
        }

        timer1 = null;
        timer = null;

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
                Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
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

    public static DownloadProgress getInstance() {
        if (myInstance == null) {
            myInstance = new DownloadProgress();
        }
        return myInstance;
    }

    public class Multithred implements Runnable {

        public void run() {
            System.out.println("thread is running...");

        }
    }
    /*
    private static class MyTimeTask extends TimerTask {

        DefaultTableModel tblModel = null;
        private JTable table = null;
        private JProgressBar progressBar = null;
        String sheduleDatetime = null;
        ArrayList<Link> list = null;
        DownloadProgress pg = new DownloadProgress();
        SimpleDateFormat df = new SimpleDateFormat(Shared.DF_TRANSACTION_DATE_FORMAT);

        MyTimeTask(ArrayList<Link> list, String sheduleDatetime) {
            this.list = list;
            this.sheduleDatetime = sheduleDatetime;
        }

        Downloader dl = new Downloader();

        public void run() {
            try {
                Date currunttime = new Date();
                if ((currunttime.equals(df.parse(sheduleDatetime))) || (currunttime.after(df.parse(sheduleDatetime)))) {
                    System.out.println("currunttime.equals(scheduletime) ");
                    pg.jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/thogakade/icon/ajax-loader.gif"))); // NOI18N
                    pg.jLabel4.setVisible(true);
                    dl.downloadFileSchdule(false, sheduleDatetime);

                }
            } catch (IOException ex) {
                Logger.getLogger(DownloadSchduler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DownloadSchduler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DownloadSchduler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(DownloadProgress.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelDownloading;
    private javax.swing.JTextField dlStartTIme;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
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

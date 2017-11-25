package Videot;  
  
import java.awt.BorderLayout;  
import java.awt.Color;  
import java.awt.Component;  
import java.awt.Dimension;  
import java.awt.FlowLayout;  
import java.awt.Graphics;  
import java.awt.Rectangle;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.IOException;  
  
import javax.media.ControllerEvent;  
import javax.media.ControllerListener;  
import javax.media.DataSink;  
import javax.media.NoPlayerException;  
import javax.media.Player;  
import javax.media.Processor;  
import javax.media.protocol.DataSource;  
import javax.swing.ImageIcon;  
import javax.swing.JButton;  
import javax.swing.JPanel;  
  
public class PlayPane extends JPanel {  
    private ImageIcon videoReqIcon = new ImageIcon("videoReq.jpg");  
    private ImageIcon VideolocalIcon = new ImageIcon("localVideo.jpg");  
    private boolean isViewBigPlaying = false;  
    private boolean isViewSmallPlaying = false;  
    private JPanel viewBigPane;  
    private JPanel viewSmallPane;  
    private JPanel controlPane;  
  
    private JButton closeButton;  
  
    private boolean localPlay = false;  
    private boolean remotePlay = false;  
  
    private DataSource localData;  
    private DataSource remoteData;  
  
    private boolean isViewRun = true;  
    private boolean isShow = true;  
    //  
    private Player localPlayer = null;  
    private Player remotePlayer = null;  
    //  
    private Processor videotapeProcessor = null;  
    private Player videotapePlayer = null;  
    private DataSink videotapeFileWriter;  
  
    public PlayPane() {  
        this.setLayout(new BorderLayout());  
     
        viewBigPane = new JPanel() {  
            public void paintComponent(Graphics g) {  
                super.paintComponent(g);  
                if (!isViewBigPlaying) {  
                    g.drawImage(videoReqIcon.getImage(), 1, 1,  
                            videoReqIcon.getIconWidth(),  
                            videoReqIcon.getIconHeight(), null);  
  
                    g.drawRect(getSmallPlayRec().x - 1,  
                            getSmallPlayRec().y - 1,  
                            getSmallPlayRec().width + 1,  
                            getSmallPlayRec().height + 1);  
                } else {  
  
                }  
            }  
        };  
        viewBigPane.setBackground(Color.black);  
        this.add(viewBigPane, BorderLayout.CENTER);  
        viewBigPane.setLayout(null);  
 
        viewSmallPane = new JPanel() {  
            public void paintComponent(Graphics g) {  
                super.paintComponent(g);  
                if (!isViewSmallPlaying) {  
                    g.drawImage(VideolocalIcon.getImage(), 0, 0, null);  
                } else {  
  
                }  
            }  
        };  
        viewSmallPane.setBounds(getSmallPlayRec());  
        viewBigPane.add(viewSmallPane);  
        viewSmallPane.setLayout(null);  
  
 
        closeButton = new JButton("Cancel");  
        controlPane = new JPanel();  
        controlPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));  
        controlPane.add(closeButton);  
        this.add(controlPane, BorderLayout.SOUTH);  
        closeButton.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (localPlayer != null) {  
                    localPlayer.stop();  
                }  
                if (remotePlayer != null) {  
                    remotePlayer.stop();  
                }  
                if (videotapePlayer != null) {  
                    videotapePlayer.stop();  
                }  
                if (videotapeProcessor != null) {  
                    videotapeProcessor.stop();  
                }  
                if (videotapeFileWriter != null) {  
                    try {  
                        videotapeFileWriter.stop();  
                        videotapeFileWriter.close();  
                    } catch (IOException e1) {  
                    }  
                }  
            }  
  
        });  
        
  
    }  
  
    public Dimension getMinimumSize() {  
        System.out  
                .println("controlPane.getHeight():" + controlPane.getHeight());  
        return new Dimension(videoReqIcon.getIconWidth() + 2,  
                videoReqIcon.getIconHeight() + controlPane.getHeight());  
    }  
  
    public Dimension getPreferredSize() {  
        System.out  
                .println("controlPane.getHeight():" + controlPane.getHeight());  
        return new Dimension(videoReqIcon.getIconWidth() + 2,  
                videoReqIcon.getIconHeight()  
                        + controlPane.getPreferredSize().height);  
    }  
  
    public void localPlay(DataSource dataSource) {  
        this.setLocalData(dataSource);  
        try {  
            localPlayer = javax.media.Manager.createPlayer(dataSource);  
  
            localPlayer.addControllerListener(new ControllerListener() {  
                public void controllerUpdate(ControllerEvent e) {  
  
                    if (e instanceof javax.media.RealizeCompleteEvent) {  
                        Component comp = null;  
                        comp = localPlayer.getVisualComponent();  
                        if (comp != null) {  
                           
                            comp.setBounds(0, 0, VideolocalIcon.getIconWidth(),  
                                    VideolocalIcon.getIconHeight());  
                            viewSmallPane.add(comp);  
                        }  
                        viewBigPane.validate();  
                    }  
                }  
            });  
            localPlayer.start();  
            localPlay = true;  
        } catch (NoPlayerException e1) {  
            e1.printStackTrace();  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
    }  
  
    private Rectangle getSmallPlayRec() {  
        int bigShowWidth = videoReqIcon.getIconWidth();  
        int bigShowHeight = videoReqIcon.getIconHeight();  
        int smallShowWidth = VideolocalIcon.getIconWidth();  
        int smallShowHeight = VideolocalIcon.getIconHeight();  
        return new Rectangle(bigShowWidth - smallShowWidth - 2, bigShowHeight  
                - smallShowHeight - 2, smallShowWidth, smallShowHeight);  
    }  
  
    public void remotePlay(DataSource dataSource) {  
        this.setLocalData(dataSource);  
        remotePlay = true;  
        try {  
            remotePlayer = javax.media.Manager.createPlayer(dataSource);  
  
            remotePlayer.addControllerListener(new ControllerListener() {  
                public void controllerUpdate(ControllerEvent e) {  
  
                    if (e instanceof javax.media.RealizeCompleteEvent) {  
                        Component comp;  
                        if ((comp = remotePlayer.getVisualComponent()) != null) {  
                          
                            comp.setBounds(1, 1, videoReqIcon.getIconWidth(),  
                                    videoReqIcon.getIconHeight());  
                            viewBigPane.add(comp);  
                        }  
                        viewBigPane.validate();  
                    }  
                }  
            });  
            remotePlayer.start();  
            remotePlay = true;  
        } catch (NoPlayerException e1) {  
            e1.printStackTrace();  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
    }  
  
    public void closeViewUI() {  
        isShow = false;  
    }  
  
    public boolean isViewRunning() {  
        return isViewRun;  
    }  
  
    public boolean isShowing() {  
        return isShow;  
    }  
  
    public void localReady() {  
        localPlay = true;  
    }  
  
    public void remoteReady() {  
        remotePlay = true;  
    }  
  
    public boolean isRemotePlay() {  
        return remotePlay;  
    }  
  
    public void setRemotePlay(boolean remotePlay) {  
        this.remotePlay = remotePlay;  
    }  
  
    public DataSource getRemoteData() {  
        return remoteData;  
    }  
  
    public void setRemoteData(DataSource remoteData) {  
        this.remoteData = remoteData;  
    }  
  
    public boolean isLocalPlay() {  
        return localPlay;  
    }  
  
    public void setLocalPlay(boolean localPlay) {  
        this.localPlay = localPlay;  
    }  
  
    public DataSource getLocalData() {  
        return localData;  
    }  
  
    public void setLocalData(DataSource localData) {  
        this.localData = localData;  
    }  
  
}  
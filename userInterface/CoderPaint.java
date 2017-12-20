package com.coder.userInterface;
/**
 * Created by angjelinmalaj on 9/08/17.
 */
import com.apple.eawt.Application;
import com.bric.swing.ColorPicker;
import com.coder.languages.LoadLanguage;
import com.coder.shapes.EnumRope;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Properties;

public class CoderPaint implements LoadLanguage {

    private JFrame frame;
    private String fontName;
    private TabbedPane myPane;
    protected ColorPicker picker;
    private final JLabel jl, jll;
    private final JMenuBar menuBar;
    private boolean status = false;
    private boolean isSaved = false;
    private JSplitPane top, jSplitPane;
    private final JSeparator separator;
    private final JScrollPane scrollPane;
    private final DrawGround drawingArea;
    private JCheckBoxMenuItem guideLines;
    private int  fontSize = 8, fontStyle = Font.PLAIN;
    private final JButton corner;
    private final JMenu mnFile, mnEdit, mnPicture, mnLang, mnThemes, mnAbout;
    private final JMenuItem chckbxmn1, chckbxmn2, chckbxmn5;
    private ImageIcon cursorImage2 = new ImageIcon(getClass().getResource("/com/coder/icons/Precision.png"));
    private Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage2.getImage(),
            new Point(12, 15), "Target"), eraserCursor;
    private final JMenuItem mntmExit, mntmUndo, mntmCopy, mntmPaste, sendMail, mntmSave, mntRotateImg,
            mntFlipImg, mntmScreenCap, mntEnglish;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new CoderPaint();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
        }

    }

    /**
     * Building the Frame
     */
    public CoderPaint() {
        super();
        frame = new JFrame();
        frame.setIconImage(
                Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/coder/icons/Applogo.png")));
        frame.setBackground(Color.BLUE);
        frame.setTitle("Angjelin Malaj");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setMinimumSize(new Dimension(600, 375));
        frame.setMaximumSize(screenSize);
        frame.setLocationRelativeTo(null);

        frame.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (isSaved == false) {
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit without saving?",
                            "Approve", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (response) {
                        case JOptionPane.YES_OPTION:
                            System.exit(0);
                            break;
                        case JOptionPane.NO_OPTION:
                            drawingArea.SaveImage();
                            isSaved = true;
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                            break;
                        default:
                            break;
                    }
                    super.windowClosing(e);
                } else {
                    System.exit(0);
                }
            }
        });
//        This class is the abstract superclass of all actual implementations of the Abstract Window Toolkit.
//        Subclasses of the Toolkit class are used to bind the various components
//        to particular native toolkit implementations

        String version = System.getProperty("os.name").toLowerCase();
        if (version.contains("windows") || version.contains("nux")) {
            frame.setIconImage(
                    Toolkit.getDefaultToolkit().getImage(getClass().getResource("/com/coder/icons/Applogo.png")));
        } else {
            Application.getApplication()
                    .setDockIconImage(new ImageIcon(getClass().getResource("/com/coder/icons/Applogo.png")).getImage());
        }

        drawingArea = new DrawGround();
        drawingArea.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, null, null, Color.DARK_GRAY));
        drawingArea.setDoubleBuffered(false);

        myPane = new TabbedPane();
        myPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        myPane.setPreferredSize(new Dimension(145, 800));
        myPane.setMinimumSize(new Dimension(145, 720));
        myPane.setFont(new Font("Dialog", Font.PLAIN, 13));
        myPane.setAutoscrolls(true);

        picker = new ColorPicker(false, false);
        picker.setName("HueColors");
        picker.setPreferredSize(new Dimension(120, 100));
        picker.setMode(ColorPicker.HUE);
        picker.addPropertyChangeListener(ColorPicker.SELECTED_COLOR_PROPERTY, (PropertyChangeEvent e) -> {

            drawingArea.setColor(picker.getColor());
            drawingArea.changePanelColor(myPane.panel);
        });
        myPane.extrasPanel.add(picker);

        final ActionListener action = (ActionEvent e) -> {
            String command = e.getActionCommand();

            if (command.equalsIgnoreCase("Pencil")) {
                frame.setCursor(costumCursor("/com/coder/icons/quill.png", 5, 27));
                drawingArea.figures = EnumRope.PENCIL;
                drawingArea.redraw();
            } else if (command.equalsIgnoreCase("Eraser")) {
                eraserCursor = Toolkit.getDefaultToolkit().createCustomCursor(drawingArea.getCursorImage(),
                        new Point(10, 10), "Eraser");
                frame.setCursor(eraserCursor);
                drawingArea.figures = EnumRope.ERASER;
                drawingArea.repaint();
            } else if (command.equalsIgnoreCase("  Line")) {
                drawingArea.figures = EnumRope.LINE;
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);

                }
            } else if (command.contains("Clear")) {
                int x = JOptionPane.showConfirmDialog(null, "Are you sure?", "Clear All", JOptionPane.YES_NO_OPTION);
                if (x == 0) {
                    drawingArea.clearArea();
                } else {
                    return;
                }
            } else if (command.equalsIgnoreCase("Curved")) {
                drawingArea.figures = EnumRope.CURVE;
                drawingArea.redraw();
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
            } else if (command.equalsIgnoreCase("  Circle")) {
                drawingArea.figures = EnumRope.OVAL;
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
            } else if (command.equalsIgnoreCase("Rectan.")) {
                drawingArea.figures = EnumRope.RECT;
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
            } else if (command.equalsIgnoreCase("Polygon")) {
                drawingArea.figures = EnumRope.TRIANGLE;
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
            } else if (command.contains("Select")) {
                drawingArea.figures = EnumRope.SELECTION;
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
            } else if (command.equalsIgnoreCase("AutoFill")) {
                frame.setCursor(costumCursor("/com/coder/icons/PaintBucket.png", 5, 25));
                drawingArea.figures = EnumRope.AUTOFILLED;
            } else if (command.equalsIgnoreCase("Bucket")) {
                frame.setCursor(costumCursor("/com/coder/icons/PaintBucket.png", 5, 25));
                drawingArea.figures = EnumRope.AUTOFILLED;
            } else if (command.equalsIgnoreCase(" Text")) {
                frame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
                drawingArea.figures = EnumRope.TEXT;
                drawingArea.setTextStroke();
            } else if (command.equalsIgnoreCase("Colors")) {
                drawingArea.ChooseColor();
                drawingArea.changePanelColor(myPane.panel);
            } else if (command.equalsIgnoreCase("Color Picker")) {
                frame.setCursor(costumCursor("/com/coder/icons/picker.png", 5, 25));
                drawingArea.figures = EnumRope.COLORPICKER;
                frame.repaint();
                drawingArea.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (drawingArea.figures == EnumRope.COLORPICKER) {
                            drawingArea.changePanelColor(myPane.panel);
                            super.mousePressed(e);
                        }
                    }
                });

            } else if (command.equalsIgnoreCase(myPane.changeJoinBox.getName())) {
                switch (myPane.changeJoinBox.strokeBox.getSelectedItem().toString()) {
                    case "0":
                        drawingArea.setBasic();
                        break;
                    case "1":
                        drawingArea.setBeveled();
                        break;
                    case "2":
                        drawingArea.setMittered();
                        break;
                    case "3":
                        drawingArea.setRounded();
                        break;
                    default:
                        break;
                }
            } else if (command.equalsIgnoreCase(myPane.editImageBox.editBox.getActionCommand())) {
                switch (myPane.editImageBox.editBox.getSelectedItem().toString()) {
                    case "0":
                        drawingArea.convertToRGB();
                        drawingArea.repaint();
                        break;
                    case "1":
                        drawingArea.BlurImage();
                        drawingArea.repaint();
                        break;
                    case "2":
                        drawingArea.flipImage();
                        drawingArea.RotateImage();
                        drawingArea.repaint();
                        break;
                    case "3":
                        drawingArea.RotateImage();
                        drawingArea.repaint();
                        break;
                    case "4":
                        drawingArea.InvertImage();
                        drawingArea.repaint();
                        break;
                    default:
                        break;
                }
            } else if (command.equalsIgnoreCase("Hitme")) {
                drawingArea.figures = EnumRope.HITME;
            } else if (command.equalsIgnoreCase("    Star")) {
                drawingArea.figures = EnumRope.STAR;
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }

            } else if (command.equalsIgnoreCase("Crop")) {
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
                drawingArea.figures = EnumRope.CROP;
                drawingArea.cropImage();
                top.setDividerLocation((int) drawingArea.getCroppedSize().getWidth());
                jSplitPane.setDividerLocation((int) drawingArea.getCroppedSize().getHeight());
                drawingArea.Notifyuser("Aspect ratio keeped.");

            } else if (command.equalsIgnoreCase("Heart")) {
                if (version.contains("windows")) {
                    frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                } else {
                    frame.setCursor(cursor);
                }
                drawingArea.figures = EnumRope.HEART;

            } else if (command.equalsIgnoreCase("Bold")) {
                if (myPane.chkBold.isSelected()) {
                    myPane.chkBold.setBackground(UIManager.getColor("List.selectionBackground"));
                    fontStyle = Font.BOLD;
                    drawingArea.setFont(fontName, fontStyle, fontSize);
                } else {
                    fontStyle = Font.PLAIN;
                    myPane.chkBold.setBackground(Color.WHITE);
                    drawingArea.setFont(fontName, fontStyle, fontSize);
                }

            } else if (command.equalsIgnoreCase("Italic")) {
                if (myPane.chkItalic.isSelected()) {
                    fontStyle = Font.ITALIC;
                    drawingArea.setFont(fontName, fontStyle, fontSize);
                    myPane.chkItalic.setBackground(UIManager.getColor("List.selectionBackground"));
                } else {
                    fontStyle = Font.PLAIN;
                    myPane.chkItalic.setBackground(Color.WHITE);
                    drawingArea.setFont(fontName, fontStyle, fontSize);
                }


            } else if (command.equalsIgnoreCase("FontBox")) {
                fontName = myPane.fontBox.getSelectedItem().toString();
                drawingArea.setFont(fontName, fontStyle, fontSize);
            } else {
                drawingArea.setBasic();
                drawingArea.repaint();
            }
        };

        final ItemListener itemListener = (ItemEvent e) -> {

            if (myPane.chckbxGredient.isSelected()) {
                drawingArea.setGredient();
            }
            if (myPane.chckbxRandomColor.isSelected()) {
                drawingArea.setRandomColor();
            } else {
                drawingArea.setUnRandomColor();
            }
            if (myPane.fillAllShapes.isSelected()) {
                drawingArea.setAllFilled();
            } else {
                drawingArea.setUnFilled();
            }
            if (myPane.chckbxDashed.isSelected()) {
                drawingArea.setDashed(myPane.btnPencil, myPane.btnText, myPane.slider);
            } else {
                drawingArea.unDashed(myPane.btnPencil, myPane.btnText, myPane.slider);
            }
        };

        final ChangeListener changeListener = (ChangeEvent e) -> {
            String source = e.getSource().toString();
            if (source.contains("javax.swing.JSlider")) {
                myPane.lblDemision.setText("Stroke : " + myPane.slider.getValue());
                drawingArea.setStroke(myPane.slider);
            } else if (source.contains("javax.swing.JSpinner")) {
                fontSize = (int) myPane.spinner.getValue();
                drawingArea.setFont(fontName, fontStyle, fontSize);
            }
        };
        myPane.slider.addChangeListener(changeListener);
        myPane.spinner.addChangeListener(changeListener);
        myPane.addItemListeners(itemListener);
        myPane.addActionToAllComponents(action);

        menuBar = new JMenuBar();
        menuBar.setOpaque(false);
        menuBar.setBackground(new Color(139, 0, 139));
        menuBar.setCursor(Cursor.getDefaultCursor());
        frame.setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        mnFile.setCursor(Cursor.getDefaultCursor());
        mnFile.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        menuBar.add(mnFile);

        mntmSave = new JMenuItem("Save as");
        mntmSave.setCursor(Cursor.getDefaultCursor());
        mntmSave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mntmSave.setMnemonic('S');
        mntmSave.setMnemonic(KeyEvent.VK_CONTROL);
        mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        mntmSave.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/Save.png")));
        mntmSave.addActionListener((ActionEvent e) -> {

            drawingArea.SaveImage();
            isSaved = true;
        });
        mnFile.add(mntmSave);

        mntmExit = new JMenuItem("Exit");
        mntmExit.setCursor(Cursor.getDefaultCursor());
        mntmExit.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        mntmExit.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/Close Window-15.png")));
        mntmExit.setMnemonic(KeyEvent.VK_Q + KeyEvent.VK_CONTROL);
        mntmExit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        mnFile.add(mntmExit);

        mnEdit = new JMenu("Edit");
        mnEdit.setCursor(Cursor.getDefaultCursor());
        mnEdit.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        menuBar.add(mnEdit);

        guideLines = new JCheckBoxMenuItem("Guide Lines");
        guideLines.setCursor(Cursor.getDefaultCursor());
        guideLines.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        guideLines.addActionListener((ActionEvent e) -> {
            if (guideLines.isSelected() == true) {
                drawingArea.figures = EnumRope.GUIDELINES;
                drawingArea.repaint();
            } else {
                drawingArea.figures = EnumRope.PENCIL;
                drawingArea.repaint();
            }
        });
        guideLines.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        guideLines.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/rsz_mathlines.png")));
        guideLines.setMnemonic(KeyEvent.VK_UNDO);
        mnEdit.add(guideLines);

        mntmUndo = new JMenuItem("Undo");
        mntmUndo.setCursor(Cursor.getDefaultCursor());
        mntmUndo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mntmUndo.addActionListener((ActionEvent e) -> {
            drawingArea.undo();
        });
        mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        mntmUndo.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/Undo-15.png")));
        mntmUndo.setMnemonic(KeyEvent.VK_UNDO);
        mnEdit.add(mntmUndo);

        mntmCopy = new JMenuItem("Copy");
        mntmCopy.setCursor(Cursor.getDefaultCursor());
        mntmCopy.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        mntmCopy.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/Copy-15.png")));
        mntmCopy.setMnemonic(KeyEvent.VK_COPY);
        mntmCopy.addActionListener((ActionEvent e) -> {
            drawingArea.figures = EnumRope.COPY;
            drawingArea.addToClipboard();
        });
        mnEdit.add(mntmCopy);

        mntmPaste = new JMenuItem("Paste");
        mntmPaste.setCursor(Cursor.getDefaultCursor());
        mntmPaste.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
        mntmPaste.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/Paste-15.png")));
        mntmPaste.setMnemonic(KeyEvent.VK_PASTE);
        mntmPaste.addActionListener((ActionEvent e) -> {
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

            drawingArea.getFromClipboard();
            drawingArea.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {

                    int count = e.getClickCount();
                    if (count >= 2) {
                        drawingArea.figures = EnumRope.DOIT;
                        drawingArea.Notifyuser("Copied image pasted.");
                    } else {
                        count++;
                        return;
                    }
                }
            });

        });
        mnEdit.add(mntmPaste);

        mnPicture = new JMenu("Picture");
        mnPicture.setCursor(Cursor.getDefaultCursor());
        mnPicture.setFont(new Font("Dialog", Font.PLAIN, 15));
        menuBar.add(mnPicture);


        mntFlipImg = new JMenuItem("Flip image");
        mntFlipImg.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/flip-image.png")));
        mntFlipImg.setCursor(Cursor.getDefaultCursor());
        mntFlipImg.setFont(new Font("Dialog", Font.PLAIN, 14));
        mntFlipImg.addActionListener((ActionEvent e) -> {
            drawingArea.flipImage();
            drawingArea.RotateImage();
            drawingArea.repaint();
        });
        mnPicture.add(mntFlipImg);

        mntRotateImg = new JMenuItem("Rotate image");
        mntRotateImg.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/rotate-image.png")));
        mntRotateImg.setCursor(Cursor.getDefaultCursor());
        mntRotateImg.setFont(new Font("Dialog", Font.PLAIN, 14));
        mntRotateImg.addActionListener((ActionEvent e) -> {
            drawingArea.RotateImage();
            drawingArea.repaint();
        });
        mnPicture.add(mntRotateImg);


        mntmScreenCap = new JMenuItem("Take Screenshot");
        mntmScreenCap.setCursor(Cursor.getDefaultCursor());
        mntmScreenCap.setToolTipText("Get the your screen picture and edit  or save it.");
        mntmScreenCap.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mntmScreenCap.addActionListener((ActionEvent e) -> {
            drawingArea.getScreenCapture();
        });
        mntmScreenCap.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/ScreenCap.png")));
        mnPicture.add(mntmScreenCap);

        mnLang = new JMenu("Languages");
        mnLang.setCursor(Cursor.getDefaultCursor());
        mnLang.setFont(new Font("Dialog", Font.PLAIN, 15));
        menuBar.add(mnLang);


        mntEnglish = new JMenuItem("English");
        mntEnglish.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/Usa.png")));
        mntEnglish.setFont(new Font("Dialog", Font.PLAIN, 14));
        mntEnglish.setCursor(Cursor.getDefaultCursor());
        mntEnglish.addActionListener(e -> {
            Locale loc = new Locale("en", "EN");
            frame.setLocale(loc);
            loadLanguage(loc.getDisplayLanguage());
            changeOrientation(frame, ComponentOrientation.LEFT_TO_RIGHT);
            changeOriantationAll(false);
        });
        mnLang.add(mntEnglish);


        mnThemes = new JMenu("Themes");
        mnThemes.setCursor(Cursor.getDefaultCursor());
        mnThemes.setFont(new Font("Dialog", Font.PLAIN, 15));
        menuBar.add(mnThemes);

        chckbxmn1 = new JMenuItem("Aluminium");
        chckbxmn1.setArmed(true);
        chckbxmn1.setCursor(Cursor.getDefaultCursor());
        chckbxmn1.setFont(new Font("Dialog", Font.PLAIN, 14));
        chckbxmn1.addActionListener(e -> {
            changeTheme("aluminium.AluminiumLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        });
        mnThemes.add(chckbxmn1);

        chckbxmn2 = new JMenuItem("Texture");
        chckbxmn2.setCursor(Cursor.getDefaultCursor());
        chckbxmn2.setFont(new Font("Dialog", Font.PLAIN, 14));
        chckbxmn2.addActionListener(e -> {
            changeTheme("texture.TextureLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        });
        mnThemes.add(chckbxmn2);


        chckbxmn5 = new JMenuItem("Default");
        chckbxmn5.setCursor(Cursor.getDefaultCursor());
        chckbxmn5.setFont(new Font("Dialog", Font.PLAIN, 14));
        chckbxmn5.addActionListener(e -> {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e1) {
            }
            SwingUtilities.updateComponentTreeUI(frame);
        });
        mnThemes.add(chckbxmn5);

        mnAbout = new JMenu("Feedback");
        mnAbout.setCursor(Cursor.getDefaultCursor());
        mnAbout.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        mnAbout.setPreferredSize(new Dimension(80, 22));
        menuBar.add(mnAbout);


        sendMail = new JMenuItem("Ask Vanessa Ortiz ");
        sendMail.setCursor(Cursor.getDefaultCursor());
        sendMail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sendMail.addActionListener((ActionEvent e) -> {
            Desktop desktop = Desktop.getDesktop();
            if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.MAIL)) {
                try {
                    URI uriMailTo = new URI("mailto:angjelinmalaj@gmail.com?subject=About%20Coder%20Paint.JAVA");
                    desktop.mail(uriMailTo);
                } catch (Exception e1) {
                    drawingArea.Notifyuser("Desktop is doesn't support Mail to!");
                }
            } else {
                drawingArea.Notifyuser("Desktop API doesn't support!");
            }
        });
        sendMail.setIcon(new ImageIcon(getClass().getResource("/com/coder/icons/MailMe.png")));
        mnAbout.add(sendMail);

        separator = new JSeparator();
        separator.setFocusable(true);
        separator.setForeground(new Color(100, 149, 237));
        separator.setAutoscrolls(true);
        menuBar.add(separator);


        frame.getContentPane().setLayout(new BorderLayout(1, 0));
        frame.getContentPane().add(myPane, BorderLayout.WEST);

        top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        top.setLeftComponent(drawingArea);
        top.setBackground(new Color(240, 240, 240));
        jll = new JLabel();
        jll.setIcon(new ImageIcon(CoderPaint.class.getResource("/com/coder/icons/Gray.png")));
        jll.setMinimumSize(new Dimension(0, 0));
        top.setRightComponent(jll);
        top.setOneTouchExpandable(true);
        top.setContinuousLayout(true);
        top.setEnabled(false);
        top.resetToPreferredSizes();

        jSplitPane.setTopComponent(top);
        jl = new JLabel();
        jl.setIcon(new ImageIcon(CoderPaint.class.getResource("/com/coder/icons/Pulldown.png")));
        jl.setMinimumSize(new Dimension(0, 0));
        jSplitPane.setBottomComponent(jl);
        jSplitPane.setOneTouchExpandable(true);
        jSplitPane.setContinuousLayout(true);
        jSplitPane.resetToPreferredSizes();
        jSplitPane.setBackground(new Color(240, 240, 240));
        jSplitPane.setToolTipText("Pull down, to paint the surface.");
        jSplitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, (PropertyChangeEvent evt) -> {
            double imageHeight = jSplitPane.getDividerLocation();
            double imageWidth = imageHeight * drawingArea.imageProps.getAspectRatio();
            myPane.lblSize.setText("      " + (int) imageWidth + " X " + (int) imageHeight);
            top.setDividerLocation((int) imageWidth);
            drawingArea.changeImageSizeDynmcally((int) imageWidth, (int) imageHeight);
            drawingArea.repaint();

        });
        scrollPane = new JScrollPane(jSplitPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        corner = new JButton();
        corner.setIcon(new ImageIcon(CoderPaint.class.getResource("/com/coder/icons/earth.png")));
        corner.addActionListener(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://"));
                } catch (IOException | URISyntaxException ex) {
                }
            }
        });
        scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(1240, 720));
    }
private void changeOriantationAll(boolean isTrue) {

	if(isTrue) {
		changeOrientation(mnFile, ComponentOrientation.RIGHT_TO_LEFT);
		changeOrientation(mnEdit, ComponentOrientation.RIGHT_TO_LEFT);
		changeOrientation(mnPicture, ComponentOrientation.RIGHT_TO_LEFT);
		changeOrientation(mnLang, ComponentOrientation.RIGHT_TO_LEFT);
		changeOrientation(mnEdit, ComponentOrientation.RIGHT_TO_LEFT);
		changeOrientation(mnAbout, ComponentOrientation.RIGHT_TO_LEFT);
	}else {
		changeOrientation(mnFile, ComponentOrientation.LEFT_TO_RIGHT);
		changeOrientation(mnEdit, ComponentOrientation.LEFT_TO_RIGHT);
		changeOrientation(mnPicture, ComponentOrientation.LEFT_TO_RIGHT);
		changeOrientation(mnLang, ComponentOrientation.LEFT_TO_RIGHT);
		changeOrientation(mnEdit, ComponentOrientation.LEFT_TO_RIGHT);
		changeOrientation(mnAbout, ComponentOrientation.LEFT_TO_RIGHT);

	}
}
	private void changeOrientation(Component c, ComponentOrientation o) {
		c.applyComponentOrientation(o);

		if (c instanceof JMenu) {
			JMenu menu = (JMenu) c;
			int ncomponents = menu.getMenuComponentCount();
			for (int i = 0; i < ncomponents; ++i) {
				changeOrientation(menu.getMenuComponent(i), o);

			}
		}

		if (c instanceof JMenuItem) {
			JMenuItem menu = (JMenuItem) c;
			menu.setHorizontalTextPosition(SwingConstants.RIGHT);

		}

	}

    protected boolean changeTheme(String theme) {
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf." + theme);
            status = true;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
        }
        return status;
    }

    protected static Cursor costumCursor(String path, int x, int y) {
        ImageIcon cursorIcon = new ImageIcon(CoderPaint.class.getResource(path));
        Cursor cursors;
        String version = System.getProperty("os.name").toLowerCase();
        if (version.contains("windows")) {
            cursors = Toolkit.getDefaultToolkit().createCustomCursor(cursorIcon.getImage(), new Point(x, y),
                    path.substring(path.length() - 5));
        } else {
            cursors = Toolkit.getDefaultToolkit().createCustomCursor(cursorIcon.getImage(), new Point(5, 15),
                    path.substring(path.length() - 5));
        }
        return cursors;
    }

	@Override
	public void loadLanguage(String Language) {
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/com/coder/languages/"+Language+".properties"));
			String Title = properties.getProperty("Frame");
			frame.setTitle(Title);
			String File = properties.getProperty("File");
			mnFile.setText(File);
			String Save = properties.getProperty("Save");
			mntmSave.setText(Save);
			String Exit = properties.getProperty("Exit");
			mntmExit.setText(Exit);
			String Edit = properties.getProperty("Edit");
			mnEdit.setText(Edit);
			String GuideLines = properties.getProperty("GuideLines");
			guideLines.setText(GuideLines);
			String Undo = properties.getProperty("Undo");
			mntmUndo.setText(Undo);
			String Copy = properties.getProperty("Copy");
			mntmCopy.setText(Copy);
			String Paste = properties.getProperty("Paste");
			mntmPaste.setText(Paste);
			String Picture = properties.getProperty("Picture");
			mnPicture.setText(Picture);
			String FlipImage = properties.getProperty("FlipImage");
			mntFlipImg.setText(FlipImage);
			String RotateImage = properties.getProperty("RotateImage");
			mntFlipImg.setText(RotateImage);
			String GetScreenCapture = properties.getProperty("GetScreenCapture");
			mntmScreenCap.setText(GetScreenCapture);
			String Languages = properties.getProperty("Languages");
			mnLang.setText(Languages);
			String Themes = properties.getProperty("Themes");
			mnThemes.setText(Themes);
			String Default = properties.getProperty("Default");
			chckbxmn5.setText(Default);
			String About = properties.getProperty("About");
			mnAbout.setText(About);
			String ShareYourOp = properties.getProperty("ShareYourOp");
			sendMail.setText(ShareYourOp);
			String Pencil = properties.getProperty("Pencil");
			myPane.btnPencil.setText(Pencil);
			String Eraser = properties.getProperty("Eraser");
			myPane.btnEraser.setText(Eraser);
			String Clear = properties.getProperty("Clear");
			myPane.btnClear.setText(Clear);
			String Colors = properties.getProperty("Colors");
			myPane.btnColorChooser.setText(Colors);
			String Text = properties.getProperty("Text");
			myPane.btnText.setText(Text);
			String Crop = properties.getProperty("Crop");
			myPane.btnCrop.setText(Crop);
			String ChooseFont = properties.getProperty("ChooseFont");
			myPane.lblSetFont.setText(ChooseFont);
			String CurrentColor = properties.getProperty("CurrentColor");
			myPane.lblCurrentColor.setText(CurrentColor);
			String SetStroke = properties.getProperty("SetStroke");
			myPane.lblDemision.setText(SetStroke);
			String Dashed = properties.getProperty("Dashed");
			myPane.chckbxDashed.setText(Dashed);
			String Gredient = properties.getProperty("Gredient");
			myPane.chckbxGredient.setText(Gredient);
			String MixColors = properties.getProperty("MixColors");
			myPane.chckbxRandomColor.setText(MixColors);
			String FillInShape = properties.getProperty("FillInShape");
			myPane.fillAllShapes.setText(FillInShape);
			SwingUtilities.updateComponentTreeUI(frame);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.ScrollPaneUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;



public class TextEditor extends Component implements ActionListener {
    //Declaring Data Members and properties
    JFrame frame;
    JMenuBar menuBar;
    JMenu File;
    JMenu Edit,font,Theme;
    JMenuItem newWindow,openFile,saveFile,cut,copy,paste,selectAll,close,size,dark,light,undo,redo;
    JTextArea textArea;
    JScrollPane scrollPane;
    Color defaultMenuBar,defaultMenuItem,defaultScrollbar;
    ScrollBarUI defaultUIH,defaultUIV;
    JComboBox font_type,font_size;
    ImageIcon image;

    protected UndoManager undoManager;

    private Document editorPaneDocument;

    int defaultSize;
    boolean isDark=false;
    boolean isSaved=false;
    TextEditor()
    {
        UIManager.put("MenuItem.acceleratorForeground",Color.BLACK);
        undoManager=new UndoManager();
        image=new ImageIcon(getClass().getResource("/icons/logo.jpg"));

        //Initialize a frame
        frame=new JFrame("*untitled-Scrapity");
        frame.setIconImage(image.getImage());
        //Initialize menuBar
        menuBar=new JMenuBar();
        defaultMenuBar=menuBar.getBackground();
        menuBar.setBorderPainted(false);
        //Initialize menus
        File=new JMenu("File");

        File.setBorderPainted(false);
        Edit=new JMenu("Edit");
        Edit.setBorderPainted(false);
        Theme=new JMenu("Theme");
        Theme.setBorderPainted(false);

        //Initialize menu items
        newWindow=new JMenuItem("New Window");
        defaultMenuItem=newWindow.getBackground();
        newWindow.setBorderPainted(false);
        newWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,Event.CTRL_MASK));
        //Adding action listener so that it can listen to the action
        newWindow.addActionListener(this);
        openFile=new JMenuItem("Open File");
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK));
        openFile.setBorderPainted(false);
        openFile.addActionListener(this);
        saveFile=new JMenuItem("Save File");
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,Event.CTRL_MASK));
        saveFile.setBorderPainted(false);
        saveFile.addActionListener(this);
        cut=new JMenuItem("Cut");
        cut.setBorderPainted(false);
        cut.addActionListener(this);
        //Setting Key Binding
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,Event.CTRL_MASK));
        copy=new JMenuItem("Copy");
        copy.setBorderPainted(false);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,Event.CTRL_MASK));
        copy.addActionListener(this);
        paste=new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,Event.CTRL_MASK));
        paste.setBorderPainted(false);
        paste.addActionListener(this);
        selectAll=new JMenuItem("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,Event.CTRL_MASK));
        selectAll.setBorderPainted(false);
        selectAll.addActionListener(this);
        close=new JMenuItem("Close");

        close.setBorderPainted(false);
        close.setMnemonic(KeyEvent.VK_F4);
        close.addActionListener(this);
        font=new JMenu("Font");
        font.setBorderPainted(false);

        size=new JMenuItem("Size");

        size.setBorderPainted(false);
        size.addActionListener(this);
        dark=new JMenuItem("Dark");

        dark.setBorderPainted(false);
        dark.addActionListener(this);
        light=new JMenuItem("Light");

        light.setBorderPainted(false);
        light.addActionListener(this);

        redo=new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,Event.CTRL_MASK));
        redo.addActionListener(this);
        redo.setBorderPainted(false);

        undo=new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,Event.CTRL_MASK));
        undo.addActionListener(this);
        undo.setBorderPainted(false);
        //Initialize Text Area
        textArea=new JTextArea();
        editorPaneDocument= textArea.getDocument();
        editorPaneDocument.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                isSaved=false;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                isSaved=false;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                isSaved=false;
            }
        });
        editorPaneDocument.addUndoableEditListener(undoManager);
        defaultSize=textArea.getFont().getSize();
        scrollPane=new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        defaultScrollbar=scrollPane.getHorizontalScrollBar().getBackground();
        defaultUIH=scrollPane.getHorizontalScrollBar().getUI();
        defaultUIV=scrollPane.getVerticalScrollBar().getUI();

        //Add menu items to menu
        File.add(newWindow);
        File.add(openFile);
        File.add(saveFile);
        Edit.add(cut);
        Edit.add(copy);
        Edit.add(paste);
        Edit.add(selectAll);
        Edit.add(undo);
        Edit.add(redo);
        Edit.add(font);
        Edit.add(close);
        font.add(size);
        Theme.add(dark);
        Theme.add(light);
        Object[] sizes={defaultSize,10,11,12,13,14,15,16,17,18,19,20};
        font_size=new JComboBox(sizes);
        font_size.setEditable(true);
        font_size.addActionListener(this);
        //Exits the program on pressing X button
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent)
        {
            String change=textArea.getText();
            if(isSaved==false && !change.isEmpty())
            {
                if(isDark)
                {
                    UIManager UI=new UIManager();
                    UI.put("OptionPane.background",Color.darkGray);
                    UI.put("Panel.background",Color.darkGray);
                    UI.put("OptionPane.messageForeground",Color.white);
                    int option=JOptionPane.showConfirmDialog(null,"Your file is not saved,save?","Scrapity",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    if(option==1)frame.dispose();
                    if(option==2 || option==-1)frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    if(option==0)
                    {
                        //Initialize a file picker
                        JFileChooser fileChooser=new JFileChooser("C:");
                        //Get choose option
                        int chooseOption=fileChooser.showSaveDialog(null);
                        //check if we clicked on save button
                        if(chooseOption==JFileChooser.APPROVE_OPTION)
                        {
                            //Create a new file with choosen directory path and file name
                            java.io.File file=new File(fileChooser.getSelectedFile().getAbsolutePath()+".txt");
                            String fileName= file.getName();
                            try{
                                //Initialize file writer
                                FileWriter fileWriter=new FileWriter(file);
                                //Initialize Buffered writer
                                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                                //Write contents of textArea to file
                                textArea.write(bufferedWriter);
                                bufferedWriter.close();
                            }
                            catch(IOException ioException){
                                ioException.printStackTrace();

                            }
                            frame.setTitle(fileName);
                        }
                        isSaved=true;
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
                else {
                    UIManager UI=new UIManager();
                    UI.put("OptionPane.background",Color.white);
                    UI.put("Panel.background",Color.white);
                    int option=JOptionPane.showConfirmDialog(null,"Your file is not saved,save?","Scrapity",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    if(option==1)frame.dispose();
                    if(option==2 || option==-1)frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    if(option==0)
                    {
                        //Initialize a file picker
                        JFileChooser fileChooser=new JFileChooser("C:");
                        //Get choose option
                        int chooseOption=fileChooser.showSaveDialog(null);
                        //check if we clicked on save button
                        if(chooseOption==JFileChooser.APPROVE_OPTION)
                        {
                            //Create a new file with choosen directory path and file name
                            java.io.File file=new File(fileChooser.getSelectedFile().getAbsolutePath()+".txt");
                            String fileName= file.getName();
                            try{
                                //Initialize file writer
                                FileWriter fileWriter=new FileWriter(file);
                                //Initialize Buffered writer
                                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                                //Write contents of textArea to file
                                textArea.write(bufferedWriter);
                                bufferedWriter.close();
                            }
                            catch(IOException ioException){
                                ioException.printStackTrace();

                            }
                            frame.setTitle(fileName);
                        }
                        isSaved=true;
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }

                }
            }
            else {
                frame.dispose();
            }
        }
    });
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Set menuBar to frame
        frame.setJMenuBar(menuBar);
        //Add text area to frame
        frame.add(scrollPane,BorderLayout.CENTER);

        menuBar.add(File);
        menuBar.add(Edit);
        menuBar.add(Theme);
        //set Dimensions of frame
        //x and y gives us the position where the frame should appear
        frame.setBounds(0,0,400,400);
        //The setVisible method makes the frame appear on the screen.
        //If you forget to do this,the frame object will exist as an object in memory but no picture will appear on screen
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getSource()==cut)
        {
            textArea.cut();
        }
        if(actionEvent.getSource()==copy)
        {
            textArea.copy();
        }
        if(actionEvent.getSource()==selectAll)
        {
            textArea.selectAll();
        }
        if(actionEvent.getSource()==size)
        {

            if(isDark)
            {
                ImageIcon imageIcon=new ImageIcon(getClass().getResource("/icons/font size negative.jpg"));
                Image image= imageIcon.getImage();
                Image newimg=image.getScaledInstance(120,120,Image.SCALE_SMOOTH);
                imageIcon=new ImageIcon(newimg);
                UIManager UI=new UIManager();
                UI.put("OptionPane.background",Color.darkGray);
                UI.put("Panel.background",Color.darkGray);
                JOptionPane.showMessageDialog(null,font_size,"Font Size",JOptionPane.QUESTION_MESSAGE,imageIcon);
            }
            else
            {
                ImageIcon imageIcon=new ImageIcon(getClass().getResource("/icons/font size.jpg"));
                Image image= imageIcon.getImage();
                Image newimg=image.getScaledInstance(120,120,Image.SCALE_SMOOTH);
                imageIcon=new ImageIcon(newimg);
                UIManager UI=new UIManager();
                UI.put("OptionPane.background",Color.white);
                UI.put("Panel.background",Color.white);
                JOptionPane.showMessageDialog(null,font_size,"Font Size",JOptionPane.QUESTION_MESSAGE,imageIcon);
            }
        }
        if(actionEvent.getSource()==font_size)
        {
            int s= (Integer)font_size.getSelectedItem();
            String f= String.valueOf(textArea.getFont());
            textArea.setFont(new Font(f,Font.PLAIN,s));
        }
        if(actionEvent.getSource()==close)
        {
            String change=textArea.getText();
            if(isSaved==false && !change.isEmpty())
            {
                if(isDark)
                {
                    UIManager UI=new UIManager();
                    UI.put("OptionPane.background",Color.darkGray);
                    UI.put("Panel.background",Color.darkGray);
                    UI.put("OptionPane.messageForeground",Color.white);
                    int option=JOptionPane.showConfirmDialog(null,"Your file is not saved,save?","Scrapity",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    if(option==1)frame.dispose();
                    if(option==2 || option==-1)frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    if(option==0)
                    {
                        //Initialize a file picker
                        JFileChooser fileChooser=new JFileChooser("C:");
                        //Get choose option
                        int chooseOption=fileChooser.showSaveDialog(null);
                        //check if we clicked on save button
                        if(chooseOption==JFileChooser.APPROVE_OPTION)
                        {
                            //Create a new file with choosen directory path and file name
                            java.io.File file=new File(fileChooser.getSelectedFile().getAbsolutePath()+".txt");
                            String fileName= file.getName();
                            try{
                                //Initialize file writer
                                FileWriter fileWriter=new FileWriter(file);
                                //Initialize Buffered writer
                                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                                //Write contents of textArea to file
                                textArea.write(bufferedWriter);
                                bufferedWriter.close();
                            }
                            catch(IOException ioException){
                                ioException.printStackTrace();

                            }
                            frame.setTitle(fileName);
                        }
                        isSaved=true;
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                }
                else {
                    UIManager UI=new UIManager();
                    UI.put("OptionPane.background",Color.white);
                    UI.put("Panel.background",Color.white);
                    int option=JOptionPane.showConfirmDialog(null,"Your file is not saved,save?","Scrapity",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
                    if(option==1)frame.dispose();
                    if(option==2 || option==-1)frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    if(option==0)
                    {
                        //Initialize a file picker
                        JFileChooser fileChooser=new JFileChooser("C:");
                        //Get choose option
                        int chooseOption=fileChooser.showSaveDialog(null);
                        //check if we clicked on save button
                        if(chooseOption==JFileChooser.APPROVE_OPTION)
                        {
                            //Create a new file with choosen directory path and file name
                            java.io.File file=new File(fileChooser.getSelectedFile().getAbsolutePath()+".txt");
                            String fileName= file.getName();
                            try{
                                //Initialize file writer
                                FileWriter fileWriter=new FileWriter(file);
                                //Initialize Buffered writer
                                BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                                //Write contents of textArea to file
                                textArea.write(bufferedWriter);
                                bufferedWriter.close();
                            }
                            catch(IOException ioException){
                                ioException.printStackTrace();

                            }
                            frame.setTitle(fileName);
                        }
                        isSaved=true;
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }

                }
            }
            else {
                frame.dispose();
            }
        }
        if(actionEvent.getSource()==dark)
        {
            isDark=true;
            File.getPopupMenu().setBorder(new LineBorder(Color.darkGray));
            Edit.getPopupMenu().setBorder(new LineBorder(Color.darkGray));
            Theme.getPopupMenu().setBorder(new LineBorder(Color.darkGray));
            undo.setBackground(Color.gray);
            redo.setBackground(Color.gray);
            newWindow.setBackground(Color.gray);
            openFile.setBackground(Color.gray);
            saveFile.setBackground(Color.gray);
            cut.setBackground(Color.gray);
            copy.setBackground(Color.gray);
            paste.setBackground(Color.gray);
            selectAll.setBackground(Color.gray);
            close.setBackground(Color.gray);
            close.setBorder(new LineBorder(Color.DARK_GRAY));
            font.getPopupMenu().setBorder(new LineBorder(Color.DARK_GRAY));
            size.setBackground(Color.gray);
            size.setBorder(new LineBorder(Color.DARK_GRAY));
            dark.setBackground(Color.gray);
            light.setBorder(new LineBorder(Color.DARK_GRAY));
            light.setBackground(Color.gray);
            textArea.setBorder(new LineBorder(Color.black));
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.WHITE);
            //The blinking cursor is called caret
            textArea.setCaretColor(Color.WHITE);
            scrollPane.setBorder(new LineBorder(Color.BLACK));
            scrollPane.getHorizontalScrollBar().setBackground(Color.GRAY);
            scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
                @Override
                protected void configureScrollBarColors(){
                    this.thumbColor=Color.DARK_GRAY;
                }
            });
            scrollPane.getVerticalScrollBar().setBackground(Color.GRAY);
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
                @Override
                protected void configureScrollBarColors(){
                    this.thumbColor=Color.DARK_GRAY;
                }
            });
            //Adding Menu in menuBar
            menuBar.setBackground(Color.darkGray);
            //Sets color of the menu name
            File.setForeground(Color.WHITE);
            Edit.setForeground(Color.WHITE);
            font.setBackground(Color.gray);
            Theme.setForeground(Color.WHITE);
            font.setOpaque(true);
        }
        if(actionEvent.getSource()==light)
        {
            isDark=false;
            File.getPopupMenu().setBorder(new LineBorder(Color.white));
            Edit.getPopupMenu().setBorder(new LineBorder(Color.white));
            Theme.getPopupMenu().setBorder(new LineBorder(Color.white));
            undo.setBackground(defaultMenuItem);
            redo.setBackground(defaultMenuItem);
            newWindow.setBackground(defaultMenuItem);
            openFile.setBackground(defaultMenuItem);
            saveFile.setBackground(defaultMenuItem);
            cut.setBackground(defaultMenuItem);
            copy.setBackground(defaultMenuItem);
            paste.setBackground(defaultMenuItem);
            selectAll.setBackground(defaultMenuItem);
            close.setBackground(defaultMenuItem);
            size.setBackground(defaultMenuItem);
            dark.setBackground(defaultMenuItem);
            light.setBackground(defaultMenuItem);
            textArea.setBorder(new LineBorder(Color.white));
            textArea.setBackground(Color.white);
            textArea.setForeground(Color.black);
            //The blinking cursor is called caret
            textArea.setCaretColor(Color.black);
            scrollPane.setBorder(new LineBorder(Color.white));
            scrollPane.getHorizontalScrollBar().setBackground(defaultScrollbar);

            scrollPane.getHorizontalScrollBar().setUI(defaultUIH);
            scrollPane.getVerticalScrollBar().setBackground(defaultScrollbar);
            scrollPane.getVerticalScrollBar().setUI(defaultUIV);
            menuBar.setBackground(defaultMenuBar);
            File.setForeground(Color.BLACK);
            Edit.setForeground(Color.BLACK);
            Theme.setForeground(Color.BLACK);
            font.setOpaque(false);

        }
        if(actionEvent.getSource()==openFile)
        {
            //open a file chooser
            JFileChooser fileChooser=new JFileChooser("C:");
            if(isDark) fileChooser.setBackground(Color.GRAY);
            int chooseOption=fileChooser.showOpenDialog(null);
            if(chooseOption==JFileChooser.APPROVE_OPTION)
            {
                //Getting selected file
                java.io.File file=fileChooser.getSelectedFile();
                String fileName= file.getName();
                //Get the path of selected file
                String filePath=file.getPath();
                try{
                    //Initialize file reader
                    FileReader fileReader=new FileReader(filePath);
                    //Initialize a buffer reader
                    BufferedReader bufferedReader=new BufferedReader(fileReader);
                    String intermediate="";
                    String output="";
                    //Read content of file line by line
                    while((intermediate= bufferedReader.readLine())!=null)
                    {
                        output+=intermediate+"\n";
                    }
                    //Set the output string to text Area
                    textArea.setText(output);
                }
                catch(FileNotFoundException fileNotFoundException){
                    fileNotFoundException.printStackTrace();
                }
                catch(IOException ioException)
                {
                    ioException.printStackTrace();
                }
                frame.setTitle(fileName);
                isSaved=true;
            }
        }
        if(actionEvent.getSource()==saveFile)
        {
            //Initialize a file picker
            JFileChooser fileChooser=new JFileChooser("C:");
            //Get choose option
            int chooseOption=fileChooser.showSaveDialog(null);
            //check if we clicked on save button
            if(chooseOption==JFileChooser.APPROVE_OPTION)
            {
                //Create a new file with choosen directory path and file name
                java.io.File file=new File(fileChooser.getSelectedFile().getAbsolutePath()+".txt");
                String fileName= file.getName();
                try{
                    //Initialize file writer
                    FileWriter fileWriter=new FileWriter(file);
                    //Initialize Buffered writer
                    BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
                    //Write contents of textArea to file
                    textArea.write(bufferedWriter);
                    bufferedWriter.close();
                }
                catch(IOException ioException){
                    ioException.printStackTrace();

                }
                isSaved=true;
                frame.setTitle(fileName);
            }
        }
        if(actionEvent.getSource()==newWindow)
        {
            TextEditor textEditor=new TextEditor();
        }
        if(actionEvent.getSource()==undo)
        {
            undoManager.undo();
        }
        if(actionEvent.getSource()==redo)
        {
            undoManager.redo();
        }
    }
    public static void main(String[] args) {
        TextEditor textEditor=new TextEditor();

    }
}

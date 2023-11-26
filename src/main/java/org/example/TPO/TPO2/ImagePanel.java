package org.example.TPO.TPO2;

import java.util.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ImagePanel extends JPanel {

    private Image img;
    private boolean correct;
    private Dimension defd = new Dimension(200,200);
    private String msg;

    public ImagePanel(String msg) {
        this.msg = msg;
        setPreferredSize(defd);
        setBorder(BorderFactory.createLineBorder(Color.blue, 2));
    }

    public void setImage(Image img) {
        this.img = img;
        int w = img.getWidth(this);       // szerokość obrazka
        int h = img. getHeight(this);     // wysokość obrazka
        if (w != -1 && w != 0 && h != -1 && h != 0) {
            correct = true;
            setPreferredSize(new Dimension(w, h));
        }
        else setPreferredSize(defd);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null && correct)
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        else
            g.drawString(msg, 10, 10);
    }

}

class ImageViewer extends JFrame implements ActionListener {

    private ListIterator lit;
    private ImagePanel imagePanel = new ImagePanel("Loading images ...");
    private JButton left = new JButton(new ImageIcon("arrow1.gif"));
    private  JButton right = new JButton(new ImageIcon("arrow2.gif"));
    private JButton os = new JButton("<html><b>Oryginalna<br>skala</b></html>");

    public ImageViewer(java.util.List imgList) {

        lit = imgList.listIterator();

        getContentPane().add(imagePanel);
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        left.addActionListener(this);
        right.addActionListener(this);
        os.addActionListener(this);
        p.add(left); p.add(right); p.add(os);
        getContentPane().add(p, "South");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        show();


        MediaTracker mt = new MediaTracker(this);
        int i=0;
        while(lit.hasNext()) mt.addImage((Image) lit.next(), ++i);
        try {
            mt.waitForAll();
        } catch (InterruptedException exc) { }
        System.out.println("Image viewer ready");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() { getNext(); }
        });
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == right) getNext();
        else if (src == left) getPrevious();
        else pack();
    }

    private boolean lastOperWasNext = true;

    private void getNext() {
        if (!lastOperWasNext) lit.next();
        if (!lit.hasNext()) while (lit.hasPrevious()) lit.previous();
        imagePanel.setImage( (Image) lit.next() );
        lastOperWasNext = true;
    }

    private void getPrevious() {
        if (lastOperWasNext) lit.previous();
        if (!lit.hasPrevious()) while (lit.hasNext()) lit.next();
        imagePanel.setImage( (Image) lit.previous() );
        lastOperWasNext = false;
    }

}

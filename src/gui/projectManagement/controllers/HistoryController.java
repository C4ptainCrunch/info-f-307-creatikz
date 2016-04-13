package gui.projectManagement.controllers;


import gui.projectManagement.views.HistoryView;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HistoryController {
    private HistoryView view;
    private String path;
    private Color currentColor = Color.BLACK;

    public HistoryController(HistoryView view, String path){
        this.path = path;
        this.view = view;
    }

    public void fillView() {
        try{
            Files.lines(Paths.get(path + "/diffs"), Charset.defaultCharset()).
                    forEach(line -> {
                        colorHelper(line);
                        appendString(line + "\n", currentColor);
                    });
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    private void appendString(String str, Color color){
        StyledDocument document = (StyledDocument) view.getHistoryPane().getDocument();
        Style style = document.addStyle("color",null);
        StyleConstants.setForeground(style, color);
        try {
            document.insertString(document.getLength(), str, style);
        }catch(BadLocationException e){
            e.getStackTrace();
        }
    }


    private void colorHelper(String str) {
        if (Pattern.matches("^\\d{4}\\/\\d\\d\\/\\d\\d \\d\\d:\\d\\d:\\d\\d$", str))
            currentColor = Color.BLACK;
        else if (Pattern.matches("^\\+.*$", str))
            currentColor = Color.GREEN;
        else if (Pattern.matches("^\\-.*$", str))
            currentColor = Color.RED;
    }

}

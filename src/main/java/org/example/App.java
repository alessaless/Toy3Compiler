package main.java.org.example;

import main.java.org.example.Lexer;
import main.java.org.example.parser;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 */
import java.io.*;

public class App {
    public static void main (String[] args) throws Exception {
        JTree tree;
        String filePath = "provaEs4.txt";
        FileInputStream stream = new FileInputStream(filePath);
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        Lexer scanner = new Lexer(reader);
        parser p = new parser(scanner);

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) p.parse().value;
        tree=new JTree(root);

        JFrame framePannello=new JFrame();
        framePannello.setSize(400, 400);
        JScrollPane treeView = new JScrollPane(tree);
        framePannello.add(treeView);
        framePannello.setVisible(true);

        while (!scanner.yyatEOF()){
            p.debug_parse();
        }
    }

}
